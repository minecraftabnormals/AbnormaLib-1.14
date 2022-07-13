package io.github.vampirestudios.vampirelib.api.callbacks.client;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.GuiComponent;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

@Environment(EnvType.CLIENT)
public interface OverlayRenderCallback {
	Event<OverlayRenderCallback> EVENT = EventFactory.createArrayBacked(OverlayRenderCallback.class, callbacks -> (stack, partialTicks, window, type) -> {
		for (OverlayRenderCallback callback : callbacks) {
			if (callback.onOverlayRender(stack, partialTicks, window, type)) {
				resetTexture();
				return true;
			}
		}
		resetTexture();
		return false;
	});

	private static void resetTexture() { // in case overlays change it, which is very likely.
		RenderSystem.setShaderTexture(0, GuiComponent.GUI_ICONS_LOCATION);
	}

	boolean onOverlayRender(PoseStack stack, float partialTicks, Window window, Types type);

	enum Types {
		AIR,
		CROSSHAIRS,
		PLAYER_HEALTH
	}
}