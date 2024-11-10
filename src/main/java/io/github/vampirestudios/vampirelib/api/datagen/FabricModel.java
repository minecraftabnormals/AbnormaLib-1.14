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

package io.github.vampirestudios.vampirelib.api.datagen;

import java.util.EnumMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;

import io.github.vampirestudios.vampirelib.api.datagen.builder.ItemModelBuilder;

public interface FabricModel {
	default ModelTemplate fabric_withDisplay(DisplayBuilder.Position position, DisplayBuilder builder) {
		throw new UnsupportedOperationException("Implemented via mixin");
	}

	default EnumMap<DisplayBuilder.Position, DisplayBuilder> fabric_getDisplayBuilders() {
		throw new UnsupportedOperationException("Implemented via mixin");
	}

	default ModelTemplate fabric_addElement(ElementBuilder builder) {
		throw new UnsupportedOperationException("Implemented via mixin");
	}

	default List<ElementBuilder> fabric_getElementBuilders() {
		throw new UnsupportedOperationException("Implemented via mixin");
	}

	default ModelTemplate fabric_addOverride(OverrideBuilder builder) {
		throw new UnsupportedOperationException("Implemented via mixin");
	}

	default List<OverrideBuilder> fabric_getOverrideBuilders() {
		throw new UnsupportedOperationException("Implemented via mixin");
	}

	default ModelTemplate fabric_setGuiLight(ItemModelBuilder.GuiLight light) {
		throw new UnsupportedOperationException("Implemented via mixin");
	}

	default ItemModelBuilder.GuiLight fabric_getGuiLight() {
		throw new UnsupportedOperationException("Implemented via mixin");
	}

	default ModelTemplate fabric_setAmbientOcclusion(boolean occlude) {
		throw new UnsupportedOperationException("Implemented via mixin");
	}

	default boolean fabric_getAmbientOcclusion() {
		throw new UnsupportedOperationException("Implemented via mixin");
	}

	default Optional<ResourceLocation> fabric_getParent() {
		throw new UnsupportedOperationException("Implemented via mixin");
	}

	default Optional<String> fabric_getVariant() {
		throw new UnsupportedOperationException("Implemented via mixin");
	}

	default Set<TextureSlot> fabric_getRequiredTextures() {
		throw new UnsupportedOperationException("Implemented via mixin");
	}
}
