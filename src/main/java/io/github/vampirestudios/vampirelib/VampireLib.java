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

package io.github.vampirestudios.vampirelib;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.SharedConstants;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;

import io.github.vampirestudios.vampirelib.api.BasicModClass;
import io.github.vampirestudios.vampirelib.api.ConvertibleBlockPair;
import io.github.vampirestudios.vampirelib.utils.BlockChiseler;
import io.github.vampirestudios.vampirelib.utils.Rands;
import io.github.vampirestudios.vampirelib.utils.registry.WoodRegistry;
import io.github.vampirestudios.vampirelib.utils.registry.WoodRegistry.WoodPropertyType;

@Environment(EnvType.CLIENT)
public class VampireLib extends BasicModClass {
	public static final VampireLib INSTANCE = new VampireLib();

	public static final List<ConvertibleBlockPair> CONVERTIBLE_BLOCKS = new ArrayList<>();

	public static final Gson GSON = new GsonBuilder()
			.setLenient().setPrettyPrinting()
			.create();

	public static final boolean TEST_CONTENT_ENABLED = false;

	public static WoodRegistry TEST_WOOD;
	public static WoodRegistry TEST_WOOD1;
	public static WoodRegistry TEST_WOOD2;
	public static WoodRegistry TEST_WOOD3;
	public static WoodRegistry TEST_WOOD4;
	public static WoodRegistry TEST_WOOD5;
	public static WoodRegistry TEST_WOOD6;
	public static WoodRegistry TEST_WOOD7;
	public static WoodRegistry TEST_WOOD8;
	public static WoodRegistry TEST_WOOD9;
	public static WoodRegistry TEST_WOOD10;
	public static WoodRegistry TEST_WOOD11;
	public static WoodRegistry TEST_WOOD12;
	public static WoodRegistry TEST_WOOD13;
	public static WoodRegistry TEST_WOOD14;
	public static WoodRegistry TEST_WOOD15;
	public static WoodRegistry TEST_WOOD16;

	public static WoodRegistry TEST_NETHER_WOOD;
	public static WoodRegistry TEST_NETHER_WOOD1;
	public static WoodRegistry TEST_NETHER_WOOD2;
	public static WoodRegistry TEST_NETHER_WOOD3;
	public static WoodRegistry TEST_NETHER_WOOD4;
	public static WoodRegistry TEST_NETHER_WOOD5;
	public static WoodRegistry TEST_NETHER_WOOD6;
	public static WoodRegistry TEST_NETHER_WOOD7;
	public static WoodRegistry TEST_NETHER_WOOD8;
	public static WoodRegistry TEST_NETHER_WOOD9;
	public static WoodRegistry TEST_NETHER_WOOD10;
	public static WoodRegistry TEST_NETHER_WOOD11;
	public static WoodRegistry TEST_NETHER_WOOD12;
	public static WoodRegistry TEST_NETHER_WOOD13;

	public VampireLib() {
		super("vampirelib", "VampireLib", "7.0.2+build.1-1.20.1");
	}

