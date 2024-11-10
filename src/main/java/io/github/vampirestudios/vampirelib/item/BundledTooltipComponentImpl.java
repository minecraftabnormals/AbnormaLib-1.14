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

package io.github.vampirestudios.vampirelib.item;

import java.util.List;

import org.joml.Matrix4f;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;

public record BundledTooltipComponentImpl(List<ClientTooltipComponent> list) implements ClientTooltipComponent {
	@Override
	public int getHeight(Font font) {
		int h = 0;

		for (ClientTooltipComponent component : list) {
			h += component.getHeight(font);
		}

		return h;
	}

	@Override
	public int getWidth(Font textRenderer) {
		int w = 0;

		for (ClientTooltipComponent component : list) {
			w = Math.max(w, component.getWidth(textRenderer));
		}

		return w;
	}

	@Override
	public void renderText(Font textRenderer, int x, int y, Matrix4f matrix4f, MultiBufferSource.BufferSource bufferSource) {
		int y1 = y;

		for (ClientTooltipComponent component : list) {
			component.renderText(textRenderer, x, y1, matrix4f, bufferSource);
			y1 += component.getHeight(textRenderer);
		}
	}

	@Override
	public void renderImage(Font textRenderer, int x, int y, int width, int height, GuiGraphics guiGraphics) {
		int y1 = y;

		for (ClientTooltipComponent component : list) {
			component.renderImage(textRenderer, x, y1, width, height, guiGraphics);
			y1 += component.getHeight(textRenderer);
		}
	}
}
