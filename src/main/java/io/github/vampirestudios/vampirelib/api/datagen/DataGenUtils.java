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

import static net.minecraft.client.data.models.BlockModelGenerators.createBooleanModelDispatch;
import static net.minecraft.client.data.models.BlockModelGenerators.createSimpleBlock;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.blockstates.Variant;
import net.minecraft.client.data.models.blockstates.VariantProperties;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;

import io.github.vampirestudios.vampirelib.api.BasicModClass;
import io.github.vampirestudios.vampirelib.utils.ResourceLocationUtils;

public class DataGenUtils {

	public DataGenUtils(BasicModClass instance) {
		ResourceLocationUtils.setModInstance(instance);
	}

	public static void generateSapling(String path, Block block, Block pottedBlock, BlockModelGenerators generators) {
		TextureMapping saplingMapping = TextureMapping.cross(ResourceLocationUtils.modId(path));
		TextureMapping pottedSaplingMapping = TextureMapping.plant(ResourceLocationUtils.modId(path));

		ResourceLocation sapling = BlockModelGenerators.PlantType.NOT_TINTED.getCross()
				.create(block, saplingMapping, generators.modelOutput);
		generators.blockStateOutput.accept(createSimpleBlock(block, sapling));
		ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(block.asItem()),
				TextureMapping.layer0(ResourceLocationUtils.modId(path)),
				generators.modelOutput);

