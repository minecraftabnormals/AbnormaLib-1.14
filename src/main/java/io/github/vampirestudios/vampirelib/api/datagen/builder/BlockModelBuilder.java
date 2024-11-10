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

package io.github.vampirestudios.vampirelib.api.datagen.builder;

import java.util.Map;

import com.google.common.base.Preconditions;

import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;

import io.github.vampirestudios.vampirelib.mixins.TextureMapAccessor;

/**
 * Dedicated builder class for standard Minecraft block models.
 */
public class BlockModelBuilder extends ModelBuilder<BlockModelBuilder> {
	private boolean occlude = true;

	private BlockModelBuilder(ResourceLocation parent) {
		super(parent);
	}

	public static BlockModelBuilder createNew(ResourceLocation parent) {
		return new BlockModelBuilder(parent);
	}

	public static BlockModelBuilder copyFrom(ModelTemplate model, TextureMapping textures) {
		Map<TextureSlot, ResourceLocation> copyTextures = ((TextureMapAccessor) textures).getEntries();
		Preconditions.checkArgument(copyTextures.keySet().equals(model.fabric_getRequiredTextures()), "Texture map does not match slots for provided model " + model);

		BlockModelBuilder builder = new BlockModelBuilder(model.fabric_getParent().orElse(null));
		copyTextures.forEach(builder::addTexture);

		model.fabric_getDisplayBuilders().forEach(builder::addDisplay);
		model.fabric_getElementBuilders().forEach(builder::addElement);
		builder.occlude = model.fabric_getAmbientOcclusion();

		return builder;
	}

	/**
	 * Toggles ambient occlusion for this model.
	 */
	public BlockModelBuilder occludes(boolean occlude) {
		this.occlude = occlude;
		return this;
	}

	@Override
	public ModelTemplate buildModel() {
		ModelTemplate blockModel = super.buildModel();
		blockModel.fabric_setAmbientOcclusion(occlude);
		return blockModel;
	}
}
