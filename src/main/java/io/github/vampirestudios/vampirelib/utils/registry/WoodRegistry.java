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

package io.github.vampirestudios.vampirelib.utils.registry;

import static net.minecraft.data.models.BlockModelGenerators.createDoor;
import static net.minecraft.data.models.BlockModelGenerators.createEmptyOrFullDispatch;
import static net.minecraft.data.models.BlockModelGenerators.createHorizontalFacingDispatch;
import static net.minecraft.data.models.BlockModelGenerators.createSimpleBlock;
import static net.minecraft.data.models.BlockModelGenerators.createTrapdoor;
import static net.minecraft.data.recipes.RecipeProvider.has;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.terraformersmc.terraform.boat.api.TerraformBoatType;
import com.terraformersmc.terraform.boat.api.TerraformBoatTypeRegistry;
import com.terraformersmc.terraform.boat.api.client.TerraformBoatClientHelper;
import com.terraformersmc.terraform.boat.api.item.TerraformBoatItemHelper;
import com.terraformersmc.terraform.sign.block.TerraformHangingSignBlock;
import com.terraformersmc.terraform.sign.block.TerraformSignBlock;
import com.terraformersmc.terraform.sign.block.TerraformWallHangingSignBlock;
import com.terraformersmc.terraform.sign.block.TerraformWallSignBlock;
import org.joml.Vector3d;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.blockstates.Condition;
import net.minecraft.data.models.blockstates.MultiPartGenerator;
import net.minecraft.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.data.models.model.TexturedModel;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.ChiseledBookShelfBlock;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.fabricmc.loader.api.FabricLoader;

import io.github.vampirestudios.vampirelib.api.datagen.CustomTagProviders;
import io.github.vampirestudios.vampirelib.api.datagen.ElementBuilder;
import io.github.vampirestudios.vampirelib.api.datagen.FaceBuilder;
import io.github.vampirestudios.vampirelib.api.datagen.builder.BlockModelBuilder;
import io.github.vampirestudios.vampirelib.blocks.CustomLadderBlock;
import io.github.vampirestudios.vampirelib.blocks.FlowerPotBaseBlock;
import io.github.vampirestudios.vampirelib.blocks.FungusBaseBlock;
import io.github.vampirestudios.vampirelib.blocks.SaplingBaseBlock;
import io.github.vampirestudios.vampirelib.blocks.entity.IBlockEntityType;
import io.github.vampirestudios.vampirelib.client.VampireLibClient;
import io.github.vampirestudios.vampirelib.utils.Utils;
import io.github.vampirestudios.vampirelib.utils.WoodMaterial;

public class WoodRegistry {
	private ResourceLocation name;
	private TreeGrower saplingGenerator;
	private ResourceKey<ConfiguredFeature<?, ?>> fungusGenerator;
	private Block baseFungusBlock;

	private final List<String> availableLeaves = new ArrayList<>();
	private final List<String> availableFloweryLeaves = new ArrayList<>();
	private final List<String> availableSaplings = new ArrayList<>();
	private final List<String> availablePottedSaplings = new ArrayList<>();
	public TagKey<Block> logsTag;
	public TagKey<Item> logsItemTag;
	private Block log;
	private Block wood;
	private Block strippedLog;
	private Block strippedWood;
	private Block stairs;
	private Block slab;
	private Block planks;
	private Block leaves;
	private Block floweryLeaves;
	private Block sapling;
	private Block pottedSapling;
	private Block fence;
	private Block fenceGate;
	private Block bookshelf;
	private Block chiseledBookshelf;
	private Block door;
	private Block trapdoor;
	private Block button;
	private Block pressurePlate;
	private Block sign;
	private Block wallSign;
	private Block hangingSign;
	private Block hangingWallSign;
	private Block ladder;
	private Block beehive;
	private Block mosaic;
	private Block mosaicStairs;
	private Block mosaicSlab;
	private Item signItem;
	private Item hangingSignItem;
	private Item boatItem;
	private Item chestBoatItem;
	private ResourceKey<TerraformBoatType> boatType;
	private boolean flammable = true;
	public WoodPropertyType woodPropertyType = WoodPropertyType.OVERWORLD;

	public boolean preRegisteredPlanks = false;

	public boolean preRegisteredLog = false;

	public boolean preRegisteredLeaves = false;

	public WoodRegistry() {}

	private WoodRegistry(ResourceLocation name) {
		this(name, null, null);
	}

	private WoodRegistry(ResourceLocation name, TreeGrower saplingGenerator) {
		this(name, saplingGenerator, null, null);
	}

	private WoodRegistry(ResourceLocation name, ResourceKey<ConfiguredFeature<?, ?>> fungusGenerator, Block baseFungusBlock) {
		this(name, null, fungusGenerator, baseFungusBlock);
	}

	private WoodRegistry(ResourceLocation name, TreeGrower saplingGenerator, ResourceKey<ConfiguredFeature<?, ?>> fungusGenerator, Block baseFungusBlock) {
		setName(name);
		setSaplingGenerator(saplingGenerator);
		setFungusGenerator(fungusGenerator);
		setBaseFungusBlock(baseFungusBlock);
		this.logsTag = TagKey.create(Registries.BLOCK, Utils.appendToPath(name, "_logs"));
		this.logsItemTag = TagKey.create(Registries.ITEM, Utils.appendToPath(name, "_logs"));
	}

	private void setSaplingGenerator(TreeGrower abstractTreeGrower) {
		this.saplingGenerator = abstractTreeGrower;
	}

	private void setName(ResourceLocation name) {
		this.name = name;
	}

	private void setFungusGenerator(ResourceKey<ConfiguredFeature<?, ?>> fungusGenerator) {
		this.fungusGenerator = fungusGenerator;
	}

	private void setBaseFungusBlock(Block baseFungusBlock) {
		this.baseFungusBlock = baseFungusBlock;
	}

	public static Builder create() {
		return new WoodRegistry.Builder();
	}

	public static WoodRegistry.Builder of(ResourceLocation name) {
		return create().of(name);
	}

	public static WoodRegistry.Builder of(ResourceLocation name, Block planks) {
		return create().of(name, planks);
	}

	public static WoodRegistry.Builder of(ResourceLocation name, TreeGrower saplingGenerator) {
		return create().of(name, saplingGenerator);
	}

	public static WoodRegistry.Builder of(ResourceLocation name, ResourceKey<ConfiguredFeature<?, ?>> fungusGenerator, Block baseFungusBlock) {
		return create().of(name, fungusGenerator, baseFungusBlock);
	}

	public static WoodRegistry.Builder of(WoodMaterial woodMaterial) {
		return create().of(woodMaterial);
	}

	public static WoodRegistry.Builder of(WoodMaterial woodMaterial, TreeGrower saplingGenerator) {
		return create().of(woodMaterial, saplingGenerator);
	}

	public static WoodRegistry.Builder of(WoodMaterial woodMaterial, ResourceKey<ConfiguredFeature<?, ?>> fungusGenerator, Block baseFungusBlock) {
		return create().of(woodMaterial, fungusGenerator, baseFungusBlock);
	}

	public ResourceLocation name() {
		return name;
	}

	public List<String> availableLeaves() {
		return availableLeaves;
	}

	public List<String> availableFloweryLeaves() {
		return availableFloweryLeaves;
	}

	public List<String> availableSaplings() {
		return availableSaplings;
	}

	public Block log() {
		return log;
	}

	public Block wood() {
		return wood;
	}

	public Block strippedLog() {
		return strippedLog;
	}

	public Block strippedWood() {
		return strippedWood;
	}

	public Block stairs() {
		return stairs;
	}

	public Block slab() {
		return slab;
	}

	public Block planks() {
		return planks;
	}

	public Block leaves() {
		return leaves;
	}

	public Block floweryLeaves() {
		return floweryLeaves;
	}

	public Block sapling() {
		return sapling;
	}

	public Block pottedSapling() {
		return pottedSapling;
	}

	public Block fence() {
		return fence;
	}

	public Block fenceGate() {
		return fenceGate;
	}

	public Block bookshelf() {
		return bookshelf;
	}

	public Block chiseledBookshelf() {
		return chiseledBookshelf;
	}

	public Block door() {
		return door;
	}

	public Block trapdoor() {
		return trapdoor;
	}

	public Block button() {
		return button;
	}

	public Block pressurePlate() {
		return pressurePlate;
	}

	public Block ladder() {
		return ladder;
	}

	public Block beehive() {
		return beehive;
	}

	public Block sign() {
		return sign;
	}

	public Block wallSign() {
		return wallSign;
	}

	public Block hangingSign() {
		return hangingSign;
	}

	public Block hangingWallSign() {
		return hangingWallSign;
	}

	public TreeGrower saplingGenerator() {
		return saplingGenerator;
	}

	public ResourceKey<ConfiguredFeature<?, ?>> fungusGenerator() {
		return fungusGenerator;
	}

	public TagKey<Block> logsTag() {
		return logsTag;
	}

	public TagKey<Item> logsItemTag() {
		return logsItemTag;
	}

	public Item signItem() {
		return signItem;
	}

	public Item hangingSignItem() {
		return hangingSignItem;
	}

	public Item boatItem() {
		return boatItem;
	}

	public Item chestBoatItem() {
		return chestBoatItem;
	}

	public void generateBlockTags(CustomTagProviders.CustomBlockTagProvider blockTags) {
		if (log != null) blockTags.tag(logsTag).add(log);
		if (strippedLog != null) blockTags.tag(logsTag).add(strippedLog);
		if (wood != null) blockTags.tag(logsTag).add(wood);
		if (strippedWood != null) blockTags.tag(logsTag).add(strippedWood);

		if ((log != null || strippedLog != null || wood != null || strippedWood != null) && logsTag != null)
			blockTags.tag(BlockTags.LOGS).addTag(logsTag);

		if (planks != null) blockTags.tag(BlockTags.PLANKS).add(planks);
		if (!availableLeaves.isEmpty() && !isNetherWood()) {
			availableLeaves.forEach(s -> {
				Block block = BuiltInRegistries.BLOCK.get(new ResourceLocation(name.getNamespace(), s));
				blockTags.tag(BlockTags.LEAVES).add(block);
			});
		}
		if (!availableLeaves.isEmpty() && isNetherWood()) {
			availableLeaves.forEach(s -> {
				Block block = BuiltInRegistries.BLOCK.get(new ResourceLocation(name.getNamespace(), s));
				blockTags.tag(BlockTags.WART_BLOCKS).add(block);
			});
		}
		if (leaves != null && !isNetherWood()) blockTags.tag(BlockTags.LEAVES).add(leaves);
		if (leaves != null && isNetherWood()) blockTags.tag(BlockTags.WART_BLOCKS).add(leaves);
		if (!availableFloweryLeaves.isEmpty() && !isNetherWood()) {
			availableFloweryLeaves.forEach(s -> {
				Block block = BuiltInRegistries.BLOCK.get(new ResourceLocation(name.getNamespace(), s));
				blockTags.tag(BlockTags.LEAVES).add(block);
			});
		}
		if (!availableFloweryLeaves.isEmpty() && isNetherWood()) {
			availableFloweryLeaves.forEach(s -> {
				Block block = BuiltInRegistries.BLOCK.get(new ResourceLocation(name.getNamespace(), s));
				blockTags.tag(BlockTags.WART_BLOCKS).add(block);
			});
		}
		if (floweryLeaves != null && !isNetherWood()) blockTags.tag(BlockTags.LEAVES).add(floweryLeaves);
		if (floweryLeaves != null && isNetherWood()) blockTags.tag(BlockTags.WART_BLOCKS).add(floweryLeaves);
		if (ladder != null) blockTags.tag(BlockTags.CLIMBABLE).add(ladder);
		if (trapdoor != null) blockTags.tag(BlockTags.WOODEN_TRAPDOORS).add(trapdoor);
		if (button != null) blockTags.tag(BlockTags.WOODEN_BUTTONS).add(button);
		if (door != null) blockTags.tag(BlockTags.WOODEN_DOORS).add(door);
		if (sapling != null) blockTags.tag(BlockTags.SAPLINGS).add(sapling);
		if (sign != null) blockTags.tag(BlockTags.STANDING_SIGNS).add(sign);
		if (wallSign != null) blockTags.tag(BlockTags.WALL_SIGNS).add(wallSign);
		if (hangingSign != null) blockTags.tag(BlockTags.CEILING_HANGING_SIGNS).add(hangingSign);
		if (hangingWallSign != null) blockTags.tag(BlockTags.WALL_HANGING_SIGNS).add(hangingWallSign);
		if (!availableSaplings.isEmpty()) {
			availableSaplings.forEach(s -> {
				Block block = BuiltInRegistries.BLOCK.get(new ResourceLocation(name.getNamespace(), s));
				blockTags.tag(BlockTags.SAPLINGS).add(block);
			});
		}
		if (pottedSapling != null) blockTags.tag(BlockTags.FLOWER_POTS).add(pottedSapling);
		if (!availablePottedSaplings.isEmpty()) {
			availablePottedSaplings.forEach(s -> {
				Block block = BuiltInRegistries.BLOCK.get(new ResourceLocation(name.getNamespace(), s));
				blockTags.tag(BlockTags.FLOWER_POTS).add(block);
			});
		}
		if (fence != null) blockTags.tag(BlockTags.WOODEN_FENCES).add(fence);
		if (fenceGate != null) blockTags.tag(BlockTags.FENCE_GATES).add(fenceGate);
		if (pressurePlate != null) blockTags.tag(BlockTags.PRESSURE_PLATES).add(pressurePlate);
		if (slab != null) blockTags.tag(BlockTags.WOODEN_SLABS).add(slab);
		if (stairs != null) blockTags.tag(BlockTags.WOODEN_STAIRS).add(stairs);
		if (mosaicSlab != null) blockTags.tag(BlockTags.WOODEN_SLABS).add(mosaicSlab);
		if (mosaicStairs != null) blockTags.tag(BlockTags.WOODEN_STAIRS).add(mosaicStairs);
		if (flammable) {
			if ((log != null || strippedLog != null || wood != null || strippedWood != null) && logsTag != null)
				blockTags.tag(BlockTags.LOGS_THAT_BURN).addTag(logsTag);
		}
	}

