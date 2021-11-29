/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.fabricmc.fabric.api.datagen.v1.provider;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.function.Consumer;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.advancements.Advancement;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

/**
 * Extend this class and implement {@link FabricAdvancementsProvider#generateAdvancement}.
 *
 * <p>Register an instance of the class with {@link FabricDataGenerator#addProvider} in a {@link net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint}
 */
public abstract class FabricAdvancementsProvider implements DataProvider {
	private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();

	private final FabricDataGenerator dataGenerator;

	protected FabricAdvancementsProvider(FabricDataGenerator dataGenerator) {
		this.dataGenerator = dataGenerator;
	}

	/**
	 * Use {@link Advancement.Builder#save(Consumer, String)} to register an advancement to build.
	 */
	public abstract void generateAdvancement(Consumer<Advancement> consumer);

	@Override
	public void run(HashCache cache) throws IOException {
		final Set<ResourceLocation> identifiers = Sets.newHashSet();
		final Set<Advancement> advancements = Sets.newHashSet();

		generateAdvancement(advancements::add);

		for (Advancement advancement : advancements) {
			if (!identifiers.add(advancement.getId())) {
				throw new IllegalStateException("Duplicate advancement " + advancement.getId());
			}

			DataProvider.save(GSON, cache, advancement.deconstruct().serializeToJson(), getOutputPath(advancement));
		}
	}

	private Path getOutputPath(Advancement advancement) {
		return dataGenerator.getOutputFolder().resolve("data/%s/advancements/%s.json".formatted(advancement.getId().getNamespace(), advancement.getId().getPath()));
	}

	@Override
	public String getName() {
		return "Advancements";
	}
}