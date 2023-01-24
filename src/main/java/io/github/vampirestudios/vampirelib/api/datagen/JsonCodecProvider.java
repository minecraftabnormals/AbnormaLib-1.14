/*
 * Copyright (c) 2023 OliviaTheVampire
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.vampirestudios.vampirelib.api.datagen;

import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import org.slf4j.Logger;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;

import net.fabricmc.fabric.api.resource.conditions.v1.ConditionJsonProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;

import io.github.vampirestudios.vampirelib.api.datagen.ExistingFileHelper.ResourceType;
import io.github.vampirestudios.vampirelib.utils.LamdbaExceptionUtils;

/**
 * <p>Dataprovider for using a Codec to generate jsons.
 * Path names for jsons are derived from the given registry folder and each entry's namespaced id, in the format:</p>
 * <pre>
 * {@code <assets/data>/entryid/registryfolder/entrypath.json }
 * </pre>
 *
 * @param <T> the type of thing being generated.
 */
public class JsonCodecProvider<T> implements DataProvider {
	private static final Logger LOGGER = LogUtils.getLogger();
	protected final PackOutput output;
	protected final ExistingFileHelper existingFileHelper;
	protected final String modid;
	protected final DynamicOps<JsonElement> dynamicOps;
	protected final PackType packType;
	protected final String directory;
	protected final Codec<T> codec;
	protected final Map<ResourceLocation, T> entries;
	protected Map<ResourceLocation, ConditionJsonProvider[]> conditions = Collections.emptyMap();

	/**
	 * @param output {@linkplain PackOutput} provided by the {@link DataGenerator}.
	 * @param dynamicOps DynamicOps to encode values to jsons with using the provided Codec, e.g. {@link JsonOps#INSTANCE}.
	 * @param packType PackType specifying whether to generate entries in assets or data.
	 * @param directory String representing the directory to generate jsons in, e.g. "dimension" or "cheesemod/cheese".
	 * @param codec Codec to encode values to jsons with using the provided DynamicOps.
	 * @param entries Map of named entries to serialize to jsons. Paths for values are derived from the ResourceLocation's entryid:entrypath as specified above.
	 */
	public JsonCodecProvider(PackOutput output, ExistingFileHelper existingFileHelper, String modid, DynamicOps<JsonElement> dynamicOps, PackType packType,
							 String directory, Codec<T> codec, Map<ResourceLocation, T> entries) {
		// Track generated data so other dataproviders can validate if needed.
		final ResourceType resourceType = new ResourceType(packType, ".json", directory);
		for (ResourceLocation id : entries.keySet()) {
			existingFileHelper.trackGenerated(id, resourceType);
		}
		this.output = output;
		this.existingFileHelper = existingFileHelper;
		this.modid = modid;
		this.dynamicOps = dynamicOps;
		this.packType = packType;
		this.directory = directory;
		this.codec = codec;
		this.entries = entries;
	}

	@Override
	public CompletableFuture<?> run(final CachedOutput cache) {
		final Path outputFolder = this.output.getOutputFolder(this.packType == PackType.CLIENT_RESOURCES
				? PackOutput.Target.RESOURCE_PACK
				: PackOutput.Target.DATA_PACK);
		ImmutableList.Builder<CompletableFuture<?>> futuresBuilder = new ImmutableList.Builder<>();

		gather(LamdbaExceptionUtils.rethrowBiConsumer((id, value) -> {
			final Path path = outputFolder.resolve(id.getNamespace()).resolve(this.directory).resolve(id.getPath() + ".json");
			JsonElement encoded = this.codec.encodeStart(this.dynamicOps, value)
					.getOrThrow(false, msg -> LOGGER.error("Failed to encode {}: {}", path, msg));
			ConditionJsonProvider[] conditions = this.conditions.get(id);
			if (conditions != null && conditions.length > 0) {
				if(encoded instanceof JsonObject obj) {
					JsonArray arr = new JsonArray();
					for(ConditionJsonProvider iCond : conditions) {
						arr.add(iCond.toJson());
					}
					obj.add(ResourceConditions.CONDITIONS_KEY, arr);
				} else {
					LOGGER.error("Attempted to apply conditions to a type that is not a JsonObject! - Path: {}", path);
				}
			}
			futuresBuilder.add(DataProvider.saveStable(cache, encoded, path));
		}));

		return CompletableFuture.allOf(futuresBuilder.build().toArray(CompletableFuture[]::new));
	}

	protected void gather(BiConsumer<ResourceLocation, T> consumer) {
		this.entries.forEach(consumer);
	}

	@Override
	public String getName() {
		return String.format("%s generator for %s", this.directory, this.modid);
	}

	/**
	 * Applies a condition map to this provider.
	 * These conditions will be applied to the created JsonElements with the matching names.
	 * Null or empty arrays will not be written, and if the top-level json type is not JsonObject, attempting to add conditions will error.
	 * @param conditions The name->condition map to apply.
	 */
	public JsonCodecProvider<T> setConditions(Map<ResourceLocation, ConditionJsonProvider[]> conditions) {
		this.conditions = conditions;
		return this;
	}
}