	public void generateItemTags(CustomTagProviders.CustomItemTagProvider itemsTag) {
		if ((log != null || strippedLog != null || wood != null || strippedWood != null) && logsTag != null &&
				logsItemTag != null) itemsTag.copy(logsTag, logsItemTag);
		itemsTag.copy(BlockTags.LOGS, ItemTags.LOGS);
		itemsTag.copy(BlockTags.LOGS_THAT_BURN, ItemTags.LOGS_THAT_BURN);
		itemsTag.copy(BlockTags.PLANKS, ItemTags.PLANKS);
		itemsTag.copy(BlockTags.LEAVES, ItemTags.LEAVES);
		itemsTag.copy(BlockTags.WART_BLOCKS, ItemTags.WART_BLOCKS);
		itemsTag.copy(BlockTags.WOODEN_TRAPDOORS, ItemTags.WOODEN_TRAPDOORS);
		itemsTag.copy(BlockTags.WOODEN_BUTTONS, ItemTags.WOODEN_BUTTONS);
		itemsTag.copy(BlockTags.WOODEN_DOORS, ItemTags.WOODEN_DOORS);
		itemsTag.copy(BlockTags.SAPLINGS, ItemTags.SAPLINGS);
		itemsTag.copy(BlockTags.WOODEN_FENCES, ItemTags.WOODEN_FENCES);
		itemsTag.copy(BlockTags.WOODEN_SLABS, ItemTags.WOODEN_SLABS);
		itemsTag.copy(BlockTags.WOODEN_STAIRS, ItemTags.WOODEN_STAIRS);
		itemsTag.copy(BlockTags.STANDING_SIGNS, ItemTags.SIGNS);
		if (boatItem != null) itemsTag.tag(ItemTags.BOATS).add(boatItem);
		if (chestBoatItem != null) itemsTag.tag(ItemTags.CHEST_BOATS).add(chestBoatItem);
	}

	@Environment(EnvType.CLIENT)
	public void generateModels(BlockModelGenerators blockStateModelGenerator) {
		this.generateModels(blockStateModelGenerator, false);
	}

	private static final Map<BookSlotModelCacheKey, ResourceLocation> CHISELED_BOOKSHELF_SLOT_MODEL_CACHE = new HashMap<>();

