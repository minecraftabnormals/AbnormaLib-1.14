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

package io.github.vampirestudios.vampirelib.api.datagen;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootDataType;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.ConditionJsonProvider;
import net.fabricmc.fabric.impl.datagen.FabricDataGenHelper;

public final class VLootTableProviderImpl {
	/**
	 * Shared run logic for {@link VBlockLootTableProvider} and {@link SimpleFabricLootTableProvider}.
	 */
	public static CompletableFuture<?> run(
			CachedOutput writer,
			VBlockLootTableProvider provider,
			LootContextParamSet lootContextType,
			FabricDataOutput fabricDataOutput) {
		HashMap<ResourceLocation, LootTable> builders = Maps.newHashMap();
		HashMap<ResourceLocation, ConditionJsonProvider[]> conditionMap = new HashMap<>();

		provider.generate((identifier, builder) -> {
			ConditionJsonProvider[] conditions = FabricDataGenHelper.consumeConditions(builder);
			conditionMap.put(identifier, conditions);

			if (builders.put(identifier, builder.setParamSet(lootContextType).build()) != null) {
				throw new IllegalStateException("Duplicate loot table " + identifier);
			}
		});

		final List<CompletableFuture<?>> futures = new ArrayList<>();

		for (Map.Entry<ResourceLocation, LootTable> entry : builders.entrySet()) {
			JsonObject tableJson = (JsonObject) LootDataType.TABLE.parser().toJsonTree(entry.getValue());
			ConditionJsonProvider.write(tableJson, conditionMap.remove(entry.getKey()));

			futures.add(DataProvider.saveStable(writer, tableJson, getOutputPath(fabricDataOutput, entry.getKey())));
		}

		return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
	}

	private static Path getOutputPath(FabricDataOutput dataOutput, ResourceLocation lootTableId) {
		return dataOutput.createPathProvider(PackOutput.Target.DATA_PACK, "loot_tables").json(lootTableId);
	}

	private VLootTableProviderImpl() {
	}
}
