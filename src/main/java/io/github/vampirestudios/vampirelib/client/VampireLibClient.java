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

package io.github.vampirestudios.vampirelib.client;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;

import io.github.vampirestudios.vampirelib.VampireLib;
import io.github.vampirestudios.vampirelib.api.BasicModClass;
import io.github.vampirestudios.vampirelib.item.BundledTooltipComponentImpl;
import io.github.vampirestudios.vampirelib.item.BundledTooltipData;
import io.github.vampirestudios.vampirelib.utils.Rands;

public class VampireLibClient extends BasicModClass {

	public static final List<ColoredLeaves> COLORED_LEAVES = new ArrayList<>();

	public VampireLibClient() {
		super(VampireLib.INSTANCE, true);
	}

	@Override
	public void onInitializeClient() {
		shouldNotPrintVersionMessage();
		getLogger().info(String.format("%s running %s v%s on client-side for %s",
				Rands.chance(15) ? "Your are" : (Rands.chance(15) ? "You're" : "You are"),
				modName(), modVersion(), SharedConstants.getCurrentVersion().getName()));
		TooltipComponentCallback.EVENT.register(maybe -> {
			if (maybe instanceof BundledTooltipData data) {
				return new BundledTooltipComponentImpl(data.list().stream().map(ClientTooltipComponent::create).toList());
			}

			return null;
		});
		COLORED_LEAVES.forEach(coloredLeaves -> {
			if (coloredLeaves.usesBiomeColor) {
				ColorProviderRegistry.BLOCK.register((block, world, pos, layer) -> world != null && pos != null ?
								BiomeColors.getAverageFoliageColor(
										world,
										pos) : FoliageColor.getDefaultColor(),
						coloredLeaves.leavesBlock);
				ColorProviderRegistry.ITEM.register((item, layer) -> {
					BlockState blockState = coloredLeaves.leavesBlock.defaultBlockState();
					return Minecraft.getInstance().getBlockColors().getColor(blockState, null, null, layer);
				}, coloredLeaves.leavesBlock);
			} else if (coloredLeaves.customColor) {
				ColorProviderRegistry.BLOCK.register((block, world, pos, layer) -> coloredLeaves.color,
						coloredLeaves.leavesBlock);
				ColorProviderRegistry.ITEM.register((item, layer) -> coloredLeaves.color,
						coloredLeaves.leavesBlock);
			}
		});
	}

	public static final class ColoredLeaves {

		private final Block leavesBlock;
		private final boolean customColor;
		private final int color;
		private final boolean usesBiomeColor;

		public ColoredLeaves(Block leavesBlock, boolean customColor, int color) {
			this(leavesBlock, customColor, color, false);
		}

		public ColoredLeaves(Block leavesBlock, boolean usesBiomeColor) {
			this(leavesBlock, false, 0, usesBiomeColor);
		}

		public ColoredLeaves(Block leavesBlock, boolean customColor, int color, boolean usesBiomeColor) {
			this.leavesBlock = leavesBlock;
			this.customColor = customColor;
			this.color = color;
			this.usesBiomeColor = usesBiomeColor;
		}

		public int getColor() {
			return color;
		}
	}

}
