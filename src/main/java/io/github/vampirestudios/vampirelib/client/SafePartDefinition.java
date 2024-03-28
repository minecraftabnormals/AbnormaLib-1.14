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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

/**
 * A safe variant of {@link PartDefinition} that returns a {@link SafeModelPart safe model part} when baking.
 */
public class SafePartDefinition extends PartDefinition {
    public SafePartDefinition(List<CubeDefinition> cubes, PartPose partPose) {
        super(cubes, partPose);
    }

    @Override
    public SafeModelPart bake(int texWidth, int texHeight) {
        Object2ObjectArrayMap<String, ModelPart> map = this.children.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().bake(texWidth, texHeight), (firstPart, secondPart) -> firstPart, Object2ObjectArrayMap::new));
        List<ModelPart.Cube> list = this.cubes.stream().map(cube -> cube.bake(texWidth, texHeight)).toList();

        SafeModelPart modelPart = new SafeModelPart(list, map);
        modelPart.loadPose(this.partPose);
        return modelPart;
    }
}
