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

package io.github.vampirestudios.vampirelib.mixins;

import java.util.function.BiConsumer;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import io.github.vampirestudios.vampirelib.api.datagen.FabricItemModelGenerator;

@Mixin(ItemModelGenerators.class)
public class ItemModelGeneratorMixin implements FabricItemModelGenerator {
	@Shadow
	@Final
	public BiConsumer<ResourceLocation, ModelInstance> modelOutput;

	@Override
	public void register(Item item, ModelTemplate model, TextureMapping textureMap) {
		model.create(ModelLocationUtils.getModelLocation(item), textureMap, this.modelOutput);
	}

	@Override
	public void register(Item item, String suffix, ModelTemplate model, TextureMapping textureMap) {
		model.create(ModelLocationUtils.getModelLocation(item, suffix), textureMap, this.modelOutput);
	}
}
