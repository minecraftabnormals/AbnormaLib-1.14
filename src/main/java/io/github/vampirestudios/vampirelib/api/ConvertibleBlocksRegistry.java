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

package io.github.vampirestudios.vampirelib.api;

import java.util.Objects;

import net.minecraft.world.level.block.Block;

import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;

import io.github.vampirestudios.vampirelib.VampireLib;

/**
 * Provides methods for registering convertible blocks.
 */
public final class ConvertibleBlocksRegistry {
	private ConvertibleBlocksRegistry() {
	}

	/**
	 * Registers a link between two different blocks.
	 * (See for example {@link OxidizableBlocksRegistry#registerOxidizableBlockPair(Block, Block)} and
	 * {@link OxidizableBlocksRegistry#registerWaxableBlockPair(Block, Block)})
	 *
	 * @param convertibleBlockPair the convertible block pair
	 */
	public static void registerConvertibleBlockPair(ConvertibleBlockPair convertibleBlockPair) {
		Objects.requireNonNull(convertibleBlockPair, "ConvertibleBlockPair cannot be null!");
		VampireLib.CONVERTIBLE_BLOCKS.add(convertibleBlockPair);
	}
}