		ResourceLocation pottedSapling = BlockModelGenerators.PlantType.NOT_TINTED.getCrossPot()
				.create(pottedBlock, pottedSaplingMapping, generators.modelOutput);
		generators.blockStateOutput.accept(createSimpleBlock(pottedBlock, pottedSapling));
	}

	public static void generateFlatModel(Item item, String path, ItemModelGenerators modelOutput) {
		ModelTemplates.FLAT_ITEM.create(
				ModelLocationUtils.getModelLocation(item),
				TextureMapping.layer0(ResourceLocationUtils.modId(path)),
				modelOutput.modelOutput
		);
	}

	public static void generateFlatModel(Block item, String path, BlockModelGenerators modelOutput) {
		ModelTemplates.FLAT_ITEM.create(
				ModelLocationUtils.getModelLocation(item),
				TextureMapping.layer0(ResourceLocationUtils.modId(path)),
				modelOutput.modelOutput
		);
	}

	public static void generateFlatHandheldModel(Item item, String path, ItemModelGenerators modelOutput) {
		ModelTemplates.FLAT_HANDHELD_ITEM.create(
				ModelLocationUtils.getModelLocation(item),
				TextureMapping.layer0(ResourceLocationUtils.modId(path)),
				modelOutput.modelOutput
		);
	}

	public static void generateSimpleCubeModels(BlockModelGenerators blockStateModelGenerator, ResourceLocation resourceLocation, Block block) {
		TextureMapping brimstoneCoalOreTextureMapping = TextureMapping.cube(resourceLocation);
		ResourceLocation brimstoneCoalOreResourceLocation = ModelTemplates.CUBE_ALL.create(block,
				brimstoneCoalOreTextureMapping,
				blockStateModelGenerator.modelOutput);
		blockStateModelGenerator.blockStateOutput.accept(createSimpleBlock(block, brimstoneCoalOreResourceLocation));
		blockStateModelGenerator.registerSimpleItemModel(block, ModelLocationUtils.getModelLocation(block));
	}

	public static void generateConnectedCubeModels(
			BlockModelGenerators blockStateModelGenerator, ResourceLocation resourceLocation,
			ResourceLocation resourceLocation2, Block block, Property<Boolean> property
	) {
		ResourceLocation notConnectedResourceLocation = ModelTemplates.CUBE_ALL.create(block, TextureMapping.cube(
						resourceLocation),
				blockStateModelGenerator.modelOutput);
		ResourceLocation connectedResourceLocation = ModelTemplates.CUBE_ALL.create(block, TextureMapping.cube(
						resourceLocation2),
				blockStateModelGenerator.modelOutput);
		blockStateModelGenerator.blockStateOutput.accept(
				createConnectedBlock(block, property, connectedResourceLocation, notConnectedResourceLocation));
		blockStateModelGenerator.registerSimpleItemModel(block, notConnectedResourceLocation);
	}

	public static void generateConnectedTopBottomSideModels(
			BlockModelGenerators blockStateModelGenerator, ResourceLocation notConnectedTexture,
			Triple<ResourceLocation, ResourceLocation, ResourceLocation> connectedTextures, Block block,
			Property<Boolean> property
	) {
		TextureMapping connectedTextureMapping = new TextureMapping()
				.put(TextureSlot.TOP, connectedTextures.getLeft())
				.put(TextureSlot.SIDE, connectedTextures.getMiddle())
				.put(TextureSlot.BOTTOM, connectedTextures.getRight());
		ResourceLocation notConnectedResourceLocation = ModelTemplates.CUBE_ALL.create(block, TextureMapping.cube(
						notConnectedTexture),
				blockStateModelGenerator.modelOutput);
		ResourceLocation connectedResourceLocation = ModelTemplates.CUBE_BOTTOM_TOP.createWithSuffix(block,
				"_connected",
				connectedTextureMapping,
				blockStateModelGenerator.modelOutput);
		blockStateModelGenerator.blockStateOutput.accept(
				createConnectedBlock(block, property, connectedResourceLocation, notConnectedResourceLocation));
		blockStateModelGenerator.registerSimpleItemModel(block, notConnectedResourceLocation);
	}

	public static void generateConnectedEndSideModels(
			BlockModelGenerators blockStateModelGenerator, ResourceLocation notConnectedTexture,
			Pair<ResourceLocation, ResourceLocation> connectedTextures, Block block,
			Property<Boolean> property
	) {
		TextureMapping connectedTextureMapping = new TextureMapping()
				.put(TextureSlot.END, connectedTextures.getLeft())
				.put(TextureSlot.SIDE, connectedTextures.getRight());
		ResourceLocation notConnectedResourceLocation = ModelTemplates.CUBE_ALL.create(block, TextureMapping.cube(notConnectedTexture),
				blockStateModelGenerator.modelOutput);
		ResourceLocation connectedResourceLocation = ModelTemplates.CUBE_COLUMN.create(block, connectedTextureMapping, blockStateModelGenerator.modelOutput);
		blockStateModelGenerator.blockStateOutput.accept(createConnectedBlock(block, property, connectedResourceLocation, notConnectedResourceLocation));
		blockStateModelGenerator.registerSimpleItemModel(block, notConnectedResourceLocation);
	}

	public static void generateSimpleColumnModels(BlockModelGenerators blockStateModelGenerator, ResourceLocation side, ResourceLocation end, Block block) {
		TextureMapping textureMapping = new TextureMapping().put(TextureSlot.SIDE, side).put(TextureSlot.END, end);
		ResourceLocation model = ModelTemplates.CUBE_COLUMN.create(block, textureMapping, blockStateModelGenerator.modelOutput);
		blockStateModelGenerator.blockStateOutput.accept(createSimpleBlock(block, model));
		blockStateModelGenerator.registerSimpleItemModel(block, ModelLocationUtils.getModelLocation(block));
	}

	public static void generateSimpleColumnModels(BlockModelGenerators blockStateModelGenerator, TextureMapping textureMapping, Block block) {
		ResourceLocation model = ModelTemplates.CUBE_COLUMN.create(block, textureMapping, blockStateModelGenerator.modelOutput);
		blockStateModelGenerator.blockStateOutput.accept(createSimpleBlock(block, model));
		blockStateModelGenerator.registerSimpleItemModel(block, ModelLocationUtils.getModelLocation(block));
	}

	public static void generateSimpleTopBottomModels(BlockModelGenerators blockStateModelGenerator, ResourceLocation side, ResourceLocation top, ResourceLocation bottom, Block block) {
		TextureMapping textureMapping = new TextureMapping().put(TextureSlot.SIDE, side).put(TextureSlot.TOP, top).put(TextureSlot.BOTTOM, bottom);
		ResourceLocation model = ModelTemplates.CUBE_BOTTOM_TOP.create(block, textureMapping, blockStateModelGenerator.modelOutput);
		blockStateModelGenerator.blockStateOutput.accept(createSimpleBlock(block, model));
		blockStateModelGenerator.registerSimpleItemModel(block, ModelLocationUtils.getModelLocation(block));
	}

	public static void createCarpet(BlockModelGenerators blockStateModelGenerator, Block block, ResourceLocation texture) {
		blockStateModelGenerator.createTrivialBlock(block, TexturedModel.CARPET.updateTexture(
				textureMapping -> textureMapping.put(TextureSlot.WOOL, texture)));
		ResourceLocation resourceLocation2 = ModelLocationUtils.getModelLocation(block);
		blockStateModelGenerator.registerSimpleItemModel(block, resourceLocation2);
	}

	public static void createCaveVines(Block vines, Block plant, BlockModelGenerators blockStateModelGenerator) {
		ResourceLocation resourceLocation = blockStateModelGenerator.createSuffixedVariant(vines, "",
				ModelTemplates.CROSS, TextureMapping::cross);
		ResourceLocation resourceLocation2 = blockStateModelGenerator.createSuffixedVariant(vines, "_lit",
				ModelTemplates.CROSS, TextureMapping::cross);
		blockStateModelGenerator.blockStateOutput.accept(MultiVariantGenerator.multiVariant(vines).with(
				createBooleanModelDispatch(BlockStateProperties.BERRIES, resourceLocation2, resourceLocation)));
		ResourceLocation resourceLocation3 = blockStateModelGenerator.createSuffixedVariant(plant, "",
				ModelTemplates.CROSS, TextureMapping::cross);
		ResourceLocation resourceLocation4 = blockStateModelGenerator.createSuffixedVariant(plant, "_lit",
				ModelTemplates.CROSS, TextureMapping::cross);
		blockStateModelGenerator.blockStateOutput.accept(MultiVariantGenerator.multiVariant(plant).with(
				createBooleanModelDispatch(BlockStateProperties.BERRIES, resourceLocation4, resourceLocation3)));
	}

	public static MultiVariantGenerator createConnectedBlock(Block block, Property<Boolean> property, ResourceLocation modelLocation, ResourceLocation modelLocation2) {
		return MultiVariantGenerator.multiVariant(block).with(PropertyDispatch.property(property)
				.select(true, Variant.variant().with(VariantProperties.MODEL, modelLocation))
				.select(false, Variant.variant().with(VariantProperties.MODEL, modelLocation2))
		);
	}
}
