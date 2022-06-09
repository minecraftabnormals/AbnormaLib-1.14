package io.github.vampirestudios.vampirelib.mixins.client;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.mojang.datafixers.util.Pair;

import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;

import io.github.vampirestudios.vampirelib.api.extensions.BlockModelExtensions;
import io.github.vampirestudios.vampirelib.client.model.BlockModelConfiguration;
import io.github.vampirestudios.vampirelib.client.model.CompositeModelState;
import io.github.vampirestudios.vampirelib.client.model.IModelGeometry;
import io.github.vampirestudios.vampirelib.client.model.PerspectiveMapWrapper;
import io.github.vampirestudios.vampirelib.client.render.TransformTypeDependentItemBakedModel;

@Mixin(BlockModel.class)
public abstract class BlockModelMixin implements BlockModelExtensions {
	@Unique
	private final BlockModelConfiguration data = new BlockModelConfiguration((BlockModel) (Object) this);

	@Shadow
	public String name;

	@Shadow
	@Final
	private List<ItemOverride> overrides;

	@Shadow
	public abstract BlockModel getRootModel();

	@Shadow
	public abstract Material getMaterial(String name);

	@Unique
	@Override
	public BlockModelConfiguration getGeometry() {
		return data;
	}

	@Unique
	@Override
	public ItemOverrides getOverrides(ModelBakery pModelBakery, BlockModel pModel, Function<Material, TextureAtlasSprite> textureGetter) {
		return this.overrides.isEmpty() ? ItemOverrides.EMPTY : new ItemOverrides(pModelBakery, pModel, pModelBakery::getModel/*, textureGetter*/, this.overrides);
	}

	@Inject(method = "getMaterials", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/model/BlockModel;getElements()Ljava/util/List;"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
	public void vl$getModelMaterials(Function<ResourceLocation, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors, CallbackInfoReturnable<Collection<Material>> cir, Set set, BlockModel blockModel, Set<Material> materials) {
		if(data.hasCustomGeometry()) {
			materials.addAll(data.getTextureDependencies(modelGetter, missingTextureErrors));
			this.overrides.forEach((p_111475_) -> {
				UnbakedModel unbakedmodel1 = modelGetter.apply(p_111475_.getModel());
				if (!Objects.equals(unbakedmodel1, this)) {
					materials.addAll(unbakedmodel1.getMaterials(modelGetter, missingTextureErrors));
				}
			});
			if (this.getRootModel() == ModelBakery.GENERATION_MARKER) {
				ItemModelGenerator.LAYERS.forEach((p_111467_) -> {
					materials.add(this.getMaterial(p_111467_));
				});
			}
			cir.setReturnValue(materials);
		}
	}

	@Inject(method = "bake(Lnet/minecraft/client/resources/model/ModelBakery;Lnet/minecraft/client/renderer/block/model/BlockModel;Ljava/util/function/Function;Lnet/minecraft/client/resources/model/ModelState;Lnet/minecraft/resources/ResourceLocation;Z)Lnet/minecraft/client/resources/model/BakedModel;", at = @At("HEAD"), cancellable = true)
	public void handleCustomModels(ModelBakery modelBakery, BlockModel otherModel, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelTransform, ResourceLocation modelLocation, boolean guiLight3d, CallbackInfoReturnable<BakedModel> cir) {
		BlockModel blockModel = (BlockModel) (Object) this;
		IModelGeometry<?> customModel = data.getCustomGeometry();
		ModelState customModelState = data.getCustomModelState();
		ModelState newModelState = modelTransform;
		if (customModelState != null)
			newModelState = new CompositeModelState(modelTransform, customModelState, modelTransform.isUvLocked());

		if (customModel != null) {
			BakedModel model = customModel.bake(blockModel.getGeometry(), modelBakery, spriteGetter, newModelState, blockModel.getOverrides(modelBakery, otherModel, spriteGetter), modelLocation);
			if (customModelState != null && !(model instanceof TransformTypeDependentItemBakedModel))
				model = new PerspectiveMapWrapper(model, customModelState);
			cir.setReturnValue(model);
		}
	}

	@Inject(method = "getElements", at = @At("HEAD"), cancellable = true)
	public void fixElements(CallbackInfoReturnable<List<BlockElement>> cir) {
		if (data.hasCustomGeometry()) cir.setReturnValue(java.util.Collections.emptyList());
	}

}