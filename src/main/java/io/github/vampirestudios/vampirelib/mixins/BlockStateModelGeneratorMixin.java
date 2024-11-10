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
import java.util.function.Consumer;

import com.google.gson.JsonObject;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.blockstates.BlockStateGenerator;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.Variant;
import net.minecraft.client.data.models.blockstates.VariantProperties;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import io.github.vampirestudios.vampirelib.api.datagen.FabricBlockStateModelGenerator;
import io.github.vampirestudios.vampirelib.api.datagen.builder.ModelBuilder;

@Mixin(BlockModelGenerators.class)
public abstract class BlockStateModelGeneratorMixin implements FabricBlockStateModelGenerator {
	@Shadow
	@Final
	public BiConsumer<ResourceLocation, ModelInstance> modelOutput;

	@Shadow
	@Final
	public Consumer<BlockStateGenerator> blockStateOutput;


	@Override
	public void registerEmptyModel(Block block) {
		registerEmptyModel(block, ModelLocationUtils.getModelLocation(block));
	}

	@Override
	public void registerEmptyModel(Block block, ResourceLocation id) {
		this.modelOutput.accept(id, JsonObject::new);
		this.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, id));
	}

	@Override
	public void buildWithSingletonState(Block block, ModelBuilder<?> builder) {
		ResourceLocation model = builder.buildModel().create(block, builder.mapTextures(), this.modelOutput);
		this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block, Variant.variant().with(VariantProperties.MODEL, model)));
	}
}