	@Override
	public void onInitialize() {
		shouldNotPrintVersionMessage();
		getLogger().info(String.format("%s running %s v%s for %s",
				Rands.chance(15) ? "Your are" : (Rands.chance(15) ? "You're" : "You are"),
				modName(), modVersion(), SharedConstants.getCurrentVersion().getName()));
		BlockChiseler.setup();

		if (TEST_CONTENT_ENABLED) {
			//Overworld
			TEST_WOOD = WoodRegistry.of(identifier("test")).defaultBlocks().build();
			TEST_WOOD1 = WoodRegistry.of(identifier("test1")).defaultBlocksColoredLeaves().build();

			TEST_WOOD2 = WoodRegistry.of(identifier("test2")).defaultBlocks().defaultExtras().build();
			TEST_WOOD3 = WoodRegistry.of(identifier("test3")).defaultBlocksColoredLeaves().defaultExtras().build();

			TEST_WOOD4 = WoodRegistry.of(identifier("test4")).defaultBlocks().defaultExtras().build();
			TEST_WOOD5 = WoodRegistry.of(identifier("test5")).defaultBlocksColoredLeaves().defaultExtras().build();

			TEST_WOOD6 = WoodRegistry.of(identifier("test6")).defaultBlocks().defaultExtras().build();
			TEST_WOOD7 = WoodRegistry.of(identifier("test7")).defaultBlocksColoredLeaves().defaultExtras().build();

			TEST_WOOD8 = WoodRegistry.of(identifier("test8")).defaultBlocks().defaultExtras().build();
			TEST_WOOD9 = WoodRegistry.of(identifier("test9")).defaultBlocksColoredLeaves().defaultExtras().build();

			TEST_WOOD10 = WoodRegistry.of(identifier("test10")).defaultBlocks().defaultExtras().build();
			TEST_WOOD11 = WoodRegistry.of(identifier("test11")).defaultBlocksColoredLeaves().defaultExtras().build();

			TEST_WOOD12 = WoodRegistry.of(identifier("test12")).defaultBlocks().defaultExtras().build();
			TEST_WOOD13 = WoodRegistry.of(identifier("test13")).defaultBlocksColoredLeaves().defaultExtras().build();

			TEST_WOOD14 = WoodRegistry.of(identifier("test14")).woodPropertyType(WoodPropertyType.OVERWORLD).leaves().sapling().build();

			TEST_WOOD15 = WoodRegistry.of(identifier("test15")).woodPropertyType(WoodPropertyType.OVERWORLD).leaves().build();

			TEST_WOOD16 = WoodRegistry.of(identifier("test16")).woodPropertyType(WoodPropertyType.OVERWORLD).sapling().build();

			//Nether
			TEST_NETHER_WOOD = WoodRegistry.of(identifier("test_nether")).defaultBlocks(WoodPropertyType.NETHER).build();
			TEST_NETHER_WOOD1 = WoodRegistry.of(identifier("test1_nether")).defaultBlocksColoredLeaves(WoodPropertyType.NETHER).build();

			TEST_NETHER_WOOD2 = WoodRegistry.of(identifier("test2_nether")).defaultBlocks(WoodPropertyType.NETHER).defaultExtras().build();
			TEST_NETHER_WOOD3 = WoodRegistry.of(identifier("test3_nether")).defaultBlocksColoredLeaves(WoodPropertyType.NETHER).defaultExtras().build();

			TEST_NETHER_WOOD4 = WoodRegistry.of(identifier("test4_nether")).defaultBlocks(WoodPropertyType.NETHER).defaultExtras().build();
			TEST_NETHER_WOOD5 = WoodRegistry.of(identifier("test5_nether")).defaultBlocksColoredLeaves(WoodPropertyType.NETHER).defaultExtras().build();

			TEST_NETHER_WOOD6 = WoodRegistry.of(identifier("test6_nether")).defaultBlocks(WoodPropertyType.NETHER).defaultExtras().nonFlammable().build();
			TEST_NETHER_WOOD7 = WoodRegistry.of(identifier("test7_nether")).defaultBlocksColoredLeaves(WoodPropertyType.NETHER).defaultExtras().build();

			TEST_NETHER_WOOD8 = WoodRegistry.of(identifier("test8_nether")).defaultBlocks(WoodPropertyType.NETHER).defaultExtras().nonFlammable().build();
			TEST_NETHER_WOOD9 = WoodRegistry.of(identifier("test9_nether")).defaultBlocksColoredLeaves(WoodPropertyType.NETHER).defaultExtras()
					.nonFlammable().build();

			TEST_NETHER_WOOD10 = WoodRegistry.of(identifier("test10_nether")).defaultBlocks(WoodPropertyType.NETHER).defaultExtras().nonFlammable().build();
			TEST_NETHER_WOOD11 = WoodRegistry.of(identifier("test11_nether")).defaultBlocksColoredLeaves(WoodPropertyType.NETHER).defaultExtras()
					.nonFlammable().build();

			TEST_NETHER_WOOD12 = WoodRegistry.of(identifier("test12_nether")).defaultBlocks(WoodPropertyType.NETHER).defaultExtras().nonFlammable().build();
			TEST_NETHER_WOOD13 = WoodRegistry.of(identifier("test13_nether")).defaultBlocksColoredLeaves(WoodPropertyType.NETHER).defaultExtras()
					.nonFlammable().build();
		}

		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			if (!world.isClientSide) {
				for (ConvertibleBlockPair convertibleBlock : CONVERTIBLE_BLOCKS) {
					ItemStack itemStack = player.getItemInHand(hand);
					BlockState blockState = world.getBlockState(hitResult.getBlockPos());
					if (convertibleBlock.getConversionItem().matches(itemStack)) {
						if (blockState.getBlock() == convertibleBlock.getOriginal()) {
							if (convertibleBlock.getSound() != null)
								world.playSound(null, hitResult.getBlockPos(), convertibleBlock.getSound(),
										SoundSource.BLOCKS, 1.0F, 1.0F);

							if (convertibleBlock.getDroppedItem() != null) {
								ItemStack newStack = new ItemStack(convertibleBlock.getDroppedItem());
								if (!newStack.isEmpty() &&
										world.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
									ItemEntity itemEntity = new ItemEntity(world, hitResult.getBlockPos().getX() + 0.5,
											hitResult.getBlockPos().getY() + 0.5,
											hitResult.getBlockPos().getZ() + 0.5,
											newStack);
									itemEntity.setDefaultPickUpDelay();
									world.addFreshEntity(itemEntity);
								}
							}

							world.setBlock(hitResult.getBlockPos(), convertibleBlock.getConverted()
									.withPropertiesOf(blockState), 11);
							itemStack.hurtAndBreak(1, player, playerEntity -> playerEntity.broadcastBreakEvent(hand));
							world.gameEvent(GameEvent.BLOCK_CHANGE, hitResult.getBlockPos(),
									GameEvent.Context.of(player, blockState));
							return InteractionResult.SUCCESS;
						}
					} else if (convertibleBlock.getReversingItem() != null &&
							convertibleBlock.getReversingItem().matches(itemStack) &&
							blockState.is(convertibleBlock.getConverted())) {
						if (convertibleBlock.getSound() != null)
							world.playSound(null, hitResult.getBlockPos(), convertibleBlock.getSound(),
									SoundSource.BLOCKS, 1.0F, 1.0F);

						if (convertibleBlock.getDroppedItem() != null) {
							ItemStack newStack = new ItemStack(convertibleBlock.getDroppedItem());
							if (!newStack.isEmpty() && world.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
								ItemEntity itemEntity = new ItemEntity(world, hitResult.getBlockPos().getX() + 0.5,
										hitResult.getBlockPos().getY() + 0.5,
										hitResult.getBlockPos().getZ() + 0.5, newStack);
								itemEntity.setDefaultPickUpDelay();
								world.addFreshEntity(itemEntity);
							}
						}

						world.setBlock(hitResult.getBlockPos(), convertibleBlock.getOriginal()
								.withPropertiesOf(blockState), 11);
						itemStack.hurtAndBreak(1, player, playerEntity -> playerEntity.broadcastBreakEvent(hand));
						world.gameEvent(GameEvent.BLOCK_CHANGE, hitResult.getBlockPos(),
								GameEvent.Context.of(player, blockState));
						return InteractionResult.SUCCESS;
					}
				}
			}
			return InteractionResult.PASS;
		});

		VFeatureFlags.builder.create(identifier("test"));
		VFeatureFlags.builder.create(identifier("test1"));
		VFeatureFlags.builder.create(identifier("test2"));
		VFeatureFlags.builder.create(identifier("test3"));
		VFeatureFlags.rebuild();
	}
}
