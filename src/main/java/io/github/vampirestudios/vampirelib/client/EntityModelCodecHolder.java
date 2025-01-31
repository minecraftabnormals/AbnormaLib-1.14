/*
 * Copyright (c) 2024-2023 OliviaTheVampire
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

package io.github.vampirestudios.vampirelib.client;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.joml.Vector3f;

import net.minecraft.ResourceLocationException;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDefinition;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MaterialDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.geom.builders.UVPair;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;

public class EntityModelCodecHolder {
    public static final Codec<ModelLayerLocation> MODEL_LAYER_LOCATION_CODEC = Codec.STRING.comapFlatMap(EntityModelCodecHolder::readModelLayerLocation, ModelLayerLocation::toString).stable();

    public static final Codec<MaterialDefinition> MATERIAL_DEFINITION_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("xTexSize").orElse(64).forGetter(materialDef -> materialDef.xTexSize),
            Codec.INT.fieldOf("yTexSize").orElse(32).forGetter(materialDef -> materialDef.yTexSize)
    ).apply(instance, MaterialDefinition::new));

    private static final Codec<CubeDeformation> SIMPLE_CUBE_DEFORMATION_CODEC = Codec.FLOAT.flatComapMap(CubeDeformation::new,
            cubeDef -> isSimpleCubeDeformation(cubeDef) ? DataResult.success(cubeDef.growX) : DataResult.error(() -> "Not all grow values match"));
    private static final Codec<CubeDeformation> COMPLEX_CUBE_DEFORMATION_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.optionalFieldOf("growX", 0F).forGetter(cubeDeformation -> cubeDeformation.growX),
            Codec.FLOAT.optionalFieldOf("growY", 0F).forGetter(cubeDeformation -> cubeDeformation.growY),
            Codec.FLOAT.optionalFieldOf("growZ", 0F).forGetter(cubeDeformation -> cubeDeformation.growZ)
    ).apply(instance, CubeDeformation::new));
    public static final Codec<CubeDeformation> CUBE_DEFORMATION_CODEC = Codec.either(SIMPLE_CUBE_DEFORMATION_CODEC, COMPLEX_CUBE_DEFORMATION_CODEC)
            .xmap(e -> e.left().orElseGet(e.right()::orElseThrow), cubeDef -> isSimpleCubeDeformation(cubeDef) ? Either.left(cubeDef) : Either.right(cubeDef));
    public static final Codec<UVPair> UV_PAIR_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.fieldOf("u").forGetter(UVPair::u),
            Codec.FLOAT.fieldOf("v").forGetter(UVPair::v)
    ).apply(instance, UVPair::new));
    public static final Codec<CubeDefinition> CUBE_DEFINITION_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.optionalFieldOf("comment").forGetter(cubeDef -> Optional.ofNullable(cubeDef.comment)),
            ExtraCodecs.VECTOR3F.fieldOf("origin").forGetter(cubeDef -> cubeDef.origin),
            ExtraCodecs.VECTOR3F.fieldOf("dimensions").forGetter(cubeDef -> cubeDef.dimensions),
            CUBE_DEFORMATION_CODEC.optionalFieldOf("grow", CubeDeformation.NONE).forGetter(cubeDef -> cubeDef.grow),
            Codec.BOOL.optionalFieldOf("mirror", false).forGetter(cubeDef -> cubeDef.mirror),
            UV_PAIR_CODEC.fieldOf("texCoord").forGetter(cubeDef -> cubeDef.texCoord),
            Codec.FLOAT.optionalFieldOf("texScaleX", 1F).forGetter(cubeDef -> cubeDef.texScale.u()),
            Codec.FLOAT.optionalFieldOf("texScaleY", 1F).forGetter(cubeDef -> cubeDef.texScale.v()),
            setOf(Direction.CODEC).fieldOf("visible_faces").forGetter(cubeDef -> cubeDef.visibleFaces)
    ).apply(instance, EntityModelCodecHolder::createCubeDefinition));

    public static <E> Codec<Set<E>> setOf(Codec<E> elementCodec) {
        return elementCodec.listOf().comapFlatMap(list -> {
            Set<E> set = new HashSet<>(list.size());
            List<E> duplicates = new ArrayList<>();
            for (E e : list) {
                // Add returns false when the element already exists
                if (!set.add(e)) duplicates.add(e);
            }
            if (!duplicates.isEmpty()) {
                String duplicatesError = duplicates.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(", "));
                // Partial success
                return DataResult.error(() -> "Duplicate elements: " + duplicatesError, set);
            }
            return DataResult.success(set);
        }, List::copyOf);
    }
    public static final Codec<PartPose> PART_POSE_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.optionalFieldOf("x", 0F).forGetter(partPose -> partPose.x),
            Codec.FLOAT.optionalFieldOf("y", 0F).forGetter(partPose -> partPose.y),
            Codec.FLOAT.optionalFieldOf("z", 0F).forGetter(partPose -> partPose.z),
            Codec.FLOAT.optionalFieldOf("xRot", 0F).forGetter(partPose -> partPose.xRot),
            Codec.FLOAT.optionalFieldOf("yRot", 0F).forGetter(partPose -> partPose.yRot),
            Codec.FLOAT.optionalFieldOf("zRot", 0F).forGetter(partPose -> partPose.zRot)
    ).apply(instance, PartPose::offsetAndRotation));

    public static final Codec<PartDefinition> PART_DEFINITION_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            CUBE_DEFINITION_CODEC.listOf().optionalFieldOf("cubes", List.of()).forGetter(partDef -> partDef.cubes),
            PART_POSE_CODEC.optionalFieldOf("partPose").forGetter(partDef -> Optional.of(partDef.partPose).filter(p -> !partPoseEquals(p, PartPose.ZERO))),
            Codec.unboundedMap(Codec.STRING, LazyCodec.of(EntityModelCodecHolder::getPartDefinitionCodec)).optionalFieldOf("children")
                    .forGetter(partDef -> Optional.of(partDef.children).filter(c -> !c.isEmpty()))
    ).apply(instance, EntityModelCodecHolder::createPartDefinition));
    public static final Codec<MeshDefinition> MESH_DEFINITION_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            MODEL_LAYER_LOCATION_CODEC.optionalFieldOf("parent").forGetter(meshDef ->
                    Optional.ofNullable(meshDef instanceof ParentedMeshDefinition parented && !parented.hasCalculatedInheritance()
                            ? parented.getParent()
                            : null)),
            CUBE_DEFORMATION_CODEC.optionalFieldOf("universalCubeDeformation").forGetter(meshDef ->
                    Optional.ofNullable(meshDef instanceof ParentedMeshDefinition parented && !parented.hasCalculatedInheritance()
                            ? parented.getUniversalCubeDeformation()
                            : null)),
            Codec.BOOL.optionalFieldOf("overwrite", true).forGetter(meshDef -> !(meshDef instanceof ParentedMeshDefinition parented) || parented.isOverwrite()),
            PART_DEFINITION_CODEC.optionalFieldOf("root").forGetter(meshDef -> Optional.of(meshDef.getRoot()))
    ).apply(instance, EntityModelCodecHolder::createMeshDefinition));

    public static final Codec<LayerDefinition> LAYER_DEFINITION_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            MESH_DEFINITION_CODEC.fieldOf("mesh").forGetter(layerDef -> layerDef.mesh),
            MATERIAL_DEFINITION_CODEC.optionalFieldOf("material").forGetter(layerDef -> Optional.ofNullable(layerDef instanceof InheritingLayerDefinition inheritingLayerDef
                    ? inheritingLayerDef.getMaterial()
                    : layerDef.material))
    ).apply(instance, InheritingLayerDefinition::new));

    public static DataResult<ModelLayerLocation> readModelLayerLocation(String layerLocation) {
        int idx = layerLocation.indexOf('#');
        String layer = idx == -1 ? "" : layerLocation.substring(idx + 1);
        if (idx == -1 || layer.isEmpty())
            return getLayerLocationParseError(layerLocation, "missing layer (part after hashtag)");

        String model = layerLocation.substring(0, idx);
        if (model.isEmpty())
            return getLayerLocationParseError(layerLocation, "missing model (part before hashtag)");

        try {
            return DataResult.success(new ModelLayerLocation(new ResourceLocation(model), layer));
        } catch (ResourceLocationException e) {
            return getLayerLocationParseError(layerLocation, e.getMessage());
        }
    }

    private static DataResult<ModelLayerLocation> getLayerLocationParseError(String input, String error) {
        return DataResult.error(() -> "Not a valid model layer location: " + input + ", " + error);
    }

    private static boolean isSimpleCubeDeformation(CubeDeformation cubeDef) {
        return cubeDef.growX == cubeDef.growY && cubeDef.growX == cubeDef.growZ;
    }

    static CubeDefinition createCubeDefinition(Optional<String> comment, Vector3f origin, Vector3f dimensions, CubeDeformation grow, boolean mirror, UVPair texCoord,
                                               float texScaleX, float texScaleY, Set<Direction> directionSet) {
        return new CubeDefinition(comment.orElse(null), texCoord.u(), texCoord.v(), origin.x(), origin.y(), origin.z(), dimensions.x(), dimensions.y(), dimensions.z(), grow, mirror,
                texScaleX, texScaleY, directionSet);
    }

    private static PartDefinition createPartDefinition(List<CubeDefinition> cubes, Optional<PartPose> partPose, Optional<Map<String, PartDefinition>> children) {
        PartDefinition partDefinition = new SafePartDefinition(cubes, partPose.orElse(PartPose.ZERO));
        children.filter(c -> !c.isEmpty()).ifPresent(c -> {
            partDefinition.children.clear();
            partDefinition.children.putAll(c);
        });
        return partDefinition;
    }

    private static MeshDefinition createMeshDefinition(Optional<ModelLayerLocation> parentOptional, Optional<CubeDeformation> universalCubeDeformation, boolean overwrite,
            Optional<PartDefinition> rootOpt) {
        rootOpt.ifPresent(root -> {
            // Ensure no cubes or a part pose can be defined for the root node.
            root.cubes = List.of();
            root.partPose = PartPose.ZERO;
        });
        return new ParentedMeshDefinition(parentOptional.orElse(null), universalCubeDeformation.orElse(null), rootOpt.orElse(null), overwrite);
    }

    private static boolean partPoseEquals(PartPose a, PartPose b) {
        if (a == null)
            return b == null;
        if (b == null) // a is not null but b is null, so false
            return false;
        return a.x == b.x && a.y == b.y && a.z == b.z && a.xRot == b.xRot && a.yRot == b.yRot && a.zRot == b.zRot;
    }

    private static Codec<PartDefinition> getPartDefinitionCodec() {
        return PART_DEFINITION_CODEC;
    }
}
