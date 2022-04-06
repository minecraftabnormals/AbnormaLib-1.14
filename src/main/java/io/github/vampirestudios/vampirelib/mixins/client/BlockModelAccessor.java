package io.github.vampirestudios.vampirelib.mixins.client;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import com.google.gson.Gson;
import com.mojang.datafixers.util.Either;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.BlockElementFace;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

@Mixin(BlockModel.class)
public interface BlockModelAccessor {
	@Accessor("GSON")
	static Gson vl$GSON() {
		throw new RuntimeException("mixin failed!");
	}

	@Accessor()
	@Mutable
	static void setGSON(Gson newGson) {
		throw new RuntimeException("mixin failed!");
	}

	@Invoker("bakeFace")
	static BakedQuad vl$bakeFace(BlockElement part, BlockElementFace partFace, TextureAtlasSprite sprite, Direction direction, ModelState transform, ResourceLocation location) {
		throw new RuntimeException("mixin failed!");
	}

	@Accessor("textureMap")
	Map<String, Either<Material, String>> vl$textureMap();
}