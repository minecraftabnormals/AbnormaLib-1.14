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
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;

import io.github.vampirestudios.vampirelib.api.datagen.DisplayBuilder;
import io.github.vampirestudios.vampirelib.api.datagen.ElementBuilder;

public abstract class ModelBuilder<T extends ModelBuilder<T>> {
	private final ResourceLocation parent;
	private final Set<TextureSlot> requiredTextures = new HashSet<>();
	private final HashMap<TextureSlot, ResourceLocation> textures = new HashMap<>();
	private final EnumMap<DisplayBuilder.Position, DisplayBuilder> displays = new EnumMap<>(DisplayBuilder.Position.class);
	private final List<ElementBuilder> elements = new ArrayList<>();

	@ApiStatus.Internal
	protected ModelBuilder(ResourceLocation parent) {
		this.parent = parent;
	}

	/**
	 * Adds a texture key for this model and associates a texture file with it. If the key already exists for this
	 * builder, its texture will simply be replaced. If using a {@link TextureSlot} instance, an optional parent for the
	 * key itself can be specified.
	 *
	 * @param key     An instanced {@link TextureSlot} to hold the texture for.
	 * @param texture The namespaced ID of the texture for this key.
	 */
	public T addTexture(TextureSlot key, ResourceLocation texture) {
		this.requiredTextures.add(key);
		this.textures.put(key, texture);
		return (T) this;
	}

	/**
	 * Adds a texture key for this model and associates a texture file with it. If the key already exists for this
	 * builder, its texture will simply be replaced.
	 *
	 * @param key     A key to hold the texture for.
	 * @param texture The namespaced ID of the texture for this key.
	 */
	public T addTexture(String key, ResourceLocation texture) {
		return addTexture(TextureSlot.create(key), texture);
	}

	/**
	 * Removes a texture and its key from this model builder.
	 *
	 * @param key The texture key to remove.
	 */
	public T removeTexture(TextureSlot key) {
		this.requiredTextures.remove(key);
		this.textures.remove(key);
		return (T) this;
	}

	/**
	 * Clears all current textures and texture keys for this model builder.
	 */
	public T clearTextures() {
		this.requiredTextures.clear();
		this.textures.clear();
		return (T) this;
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
	 * @param display  A {@link DisplayBuilder} to build the required display property from.
	 */
	public T addDisplay(DisplayBuilder.Position position, DisplayBuilder display) {
		this.displays.put(position, display);
		return (T) this;
	}

	/**
	 * Removes a display entry from this model builder.
	 *
	 * @param position The display position whose entry to remove.
	 */
	public T removeDisplay(DisplayBuilder.Position position) {
		this.displays.remove(position);
		return (T) this;
	}

	/**
	 * Clears all current {@link DisplayBuilder}s for this model builder.
	 */
	public T clearDisplays() {
		this.displays.clear();
		return (T) this;
	}

	/**
	 * Adds an entry to the <code>elements</code> property of the model. Element entries consist of a pair of opposite
	 * vertices of a cuboid to draw the element out as, with an optional rotation, shading and set of rendered faces.
	 *
	 * @param element An {@link ElementBuilder} to build an individual entry from.
	 */
	public T addElement(ElementBuilder element) {
		this.elements.add(element);
		return (T) this;
	}

	/**
	 * Clears all current {@link ElementBuilder}s for this model builder.
	 */
	public T clearElements() {
		this.elements.clear();
		return (T) this;
	}

	/**
	 * @return A completed {@link ModelTemplate} to generate alongside some texture map, which may or may not also be provided
	 * via this builder.
	 */
	public ModelTemplate buildModel() {
		TextureSlot[] textures = Arrays.copyOf(requiredTextures.toArray(), requiredTextures.size(), TextureSlot[].class);
		ModelTemplate model = new ModelTemplate(Optional.ofNullable(parent), Optional.empty(), textures);

		displays.forEach(model::fabric_withDisplay);
		elements.forEach(model::fabric_addElement);
		return model;
	}

	/**
	 * @return A completed {@link TextureMapping} to accompany any models built by this builder or otherwise.
	 */
	public TextureMapping mapTextures() {
		TextureMapping map = new TextureMapping();
		textures.forEach(map::put);
		return map;
	}
}
