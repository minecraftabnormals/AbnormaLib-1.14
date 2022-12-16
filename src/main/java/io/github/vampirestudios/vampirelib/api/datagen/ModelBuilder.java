/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.vampirestudios.vampirelib.api.datagen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;

public abstract class ModelBuilder {
	protected final ResourceLocation parent;
	protected final Set<TextureSlot> requiredTextures = new HashSet<>();
	protected final EnumMap<DisplayBuilder.Position, DisplayBuilder> displays = new EnumMap<>(DisplayBuilder.Position.class);
	protected final List<ElementBuilder> elements = new ArrayList<>();

	protected ModelBuilder(ResourceLocation parent) {
		this.parent = parent;
	}

	public ModelBuilder addTextureKey(TextureSlot texture) {
		this.requiredTextures.add(texture);
		return this;
	}

	/**
	 * Adds an entry to the <code>display</code> property of the model. Display entries consist of a linear
	 * transformation (translation, rotation and scaling) applied to a given "position" that the item (or BlockItem)
	 * with the given model may be in, e.g. whether it's being displayed in a
	 * player's hand or in a GUI.
	 *
	 * @param position A {@link DisplayBuilder.Position} to set the property for. Can be either hand of either first or
	 *                 third person, the head, the ground (as a dropped item), within GUIs or within item frames
	 *                 (<code>FIXED</code>).
	 * @param display A {@link DisplayBuilder} to build the required display property from.
	 */
	public ModelBuilder addDisplay(DisplayBuilder.Position position, DisplayBuilder display) {
		this.displays.put(position, display);
		return this;
	}

	/**
	 * Adds an entry to the <code>elements</code> property of the model. Element entries consist of a pair of opposite
	 * vertices of a cuboid to draw the element out as, with an optional rotation, shading and set of rendered faces.
	 *
	 * @param element An {@link ElementBuilder} to build an individual entry from.
	 */
	public ModelBuilder addElement(ElementBuilder element) {
		this.elements.add(element);
		return this;
	}

	public ModelTemplate build() {
		TextureSlot[] textures = Arrays.copyOf(requiredTextures.toArray(), requiredTextures.size(), TextureSlot[].class);
		ModelTemplate model = new ModelTemplate(Optional.ofNullable(parent), Optional.empty(), textures);

		displays.forEach(model::withDisplay);
		elements.forEach(model::addElement);
		return model;
	}
}