	@Environment(EnvType.CLIENT)
	public void generateModels(BlockModelGenerators blockStateModelGenerator, boolean customPottedTexture) {
		TextureMapping logMapping = new TextureMapping()
				.put(TextureSlot.SIDE, new ResourceLocation(name.getNamespace(),
						String.format("wood_types/%s/%s", name.getPath(),
								isNetherWood() ? "stem" : "log")))
				.put(TextureSlot.END, new ResourceLocation(name.getNamespace(),
						String.format("wood_types/%s/%s_top", name.getPath(),
								isNetherWood() ? "stem" : "log")));
		TextureMapping strippedLogMapping = new TextureMapping()
				.put(TextureSlot.SIDE, new ResourceLocation(name.getNamespace(),
						String.format("wood_types/%s/stripped_%s", name.getPath(),
								isNetherWood() ? "stem" : "log")))
				.put(TextureSlot.END, new ResourceLocation(name.getNamespace(),
						String.format("wood_types/%s/stripped_%s_top", name.getPath(),
								isNetherWood() ? "stem" : "log")));

		TextureMapping leavesMapping = TextureMapping.cube(
				new ResourceLocation(name.getNamespace(), String.format("wood_types/%s/%s", name.getPath(),
						isNetherWood() ? "wart_block" : "leaves")));

		TextureMapping floweryLeavesMapping = TextureMapping.cube(
				new ResourceLocation(name.getNamespace(), String.format("wood_types/%s/%s", name.getPath(),
						"flowery_leaves")));

		TextureMapping saplingMapping = TextureMapping.cross(
				new ResourceLocation(name.getNamespace(), String.format("wood_types/%s/%s", name.getPath(),
						isNetherWood() ? "fungi" : "sapling")));
		TextureMapping saplingPlantMapping = TextureMapping.plant(new ResourceLocation(name.getNamespace(),
				customPottedTexture ? String.format(
						"wood_types/%s/potted_%s",
						name.getPath(),
						isNetherWood() ? "fungi" : "sapling") :
						String.format("wood_types/%s/%s",
								name.getPath(),
								isNetherWood() ? "fungi" : "sapling")));

		TextureMapping planksMapping = TextureMapping.cube(
				new ResourceLocation(name.getNamespace(), String.format("wood_types/%s/planks", name.getPath())));

		TextureMapping mosaicMapping = TextureMapping.cube(
				new ResourceLocation(name.getNamespace(), String.format("wood_types/%s/mosaic", name.getPath())));

		TextureMapping signMapping = TextureMapping.particle(
				new ResourceLocation(name.getNamespace(), String.format("wood_types/%s/sign", name.getPath())));

		TextureMapping hangingSignMapping = TextureMapping.particle(
				new ResourceLocation(name.getNamespace(), String.format("wood_types/%s/hanging_sign", name.getPath())));

		TextureMapping doorMapping = new TextureMapping()
				.put(TextureSlot.TOP,
						new ResourceLocation(name.getNamespace(), String.format("wood_types/%s/door_top", name.getPath())))
				.put(TextureSlot.BOTTOM,
						new ResourceLocation(name.getNamespace(), String.format("wood_types/%s/door_bottom", name.getPath())));

		TextureMapping trapdoorMapping = TextureMapping.defaultTexture(
				new ResourceLocation(name.getNamespace(), String.format("wood_types/%s/trapdoor", name.getPath())));

		TextureMapping bookshelfMapping = TextureMapping.column(
				new ResourceLocation(name.getNamespace(), String.format("wood_types/%s/bookshelf", name.getPath())),
				new ResourceLocation(name.getNamespace(), String.format("wood_types/%s/planks", name.getPath()))
		);
		TextureMapping occupiedTextureMapping = new TextureMapping()
				.put(TextureSlot.TEXTURE, new ResourceLocation(name.getNamespace(), String.format("wood_types/%s/chiseled_bookshelf", name.getPath())))
				.put(TextureSlot.TOP, new ResourceLocation(name.getNamespace(), String.format("wood_types/%s/chiseled_bookshelf_top", name.getPath())))
				.put(TextureSlot.BOTTOM, new ResourceLocation(name.getNamespace(), String.format("wood_types/%s/chiseled_bookshelf_top", name.getPath())))
				.put(TextureSlot.SIDE, new ResourceLocation(name.getNamespace(), String.format("wood_types/%s/chiseled_bookshelf_side", name.getPath())));
		TextureMapping emptyTextureMapping = new TextureMapping()
				.put(TextureSlot.TEXTURE, new ResourceLocation(name.getNamespace(), String.format("wood_types/%s/chiseled_bookshelf_empty", name.getPath())))
				.put(TextureSlot.TOP, new ResourceLocation(name.getNamespace(), String.format("wood_types/%s/chiseled_bookshelf_top", name.getPath())))
				.put(TextureSlot.BOTTOM, new ResourceLocation(name.getNamespace(), String.format("wood_types/%s/chiseled_bookshelf_top", name.getPath())))
				.put(TextureSlot.SIDE, new ResourceLocation(name.getNamespace(), String.format("wood_types/%s/chiseled_bookshelf_side", name.getPath())));

		TextureMapping beehiveTextureMapping = new TextureMapping()
				.put(TextureSlot.SIDE, new ResourceLocation(name.getNamespace(), String.format("wood_types/%s/beehive_side", name.getPath())))
				.put(TextureSlot.FRONT, new ResourceLocation(name.getNamespace(), String.format("wood_types/%s/beehive_front", name.getPath())))
				.put(TextureSlot.END, new ResourceLocation(name.getNamespace(), String.format("wood_types/%s/beehive_end", name.getPath())));
		TextureMapping beehiveHoneyTextureMapping = new TextureMapping()
				.put(TextureSlot.SIDE, new ResourceLocation(name.getNamespace(), String.format("wood_types/%s/beehive_side", name.getPath())))
				.put(TextureSlot.FRONT, new ResourceLocation(name.getNamespace(), String.format("wood_types/%s/beehive_front_honey", name.getPath())))
				.put(TextureSlot.END, new ResourceLocation(name.getNamespace(), String.format("wood_types/%s/beehive_end", name.getPath())));

		if (log != null && !preRegisteredLog) {
			blockStateModelGenerator.new WoodProvider(logMapping).logWithHorizontal(log);
			blockStateModelGenerator.delegateItemModel(log, ModelLocationUtils.getModelLocation(log));
		}
		if (wood != null) {
			blockStateModelGenerator.new WoodProvider(logMapping).wood(wood);
			blockStateModelGenerator.delegateItemModel(wood, ModelLocationUtils.getModelLocation(wood));
		}
		if (strippedLog != null) {
			blockStateModelGenerator.new WoodProvider(strippedLogMapping).logWithHorizontal(strippedLog);
			blockStateModelGenerator.delegateItemModel(strippedLog, ModelLocationUtils.getModelLocation(strippedLog));
		}
		if (strippedWood != null) {
			blockStateModelGenerator.new WoodProvider(strippedLogMapping).wood(strippedWood);
			blockStateModelGenerator.delegateItemModel(strippedWood, ModelLocationUtils.getModelLocation(strippedWood));
		}
		if (!preRegisteredLog) {
			if (!availableLeaves.isEmpty()) {
				availableLeaves.forEach(s -> {
					TextureMapping leaves2Mapping = TextureMapping.cube(
							new ResourceLocation(name.getNamespace(), String.format("wood_types/%s/%s", name.getPath(), s)));
					Block block = BuiltInRegistries.BLOCK.get(new ResourceLocation(name.getNamespace(), s));
					blockStateModelGenerator.createTrivialBlock(block, TexturedModel.createDefault(block1 -> leaves2Mapping,
							isNetherWood() ? ModelTemplates.CUBE_ALL : ModelTemplates.LEAVES));
//				ResourceLocation resourceLocation = ModelLocationUtils.getModelLocation(block);
//				blockStateModelGenerator.delegateItemModel(block, resourceLocation);
				});
			} else {
				if (leaves != null) {
					blockStateModelGenerator.createTrivialBlock(leaves, TexturedModel.createDefault(block -> leavesMapping,
							isNetherWood() ? ModelTemplates.CUBE_ALL : ModelTemplates.LEAVES));
					ResourceLocation resourceLocation = ModelLocationUtils.getModelLocation(leaves);
					blockStateModelGenerator.delegateItemModel(leaves, resourceLocation);
				}
			}
		}
		if (!availableFloweryLeaves.isEmpty()) {
			availableFloweryLeaves.forEach(s -> {
				TextureMapping leaves2Mapping = TextureMapping.cube(
						new ResourceLocation(name.getNamespace(), String.format("wood_types/%s/%s", name.getPath(), s)));
				Block block = BuiltInRegistries.BLOCK.get(new ResourceLocation(name.getNamespace(), s));
				blockStateModelGenerator.createTrivialBlock(block, TexturedModel.createDefault(block1 -> leaves2Mapping,
						isNetherWood() ? ModelTemplates.CUBE_ALL : ModelTemplates.LEAVES));
				ResourceLocation resourceLocation = ModelLocationUtils.getModelLocation(block);
				blockStateModelGenerator.delegateItemModel(block, resourceLocation);
			});
		} else {
			if (floweryLeaves != null) {
				blockStateModelGenerator.createTrivialBlock(leaves,
						TexturedModel.createDefault(block -> floweryLeavesMapping,
								isNetherWood() ? ModelTemplates.CUBE_ALL : ModelTemplates.LEAVES));
				ResourceLocation resourceLocation = ModelLocationUtils.getModelLocation(leaves);
				blockStateModelGenerator.delegateItemModel(leaves, resourceLocation);
			}
		}
		if (door != null) {
			ResourceLocation resourceLocation = ModelTemplates.DOOR_BOTTOM_LEFT.create(door, doorMapping,
					blockStateModelGenerator.modelOutput);
			ResourceLocation resourceLocation2 = ModelTemplates.DOOR_BOTTOM_LEFT_OPEN.create(door, doorMapping,
					blockStateModelGenerator.modelOutput);
			ResourceLocation resourceLocation3 = ModelTemplates.DOOR_TOP_LEFT.create(door, doorMapping,
					blockStateModelGenerator.modelOutput);
			ResourceLocation resourceLocation4 = ModelTemplates.DOOR_TOP_LEFT_OPEN.create(door, doorMapping,
					blockStateModelGenerator.modelOutput);
			ResourceLocation resourceLocation5 = ModelTemplates.DOOR_BOTTOM_RIGHT.create(door, doorMapping,
					blockStateModelGenerator.modelOutput);
			ResourceLocation resourceLocation6 = ModelTemplates.DOOR_BOTTOM_RIGHT_OPEN.create(door, doorMapping,
					blockStateModelGenerator.modelOutput);
			ResourceLocation resourceLocation7 = ModelTemplates.DOOR_TOP_RIGHT.create(door, doorMapping,
					blockStateModelGenerator.modelOutput);
			ResourceLocation resourceLocation8 = ModelTemplates.DOOR_TOP_RIGHT_OPEN.create(door, doorMapping,
					blockStateModelGenerator.modelOutput);
			blockStateModelGenerator.blockStateOutput.accept(
					createDoor(door, resourceLocation, resourceLocation2, resourceLocation5, resourceLocation6,
							resourceLocation3, resourceLocation4, resourceLocation7, resourceLocation8));
			ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(door.asItem()),
					TextureMapping.layer0(new ResourceLocation(
							name.getNamespace(),
							String.format("wood_types/%s/door", name.getPath())
					)), blockStateModelGenerator.modelOutput);
		}
		if (trapdoor != null) {
			ResourceLocation resourceLocation = ModelTemplates.TRAPDOOR_TOP.create(trapdoor, trapdoorMapping,
					blockStateModelGenerator.modelOutput);
			ResourceLocation resourceLocation2 = ModelTemplates.TRAPDOOR_BOTTOM.create(trapdoor, trapdoorMapping,
					blockStateModelGenerator.modelOutput);
			ResourceLocation resourceLocation3 = ModelTemplates.TRAPDOOR_OPEN.create(trapdoor, trapdoorMapping,
					blockStateModelGenerator.modelOutput);
			blockStateModelGenerator.blockStateOutput.accept(
					createTrapdoor(trapdoor, resourceLocation, resourceLocation2, resourceLocation3));
			blockStateModelGenerator.delegateItemModel(trapdoor, resourceLocation2);
		}
		if (planks != null && !preRegisteredPlanks) {
			blockStateModelGenerator.createTrivialBlock(planks, TexturedModel.createDefault(block -> planksMapping,
					ModelTemplates.CUBE_ALL));
			ResourceLocation resourceLocation = ModelLocationUtils.getModelLocation(planks);
			blockStateModelGenerator.delegateItemModel(planks, resourceLocation);
		}
		if (mosaic != null) {
			blockStateModelGenerator.createTrivialBlock(mosaic, TexturedModel.createDefault(block -> mosaicMapping,
					ModelTemplates.CUBE_ALL));
			ResourceLocation resourceLocation = ModelLocationUtils.getModelLocation(mosaic);
			blockStateModelGenerator.delegateItemModel(mosaic, resourceLocation);
		}
		if (ladder != null) {
			blockStateModelGenerator.blockStateOutput.accept(
					MultiVariantGenerator.multiVariant(ladder, net.minecraft.data.models.blockstates.Variant.variant()
									.with(
											VariantProperties.MODEL,
											ModelLocationUtils.getModelLocation(
													ladder)))
							.with(createHorizontalFacingDispatch()));
			ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(ladder.asItem()),
					TextureMapping.layer0(new ResourceLocation(
							name.getNamespace(),
							String.format("wood_types/%s/ladder", name.getPath())
					)), blockStateModelGenerator.modelOutput);
		}
		if (!availableSaplings.isEmpty()) {
			availableSaplings.forEach(s -> {
				TextureMapping sapling2Mapping = TextureMapping.cross(
						new ResourceLocation(name.getNamespace(), String.format("wood_types/%s/%s", name.getPath(),
								isNetherWood() ? "fungi" : "sapling")));
				ResourceLocation resourceLocation = BlockModelGenerators.TintState.NOT_TINTED.getCross()
						.create(sapling,
								sapling2Mapping,
								blockStateModelGenerator.modelOutput);
				blockStateModelGenerator.blockStateOutput.accept(createSimpleBlock(sapling, resourceLocation));
				ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(sapling.asItem()),
						TextureMapping.layer0(new ResourceLocation(
								name.getNamespace(),
								String.format("wood_types/%s/%s", name.getPath(),
										isNetherWood() ? "fungi" : "sapling")
						)), blockStateModelGenerator.modelOutput);
			});
		} else {
			if (sapling != null) {
				ResourceLocation resourceLocation = BlockModelGenerators.TintState.NOT_TINTED.getCross()
						.create(sapling,
								saplingMapping,
								blockStateModelGenerator.modelOutput);
				blockStateModelGenerator.blockStateOutput.accept(createSimpleBlock(sapling, resourceLocation));
				ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(sapling.asItem()),
						TextureMapping.layer0(new ResourceLocation(
								name.getNamespace(),
								String.format("wood_types/%s/%s", name.getPath(),
										isNetherWood() ? "fungi" : "sapling")
						)), blockStateModelGenerator.modelOutput);
			}
		}
		if (!availablePottedSaplings.isEmpty()) {
			availablePottedSaplings.forEach(s -> {
				TextureMapping sapling2PlantMapping = TextureMapping.plant(new ResourceLocation(name.getNamespace(),
						customPottedTexture ? String.format(
								"wood_types/%s/potted_%s",
								name.getPath(),
								isNetherWood() ? "fungi" : "sapling") :
								String.format(
										"wood_types/%s/%s",
										name.getPath(),
										isNetherWood() ? "fungi" : "sapling")));
				ResourceLocation resourceLocation = BlockModelGenerators.TintState.NOT_TINTED.getCrossPot()
						.create(pottedSapling,
								sapling2PlantMapping,
								blockStateModelGenerator.modelOutput);
				blockStateModelGenerator.blockStateOutput.accept(createSimpleBlock(pottedSapling, resourceLocation));
			});
		} else {
			if (pottedSapling != null) {
				ResourceLocation resourceLocation = BlockModelGenerators.TintState.NOT_TINTED.getCrossPot()
						.create(pottedSapling,
								saplingPlantMapping,
								blockStateModelGenerator.modelOutput);
				blockStateModelGenerator.blockStateOutput.accept(createSimpleBlock(pottedSapling, resourceLocation));
			}
		}

		if (bookshelf != null) {
			ResourceLocation resourceLocation = ModelTemplates.CUBE_COLUMN.create(bookshelf, bookshelfMapping,
					blockStateModelGenerator.modelOutput);
			blockStateModelGenerator.blockStateOutput.accept(createSimpleBlock(bookshelf, resourceLocation));
			ResourceLocation resourceLocationItem = ModelLocationUtils.getModelLocation(bookshelf);
			blockStateModelGenerator.delegateItemModel(bookshelf, resourceLocationItem);
		}
		if (chiseledBookshelf != null) {
			createChiseledBookshelf(blockStateModelGenerator, chiseledBookshelf, occupiedTextureMapping, emptyTextureMapping);
		}
		if (fence != null) {
			ResourceLocation resourceLocation = ModelTemplates.FENCE_POST.create(fence, planksMapping,
					blockStateModelGenerator.modelOutput);
			ResourceLocation resourceLocation2 = ModelTemplates.FENCE_SIDE.create(fence, planksMapping,
					blockStateModelGenerator.modelOutput);
			blockStateModelGenerator.blockStateOutput.accept(
					BlockModelGenerators.createFence(fence, resourceLocation, resourceLocation2));
			ResourceLocation resourceLocation3 = ModelTemplates.FENCE_INVENTORY.create(fence, planksMapping,
					blockStateModelGenerator.modelOutput);
			blockStateModelGenerator.delegateItemModel(fence, resourceLocation3);
		}
		if (fenceGate != null) {
			ResourceLocation resourceLocation = ModelTemplates.FENCE_GATE_OPEN.create(fenceGate, planksMapping,
					blockStateModelGenerator.modelOutput);
			ResourceLocation resourceLocation2 = ModelTemplates.FENCE_GATE_CLOSED.create(fenceGate, planksMapping,
					blockStateModelGenerator.modelOutput);
			ResourceLocation resourceLocation3 = ModelTemplates.FENCE_GATE_WALL_OPEN.create(fenceGate, planksMapping,
					blockStateModelGenerator.modelOutput);
			ResourceLocation resourceLocation4 = ModelTemplates.FENCE_GATE_WALL_CLOSED.create(fenceGate, planksMapping,
					blockStateModelGenerator.modelOutput);
			blockStateModelGenerator.blockStateOutput.accept(
					BlockModelGenerators.createFenceGate(fenceGate, resourceLocation, resourceLocation2, resourceLocation3,
							resourceLocation4, !isBambooWood()));
			blockStateModelGenerator.delegateItemModel(fenceGate, resourceLocation2);
		}
		if (pressurePlate != null) {
			ResourceLocation resourceLocation = ModelTemplates.PRESSURE_PLATE_UP.create(pressurePlate, planksMapping,
					blockStateModelGenerator.modelOutput);
			ResourceLocation resourceLocation2 = ModelTemplates.PRESSURE_PLATE_DOWN.create(pressurePlate, planksMapping,
					blockStateModelGenerator.modelOutput);
			blockStateModelGenerator.blockStateOutput.accept(
					BlockModelGenerators.createPressurePlate(pressurePlate, resourceLocation, resourceLocation2));
			blockStateModelGenerator.delegateItemModel(pressurePlate, resourceLocation);
		}
		if (slab != null) {
			ResourceLocation resourceLocation = ModelTemplates.SLAB_TOP.create(slab, planksMapping,
					blockStateModelGenerator.modelOutput);
			ResourceLocation resourceLocation2 = ModelTemplates.SLAB_BOTTOM.create(slab, planksMapping,
					blockStateModelGenerator.modelOutput);
			blockStateModelGenerator.blockStateOutput.accept(
					BlockModelGenerators.createSlab(slab, resourceLocation2, resourceLocation,
							ModelLocationUtils.getModelLocation(planks)));
			blockStateModelGenerator.delegateItemModel(slab, resourceLocation2);
		}
		if (mosaicSlab != null) {
			ResourceLocation resourceLocation = ModelTemplates.SLAB_TOP.create(mosaicSlab, planksMapping,
					blockStateModelGenerator.modelOutput);
			ResourceLocation resourceLocation2 = ModelTemplates.SLAB_BOTTOM.create(mosaicSlab, planksMapping,
					blockStateModelGenerator.modelOutput);
			blockStateModelGenerator.blockStateOutput.accept(
					BlockModelGenerators.createSlab(mosaicSlab, resourceLocation2, resourceLocation,
							ModelLocationUtils.getModelLocation(mosaic)));
			blockStateModelGenerator.delegateItemModel(mosaicSlab, resourceLocation2);
		}
		if (sign != null && wallSign != null && signItem != null) {
			ResourceLocation resourceLocation = ModelTemplates.PARTICLE_ONLY.create(sign, signMapping,
					blockStateModelGenerator.modelOutput);
			blockStateModelGenerator.blockStateOutput.accept(
					BlockModelGenerators.createSimpleBlock(sign, resourceLocation));
			blockStateModelGenerator.blockStateOutput.accept(
					BlockModelGenerators.createSimpleBlock(wallSign, resourceLocation));
			ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(signItem),
					TextureMapping.layer0(new ResourceLocation(
							name.getNamespace(),
							String.format("wood_types/%s/sign_item", name.getPath())
					)), blockStateModelGenerator.modelOutput);
			blockStateModelGenerator.skipAutoItemBlock(wallSign);
		}
		if (hangingSign != null && hangingWallSign != null && hangingSignItem != null) {
			ResourceLocation resourceLocation = ModelTemplates.PARTICLE_ONLY.create(hangingSign, hangingSignMapping,
					blockStateModelGenerator.modelOutput);
			blockStateModelGenerator.blockStateOutput.accept(
					BlockModelGenerators.createSimpleBlock(hangingSign, resourceLocation));
			blockStateModelGenerator.blockStateOutput.accept(
					BlockModelGenerators.createSimpleBlock(hangingWallSign, resourceLocation));
			ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(hangingSignItem),
					TextureMapping.layer0(new ResourceLocation(
							name.getNamespace(),
							String.format("wood_types/%s/hanging_sign_item", name.getPath())
					)), blockStateModelGenerator.modelOutput);
			blockStateModelGenerator.skipAutoItemBlock(hangingWallSign);
		}
		if (boatItem != null) {
			ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(boatItem),
					TextureMapping.layer0(new ResourceLocation(
							name.getNamespace(),
							String.format("wood_types/%s/boat_item", name.getPath())
					)), blockStateModelGenerator.modelOutput);
		}
		if (chestBoatItem != null) {
			ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(chestBoatItem),
					TextureMapping.layer0(new ResourceLocation(
							name.getNamespace(),
							String.format("wood_types/%s/chest_boat_item", name.getPath())
					)), blockStateModelGenerator.modelOutput);
		}
		if (stairs != null) {
			ResourceLocation resourceLocation = ModelTemplates.STAIRS_INNER.create(stairs, planksMapping,
					blockStateModelGenerator.modelOutput);
			ResourceLocation resourceLocation2 = ModelTemplates.STAIRS_STRAIGHT.create(stairs, planksMapping,
					blockStateModelGenerator.modelOutput);
			ResourceLocation resourceLocation3 = ModelTemplates.STAIRS_OUTER.create(stairs, planksMapping,
					blockStateModelGenerator.modelOutput);
			blockStateModelGenerator.blockStateOutput.accept(
					BlockModelGenerators.createStairs(stairs, resourceLocation, resourceLocation2, resourceLocation3));
			blockStateModelGenerator.delegateItemModel(stairs, resourceLocation2);
		}
		if (mosaicStairs != null) {
			ResourceLocation resourceLocation = ModelTemplates.STAIRS_INNER.create(mosaicStairs, planksMapping,
					blockStateModelGenerator.modelOutput);
			ResourceLocation resourceLocation2 = ModelTemplates.STAIRS_STRAIGHT.create(mosaicStairs, planksMapping,
					blockStateModelGenerator.modelOutput);
			ResourceLocation resourceLocation3 = ModelTemplates.STAIRS_OUTER.create(mosaicStairs, planksMapping,
					blockStateModelGenerator.modelOutput);
			blockStateModelGenerator.blockStateOutput.accept(
					BlockModelGenerators.createStairs(mosaicStairs, resourceLocation, resourceLocation2, resourceLocation3));
			blockStateModelGenerator.delegateItemModel(mosaicStairs, resourceLocation2);
		}
		if (button != null) {
			ResourceLocation resourceLocation = ModelTemplates.BUTTON.create(button, planksMapping,
					blockStateModelGenerator.modelOutput);
			ResourceLocation resourceLocation2 = ModelTemplates.BUTTON_PRESSED.create(button, planksMapping,
					blockStateModelGenerator.modelOutput);
			blockStateModelGenerator.blockStateOutput.accept(
					BlockModelGenerators.createButton(button, resourceLocation, resourceLocation2));
			ResourceLocation resourceLocation3 = ModelTemplates.BUTTON_INVENTORY.create(button, planksMapping,
					blockStateModelGenerator.modelOutput);
			blockStateModelGenerator.delegateItemModel(button, resourceLocation3);
		}
		if (beehive != null) {
			TextureMapping textureMapping = beehiveTextureMapping.copyForced(TextureSlot.SIDE, TextureSlot.PARTICLE);
			TextureMapping textureMapping2 = beehiveHoneyTextureMapping.copyForced(TextureSlot.SIDE, TextureSlot.PARTICLE);
			ResourceLocation resourceLocation = ModelTemplates.CUBE_ORIENTABLE_TOP_BOTTOM.create(beehive, textureMapping, blockStateModelGenerator.modelOutput);
			ResourceLocation resourceLocation2 = ModelTemplates.CUBE_ORIENTABLE_TOP_BOTTOM.createWithSuffix(beehive, "_honey", textureMapping2, blockStateModelGenerator.modelOutput);
			blockStateModelGenerator.blockStateOutput
					.accept(
							MultiVariantGenerator.multiVariant(beehive)
									.with(createHorizontalFacingDispatch())
									.with(createEmptyOrFullDispatch(BlockStateProperties.LEVEL_HONEY, 5, resourceLocation2, resourceLocation))
					);
		}
	}

	private void createChiseledBookshelf(BlockModelGenerators blockModelGenerators, Block block, TextureMapping occupiedTextureMapping, TextureMapping emptyTextureMapping) {
		BlockModelBuilder builder = BlockModelBuilder.createNew(new ResourceLocation("block/chiseled_bookshelf"))
				.addTexture("top", emptyTextureMapping.get(TextureSlot.TOP))
				.addTexture("side", emptyTextureMapping.get(TextureSlot.SIDE))
				.addTexture("particle", emptyTextureMapping.get(TextureSlot.TOP))
				.addElement(new ElementBuilder(new Vector3d(0), new Vector3d(16))
						.addFace(Direction.EAST, new FaceBuilder("side")
								.withUv(0, 0, 16, 16)
								.withCulling(Direction.EAST)
						).addFace(Direction.SOUTH, new FaceBuilder("side")
								.withUv(0, 0, 16, 16)
								.withCulling(Direction.SOUTH)
						).addFace(Direction.WEST, new FaceBuilder("side")
								.withUv(0, 0, 16, 16)
								.withCulling(Direction.WEST)
						).addFace(Direction.UP, new FaceBuilder("top")
								.withUv(0, 0, 16, 16)
								.withCulling(Direction.UP)
						).addFace(Direction.DOWN, new FaceBuilder("top")
								.withUv(0, 0, 16, 16)
								.withCulling(Direction.DOWN)
						)
				);
		BlockModelBuilder builderItem = BlockModelBuilder.createNew(new ResourceLocation("block/chiseled_bookshelf_inventory"))
				.addTexture("top", emptyTextureMapping.get(TextureSlot.TOP))
				.addTexture("side", emptyTextureMapping.get(TextureSlot.SIDE))
				.addTexture("front", emptyTextureMapping.get(TextureSlot.TEXTURE))
				.addTexture("particle", emptyTextureMapping.get(TextureSlot.TOP))
				.addElement(new ElementBuilder(new Vector3d(0), new Vector3d(16))
						.addFace(Direction.NORTH, new FaceBuilder("front")
								.withUv(0, 0, 16, 16)
								.withCulling(Direction.NORTH)
						).addFace(Direction.EAST, new FaceBuilder("side")
								.withUv(0, 0, 16, 16)
								.withCulling(Direction.EAST)
						).addFace(Direction.SOUTH, new FaceBuilder("side")
								.withUv(0, 0, 16, 16)
								.withCulling(Direction.SOUTH)
						).addFace(Direction.WEST, new FaceBuilder("side")
								.withUv(0, 0, 16, 16)
								.withCulling(Direction.WEST)
						).addFace(Direction.UP, new FaceBuilder("top")
								.withUv(0, 0, 16, 16)
								.withCulling(Direction.UP)
						).addFace(Direction.DOWN, new FaceBuilder("top")
								.withUv(0, 0, 16, 16)
								.withCulling(Direction.DOWN)
						)
				);
		ResourceLocation blockModel = builder.buildModel().create(block, builder.mapTextures(), blockModelGenerators.modelOutput);
		ResourceLocation blockInventoryModel = builderItem.buildModel().createWithSuffix(block, "_inventory", builderItem.mapTextures(), blockModelGenerators.modelOutput);
		MultiPartGenerator multiPartGenerator = MultiPartGenerator.multiPart(block);
		Map.of(Direction.NORTH, VariantProperties.Rotation.R0, Direction.EAST, VariantProperties.Rotation.R90, Direction.SOUTH, VariantProperties.Rotation.R180, Direction.WEST, VariantProperties.Rotation.R270).forEach((direction, rotation) -> {
			Condition.TerminalCondition terminalCondition = Condition.condition().term(BlockStateProperties.HORIZONTAL_FACING, direction);
			multiPartGenerator.with(terminalCondition, Variant.variant().with(VariantProperties.MODEL, blockModel).with(VariantProperties.Y_ROT, rotation).with(VariantProperties.UV_LOCK, true));
			this.addSlotStateAndRotationVariants(blockModelGenerators, block, multiPartGenerator, terminalCondition, rotation, occupiedTextureMapping, emptyTextureMapping);
		});
		blockModelGenerators.blockStateOutput.accept(multiPartGenerator);
		blockModelGenerators.delegateItemModel(block, blockInventoryModel);
		CHISELED_BOOKSHELF_SLOT_MODEL_CACHE.clear();
	}

	public final void addSlotStateAndRotationVariants(BlockModelGenerators blockModelGenerators, Block block, MultiPartGenerator multiPartGenerator, Condition.TerminalCondition terminalCondition, VariantProperties.Rotation rotation, TextureMapping occupiedTextureMapping, TextureMapping emptyTextureMapping) {
		Map.of(BlockStateProperties.CHISELED_BOOKSHELF_SLOT_0_OCCUPIED, ModelTemplates.CHISELED_BOOKSHELF_SLOT_TOP_LEFT, BlockStateProperties.CHISELED_BOOKSHELF_SLOT_1_OCCUPIED, ModelTemplates.CHISELED_BOOKSHELF_SLOT_TOP_MID, BlockStateProperties.CHISELED_BOOKSHELF_SLOT_2_OCCUPIED, ModelTemplates.CHISELED_BOOKSHELF_SLOT_TOP_RIGHT, BlockStateProperties.CHISELED_BOOKSHELF_SLOT_3_OCCUPIED, ModelTemplates.CHISELED_BOOKSHELF_SLOT_BOTTOM_LEFT, BlockStateProperties.CHISELED_BOOKSHELF_SLOT_4_OCCUPIED, ModelTemplates.CHISELED_BOOKSHELF_SLOT_BOTTOM_MID, BlockStateProperties.CHISELED_BOOKSHELF_SLOT_5_OCCUPIED, ModelTemplates.CHISELED_BOOKSHELF_SLOT_BOTTOM_RIGHT).forEach((booleanProperty, modelTemplate) -> {
			this.addBookSlotModel(blockModelGenerators, block, multiPartGenerator, terminalCondition, rotation, booleanProperty, modelTemplate, true, occupiedTextureMapping, emptyTextureMapping);
			this.addBookSlotModel(blockModelGenerators, block, multiPartGenerator, terminalCondition, rotation, booleanProperty, modelTemplate, false, occupiedTextureMapping, emptyTextureMapping);
		});
	}

	public final void addBookSlotModel(BlockModelGenerators blockModelGenerators, Block block, MultiPartGenerator multiPartGenerator, Condition.TerminalCondition terminalCondition, VariantProperties.Rotation rotation, BooleanProperty booleanProperty, ModelTemplate modelTemplate, boolean bl, TextureMapping occupiedTextureMapping, TextureMapping emptyTextureMapping) {
		String string = bl ? "_occupied" : "_empty";
		TextureMapping textureMapping = bl ? occupiedTextureMapping : emptyTextureMapping;
		BookSlotModelCacheKey bookSlotModelCacheKey = new BookSlotModelCacheKey(modelTemplate, string);
		ResourceLocation resourceLocation = CHISELED_BOOKSHELF_SLOT_MODEL_CACHE.computeIfAbsent(bookSlotModelCacheKey, (bookSlotModelCacheKeyx) -> {
			return modelTemplate.createWithSuffix(block, string, textureMapping, blockModelGenerators.modelOutput);
		});
		multiPartGenerator.with(Condition.and(terminalCondition, Condition.condition().term(booleanProperty, bl)), Variant.variant().with(VariantProperties.MODEL, resourceLocation).with(VariantProperties.Y_ROT, rotation));
	}

	record BookSlotModelCacheKey(ModelTemplate template, String modelSuffix) {
	}

	@Environment(EnvType.CLIENT)
	public void generateLang(FabricLanguageProvider.TranslationBuilder translationBuilder, Map<String, String> lang) {
		String translatedName = getTranslation(name.getPath(), lang);

		String logName = isNetherWood() ? getTranslation("stem", lang) : isBambooWood() ? getTranslation("block", lang) : getTranslation("log", lang);
		String strippedLogName = isNetherWood() ? getTranslation("stripped_stem", lang) : isBambooWood() ? getTranslation("stripped_block", lang) : getTranslation("stripped_log", lang);
		String woodName = isNetherWood() ? getTranslation("hyphae", lang) : getTranslation("wood", lang);
		String strippedWoodName = isNetherWood() ? getTranslation("stripped_hyphae", lang) : getTranslation("stripped_wood", lang);
		String saplingName = isNetherWood() ? getTranslation("fungus", lang) : getTranslation("sapling", lang);
		String pottedSaplingName = isNetherWood() ? getTranslation("potted_fungus", lang) : getTranslation("potted_sapling", lang);
		String foliageBlockName = isNetherWood() ? getTranslation("wart_block", lang) : getTranslation("leaves", lang);
		String floweryFoliageBlockName = isNetherWood() ? getTranslation("flowering_wart_block", lang) : getTranslation("flowering_leaves", lang);

		// Add translations for log, stripped log, wood, and stripped wood
		if (log != null) translationBuilder.add(log, String.format(logName, translatedName));
		if (strippedLog != null) translationBuilder.add(strippedLog, String.format(strippedLogName, translatedName));
		if (wood != null) translationBuilder.add(wood, String.format(woodName, translatedName));
		if (strippedWood != null) translationBuilder.add(strippedWood, String.format(strippedWoodName, translatedName));

		// Add translations for planks
		if (planks != null)
			translationBuilder.add(planks, String.format(getTranslation("planks", lang), translatedName));

		// Add translations for saplings and potted saplings
		if (!availableSaplings.isEmpty()) {
			availableSaplings.forEach(s -> {
				String registryName = isNetherWood() ? s + "_fungus" : s + "_sapling";
				Block block = BuiltInRegistries.BLOCK.get(new ResourceLocation(name.getNamespace(), registryName));
				translationBuilder.add(block, String.format(saplingName, translatedName));
			});
		} else {
			if (sapling != null) translationBuilder.add(sapling, String.format(saplingName, translatedName));
		}
		if (!availablePottedSaplings.isEmpty()) {
			availablePottedSaplings.forEach(s -> {
				String registryName = "potted_" + (isNetherWood() ? s + "_fungus" : s + "_sapling");
				Block block = BuiltInRegistries.BLOCK.get(new ResourceLocation(name.getNamespace(), registryName));
				translationBuilder.add(block, String.format(pottedSaplingName, translate(s, lang)));
			});
		} else {
			if (pottedSapling != null)
				translationBuilder.add(pottedSapling, String.format(pottedSaplingName, translatedName));
		}

		// Add translations for trapdoor and door
		if (trapdoor != null)
			translationBuilder.add(trapdoor, String.format(getTranslation("trapdoor", lang), translatedName));
		if (door != null) translationBuilder.add(door, String.format(getTranslation("door", lang), translatedName));

		// Add translations for leaves and flowering leaves
		if (!availableLeaves.isEmpty()) {
			availableLeaves.forEach(s -> {
				String registryName = isNetherWood() ? s + "_wart_block" : s + "_leaves";
				Block block = BuiltInRegistries.BLOCK.get(new ResourceLocation(name.getNamespace(), registryName));
				translationBuilder.add(block, String.format(foliageBlockName, translate(s, lang)));
			});
		} else {
			if (leaves != null) translationBuilder.add(leaves, String.format(foliageBlockName, translatedName));
		}
		if (!availableFloweryLeaves.isEmpty()) {
			availableFloweryLeaves.forEach(s -> {
				String registryName = isNetherWood() ? "flowery_" + s + "_wart_block" : "flowery_" + s + "_leaves";
				Block block = BuiltInRegistries.BLOCK.get(new ResourceLocation(name.getNamespace(), registryName));
				translationBuilder.add(block, String.format(floweryFoliageBlockName, translate(s, lang)));
			});
		} else {
			if (floweryLeaves != null)
				translationBuilder.add(leaves, String.format(floweryFoliageBlockName, translatedName));
		}

		// Add translations for fence, gate, pressure plate, and button
		if (fence != null) translationBuilder.add(fence, String.format(getTranslation("fence", lang), translatedName));
		if (fenceGate != null)
			translationBuilder.add(fenceGate, String.format(getTranslation("fence_gate", lang), translatedName));
		if (pressurePlate != null)
			translationBuilder.add(pressurePlate, String.format(getTranslation("pressure_plate", lang), translatedName));
		if (button != null)
			translationBuilder.add(button, String.format(getTranslation("button", lang), translatedName));
		if (ladder != null)
			translationBuilder.add(ladder, String.format(getTranslation("ladder", lang), translatedName));
		if (bookshelf != null)
			translationBuilder.add(bookshelf, String.format(getTranslation("bookshelf", lang), translatedName));
		if (chiseledBookshelf != null)
			translationBuilder.add(chiseledBookshelf, String.format(getTranslation("chiseled_bookshelf", lang), translatedName));
		if (mosaic != null)
			translationBuilder.add(mosaic, String.format(getTranslation("mosaic", lang), translatedName));
		if (mosaicSlab != null)
			translationBuilder.add(mosaicSlab, String.format(getTranslation("mosaic_slab", lang), translatedName));
		if (mosaicStairs != null)
			translationBuilder.add(mosaicStairs, String.format(getTranslation("mosaic_stairs", lang), translatedName));
		if (slab != null) translationBuilder.add(slab, String.format(getTranslation("slab", lang), translatedName));
		if (stairs != null)
			translationBuilder.add(stairs, String.format(getTranslation("stairs", lang), translatedName));
		if (boatItem != null)
			translationBuilder.add(boatItem, String.format(getTranslation("boat", lang), translatedName));
		if (chestBoatItem != null)
			translationBuilder.add(chestBoatItem, String.format(getTranslation("chest_boat", lang), translatedName));
		if (sign != null) translationBuilder.add(sign, String.format(getTranslation("sign", lang), translatedName));
		if (hangingSign != null)
			translationBuilder.add(hangingSign, String.format(getTranslation("hanging_sign", lang), translatedName));
	}

	public void generateLoot(FabricBlockLootTableProvider lootTablesProvider) {
		if (log != null) lootTablesProvider.dropSelf(log);
		if (strippedLog != null) lootTablesProvider.dropSelf(strippedLog);
		if (wood != null) lootTablesProvider.dropSelf(wood);
		if (strippedWood != null) lootTablesProvider.dropSelf(strippedWood);
		if (planks != null) lootTablesProvider.dropSelf(planks);
		if (mosaic != null) lootTablesProvider.dropSelf(mosaic);
		if (!availableLeaves.isEmpty() && !isNetherWood()) {
			availableLeaves.forEach(s -> {
				String registryName = isNetherWood() ? s + "_wart_block" : s + "_leaves";
				Block block = BuiltInRegistries.BLOCK.get(new ResourceLocation(name.getNamespace(), registryName));

				String saplingName = isNetherWood() ? s + "_fungus" : s + "_sapling";
				if (!availableSaplings.isEmpty() && availableSaplings.contains(saplingName)) {
					Block sapling = BuiltInRegistries.BLOCK.get(
							new ResourceLocation(name.getNamespace(), saplingName));
					lootTablesProvider.add(block, block1 -> lootTablesProvider.createLeavesDrops(block1, sapling, 0.05F, 0.0625F,
							0.083333336F, 0.1F));
				} else {
					if (sapling != null) lootTablesProvider.add(block,
							block1 -> lootTablesProvider.createLeavesDrops(block1, sapling,
									0.05F, 0.0625F,
									0.083333336F,
									0.1F));
					else lootTablesProvider.dropSelf(block);
				}
			});
		} else {
			if (leaves != null && !isNetherWood()) {
				if (sapling != null) lootTablesProvider.add(leaves,
						block1 -> lootTablesProvider.createLeavesDrops(block1, sapling,
								0.05F, 0.0625F,
								0.083333336F, 0.1F));
				else lootTablesProvider.dropSelf(leaves);
			}
		}
		if (!availableLeaves.isEmpty()) {
			availableLeaves.forEach(s -> {
				String registryName = isNetherWood() ? s + "_wart_block" : s + "_leaves";
				Block block = BuiltInRegistries.BLOCK.get(new ResourceLocation(name.getNamespace(), registryName));
				lootTablesProvider.dropSelf(block);
			});
		}
		if (!availableFloweryLeaves.isEmpty()) {
			availableFloweryLeaves.forEach(s -> {
				String registryName = isNetherWood() ? "flowery_" + s + "_wart_block" : "flowery_" + s + "_leaves";
				Block block = BuiltInRegistries.BLOCK.get(new ResourceLocation(name.getNamespace(), registryName));

				String saplingName = isNetherWood() ? s + "_fungus" : s + "_sapling";
				if (!availableSaplings.isEmpty() && availableSaplings.contains(saplingName)) {
					Block sapling = BuiltInRegistries.BLOCK.get(new ResourceLocation(name.getNamespace(), saplingName));
					lootTablesProvider.add(block, block1 -> lootTablesProvider.createLeavesDrops(block1, sapling, 0.05F, 0.0625F,
							0.083333336F, 0.1F));
				} else {
					if (sapling != null) lootTablesProvider.add(block,
							block1 -> lootTablesProvider.createLeavesDrops(block1, sapling,
									0.05F, 0.0625F,
									0.083333336F,
									0.1F));
					else lootTablesProvider.dropSelf(block);
				}
			});
		}
		if (!availableFloweryLeaves.isEmpty()) {
			availableFloweryLeaves.forEach(s -> {
				String registryName = isNetherWood() ? "flowery_" + s + "_wart_block" : "flowery_" + s + "_leaves";
				Block block = BuiltInRegistries.BLOCK.get(new ResourceLocation(name.getNamespace(), registryName));
				lootTablesProvider.dropSelf(block);
			});
		}
		if (ladder != null) lootTablesProvider.dropSelf(ladder);
		if (trapdoor != null) lootTablesProvider.dropSelf(trapdoor);
		if (button != null) lootTablesProvider.dropSelf(button);
		if (door != null) lootTablesProvider.add(door, lootTablesProvider::createDoorTable);
		if (sapling != null) lootTablesProvider.dropSelf(sapling);
		if (!availableSaplings.isEmpty()) {
			availableSaplings.forEach(s -> {
				String saplingName = isNetherWood() ? s + "_fungus" : s + "_sapling";
				Block block = BuiltInRegistries.BLOCK.get(new ResourceLocation(name.getNamespace(), saplingName));
				lootTablesProvider.dropSelf(block);
			});
		}
		if (fence != null) lootTablesProvider.dropSelf(fence);
		if (fenceGate != null) lootTablesProvider.dropSelf(fenceGate);
		if (pressurePlate != null) lootTablesProvider.dropSelf(pressurePlate);
		if (slab != null) lootTablesProvider.add(slab, lootTablesProvider::createSlabItemTable);
		if (mosaicSlab != null) lootTablesProvider.add(mosaicSlab, lootTablesProvider::createSlabItemTable);
		if (stairs != null) lootTablesProvider.dropSelf(stairs);
		if (mosaicStairs != null) lootTablesProvider.dropSelf(mosaicStairs);
		if (sign != null) lootTablesProvider.dropSelf(sign);
		if (hangingSign != null) lootTablesProvider.dropSelf(hangingSign);
		if (bookshelf != null) lootTablesProvider.add(bookshelf,
				block -> lootTablesProvider.createSingleItemTableWithSilkTouch(block, Items.BOOK,
						ConstantValue.exactly(3.0F)));
		if (chiseledBookshelf != null) lootTablesProvider.dropWhenSilkTouch(chiseledBookshelf);
	}

	public void generateRecipes(RecipeOutput exporter) {
		if (planks != null && logsItemTag != null)
			RecipeProvider.planksFromLogs(exporter, planks, logsItemTag, isBambooWood() ? 2 : 4);
		if (mosaic != null && slab != null)
			RecipeProvider.mosaicBuilder(exporter, RecipeCategory.DECORATIONS, mosaic, slab);
		if (wood != null && log != null) RecipeProvider.woodFromLogs(exporter, wood, log);
		if (strippedWood != null && strippedLog != null)
			RecipeProvider.woodFromLogs(exporter, strippedWood, strippedLog);
		if (trapdoor != null && planks != null) RecipeProvider.trapdoorBuilder(trapdoor, Ingredient.of(planks));
		if (door != null && planks != null) RecipeProvider.doorBuilder(door, Ingredient.of(planks));
		if (fence != null && planks != null) RecipeProvider.fenceBuilder(fence, Ingredient.of(planks));
		if (fenceGate != null && planks != null) RecipeProvider.fenceGateBuilder(fenceGate, Ingredient.of(planks));
		if (slab != null && planks != null) RecipeProvider.slab(exporter, RecipeCategory.BUILDING_BLOCKS, slab, planks);
		if (stairs != null && planks != null) RecipeProvider.stairBuilder(stairs, Ingredient.of(planks));
		if (mosaicSlab != null && mosaic != null)
			RecipeProvider.slab(exporter, RecipeCategory.BUILDING_BLOCKS, mosaicSlab, mosaic);
		if (mosaicStairs != null && mosaic != null) RecipeProvider.stairBuilder(mosaicStairs, Ingredient.of(mosaic));
		if (pressurePlate != null && planks != null) RecipeProvider.pressurePlate(exporter, pressurePlate, planks);
		if (button != null && planks != null) RecipeProvider.buttonBuilder(button, Ingredient.of(planks));
		if (boatItem != null && planks != null) RecipeProvider.woodenBoat(exporter, boatItem, planks);
		if (chestBoatItem != null && boatItem != null) RecipeProvider.chestBoat(exporter, chestBoatItem, planks);
		if (signItem != null && planks != null) RecipeProvider.signBuilder(signItem, Ingredient.of(planks));
		if (hangingSignItem != null && strippedLog != null)
			RecipeProvider.hangingSign(exporter, hangingSignItem, strippedLog);
		if (bookshelf != null && planks != null)
			ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, bookshelf)
					.define('#', planks)
					.define('X', Items.BOOK)
					.pattern("###")
					.pattern("XXX")
					.pattern("###")
					.unlockedBy("has_book", has(Items.BOOK))
					.save(exporter);
		if (chiseledBookshelf != null && planks != null && slab != null)
			ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, chiseledBookshelf)
					.define('#', planks)
					.define('X', slab)
					.pattern("###")
					.pattern("XXX")
					.pattern("###")
					.unlockedBy("has_book", has(Items.BOOK))
					.save(exporter);

	}

	protected boolean isNetherWood() {
		return woodPropertyType == WoodPropertyType.NETHER;
	}

	protected boolean isBambooWood() {
		return woodPropertyType == WoodPropertyType.BAMBOO;
	}

	protected boolean isCherryWood() {
		return woodPropertyType == WoodPropertyType.CHERRY;
	}

	public static class Builder {
		public ResourceLocation name;
		private WoodRegistry woodRegistry;
		private RegistryHelper registryHelper;
		private WoodType woodType;

		public Builder setSaplingGenerator(TreeGrower abstractTreeGrower) {
			woodRegistry.setSaplingGenerator(abstractTreeGrower);
			return this;
		}

		public Builder setName(ResourceLocation name) {
			woodRegistry.setName(name);
			this.name = name;
			return this;
		}

		public Builder setFungusGenerator(ResourceKey<ConfiguredFeature<?, ?>> fungusGenerator) {
			woodRegistry.setFungusGenerator(fungusGenerator);
			return this;
		}

		public Builder setBaseFungusBlock(Block block) {
			woodRegistry.setBaseFungusBlock(block);
			return this;
		}

		public Builder setPlanks(Block planks) {
			woodRegistry.planks = planks;
			woodRegistry.preRegisteredPlanks = true;
			return this;
		}

		public Builder setLog(Block log) {
			woodRegistry.log = log;
			woodRegistry.preRegisteredLog = true;
			return this;
		}

		public Builder setLeaves(Block leaves) {
			woodRegistry.leaves = leaves;
			woodRegistry.preRegisteredLeaves = true;
			return this;
		}

		public Builder setWoodType(WoodType woodType) {
			this.woodType = woodType;
			return this;
		}

		public Builder of(ResourceLocation name) {
			woodRegistry = new WoodRegistry(name);
			this.name = name;
			registryHelper = RegistryHelper.createRegistryHelper(this.name.getNamespace());
			return this;
		}

		public Builder of(ResourceLocation name, TreeGrower saplingGenerator) {
			return of(name).setSaplingGenerator(saplingGenerator);
		}

		public Builder of(ResourceLocation name, ResourceKey<ConfiguredFeature<?, ?>> fungusGenerator, Block baseFungusBlock) {
			return of(name).setFungusGenerator(fungusGenerator).setBaseFungusBlock(baseFungusBlock);
		}

		public Builder of(ResourceLocation name, Block planks) {
			return of(name).setPlanks(planks);
		}

		public Builder of(WoodMaterial woodMaterial) {
			return of(name).setWoodType(woodMaterial.woodType()).setLog(woodMaterial.log())
					.setLeaves(woodMaterial.leaves());
		}

		public Builder of(WoodMaterial woodMaterial, TreeGrower saplingGenerator) {
			return of(woodMaterial).setSaplingGenerator(saplingGenerator);
		}

		public Builder of(WoodMaterial woodMaterial, ResourceKey<ConfiguredFeature<?, ?>> fungusGenerator, Block baseFungusBlock) {
			return of(woodMaterial).setFungusGenerator(fungusGenerator).setBaseFungusBlock(baseFungusBlock);
		}

		public Builder of(WoodMaterial woodMaterial, Block planks, TreeGrower saplingGenerator) {
			return of(woodMaterial, saplingGenerator).setPlanks(planks);
		}

		public Builder of(WoodMaterial woodMaterial, Block planks, ResourceKey<ConfiguredFeature<?, ?>> fungusGenerator, Block baseFungusBlock) {
			return of(woodMaterial, fungusGenerator, baseFungusBlock).setPlanks(planks);
		}

		public Builder log() {
			String logName = woodRegistry.isNetherWood() ? name.getPath() + "_stem" : woodRegistry.isBambooWood() ? this.name.getPath() + "_block"
					: this.name.getPath() + "_log";
			Block block = woodRegistry.isNetherWood() ? Blocks.WARPED_STEM : woodRegistry.isBambooWood() ? Blocks.BAMBOO_BLOCK :
					woodRegistry.isCherryWood() ? Blocks.CHERRY_LOG : Blocks.DARK_OAK_LOG;
			Block creativeTabBlock = woodRegistry.isNetherWood() ? Blocks.STONE : woodRegistry.isBambooWood() ? Blocks.CRIMSON_STEM : Blocks.BAMBOO_BLOCK;
			BlockBehaviour.Properties blockSettings = FabricBlockSettings.copyOf(block);
			woodRegistry.log = registryHelper.blocks().registerBlockWood(
					new RotatedPillarBlock(blockSettings), logName,
					creativeTabBlock, CreativeModeTabs.BUILDING_BLOCKS
			);
			Block finalBlock = woodRegistry.isNetherWood() ? Blocks.OAK_LEAVES : Blocks.MUSHROOM_STEM;
			ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.NATURAL_BLOCKS).register(entries -> entries.addBefore(finalBlock, woodRegistry.log));
			woodType = woodRegistry.isNetherWood() ? WoodType.CRIMSON : woodRegistry.isBambooWood() ? WoodType.BAMBOO
					: woodRegistry.isCherryWood() ? WoodType.CHERRY : WoodType.ACACIA;
			return this;
		}

		public Builder wood() {
			String woodName = woodRegistry.isNetherWood() ? name.getPath() + "_hyphae" : name.getPath() + "_wood";
			Block block = woodRegistry.isNetherWood() ? Blocks.WARPED_HYPHAE : woodRegistry.isCherryWood() ? Blocks.CHERRY_WOOD : Blocks.DARK_OAK_WOOD;
			BlockBehaviour.Properties blockSettings = FabricBlockSettings.copyOf(block);
			woodRegistry.wood = registryHelper.blocks().registerBlock(
					new RotatedPillarBlock(blockSettings),
					woodName,
					CreativeModeTabs.BUILDING_BLOCKS, woodRegistry.log
			);
			return this;
		}

		public Builder strippedLog() {
			String logName = woodRegistry.isNetherWood() ? name.getPath() + "_stem" : woodRegistry.isBambooWood() ? this.name.getPath() + "_block"
					: this.name.getPath() + "_log";
			Block block = woodRegistry.isNetherWood() ? Blocks.STRIPPED_WARPED_STEM : woodRegistry.isBambooWood() ? Blocks.STRIPPED_BAMBOO_BLOCK
					: Blocks.STRIPPED_DARK_OAK_LOG;
			BlockBehaviour.Properties blockSettings = FabricBlockSettings.copyOf(block);
			woodRegistry.strippedLog = registryHelper.blocks().registerBlock(
					new RotatedPillarBlock(blockSettings),
					"stripped_" + logName,
					CreativeModeTabs.BUILDING_BLOCKS, woodRegistry.wood
			);
			return this;
		}

		public Builder strippedWood() {
			String woodName = woodRegistry.isNetherWood() ? name.getPath() + "_hyphae" : name.getPath() + "_wood";
			Block block = woodRegistry.isNetherWood() ? Blocks.STRIPPED_WARPED_HYPHAE : woodRegistry.isCherryWood() ? Blocks.STRIPPED_CHERRY_WOOD
					: Blocks.STRIPPED_DARK_OAK_WOOD;
			BlockBehaviour.Properties blockSettings = FabricBlockSettings.copyOf(block);
			woodRegistry.strippedWood = registryHelper.blocks().registerBlock(
					new RotatedPillarBlock(blockSettings),
					"stripped_" + woodName,
					CreativeModeTabs.BUILDING_BLOCKS, woodRegistry.strippedLog
			);
			return this;
		}

		public Builder stairs() {
			woodRegistry.stairs = registryHelper.blocks().registerBlock(new StairBlock(woodRegistry.planks.defaultBlockState(),
				FabricBlockSettings.ofFullCopy(woodRegistry.planks)
			), name.getPath() + "_stairs", CreativeModeTabs.BUILDING_BLOCKS, woodRegistry.planks != null
					? woodRegistry.planks : woodRegistry.strippedWood);
			return this;
		}

		public Builder mosaicStairs() {
			woodRegistry.stairs = registryHelper.blocks().registerBlock(new StairBlock(woodRegistry.mosaic.defaultBlockState(),
					FabricBlockSettings.ofFullCopy(woodRegistry.mosaic)
			), name.getPath() + "_mosaic_stairs", CreativeModeTabs.BUILDING_BLOCKS, woodRegistry.stairs);
			return this;
		}

		public Builder slab() {
			woodRegistry.slab = registryHelper.blocks().registerBlock(
					new SlabBlock(FabricBlockSettings.ofLegacyCopy(woodRegistry.planks)),
					name.getPath() + "_slab",
					CreativeModeTabs.BUILDING_BLOCKS, woodRegistry.mosaicStairs != null ? woodRegistry.mosaicStairs : woodRegistry.stairs
			);
			return this;
		}

		public Builder mosaicSlab() {
			woodRegistry.mosaicSlab = registryHelper.blocks().registerBlock(
					new SlabBlock(FabricBlockSettings.ofLegacyCopy(woodRegistry.planks)),
					name.getPath() + "_mosaic_slab",
					CreativeModeTabs.BUILDING_BLOCKS, woodRegistry.slab
			);
			return this;
		}

		public Builder planks() {
			Block block = woodRegistry.isNetherWood() ? Blocks.WARPED_PLANKS : woodRegistry.isBambooWood() ? Blocks.BAMBOO_PLANKS : Blocks.MANGROVE_PLANKS;
			woodRegistry.planks = registryHelper.blocks().registerBlock(
					new Block(FabricBlockSettings.copyOf(block)),
					name.getPath() + "_planks",
					CreativeModeTabs.BUILDING_BLOCKS, woodRegistry.strippedWood
			);
			return this;
		}

		public Builder mosaic() {
			Block block = woodRegistry.isNetherWood() ? Blocks.WARPED_PLANKS : woodRegistry.isBambooWood() ? Blocks.BAMBOO_PLANKS : Blocks.MANGROVE_PLANKS;
			woodRegistry.mosaic = registryHelper.blocks().registerBlock(
					new Block(FabricBlockSettings.copyOf(block)),
					name.getPath() + "_mosaic",
					CreativeModeTabs.BUILDING_BLOCKS, woodRegistry.planks
			);
			return this;
		}

		public Builder leaves() {
			String leavesName = woodRegistry.isNetherWood() ? "_wart_block" : "_leaves";
			Block block = woodRegistry.isNetherWood() ? Blocks.WARPED_WART_BLOCK : Blocks.FLOWERING_AZALEA_LEAVES;
			Block creativeTabBlock = woodRegistry.isNetherWood() ? Blocks.SHROOMLIGHT : Blocks.BROWN_MUSHROOM_BLOCK;
			BlockBehaviour.Properties properties = FabricBlockSettings.copyOf(block);
			woodRegistry.leaves = registryHelper.blocks().registerBlockWood(
					woodRegistry.isNetherWood() ? new Block(properties) : new LeavesBlock(properties),
					name.getPath() + leavesName,
					CreativeModeTabs.NATURAL_BLOCKS, creativeTabBlock
			);
			return this;
		}

		public Builder leaves(String nameIn) {
			String leavesName = woodRegistry.isNetherWood() ? "_wart_block" : "_leaves";
			Block block = woodRegistry.isNetherWood() ? Blocks.WARPED_WART_BLOCK : Blocks.FLOWERING_AZALEA_LEAVES;
			Block creativeTabBlock = woodRegistry.isNetherWood() ? Blocks.SHROOMLIGHT : Blocks.BROWN_MUSHROOM_BLOCK;
			BlockBehaviour.Properties properties = FabricBlockSettings.copyOf(block);
			woodRegistry.leaves = registryHelper.blocks().registerBlockWood(
					woodRegistry.isNetherWood() ? new Block(properties) : new LeavesBlock(properties),
					nameIn + leavesName,
					CreativeModeTabs.NATURAL_BLOCKS, creativeTabBlock
			);
			return this;
		}

		public Builder leaves(String... nameIn) {
			String leavesName = woodRegistry.isNetherWood() ? "_wart_block" : "_leaves";
			Block block = woodRegistry.isNetherWood() ? Blocks.WARPED_WART_BLOCK : Blocks.FLOWERING_AZALEA_LEAVES;
			Block creativeTabBlock = woodRegistry.isNetherWood() ? Blocks.SHROOMLIGHT : Blocks.BROWN_MUSHROOM_BLOCK;
			BlockBehaviour.Properties properties = FabricBlockSettings.copyOf(block);
			for (String name : nameIn) {
				woodRegistry.leaves = registryHelper.blocks().registerBlockWood(
						woodRegistry.isNetherWood() ? new Block(properties) : new LeavesBlock(properties),
						name + leavesName,
						CreativeModeTabs.NATURAL_BLOCKS, creativeTabBlock
				);
				woodRegistry.availableLeaves.add(name);
			}
			return this;
		}

		public Builder coloredLeaves() {
			String leavesName = woodRegistry.isNetherWood() ? "_wart_block" : "_leaves";
			Block block = woodRegistry.isNetherWood() ? Blocks.WARPED_WART_BLOCK : Blocks.FLOWERING_AZALEA_LEAVES;
			Block creativeTabBlock = woodRegistry.isNetherWood() ? Blocks.SHROOMLIGHT : Blocks.BROWN_MUSHROOM_BLOCK;
			BlockBehaviour.Properties properties = FabricBlockSettings.copyOf(block);
			woodRegistry.leaves = registryHelper.blocks().registerBlockWood(
					woodRegistry.isNetherWood() ? new Block(properties) : new LeavesBlock(properties),
					name.getPath() + leavesName,
					CreativeModeTabs.NATURAL_BLOCKS, creativeTabBlock
			);
			VampireLibClient.COLORED_LEAVES.add(new VampireLibClient.ColoredLeaves(woodRegistry.leaves, true));
			return this;
		}

		public Builder coloredLeaves(int color) {
			String leavesName = woodRegistry.isNetherWood() ? "_wart_block" : "_leaves";
			Block block = woodRegistry.isNetherWood() ? Blocks.WARPED_WART_BLOCK : Blocks.FLOWERING_AZALEA_LEAVES;
			Block creativeTabBlock = woodRegistry.isNetherWood() ? Blocks.SHROOMLIGHT : Blocks.BROWN_MUSHROOM_BLOCK;
			BlockBehaviour.Properties properties = FabricBlockSettings.copyOf(block);
			woodRegistry.leaves = registryHelper.blocks().registerBlockWood(
					woodRegistry.isNetherWood() ? new Block(properties) : new LeavesBlock(properties),
					name.getPath() + leavesName,
					CreativeModeTabs.NATURAL_BLOCKS, creativeTabBlock
			);
			VampireLibClient.COLORED_LEAVES.add(new VampireLibClient.ColoredLeaves(woodRegistry.leaves, true, color));
			return this;
		}

		public Builder coloredLeaves(String nameIn) {
			String leavesName = woodRegistry.isNetherWood() ? "_wart_block" : "_leaves";
			Block block = woodRegistry.isNetherWood() ? Blocks.WARPED_WART_BLOCK : Blocks.FLOWERING_AZALEA_LEAVES;
			Block creativeTabBlock = woodRegistry.isNetherWood() ? Blocks.SHROOMLIGHT : Blocks.BROWN_MUSHROOM_BLOCK;
			BlockBehaviour.Properties properties = FabricBlockSettings.copyOf(block);
			woodRegistry.leaves = registryHelper.blocks().registerBlockWood(
					woodRegistry.isNetherWood() ? new Block(properties) : new LeavesBlock(properties),
					nameIn + leavesName,
					CreativeModeTabs.NATURAL_BLOCKS, creativeTabBlock
			);
			VampireLibClient.ColoredLeaves coloredLeaves = new VampireLibClient.ColoredLeaves(woodRegistry.leaves,
					true);
			VampireLibClient.COLORED_LEAVES.add(coloredLeaves);
			return this;
		}

		public Builder coloredLeaves(String... nameIn) {
			String leavesName = woodRegistry.isNetherWood() ? "_wart_block" : "_leaves";
			Block block = woodRegistry.isNetherWood() ? Blocks.WARPED_WART_BLOCK : Blocks.FLOWERING_AZALEA_LEAVES;
			Block creativeTabBlock = woodRegistry.isNetherWood() ? Blocks.SHROOMLIGHT : Blocks.BROWN_MUSHROOM_BLOCK;
			BlockBehaviour.Properties properties = FabricBlockSettings.copyOf(block);
			for (String name : nameIn) {
				woodRegistry.leaves = registryHelper.blocks().registerBlockWood(
						woodRegistry.isNetherWood() ? new Block(properties) : new LeavesBlock(properties),
						name + leavesName,
						CreativeModeTabs.NATURAL_BLOCKS, creativeTabBlock
				);
				VampireLibClient.COLORED_LEAVES.add(new VampireLibClient.ColoredLeaves(woodRegistry.leaves, true));
				woodRegistry.availableLeaves.add(name);
			}
			return this;
		}

		public Builder coloredLeaves(String nameIn, int color) {
			String leavesName = woodRegistry.isNetherWood() ? "_wart_block" : "_leaves";
			Block block = woodRegistry.isNetherWood() ? Blocks.WARPED_WART_BLOCK : Blocks.FLOWERING_AZALEA_LEAVES;
			Block creativeTabBlock = woodRegistry.isNetherWood() ? Blocks.SHROOMLIGHT : Blocks.BROWN_MUSHROOM_BLOCK;
			BlockBehaviour.Properties properties = FabricBlockSettings.copyOf(block);
			woodRegistry.leaves = registryHelper.blocks().registerBlockWood(
					woodRegistry.isNetherWood() ? new Block(properties) : new LeavesBlock(properties),
					nameIn + leavesName,
					CreativeModeTabs.NATURAL_BLOCKS, creativeTabBlock
			);
			VampireLibClient.COLORED_LEAVES.add(new VampireLibClient.ColoredLeaves(woodRegistry.leaves, true, color));
			return this;
		}

		public Builder coloredLeaves(int color, String... nameIn) {
			String leavesName = woodRegistry.isNetherWood() ? "_wart_block" : "_leaves";
			Block block = woodRegistry.isNetherWood() ? Blocks.WARPED_WART_BLOCK : Blocks.FLOWERING_AZALEA_LEAVES;
			Block creativeTabBlock = woodRegistry.isNetherWood() ? Blocks.SHROOMLIGHT : Blocks.BROWN_MUSHROOM_BLOCK;
			BlockBehaviour.Properties properties = FabricBlockSettings.copyOf(block);
			for (String name : nameIn) {
				woodRegistry.leaves = registryHelper.blocks().registerBlockWood(
						woodRegistry.isNetherWood() ? new Block(properties) : new LeavesBlock(properties),
						name + leavesName,
						CreativeModeTabs.NATURAL_BLOCKS, creativeTabBlock
				);
				VampireLibClient.COLORED_LEAVES.add(
						new VampireLibClient.ColoredLeaves(woodRegistry.leaves, true, color));
				woodRegistry.availableLeaves.add(name);
			}
			return this;
		}

		public Builder coloredLeaves(ColoredBlock... coloredLeavesBlocks) {
			String leavesName = woodRegistry.isNetherWood() ? "_wart_block" : "_leaves";
			Block block = woodRegistry.isNetherWood() ? Blocks.WARPED_WART_BLOCK : Blocks.FLOWERING_AZALEA_LEAVES;
			Block creativeTabBlock = woodRegistry.isNetherWood() ? Blocks.SHROOMLIGHT : Blocks.BROWN_MUSHROOM_BLOCK;
			BlockBehaviour.Properties properties = FabricBlockSettings.copyOf(block);
			for (ColoredBlock coloredLeavesBlock : coloredLeavesBlocks) {
				woodRegistry.leaves = registryHelper.blocks().registerBlockWood(
						woodRegistry.isNetherWood() ? new Block(properties) : new LeavesBlock(properties),
						coloredLeavesBlock.name + leavesName,
						CreativeModeTabs.NATURAL_BLOCKS, creativeTabBlock
				);
				VampireLibClient.COLORED_LEAVES.add(
						new VampireLibClient.ColoredLeaves(woodRegistry.leaves, true, coloredLeavesBlock.color));
				woodRegistry.availableLeaves.add(coloredLeavesBlock.name);
			}
			return this;
		}

		public Builder sapling() {
			if (!woodRegistry.isNetherWood())
				woodRegistry.sapling = registryHelper.blocks().registerBlockWood(
					new SaplingBaseBlock(woodRegistry.saplingGenerator),
					name.getPath() + "_sapling",
					CreativeModeTabs.NATURAL_BLOCKS, Blocks.BROWN_MUSHROOM
			);
			else woodRegistry.sapling = registryHelper.blocks().registerBlockWood(
					new FungusBaseBlock(woodRegistry.fungusGenerator, woodRegistry.baseFungusBlock),
					name.getPath() + "_fungus",
					CreativeModeTabs.NATURAL_BLOCKS, Blocks.SHORT_GRASS
			);
			return this;
		}

		public Builder pottedSapling() {
			String name = "potted_" + this.name.getPath() + (!woodRegistry.isNetherWood() ?
					"_sapling" : "_fungus");
			woodRegistry.pottedSapling = registryHelper.blocks().registerBlockWithoutItem(
					name,
					new FlowerPotBaseBlock(woodRegistry.sapling)
			);
			return this;
		}

		public Builder sapling(String nameIn) {
			if (!woodRegistry.isNetherWood()) {
				woodRegistry.sapling = registryHelper.blocks().registerBlockWood(
						new SaplingBaseBlock(woodRegistry.saplingGenerator),
						nameIn + "_sapling",
						CreativeModeTabs.NATURAL_BLOCKS, Blocks.BROWN_MUSHROOM
				);
			} else {
				woodRegistry.sapling = registryHelper.blocks().registerBlockWood(
						new FungusBaseBlock(woodRegistry.fungusGenerator, woodRegistry.baseFungusBlock),
						nameIn + "_fungus",
						CreativeModeTabs.NATURAL_BLOCKS, Blocks.SHORT_GRASS
				);
			}
			woodRegistry.availableSaplings.add(nameIn);
			return this;
		}

		public Builder pottedSapling(String nameIn) {
			String name = "potted_" + nameIn + (!woodRegistry.isNetherWood() ?
					"_sapling" : "_fungus");
			woodRegistry.pottedSapling = registryHelper.blocks().registerBlockWithoutItem(
					name,
					new FlowerPotBaseBlock(woodRegistry.sapling)
			);
			return this;
		}

		public Builder saplings(String... names) {
			for (String saplingName : names) {
				if (!woodRegistry.isNetherWood()) {
					woodRegistry.sapling = registryHelper.blocks().registerBlockWood(
							new SaplingBaseBlock(woodRegistry.saplingGenerator),
							saplingName + "_sapling",
							CreativeModeTabs.NATURAL_BLOCKS, Blocks.BROWN_MUSHROOM
					);
				} else {
					woodRegistry.sapling = registryHelper.blocks().registerBlockWood(
							new FungusBaseBlock(woodRegistry.fungusGenerator, woodRegistry.baseFungusBlock),
							saplingName + "_fungus",
							CreativeModeTabs.NATURAL_BLOCKS, Blocks.SHORT_GRASS
					);
				}
				woodRegistry.availableSaplings.add(saplingName);
			}
			return this;
		}

		public Builder pottedSapling(String... names) {
			for (String saplingName : names) {
				String name = "potted_" + saplingName + (!woodRegistry.isNetherWood() ?
						"_sapling" : "_fungus");
				woodRegistry.pottedSapling = registryHelper.blocks().registerBlockWithoutItem(
						name,
						new FlowerPotBaseBlock(woodRegistry.sapling));
			}
			return this;
		}

		public Builder fence() {
			Block block = woodRegistry.isNetherWood() ? Blocks.WARPED_FENCE : woodRegistry.isBambooWood() ? Blocks.BAMBOO_FENCE : Blocks.DARK_OAK_FENCE;
			woodRegistry.fence = registryHelper.blocks().registerBlock(
					new FenceBlock(BlockBehaviour.Properties.ofLegacyCopy(block)),
					name.getPath() + "_fence",
					CreativeModeTabs.BUILDING_BLOCKS, woodRegistry.mosaicSlab != null
							? woodRegistry.mosaicSlab : woodRegistry.slab
			);
			return this;
		}

		public Builder fenceGate() {
			Block block = woodRegistry.isNetherWood() ? Blocks.WARPED_FENCE_GATE : woodRegistry.isBambooWood() ? Blocks.BAMBOO_FENCE_GATE : Blocks.DARK_OAK_FENCE_GATE;
			woodRegistry.fenceGate = registryHelper.blocks().registerBlock(
					new FenceGateBlock(woodType, BlockBehaviour.Properties.ofLegacyCopy(block)),
					name.getPath() + "_fence_gate",
					CreativeModeTabs.BUILDING_BLOCKS, woodRegistry.fence
			);
			return this;
		}

		public Builder bookshelf() {
			woodRegistry.bookshelf = registryHelper.blocks().registerBlockWood(
					new Block(BlockBehaviour.Properties.ofLegacyCopy(woodRegistry.planks)),
					name.getPath() + "_bookshelf",
					CreativeModeTabs.FUNCTIONAL_BLOCKS, Blocks.CHISELED_BOOKSHELF
			);
			return this;
		}

		public Builder chiseledBookshelf() {
			woodRegistry.chiseledBookshelf = registryHelper.blocks().registerBlockWood(
					new ChiseledBookShelfBlock(BlockBehaviour.Properties.ofLegacyCopy(woodRegistry.planks)),
					"chiseled_" + name.getPath() + "_bookshelf",
					CreativeModeTabs.FUNCTIONAL_BLOCKS, Blocks.LECTERN
			);
			((IBlockEntityType) BlockEntityType.CHISELED_BOOKSHELF).vlAddBlocks(woodRegistry.chiseledBookshelf);
			return this;
		}

		public Builder door() {
			Block block = woodRegistry.isNetherWood() ? Blocks.WARPED_DOOR : woodRegistry.isBambooWood() ? Blocks.BAMBOO_DOOR : Blocks.DARK_OAK_DOOR;
			woodRegistry.door = registryHelper.blocks().registerDoubleBlock(
					new DoorBlock(woodType.setType(), BlockBehaviour.Properties.ofLegacyCopy(block)),
					name.getPath() + "_door",
					CreativeModeTabs.BUILDING_BLOCKS, woodRegistry.fenceGate
			);
			return this;
		}

		public Builder trapdoor() {
			Block block = woodRegistry.isNetherWood() ? Blocks.WARPED_TRAPDOOR : woodRegistry.isBambooWood() ? Blocks.BAMBOO_TRAPDOOR : Blocks.MANGROVE_TRAPDOOR;
			woodRegistry.trapdoor = registryHelper.blocks().registerBlock(
					new TrapDoorBlock(woodType.setType(), BlockBehaviour.Properties.ofLegacyCopy(block)),
					name.getPath() + "_trapdoor",
					CreativeModeTabs.BUILDING_BLOCKS, woodRegistry.door
			);
			return this;
		}

		public Builder pressurePlate() {
			Block block = woodRegistry.isNetherWood() ? Blocks.WARPED_PRESSURE_PLATE : woodRegistry.isBambooWood() ? Blocks.BAMBOO_PRESSURE_PLATE : Blocks.DARK_OAK_PRESSURE_PLATE;
			woodRegistry.pressurePlate = registryHelper.blocks().registerBlock(
					new PressurePlateBlock(woodType.setType(), BlockBehaviour.Properties.ofLegacyCopy(block)),
					name.getPath() + "_pressure_plate",
					CreativeModeTabs.BUILDING_BLOCKS, woodRegistry.trapdoor
			);
			return this;
		}

		public Builder button() {
			woodRegistry.button = registryHelper.blocks().registerBlock(
					woodenButton(woodType.setType()), name.getPath() + "_button",
					CreativeModeTabs.BUILDING_BLOCKS, woodRegistry.pressurePlate
			);
			return this;
		}

		private static ButtonBlock woodenButton(BlockSetType blockSetType) {
			return new ButtonBlock(blockSetType, 30, BlockBehaviour.Properties.of().noCollission().strength(0.5F));
		}

		public Builder ladder() {
			woodRegistry.ladder = registryHelper.blocks().registerBlockWood(
					new CustomLadderBlock(),
					name.getPath() + "_ladder",
					CreativeModeTabs.FUNCTIONAL_BLOCKS, Blocks.SCAFFOLDING
			);
			return this;
		}

		public Builder beehive() {
			woodRegistry.beehive = registryHelper.blocks().registerBlockWood(
					new BeehiveBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.BEEHIVE)),
					name.getPath() + "_beehive",
					CreativeModeTabs.FUNCTIONAL_BLOCKS, Blocks.SUSPICIOUS_SAND
			);
			((IBlockEntityType) BlockEntityType.BEEHIVE).vlAddBlocks(woodRegistry.beehive);
			return this;
		}

		public Builder sign() {
			Item baseHangingSignItem = this.woodRegistry.isNetherWood() ? Items.CHEST :
					this.woodRegistry.isBambooWood() ? Items.CRIMSON_SIGN : Items.BAMBOO_SIGN;
			Block baseSignBlock = this.woodRegistry.isNetherWood() ? Blocks.WARPED_SIGN :
					this.woodRegistry.isBambooWood() ? Blocks.BAMBOO_SIGN : Blocks.MANGROVE_SIGN;
			ResourceLocation signTexture = new ResourceLocation(this.name.getNamespace(),
					"wood_types/" + this.name.getPath() + "/sign");
			this.woodRegistry.sign = this.registryHelper.blocks().registerBlockWithoutItem(
					this.name.getPath() + "_sign",
					new TerraformSignBlock(signTexture, FabricBlockSettings.copyOf(baseSignBlock))
			);
			this.woodRegistry.wallSign = this.registryHelper.blocks().registerBlockWithoutItem(
					this.name.getPath() + "_wall_sign",
					new TerraformWallSignBlock(signTexture, FabricBlockSettings.copyOf(baseSignBlock))
			);
			this.woodRegistry.signItem = this.registryHelper.items().registerItemWood(
					this.name.getPath() + "_sign",
					new SignItem(
							new Item.Properties().stacksTo(16), this.woodRegistry.sign,
							this.woodRegistry.wallSign
					),
					CreativeModeTabs.FUNCTIONAL_BLOCKS, baseHangingSignItem
			);
			((IBlockEntityType) BlockEntityType.SIGN).vlAddBlocks(this.woodRegistry.sign, this.woodRegistry.wallSign);
			return this;
		}

		public Builder hangingSign() {
			Block baseHangingSignBlock = this.woodRegistry.isNetherWood() ? Blocks.WARPED_HANGING_SIGN :
					this.woodRegistry.isBambooWood() ? Blocks.BAMBOO_HANGING_SIGN : Blocks.MANGROVE_HANGING_SIGN;
			ResourceLocation hangingSignTexture = new ResourceLocation(this.name.getNamespace(),
					"wood_types/" + this.name.getPath() + "/hanging_sign");
			ResourceLocation hangingSignGuiTexture = new ResourceLocation(this.name.getNamespace(),
					"gui/hanging_signs/" + this.name.getPath());
			this.woodRegistry.hangingSign = this.registryHelper.blocks().registerBlockWithoutItem(
					this.name.getPath() + "_hanging_sign",
					new TerraformHangingSignBlock(hangingSignTexture, hangingSignGuiTexture, FabricBlockSettings.copyOf(baseHangingSignBlock))
			);
			this.woodRegistry.hangingWallSign = this.registryHelper.blocks().registerBlockWithoutItem(
					this.name.getPath() + "_wall_hanging_sign",
					new TerraformWallHangingSignBlock(hangingSignTexture, hangingSignGuiTexture, FabricBlockSettings.copyOf(baseHangingSignBlock))
			);
			this.woodRegistry.hangingSignItem = this.registryHelper.items().registerItem(
					this.name.getPath() + "_hanging_sign",
					new HangingSignItem(
							this.woodRegistry.hangingSign, this.woodRegistry.hangingWallSign,
							new Item.Properties().stacksTo(16)
					),
					CreativeModeTabs.FUNCTIONAL_BLOCKS, woodRegistry.signItem
			);
			((IBlockEntityType) BlockEntityType.HANGING_SIGN).vlAddBlocks(this.woodRegistry.hangingSign, this.woodRegistry.hangingWallSign);
			return this;
		}

		public Builder boat() {
			String name = woodRegistry.isBambooWood() ? "_raft" : "_boat";
			woodRegistry.boatType = TerraformBoatTypeRegistry.createKey(Utils.appendToPath(this.name, name));

			woodRegistry.boatItem = TerraformBoatItemHelper.registerBoatItem(Utils.appendToPath(this.name, name),
					woodRegistry.boatType, false);
			woodRegistry.chestBoatItem = TerraformBoatItemHelper.registerBoatItem(
					Utils.appendToPath(this.name, "_chest" + name), woodRegistry.boatType, true);
			TerraformBoatType.Builder builder = new TerraformBoatType.Builder()
					.item(woodRegistry.boatItem)
					.chestItem(woodRegistry.chestBoatItem)
					.planks(woodRegistry.planks.asItem());
			if (this.woodRegistry.isBambooWood()) builder.raft();
			Registry.register(TerraformBoatTypeRegistry.INSTANCE, Utils.appendToPath(this.name, name), builder.build());
			if (FabricLoader.getInstance().getEnvironmentType().equals(EnvType.CLIENT)) {
				TerraformBoatClientHelper.registerModelLayers(Utils.appendToPath(this.name, name), woodRegistry.isBambooWood());
			}

			Item baseBoatItem = this.woodRegistry.isNetherWood() ? Items.RAIL :
				this.woodRegistry.isBambooWood() ? Items.RAIL : Items.BAMBOO_RAFT;
			ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(entries -> {
				entries.addBefore(baseBoatItem, woodRegistry.boatItem);
				entries.addAfter(woodRegistry.boatItem, woodRegistry.chestBoatItem);
			});
			return this;
		}

		public Builder nonFlammable() {
			woodRegistry.flammable = false;
			return this;
		}

		public Builder woodPropertyType(WoodPropertyType woodPropertyType) {
			woodRegistry.woodPropertyType = woodPropertyType;
			if (woodType == null) switch (woodPropertyType) {
				case OVERWORLD, AZALEA -> woodType = WoodType.OAK;
				case NETHER -> woodType = WoodType.CRIMSON;
				case CHERRY -> woodType = WoodType.CHERRY;
				case BAMBOO -> woodType = WoodType.BAMBOO;
			}
			return this;
		}

		public Builder defaultLogsAndWoods() {
			return this.log().wood().strippedLog().strippedWood();
		}

		public Builder defaultBlocks(WoodPropertyType woodPropertyType) {
			return this.woodPropertyType(woodPropertyType).defaultLogsAndWoods()
					.planks().leaves().sapling().pottedSapling().stairs().slab()
					.fence().fenceGate().door().trapdoor()
					.pressurePlate()
					.button().sign().hangingSign().boat();
		}

		public Builder defaultBlocks() {
			return this.defaultBlocks(WoodPropertyType.OVERWORLD);
		}

		public Builder defaultExtras() {
			return this.bookshelf().chiseledBookshelf().beehive().ladder();
		}

		public Builder defaultBlocksColoredLeaves(WoodPropertyType woodPropertyType) {
			return this.woodPropertyType(woodPropertyType).defaultLogsAndWoods()
					.planks().coloredLeaves().sapling().pottedSapling().stairs()
					.slab().fence().fenceGate().door().trapdoor()
					.pressurePlate()
					.button().sign().hangingSign().boat();
		}

		public Builder defaultBlocksColoredLeaves() {
			return this.defaultBlocksColoredLeaves(WoodPropertyType.OVERWORLD);
		}

		public Builder defaultBlocksColoredLeaves(WoodPropertyType woodPropertyType, int color) {
			return this.woodPropertyType(woodPropertyType).defaultLogsAndWoods().planks()
					.coloredLeaves(color).sapling().pottedSapling().stairs().slab()
					.fence().fenceGate().door().trapdoor()
					.pressurePlate()
					.button().sign().hangingSign().boat();
		}

		public Builder defaultBlocksColoredLeaves(int color) {
			return this.defaultBlocksColoredLeaves(WoodPropertyType.OVERWORLD, color);
		}

		public WoodRegistry build() {
			if (woodRegistry.leaves != null && !woodRegistry.isNetherWood())
				ComposterBlock.COMPOSTABLES.put(woodRegistry.leaves, 0.3F);
			if (woodRegistry.flammable) {
				// flammable blocks
				int baseBurnChance = 5;
				int largeBurnChance = baseBurnChance * 6;

				int baseSpreadChance = 20;
				int smallSpreadChance = baseSpreadChance / 4;
				int largeSpreadChance = baseSpreadChance * 3;

				FlammableBlockRegistry flammableBlockRegistry = FlammableBlockRegistry.getDefaultInstance();
				if (woodRegistry.planks != null && !woodRegistry.preRegisteredPlanks)
					flammableBlockRegistry.add(woodRegistry.planks, baseBurnChance, baseSpreadChance);
				if (woodRegistry.slab != null)
					flammableBlockRegistry.add(woodRegistry.slab, baseBurnChance, baseSpreadChance);
				if (woodRegistry.fenceGate != null)
					flammableBlockRegistry.add(woodRegistry.fenceGate, baseBurnChance, baseSpreadChance);
				if (woodRegistry.fence != null)
					flammableBlockRegistry.add(woodRegistry.fence, baseBurnChance, baseSpreadChance);
				if (woodRegistry.stairs != null)
					flammableBlockRegistry.add(woodRegistry.stairs, baseBurnChance, baseSpreadChance);
				if (woodRegistry.log != null)
					flammableBlockRegistry.add(woodRegistry.log, baseBurnChance, smallSpreadChance);
				if (woodRegistry.strippedLog != null)
					flammableBlockRegistry.add(woodRegistry.strippedLog, baseBurnChance, smallSpreadChance);
				if (woodRegistry.strippedWood != null)
					flammableBlockRegistry.add(woodRegistry.strippedWood, baseBurnChance, smallSpreadChance);
				if (woodRegistry.wood != null)
					flammableBlockRegistry.add(woodRegistry.wood, baseBurnChance, smallSpreadChance);
				if (woodRegistry.leaves != null)
					flammableBlockRegistry.add(woodRegistry.leaves, largeBurnChance, largeSpreadChance);

				// fuel registering
				int fenceFuelTime = 300;

				FuelRegistry fuelRegistry = FuelRegistry.INSTANCE;
				if (woodRegistry.fence != null)
					fuelRegistry.add(woodRegistry.fence, fenceFuelTime);
				if (woodRegistry.fenceGate != null)
					fuelRegistry.add(woodRegistry.fenceGate, fenceFuelTime);
			}

			if (woodRegistry.log != null && woodRegistry.wood != null && woodRegistry.strippedLog != null &&
					woodRegistry.strippedWood != null) {
				StrippableBlockRegistry.register(woodRegistry.log, woodRegistry.strippedLog);
				StrippableBlockRegistry.register(woodRegistry.wood, woodRegistry.strippedWood);
			}

			if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
				if (woodRegistry.leaves != null) {
					BlockRenderLayerMap.INSTANCE.putBlock(woodRegistry.leaves, RenderType.cutoutMipped());
				}
				if (woodRegistry.sapling != null) {
					BlockRenderLayerMap.INSTANCE.putBlock(woodRegistry.sapling, RenderType.cutoutMipped());
				}
				if (woodRegistry.door != null) {
					BlockRenderLayerMap.INSTANCE.putBlock(woodRegistry.door, RenderType.cutoutMipped());
				}
				if (woodRegistry.trapdoor != null) {
					BlockRenderLayerMap.INSTANCE.putBlock(woodRegistry.trapdoor, RenderType.cutoutMipped());
				}
			}

			return woodRegistry;
		}
	}

	public enum WoodPropertyType {
		OVERWORLD,
		NETHER,
		CHERRY,
		BAMBOO,
		AZALEA
	}

	public record ColoredBlock(String name, int color) {
	}

	private String translate(String name, Map<String, String> lang) {
		return getTranslation(name, lang);
	}

	public String getTranslation(String key, Map<String, String> lang) {
		if (lang.containsKey(key)) {
			return lang.get(key);
		}
		return key.replace("_", " ");
	}

}
