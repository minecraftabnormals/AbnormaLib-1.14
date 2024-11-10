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

import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.world.item.Item;

import io.github.vampirestudios.vampirelib.api.datagen.builder.ModelBuilder;

/**
 * Fabric-provided extensions for {@link net.minecraft.client.data.models.ItemModelGenerators}.
 *
 * <p>Note: This interface is automatically implemented on all generators via Mixin and interface injection.
 */
public interface FabricItemModelGenerator {
	/**
	 * Generates an item's model based on the given model template and corresponding texture mapping. Both the model and
	 * mapping must be made with the same set of texture keys.
	 *
	 * @param item The item to generate this model for.
	 * @param model The desired template to generate this model out of.
	 * @param textureMap The desired texture mapping for this model.
	 */
	default void register(Item item, ModelTemplate model, TextureMapping textureMap) {
		throw new UnsupportedOperationException("Implemented via mixin");
	}

	/**
	 * Generates an item's model based on an already-prepared {@link ModelBuilder}. Textures get automatically mapped to
	 * the resulting model when built.
	 *
	 * @param item The item to generate this model for.
	 * @param modelBuilder The desired model builder to generate from.
	 */
	default void build(Item item, ModelBuilder<?> modelBuilder) {
		throw new UnsupportedOperationException("Implemented via mixin");
	}

	/**
	 * Generates an item's model based on the given model template and corresponding texture mapping. Both the model and
	 * mapping must be made with the same set of texture keys.
	 *
	 * @param item The item to generate this model for.
	 * @param suffix An optional suffix for the generated model's file name.
	 * @param model The desired template to generate this model out of.
	 * @param textureMap The desired texture mapping for this model.
	 */
	default void register(Item item, String suffix, ModelTemplate model, TextureMapping textureMap) {
		throw new UnsupportedOperationException("Implemented via mixin");
	}

	/**
	 * Generates an item's model based on an already-prepared {@link ModelBuilder}. Textures get automatically mapped to
	 * the resulting model when built.
	 *
	 * @param item The item to generate this model for.
	 * @param suffix An optional suffix for the generated model's file name.
	 * @param modelBuilder The desired model builder to generate from.
	 */
	default void build(Item item, String suffix, ModelBuilder<?> modelBuilder) {
		throw new UnsupportedOperationException("Implemented via mixin");
	}
}
