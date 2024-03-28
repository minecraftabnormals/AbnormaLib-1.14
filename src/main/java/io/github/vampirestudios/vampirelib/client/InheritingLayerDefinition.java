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

import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MaterialDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;

/**
 * A type of layer definition that can optionally inherit a material definition
 */
public class InheritingLayerDefinition extends LayerDefinition {
    public static final MaterialDefinition DEFAULT_MATERIAL = new MaterialDefinition(64, 32);
    private MaterialDefinition internalMaterial;

    public InheritingLayerDefinition(MeshDefinition mesh, Optional<MaterialDefinition> materialOptional) {
        this(mesh, materialOptional.orElse(null));
    }

    public InheritingLayerDefinition(MeshDefinition mesh, @Nullable MaterialDefinition material) {
        super(mesh, material == null ? DEFAULT_MATERIAL : material);
        this.internalMaterial = material;
    }

    @Override
    public ModelPart bakeRoot() {
        MaterialDefinition material = this.internalMaterial == null ? DEFAULT_MATERIAL : this.internalMaterial;
        return this.mesh.getRoot().bake(material.xTexSize, material.yTexSize);
    }

    @Nullable
    public MaterialDefinition getMaterial() {
        return internalMaterial;
    }

    public void setMaterial(@Nullable MaterialDefinition internalMaterial) {
        this.internalMaterial = internalMaterial;
    }
}
