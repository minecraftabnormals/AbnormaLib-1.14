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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.model.geom.ModelPart;

/**
 * A safe variant of {@link ModelPart}.
 * If a child is requested from this model part that does not exist, it will return an empty model part, which is also safe like this one.
 * The same empty model part instance is guaranteed to be returned for each call to {@link #getChild(String)} with the same name.
 */
public class SafeModelPart extends ModelPart {
    protected final Map<String, Empty> cachedEmpties = new HashMap<>();

    public SafeModelPart(List<Cube> cubes, Map<String, ModelPart> children) {
        super(cubes, children);
    }

    @Override
    public ModelPart getChild(String name) {
        ModelPart childPart = this.children.get(name);
        return childPart == null ? getEmptyChild(name) : childPart;
    }

    protected ModelPart getEmptyChild(String name) {
        return cachedEmpties.computeIfAbsent(name, n -> new Empty());
    }

    private static class Empty extends SafeModelPart {
        private Empty() {
            super(List.of(), Map.of());
        }

        @Override
        public ModelPart getChild(String name) {
            return getEmptyChild(name);
        }
    }
}
