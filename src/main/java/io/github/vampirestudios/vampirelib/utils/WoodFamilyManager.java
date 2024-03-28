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

package io.github.vampirestudios.vampirelib.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;

//TODO: vanilla block variants? (BlockFamily)
//TODO: generalize beyond wood? (already partly there)
//TODO: integration with the Variant Selector? (kindof a different goal)
public abstract class WoodFamilyManager {
	@SuppressWarnings("unused")
	public record Variant<T>(String name) { }

	public static final Variant<Block> LOG = new Variant<>("log");
	public static final Variant<Block> WOOD = new Variant<>("wood");
	public static final Variant<Block> PLANKS = new Variant<>("planks");
	public static final Variant<Block> STRIPPED_LOG = new Variant<>("stripped_log");
	public static final Variant<Block> STRIPPED_WOOD = new Variant<>("stripped_wood");
	public static final Variant<Block> SLAB = new Variant<>("slab");
	public static final Variant<Block> STAIRS = new Variant<>("stairs");
	public static final Variant<Block> FENCE = new Variant<>("fence");
	public static final Variant<Block> FENCE_GATE = new Variant<>("fence_gate");
	public static final Variant<Block> DOOR = new Variant<>("door");
	public static final Variant<Block> TRAPDOOR = new Variant<>("trapdoor");
	public static final Variant<Block> BUTTON = new Variant<>("button");
	public static final Variant<Block> PRESSURE_PLATE = new Variant<>("pressure_plate");
	public static final Variant<Block> SIGN = new Variant<>("sign");
	public static final Variant<Block> WALL_SIGN = new Variant<>("wall_sign");

	public static final Variant<Item> BOAT = new Variant<>("boat");
	public static final Variant<Item> CHEST_BOAT = new Variant<>("chest_boat");

	public static final Variant<WoodType> WOOD_TYPE = new Variant<>("wood_type");

	private final Map<Object, WoodFamily> families = new HashMap<>();

	//non-static
	public class WoodFamily {
		private final Map<Variant<?>, Object> variants = new HashMap<>();
		private final WoodType root;

		public WoodFamily(WoodType root) {
			this.root = root;
		}

		@SuppressWarnings("unchecked")
		public <T> @Nullable T get(Variant<T> key) {
			return (T) variants.get(key);
		}

		private <T> void put(Variant<T> key, @NotNull T value) {
			variants.put(key, value);
			families.put(value, this); // <- allows getFamily to work
		}

		public Collection<Object> entries() {
			return variants.values();
		}
	}

	/**
	 * Create a new wood family w/ this at the root.
	 */
	public WoodFamily getOrCreateFamily(WoodType root) {
		return families.computeIfAbsent(root, __ -> new WoodFamily(root));
	}

	/**
	 * Given any object in the family, return the rest of the family.
	 */
	public @Nullable WoodFamily getFamily(Object member) {
		return families.get(member);
	}

	/**
	 * Given any object in the family, return an object of a different variant.
	 */
	public <T> @Nullable T get(Object otherMember, Variant<T> variant) {
		WoodFamily family = families.get(otherMember);
		return family == null ? null : family.get(variant);
	}

	//TODO: make into a builder-style api
	public void makeWoodFamily(String name, MapColor color, MapColor barkColor, boolean hasLog, boolean hasBoat, boolean flammable) {
//		WoodType woodType = registerWoodType(module.zeta.registry.newResourceLocation(name).toString());
//		WoodFamily family = getOrCreateFamily(woodType);

		//TODO: everything
	}

	public abstract WoodType registerWoodType(String name);
}
