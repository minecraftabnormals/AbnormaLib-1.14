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

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.joml.Vector3f;

import net.minecraft.Util;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDefinition;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MaterialDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.geom.builders.UVPair;
import net.minecraft.core.Direction;
import net.minecraft.util.ExtraCodecs;

/**
 * Codecs for model loading.
 */
public final class ModelCodecs {
	private static final Set<Direction> ALL_DIRECTIONS = EnumSet.allOf(Direction.class);

	public static final Codec<MaterialDefinition> MATERIAL_DEFINITION = RecordCodecBuilder.create(instance ->
			instance.group(
					Codec.INT.fieldOf("width").forGetter(materialDefinition -> materialDefinition.xTexSize),
					Codec.INT.fieldOf("height").forGetter(materialDefinition -> materialDefinition.yTexSize)
			).apply(instance, MaterialDefinition::new)
	);

	public static final Codec<PartPose> PART_POSE = RecordCodecBuilder.create(instance ->
			instance.group(
					ExtraCodecs.VECTOR3F.optionalFieldOf("origin", new Vector3f()).forGetter(obj -> new Vector3f(obj.x(), obj.y(), obj.z())),
					ExtraCodecs.VECTOR3F.optionalFieldOf("rotation", new Vector3f()).forGetter(obj -> new Vector3f(obj.xRot(), obj.yRot(), obj.zRot()))
			).apply(instance, (origin, rot) -> PartPose.offsetAndRotation(origin.x(), origin.y(), origin.z(), rot.x(), rot.y(), rot.z()))
	);

	public static final Codec<CubeDeformation> CUBE_DEFORMATION = ExtraCodecs.VECTOR3F.xmap(
			vec -> new CubeDeformation(vec.x(), vec.y(), vec.z()),
			dil -> new Vector3f(dil.growX, dil.growY, dil.growZ)
	);

	private static CubeDefinition createCubeDefinition(Optional<String> name, Vector3f offset, Vector3f dimensions, CubeDeformation cubeDeformation, boolean mirror, UVPair uv, UVPair uvSize, Optional<List<Direction>> faces) {
		return new CubeDefinition(
				name.orElse(null),
				uv.u(), uv.v(),
				offset.x(), offset.y(), offset.z(),
				dimensions.x(), dimensions.y(), dimensions.z(),
				cubeDeformation,
				mirror,
				uvSize.u(), uvSize.v(),
				faces.map(Set::copyOf).orElse(ALL_DIRECTIONS)
		);
	}

	// If the set has all faces, return empty
	private static Optional<List<Direction>> optionalFaceList(Set<Direction> faces) {
		for (Direction direction : Direction.values()) {
			if (!faces.contains(direction)) {
				return Optional.of(List.copyOf(faces));
			}
		}

		return Optional.empty();
	}

	private static final UVPair DEFAULT_UV_SCALE = new UVPair(1.0f, 1.0f);

	public static final Codec<UVPair> UV_PAIR = Codec.FLOAT.listOf().comapFlatMap(list -> Util.fixedSize(list, 2).map(listx ->
		new UVPair(listx.get(0), listx.get(1))
	), uvPair -> List.of(uvPair.u(), uvPair.v()));

	public static final Codec<CubeDefinition> CUBE_DEFINITION = RecordCodecBuilder.create(instance ->
			instance.group(
					Codec.STRING.optionalFieldOf("name").forGetter(obj -> Optional.ofNullable(obj.comment)),
					ExtraCodecs.VECTOR3F.fieldOf("offset").forGetter(obj -> obj.origin),
					ExtraCodecs.VECTOR3F.fieldOf("dimensions").forGetter(obj -> obj.dimensions),
					CUBE_DEFORMATION.optionalFieldOf("dilation", CubeDeformation.NONE).forGetter(obj -> obj.grow),
					Codec.BOOL.optionalFieldOf("mirror", false).forGetter(obj -> obj.mirror),
					UV_PAIR.fieldOf("uv").forGetter(obj -> obj.texCoord),
					UV_PAIR.optionalFieldOf("uv_scale", DEFAULT_UV_SCALE).forGetter(obj -> obj.texScale),
					Codec.list(Direction.CODEC).optionalFieldOf("faces").forGetter(obj -> optionalFaceList(obj.visibleFaces))
			).apply(instance, ModelCodecs::createCubeDefinition)
	);

	public static final Codec<PartDefinition> PART_DEFINITION = RecordCodecBuilder.create(instance ->
			instance.group(
					PART_POSE.optionalFieldOf("transform", PartPose.ZERO).forGetter(obj -> obj.partPose),
					Codec.list(CUBE_DEFINITION).fieldOf("cuboids").forGetter(obj -> obj.cubes),
					LazyTypeUnboundedMapCodec.of(Codec.STRING, () -> ModelCodecs.PART_DEFINITION).optionalFieldOf("children", new HashMap<>()).forGetter(obj -> obj.children)
			).apply(instance, (transform, cuboids, children) -> {
				var data = new PartDefinition(cuboids, transform);
				data.children.putAll(children);
				return data;
			})
	);

	public static final Codec<LayerDefinition> LAYER_DEFINITION = RecordCodecBuilder.create(instance ->
			instance.group(
					MATERIAL_DEFINITION.fieldOf("texture").forGetter(obj -> obj.material),
					Codec.unboundedMap(Codec.STRING, PART_DEFINITION).fieldOf("bones").forGetter(obj -> obj.mesh.getRoot().children)
			).apply(instance, (texture, bones) -> {
				var data = new MeshDefinition();
				data.getRoot().children.putAll(bones);
				return new LayerDefinition(data, texture);
			})
	);
}
