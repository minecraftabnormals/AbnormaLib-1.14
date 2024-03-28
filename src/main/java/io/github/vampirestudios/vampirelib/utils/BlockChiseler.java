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

package io.github.vampirestudios.vampirelib.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;

public class BlockChiseler {

	public static Map<ResourceLocation, ChiselEntry> chiselRegistry = new HashMap<>();
	public static Map<TagKey<Item>, Set<ChiselEntry>> toolTagsToEntries = new HashMap<>();
	public static Map<Item, Set<ChiselEntry>> itemsToEntries = new HashMap<>();

	public static void create(ResourceLocation identifier, TagKey<Item> toolTag, Collection<Block> chiselBlocks) {
		ChiselEntry chiselEntry = new ChiselEntry(chiselBlocks);
		chiselRegistry.put(identifier, chiselEntry);
		if (toolTagsToEntries.containsKey(toolTag)) {
			toolTagsToEntries.get(toolTag).add(chiselEntry);
		} else {
			toolTagsToEntries.put(toolTag, new HashSet<>(Collections.singleton(chiselEntry)));
		}
	}

	public static void create(ResourceLocation identifier, Item item, Collection<Block> chiselBlocks) {
		ChiselEntry chiselEntry = new ChiselEntry(chiselBlocks);
		chiselRegistry.put(identifier, chiselEntry);
		if (itemsToEntries.containsKey(item)) {
			itemsToEntries.get(item).add(chiselEntry);
		} else {
			itemsToEntries.put(item, new HashSet<>(Collections.singleton(chiselEntry)));
		}
	}

	public static void create(ResourceLocation identifier, TagKey<Item> toolTag, ChiselEntry chiselEntry) {
		chiselRegistry.put(identifier, chiselEntry);
		if (toolTagsToEntries.containsKey(toolTag)) {
			toolTagsToEntries.get(toolTag).add(chiselEntry);
		} else {
			toolTagsToEntries.put(toolTag, new HashSet<>(Collections.singleton(chiselEntry)));
		}
	}

	public static void create(ResourceLocation identifier, Item item, ChiselEntry chiselEntry) {
		chiselRegistry.put(identifier, chiselEntry);
		if (itemsToEntries.containsKey(item)) {
			itemsToEntries.get(item).add(chiselEntry);
		} else {
			itemsToEntries.put(item, new HashSet<>(Collections.singleton(chiselEntry)));
		}
	}

	public static void add(ResourceLocation identifier, Collection<Block> carvedBlocks) {
		if (chiselRegistry.containsKey(identifier)) {
			chiselRegistry.get(identifier).chiselDeque.addAll(carvedBlocks);
		} else
			throw new RuntimeException("Unknown chisel entry: \"" + identifier + "\".");
	}

	public static void setup() {
		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			if (!world.isClientSide()) {
				BlockState hitBlockState = world.getBlockState(hitResult.getBlockPos());
				ItemStack heldStack = player.getItemInHand(hand);
				for (Map.Entry<TagKey<Item>, Set<ChiselEntry>> toolToEntries : toolTagsToEntries.entrySet()) {
					if (!new ItemStack(heldStack.getItem()).is(toolToEntries.getKey()))
						continue;
					return loopThroughChiselEntries(player, world, hand, hitResult, hitBlockState, heldStack,
							toolToEntries.getValue());
				}
				for (Map.Entry<Item, Set<ChiselEntry>> itemsToEntry : itemsToEntries.entrySet()) {
					if (itemsToEntry.getKey() != heldStack.getItem())
						continue;
					return loopThroughChiselEntries(player, world, hand, hitResult, hitBlockState, heldStack,
							itemsToEntry.getValue());
				}
			}
			return InteractionResult.PASS;
		});
	}

	private static InteractionResult loopThroughChiselEntries(
			Player player, Level level, InteractionHand hand, BlockHitResult hitResult,
			BlockState hitBlockState, ItemStack heldStack, Set<ChiselEntry> entries
	) {
		for (ChiselEntry chiselEntry : entries) {
			Block newBlock;
			if (player.isShiftKeyDown())
				newBlock = chiselEntry.getPreviousBlock(hitBlockState.getBlock());
			else
				newBlock = chiselEntry.getNextBlock(hitBlockState.getBlock());
			if (newBlock == null) continue;

			level.playSound(null, hitResult.getBlockPos(), SoundEvents.PUMPKIN_CARVE, SoundSource.BLOCKS, 1.0F, 1.0F);
			level.setBlockAndUpdate(hitResult.getBlockPos(), copyTo(hitBlockState, newBlock.defaultBlockState()));
			if (heldStack.getItem().canBeDepleted())
				heldStack.hurtAndBreak(1, player, playerEntity -> player.broadcastBreakEvent(hand));
		}
		return InteractionResult.SUCCESS;
	}

	@SuppressWarnings("rawtypes")
	private static BlockState copyTo(BlockState from, BlockState to) {
		Collection<Property<?>> fromProperties = from.getValues().keySet();
		Collection<Property<?>> toProperties = to.getValues().keySet();
		for (Property property : fromProperties) {
			if (toProperties.contains(property)) {
				to = to.setValue(property, from.getValue(property));
			}
		}
		return to;
	}

	public static class ChiselEntry {
		Deque<Block> chiselDeque;

		ChiselEntry(Collection<Block> blocks) {
			this.chiselDeque = new ConcurrentLinkedDeque<>(blocks);
		}

		Block getNextBlock(Block oldBlock) {
			for (Iterator<Block> iterator = chiselDeque.iterator();
				 iterator.hasNext(); ) {
				if (iterator.next() == oldBlock) {
					if (iterator.hasNext())
						return iterator.next();
					else
						return chiselDeque.getFirst();
				}
			}
			return null;
		}

		Block getPreviousBlock(Block oldBlock) {
			for (Iterator<Block> iterator = chiselDeque.descendingIterator();
				 iterator.hasNext(); ) {
				if (iterator.next() == oldBlock) {
					if (iterator.hasNext())
						return iterator.next();
					else
						return chiselDeque.getLast();
				}
			}
			return null;
		}
	}

}
