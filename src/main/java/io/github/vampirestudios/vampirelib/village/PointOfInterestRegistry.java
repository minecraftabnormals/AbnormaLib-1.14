/*
 * Copyright (c) 2022-2023 OliviaTheVampire
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

package io.github.vampirestudios.vampirelib.village;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;

public interface PointOfInterestRegistry {

	static PoiType register(ResourceLocation id, PoiType pointOfInterestTypeCustom) {
		if (!BuiltInRegistries.POINT_OF_INTEREST_TYPE.containsKey(id) &&
				!pointOfInterestTypeCustom.matchingStates().isEmpty())
			return Registry.register(BuiltInRegistries.POINT_OF_INTEREST_TYPE, id, pointOfInterestTypeCustom);
		else return BuiltInRegistries.POINT_OF_INTEREST_TYPE.get(id);
	}

}
