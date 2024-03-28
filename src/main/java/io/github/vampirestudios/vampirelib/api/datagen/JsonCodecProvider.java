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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

public abstract class JsonCodecProvider<T> implements DataProvider {
	private final PackOutput.PathProvider pathResolver;
	private final Codec<T> codec;

	protected JsonCodecProvider(PackOutput dataOutput, PackOutput.Target outputType, String directoryName, Codec<T> codec) {
		this.pathResolver = dataOutput.createPathProvider(outputType, directoryName);
		this.codec = codec;
	}

	@Override
	public CompletableFuture<?> run(final CachedOutput cache) {
		Map<ResourceLocation, JsonElement> entries = new HashMap<>();
		BiConsumer<ResourceLocation, T> provider = (id, value) -> {
			JsonElement json = this.convert(id, value);
			JsonElement existingJson = entries.put(id, json);

			if (existingJson != null) {
				throw new IllegalArgumentException("Duplicate entry " + id);
			}
		};

		this.configure(provider);
		return this.write(cache, entries);
	}

	/**
	 * Implement this method to register entries to generate.
	 *
	 * @param provider A consumer that accepts an {@link ResourceLocation} and a value to register.
	 */
	protected abstract void configure(BiConsumer<ResourceLocation, T> provider);

	private JsonElement convert(ResourceLocation id, T value) {
		DataResult<JsonElement> dataResult = this.codec.encodeStart(JsonOps.INSTANCE, value);
		return dataResult.get()
			.mapRight(partial -> "Invalid entry %s: %s".formatted(id, partial.message()))
			.orThrow();
	}

	private CompletableFuture<?> write(CachedOutput writer, Map<ResourceLocation, JsonElement> entries) {
		return CompletableFuture.allOf(entries.entrySet().stream().map(entry -> {
			Path path = this.pathResolver.json(entry.getKey());
			return DataProvider.saveStable(writer, entry.getValue(), path);
		}).toArray(CompletableFuture[]::new));
	}

}
