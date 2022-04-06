package io.github.vampirestudios.vampirelib.client;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/**
 * The {@link TypedBlockEntityWithoutLevelRenderer} responsible for rendering the items of Blueprint's chests.
 *
 * @param <C> The type of {@link BlockEntity} the renderer is for.
 */
@Environment(EnvType.CLIENT)
public class ChestBlockEntityWithoutLevelRenderer<C extends BlockEntity> extends TypedBlockEntityWithoutLevelRenderer<C> {

	public ChestBlockEntityWithoutLevelRenderer(BlockEntityRenderDispatcher dispatcher, EntityModelSet modelSet, C be) {
		super(dispatcher, modelSet, be);
	}

	@Override
	public void renderByItem(ItemStack itemStackIn, TransformType transformType, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
		BlockItem blockItem = (BlockItem) itemStackIn.getItem();
		BlueprintChestBlockEntityRenderer.itemBlock = blockItem.getBlock();
		super.renderByItem(itemStackIn, transformType, poseStack, buffer, combinedLight, combinedOverlay);
		BlueprintChestBlockEntityRenderer.itemBlock = null;
	}

}