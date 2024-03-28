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

package io.github.vampirestudios.vampirelib.village;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.level.biome.Biome;

public interface VillagerTypeRegistry {
	HashMap<ResourceKey<Biome>, ArrayList<VillagerType>> customVillagerTypes = new HashMap<>();

	@SafeVarargs
	static VillagerType register(ResourceLocation id, ResourceKey<Biome>... biomes) {
		VillagerType villagerType = Registry.register(BuiltInRegistries.VILLAGER_TYPE, id, new VillagerType(id.getPath()));
		for (ResourceKey<Biome> biome : biomes) {
			if (customVillagerTypes.containsKey(biome)) customVillagerTypes.get(biome).add(villagerType);
			else customVillagerTypes.put(biome, new ArrayList<>(Collections.singleton(villagerType)));

			if (VillagerType.BY_BIOME.containsKey(biome)) VillagerType.BY_BIOME.replace(biome, villagerType);
		}
		return villagerType;
	}

	static VillagerType getVillagerTypeForBiome(Holder<Biome> biome) {
		Optional<ResourceKey<Biome>> optionalBiomeResourceKey = biome.unwrapKey();
		if (optionalBiomeResourceKey.isPresent()) {
			ResourceKey<Biome> biomeResourceKey = optionalBiomeResourceKey.get();
			if (customVillagerTypes.containsKey(biomeResourceKey)) {
				List<VillagerType> villagerTypes = customVillagerTypes.get(biomeResourceKey);
				return villagerTypes.get(Mth.floor(villagerTypes.size() * Math.random()));
			} else return VillagerType.byBiome(biome);
		} else {
			return VillagerType.PLAINS;
		}
	}
}
