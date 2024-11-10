/*
 * Copyright (c) 2024 OliviaTheVampire
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

import com.google.common.collect.ImmutableSet;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public interface VillagerProfessionRegistry {

	static VillagerProfession register(ResourceLocation name, ResourceKey<PoiType> workStation) {
		return register(name, workStation, ImmutableSet.of());
	}

	static VillagerProfession register(ResourceLocation name, ResourceKey<PoiType> workStation, ImmutableSet<Item> gatherableItems) {
		return register(name, workStation, gatherableItems, ImmutableSet.of());
	}

	static VillagerProfession register(ResourceLocation name, ResourceKey<PoiType> workStation, ImmutableSet<Item> gatherableItems, ImmutableSet<Block> secondaryJobSites) {
		return register(name, workStation, gatherableItems, secondaryJobSites, null);
	}

	static VillagerProfession register(ResourceLocation name, ResourceKey<PoiType> workStation, ImmutableSet<Item> gatherableItems, ImmutableSet<Block> secondaryJobSites, SoundEvent workSound) {
		return Registry.register(BuiltInRegistries.VILLAGER_PROFESSION, name,
				new VillagerProfession(name.toString(), holder -> holder.is(workStation), PoiType.NONE,
						gatherableItems, secondaryJobSites, workSound));
	}

	static VillagerProfession register(String name, ResourceKey<PoiType> workStation, ImmutableSet<Item> gatherableItems, ImmutableSet<Block> secondaryJobSites, SoundEvent workSound) {
		return Registry.register(BuiltInRegistries.VILLAGER_PROFESSION, name,
				new VillagerProfession(name, holder -> holder.is(workStation), PoiType.NONE,
						gatherableItems, secondaryJobSites, workSound));
	}

}
