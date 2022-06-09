package io.github.vampirestudios.vampirelib.mixins.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.FogType;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import io.github.vampirestudios.vampirelib.api.callbacks.FogEvents;
import io.github.vampirestudios.vampirelib.api.callbacks.FogEvents.ColorData;

@Environment(EnvType.CLIENT)
@Mixin(FogRenderer.class)
public abstract class FogRendererMixin {
	@Shadow
	private static float fogRed;

	@Shadow
	private static float fogGreen;

	@Shadow
	private static float fogBlue;

	@ModifyArgs(method = "setupColor", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;clearColor(FFFF)V", remap = false))
	private static void port_lib$modifyFogColors(Args args, Camera camera, float partialTicks, ClientLevel level, int renderDistanceChunks, float bossColorModifier) {
		ColorData data = new ColorData(camera, fogRed, fogGreen, fogBlue);
		FogEvents.SET_COLOR.invoker().setColor(data, partialTicks);
		fogRed = data.getRed();
		fogGreen = data.getGreen();
		fogBlue = data.getBlue();
	}

	@Inject(method = "setupFog", at = @At("HEAD"), cancellable = true)
	private static void port_lib$setupFog(Camera activeRenderInfo, FogRenderer.FogMode fogType, float f, boolean bl, CallbackInfo ci) {
		float density = FogEvents.SET_DENSITY.invoker().setDensity(activeRenderInfo, 0.1f);
		if (density != 0.1f) {
			RenderSystem.setShaderFogStart(-8.0F);
			RenderSystem.setShaderFogEnd(density * 0.5F);
			ci.cancel();
		}
	}

	@Inject(method = "setupFog", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILHARD)
	private static void port_lib$fogRenderEvent(Camera activeRenderInfo, FogRenderer.FogMode fogType, float farPlaneDistance, boolean nearFog, CallbackInfo ci, FogType fogType2, Entity entity, FogShape fogShape, float f, float g) {
		FogEvents.FogData data = new FogEvents.FogData(f, g, fogShape);
		if (FogEvents.ACTUAL_RENDER_FOG.invoker().onFogRender(fogType, activeRenderInfo, data)) {
			RenderSystem.setShaderFogStart(data.getNearPlaneDistance());
			RenderSystem.setShaderFogEnd(data.getFarPlaneDistance());
			RenderSystem.setShaderFogShape(data.getFogShape());
		}
	}
}