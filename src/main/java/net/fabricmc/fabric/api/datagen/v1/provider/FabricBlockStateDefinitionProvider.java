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

import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.ModelProvider;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

/**
 * Extend this class and implement {@link FabricBlockStateDefinitionProvider#generateBlockStateModels} and {@link FabricBlockStateDefinitionProvider#generateItemModels}.
 *
 * <p>Register an instance of the class with {@link FabricDataGenerator#addProvider} in a {@link net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint}
 */
public abstract class FabricBlockStateDefinitionProvider extends ModelProvider {
	protected final FabricDataGenerator dataGenerator;

	public FabricBlockStateDefinitionProvider(FabricDataGenerator generator) {
		super(generator);
		this.dataGenerator = generator;
	}

	public abstract void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator);

	public abstract void generateItemModels(ItemModelGenerators itemModelGenerator);
}