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

import java.util.List;

import com.google.common.collect.ImmutableSet;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class VillagerProfessionCreator {

	private List<VillagerHouse> villagerHouses;
	private boolean hasHouse;
	private ResourceLocation name;
	private ResourceKey<PoiType> pointOfInterest;
	private ImmutableSet<Item> gatherableItems;
	private ImmutableSet<Block> secondaryJobSites;

	public static VillagerProfessionCreator creator() {
		return new VillagerProfessionCreator();
	}

	public VillagerProfessionCreator hasHouse(boolean hasHouse) {
		this.hasHouse = hasHouse;
		return this;
	}

	public VillagerProfessionCreator villagerHouses(List<VillagerHouse> villagerHouses) {
		this.villagerHouses = villagerHouses;
		return this;
	}

	public VillagerProfessionCreator name(ResourceLocation name) {
		this.name = name;
		return this;
	}

	public VillagerProfessionCreator pointOfInterest(ResourceKey<PoiType> pointOfInterest) {
		this.pointOfInterest = pointOfInterest;
		return this;
	}

	public VillagerProfessionCreator gatherableItems(ImmutableSet<Item> gatherableItems) {
		this.gatherableItems = gatherableItems;
		return this;
	}

	public VillagerProfessionCreator secondaryJobSites(ImmutableSet<Block> secondaryJobSites) {
		this.secondaryJobSites = secondaryJobSites;
		return this;
	}

	public VillagerProfession register() {
		if (hasHouse) {
            /*StructurePoolAddCallback.EVENT.register(initialPool -> villagerHouses.forEach(villagerHouse -> {
                if(initialPool.getUnderlying().getId().toString().equals(villagerHouse.getPool())) {
                    initialPool.addStructurePoolElement(new SinglePoolElement(villagerHouse.isCustomHouse() ? villagerHouse.getStructureName() :
                                    villagerHouse.getPool() + "/" + villagerHouse.getStructureName()), 1);
                }
            }));*/
		}

		return VillagerProfessionRegistry.register(name, pointOfInterest, gatherableItems, secondaryJobSites);
	}

}
