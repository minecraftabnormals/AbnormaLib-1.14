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

import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;
import org.joml.Vector4i;

import net.minecraft.core.Direction;
import net.minecraft.client.data.models.blockstates.VariantProperties;
import net.minecraft.client.data.models.model.TextureSlot;

/**
 * Instantiate this class in order to provide a set of <code>faces</code> to be rendered for an element of a JSON model.
 */
public class FaceBuilder {
	private final TextureSlot texture;
	private Vector4i uv = new Vector4i(0);
	@Nullable
	private Direction cullFace = null;
	private VariantProperties.Rotation rotation = VariantProperties.Rotation.R0;
	private int tintIndex = -1;

	/**
	 * Create a new face builder for the given texture key.
	 *
	 * @param texture A key corresponding to the texture to be applied for this element face.
	 */
	public FaceBuilder(TextureSlot texture) {
		this.texture = texture;
	}

	public FaceBuilder(String textureKey) {
		this.texture = TextureSlot.create(textureKey);
	}

	/**
	 * Specifies the area of the texture to use for the face being built. Defaults to the zero vector, in which case
	 * the UV is automatically determined based on the position of the element.
	 *
	 * @param uv The UV coordinates to use for this face texture, given as a {@link Vector4i}.
	 */
	public FaceBuilder withUv(Vector4i uv) {
		validateUv(uv);
		this.uv = uv;
		return this;
	}

	/**
	 * Specifies the area of the texture to use for the face being built, given in terms of a pair of 2D vectors.
	 * Defaults to the zero vector, in which case the UV is automatically determined based on the position of the element.
	 */
	public FaceBuilder withUv(Vector2i u, Vector2i v) {
		return withUv(new Vector4i(u, v.x(), v.y()));
	}

	/**
	 * Specifies the area of the texture to use for the face being built, given in terms of individual vector components.
	 * Defaults to the zero vector, in which case the UV is automatically determined based on the position of the element.
	 */
	public FaceBuilder withUv(int x1, int y1, int x2, int y2) {
		return withUv(new Vector4i(x1, y1, x2, y2));
	}

	/**
	 * Specifies whether this face need not be rendered when there is a block adjacent to it in the specified position.
	 *
	 * @param cullFace The position from which an adjacent block should cull this face, or <code>null</code> to never hide (default).
	 */
	public FaceBuilder withCulling(@Nullable Direction cullFace) {
		this.cullFace = cullFace;
		return this;
	}

	/**
	 * Specifies a texture rotation for the face being built. Only accepts quarter-turns.
	 */
	public FaceBuilder withRotation(VariantProperties.Rotation rotation) {
		this.rotation = rotation;
		return this;
	}

	/**
	 * Specified a tint index for this face. Tint indexes are hardcoded into a respective item/block and correspond to
	 * some colour to be applied to a given face texture as defined in its class. Defaults to -1 (no tint index).
	 */
	public FaceBuilder withTintIndex(int tintIndex) {
		this.tintIndex = tintIndex;
		return this;
	}

	private void validateUv(Vector4i uv) {
		int[] components = {uv.x, uv.y, uv.z, uv.w};

		for (int c : components) {
			Preconditions.checkArgument(c >= 0 && c <= 16, "Component out of range");
		}
	}

	@ApiStatus.Internal
	public JsonObject build() {
		var face = new JsonObject();

		var uv = new JsonArray();
		uv.add(this.uv.x());
		uv.add(this.uv.y());
		uv.add(this.uv.z());
		uv.add(this.uv.w());
		face.add("uv", uv);

		face.addProperty("texture", "#" + texture.getId());

		if (cullFace != null) {
			face.addProperty("cullface", cullFace.name().toLowerCase());
		}

		if (rotation != VariantProperties.Rotation.R0) {
			face.addProperty("rotation", rotation.name().substring(1));
		}

		if (this.tintIndex != -1) {
			face.addProperty("tintindex", tintIndex);
		}

		return face;
	}
}
