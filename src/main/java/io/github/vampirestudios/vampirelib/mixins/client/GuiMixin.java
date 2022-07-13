package io.github.vampirestudios.vampirelib.mixins.client;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.player.Player;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import io.github.vampirestudios.vampirelib.api.callbacks.client.OverlayRenderCallback;

import static net.minecraft.client.gui.GuiComponent.GUI_ICONS_LOCATION;

@Environment(EnvType.CLIENT)
@Mixin(Gui.class)
public abstract class GuiMixin {
	@Unique
	public float port_lib$partialTicks;
	@Shadow
	@Final
	private Minecraft minecraft;

	@Inject(method = "render", at = @At("HEAD"))
	public void port_lib$render(PoseStack matrixStack, float f, CallbackInfo ci) {
		port_lib$partialTicks = f;
	}

	//This might be the wrong method to inject to
	@Inject(
		method = "renderPlayerHealth",
		at = @At(
			value = "INVOKE",
			shift = At.Shift.AFTER,
			target = "Lnet/minecraft/util/profiling/ProfilerFiller;pop()V"
		),
		cancellable = true)
	private void port_lib$renderStatusBars(PoseStack matrixStack, CallbackInfo ci) {
		if (OverlayRenderCallback.EVENT.invoker().onOverlayRender(matrixStack, port_lib$partialTicks, minecraft.getWindow(), OverlayRenderCallback.Types.AIR)) {
			ci.cancel();
		}
	}

	@Inject(
		method = "renderHearts",
		at = @At(
			value = "HEAD"
		),
		cancellable = true
	)
	private void port_lib$renderHealth(PoseStack matrixStack, Player player, int i, int j, int k, int l, float f, int m, int n, int o, boolean bl, CallbackInfo ci) {
		if (OverlayRenderCallback.EVENT.invoker().onOverlayRender(matrixStack, port_lib$partialTicks, minecraft.getWindow(), OverlayRenderCallback.Types.PLAYER_HEALTH)) {
			ci.cancel();
		}
	}

	@Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
	private void port_lib$renderCrosshair(PoseStack matrixStack, CallbackInfo ci) {
		if (OverlayRenderCallback.EVENT.invoker().onOverlayRender(matrixStack, port_lib$partialTicks, minecraft.getWindow(), OverlayRenderCallback.Types.CROSSHAIRS)) {
			ci.cancel();
			return;
		}
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, GUI_ICONS_LOCATION);
		RenderSystem.enableBlend();
	}
}