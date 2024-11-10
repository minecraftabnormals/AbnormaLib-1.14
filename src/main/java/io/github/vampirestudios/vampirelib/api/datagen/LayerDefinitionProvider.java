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

import java.util.function.BiConsumer;

import net.minecraft.client.model.BatModel;
import net.minecraft.client.model.SnifferModel;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import io.github.vampirestudios.vampirelib.client.EntityModelCodecHolder;

public class LayerDefinitionProvider extends JsonCodecProvider<LayerDefinition> {
	public LayerDefinitionProvider(PackOutput output) {
		super(output, PackOutput.Target.RESOURCE_PACK, "models/entity", EntityModelCodecHolder.LAYER_DEFINITION_CODEC);
	}

	@Override
	protected void configure(BiConsumer<ResourceLocation, LayerDefinition> provider) {
		provider.accept(ResourceLocation.withDefaultNamespace("sniffer"), SnifferModel.createBodyLayer());
		provider.accept(ResourceLocation.withDefaultNamespace("bat"), BatModel.createBodyLayer());
	}

	@Override
	public String getName() {
		return "Layer Definition Generator";
	}
}
