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

package io.github.vampirestudios.vampirelib.api.datagen.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;

import io.github.vampirestudios.vampirelib.api.datagen.OverrideBuilder;
import io.github.vampirestudios.vampirelib.mixins.TextureMapAccessor;

public class ItemModelBuilder extends ModelBuilder<ItemModelBuilder> {
	private final List<OverrideBuilder> overrides = new ArrayList<>();
	private GuiLight guiLight = null;

	private ItemModelBuilder(ResourceLocation parent) {
		super(parent);
	}

	public static ItemModelBuilder createNew(ResourceLocation parent) {
		return new ItemModelBuilder(parent);
	}

	public static ItemModelBuilder copyFrom(ModelTemplate model, TextureMapping textures) {
		Map<TextureSlot, ResourceLocation> copyTextures = ((TextureMapAccessor) textures).getEntries();
		Preconditions.checkArgument(copyTextures.keySet().equals(model.fabric_getRequiredTextures()), "Texture map does not match slots for provided model " + model);

		ItemModelBuilder builder = new ItemModelBuilder(model.fabric_getParent().orElse(null));
		copyTextures.forEach(builder::addTexture);

		model.fabric_getDisplayBuilders().forEach(builder::addDisplay);
		model.fabric_getElementBuilders().forEach(builder::addElement);
		builder.guiLight = model.fabric_getGuiLight();
		builder.overrides.addAll(model.fabric_getOverrideBuilders());

		return builder;
	}

	/**
	 * Sets the <code>gui_light</code> property for this model.
	 *
	 * @param guiLight Either one of the two {@link ItemModelBuilder.GuiLight} entries
	 *                 (<code>FRONT</code>/<code>SIDE</code>), or <code>null</code> to omit it from the end model file.
	 */
	public ItemModelBuilder setGuiLight(@Nullable GuiLight guiLight) {
		this.guiLight = guiLight;
		return this;
	}

	/**
	 * Adds an entry to the <code>overrides</code> property of the model. Override entries consist of a model ID and a
	 * set of "predicates" to override upon, all represented as a float between 0 and 1.
	 *
	 * @param override An {@link OverrideBuilder} to build an individual entry from.
	 */
	public ItemModelBuilder addOverride(OverrideBuilder override) {
		this.overrides.add(override);
		return this;
	}

	/**
	 * Clears all current {@link OverrideBuilder}s for this model builder.
	 */
	public ItemModelBuilder clearOverrides() {
		this.overrides.clear();
		return this;
	}

	@Override
	public ModelTemplate buildModel() {
		ModelTemplate itemModel = super.buildModel();
		itemModel.fabric_setGuiLight(guiLight);
		overrides.forEach(itemModel::fabric_addOverride);
		return itemModel;
	}

	/**
	 * Possible entries for the <code>gui_light</code> property. Internally, this defaults to <code>SIDE</code> if not
	 * directly specified. "Flat" item models (i.e. those with the <code>item/generated</code> parent) usually inherit
	 * <code>FRONT</code> for this property.
	 */
	public enum GuiLight {
		/**
		 * Render as a flat item icon within GUIs.
		 */
		FRONT,
		/**
		 * Render at an angle similarly to a block model within GUIs.
		 */
		SIDE
	}
}
