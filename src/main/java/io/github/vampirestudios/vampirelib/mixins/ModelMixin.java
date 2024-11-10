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

package io.github.vampirestudios.vampirelib.mixins;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;

import io.github.vampirestudios.vampirelib.api.datagen.DisplayBuilder;
import io.github.vampirestudios.vampirelib.api.datagen.ElementBuilder;
import io.github.vampirestudios.vampirelib.api.datagen.FabricModel;
import io.github.vampirestudios.vampirelib.api.datagen.OverrideBuilder;
import io.github.vampirestudios.vampirelib.api.datagen.builder.ItemModelBuilder;

@Mixin(ModelTemplate.class)
public class ModelMixin implements FabricModel {
	@Shadow
	@Final
	private Optional<ResourceLocation> model;
	@Shadow
	@Final
	private Set<TextureSlot> requiredSlots;
	@Shadow
	@Final
	private Optional<String> suffix;
	@Unique
	private final EnumMap<DisplayBuilder.Position, DisplayBuilder> displays = new EnumMap<>(DisplayBuilder.Position.class);
	@Unique
	private final List<ElementBuilder> elements = new ArrayList<>();
	@Unique
	private final List<OverrideBuilder> overrides = new ArrayList<>();
	@Unique
	private ItemModelBuilder.GuiLight guiLight = null;
	@Unique
	private boolean ambientOcclusion = true;

	@Override
	public Optional<ResourceLocation> fabric_getParent() {
		return model;
	}

	@Override
	public Set<TextureSlot> fabric_getRequiredTextures() {
		return requiredSlots;
	}

	@Override
	public Optional<String> fabric_getVariant() {
		return suffix;
	}

	@Override
	public ModelTemplate fabric_withDisplay(DisplayBuilder.Position position, DisplayBuilder builder) {
		this.displays.put(position, builder);
		return (ModelTemplate) (Object) this;
	}

	@Override
	public EnumMap<DisplayBuilder.Position, DisplayBuilder> fabric_getDisplayBuilders() {
		return displays;
	}

	@Override
	public ModelTemplate fabric_addElement(ElementBuilder builder) {
		this.elements.add(builder);
		return (ModelTemplate) (Object) this;
	}

	@Override
	public List<ElementBuilder> fabric_getElementBuilders() {
		return elements;
	}

	@Override
	public ModelTemplate fabric_addOverride(OverrideBuilder builder) {
		this.overrides.add(builder);
		return (ModelTemplate) (Object) this;
	}

	@Override
	public List<OverrideBuilder> fabric_getOverrideBuilders() {
		return overrides;
	}

	@Override
	public ModelTemplate fabric_setGuiLight(ItemModelBuilder.GuiLight guiLight) {
		this.guiLight = guiLight;
		return (ModelTemplate) (Object) this;
	}

	@Override
	public ItemModelBuilder.GuiLight fabric_getGuiLight() {
		return guiLight;
	}

	@Override
	public ModelTemplate fabric_setAmbientOcclusion(boolean ambientOcclusion) {
		this.ambientOcclusion = ambientOcclusion;
		return (ModelTemplate) (Object) this;
	}

	@Override
	public boolean fabric_getAmbientOcclusion() {
		return ambientOcclusion;
	}

	@Inject(method = "method_65503", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILHARD)
	public void addExtraProperties(Map<TextureSlot, ResourceLocation> map, CallbackInfoReturnable<JsonElement> cir, JsonObject jsonObject) {
		if (!displays.isEmpty()) {
			JsonObject display = new JsonObject();
			this.displays.forEach((p, d) -> display.add(p.name().toLowerCase(), d.build()));
			jsonObject.add("display", display);
		}

		if (!elements.isEmpty()) {
			JsonArray elements = new JsonArray();
			this.elements.forEach(e -> elements.add(e.build()));
			jsonObject.add("elements", elements);
		}

		if (!overrides.isEmpty()) {
			JsonArray overrides = new JsonArray();
			this.overrides.forEach(o -> overrides.add(o.build()));
			jsonObject.add("overrides", overrides);
		}

		if (guiLight != null) {
			jsonObject.addProperty("gui_light", guiLight.name().toLowerCase());
		}

		if (!ambientOcclusion) {
			jsonObject.addProperty("ambientocclusion", false);
		}

		displays.clear();
		elements.clear();
		overrides.clear();
		guiLight = null;
		ambientOcclusion = true;
	}
}
