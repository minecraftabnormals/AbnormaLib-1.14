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

package io.github.vampirestudios.vampirelib.api.datagen;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import io.github.vampirestudios.vampirelib.api.datagen.builder.ModelBuilder;

/**
 * Fabric-provided extensions for {@link net.minecraft.data.models.BlockModelGenerators}.
 *
 * <p>Note: This interface is automatically implemented on all generators via Mixin and interface injection.
 */
public interface FabricBlockStateModelGenerator {
	/**
	 * Create a singleton block state and empty model file for this block. Useful in special cases where a block may
	 * be rendered/modelled entirely through code rather than a standard model.
	 *
	 * @param block The block to register a block state and model for.
	 */
	default void registerEmptyModel(Block block) {
		throw new UnsupportedOperationException("Implemented via mixin");
	}

	/**
	 * Create a singleton block state and empty model file with the given ID for this block. Useful in special cases
	 * where a block may be rendered/modelled entirely through code rather than a standard model.
	 *
	 * @param block The block to register a block state and model for.
	 * @param id   The ID/path for the generated "model".
	 */
	default void registerEmptyModel(Block block, ResourceLocation id) {
		throw new UnsupportedOperationException("Implemented via mixin");
	}

	/**
	 * Create a singleton block state and model file to be built by an instanced @{link ModelBuilder} for a given block.
	 *
	 * @param block The block to register a block state and model for.
	 * @param builder The {@link ModelBuilder} from which to build a block model.
	 */
	default void buildWithSingletonState(Block block, ModelBuilder<?> builder) {
		throw new UnsupportedOperationException("Implemented via mixin");
	}
}
