/*
 * Copyright (c) 2023 OliviaTheVampire
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

package io.github.vampirestudios.vampirelib.utils;

import java.util.Locale;
import java.util.Map;

import net.minecraft.resources.ResourceLocation;

public class Utils {

	public static String toTitleCase(String lowerCase) {
		return "" + Character.toUpperCase(lowerCase.charAt(0)) + lowerCase.substring(1);
	}

	public static String nameToId(String name, Map<String, String> specialCharMap) {
		// strip name of special chars
		for (Map.Entry<String, String> specialChar : specialCharMap.entrySet()) {
			name = name.replace(specialChar.getKey(), specialChar.getValue());
		}
		return name.toLowerCase(Locale.ENGLISH);
	}

	public static ResourceLocation appendToPath(ResourceLocation identifier, String suffix) {
		return new ResourceLocation(identifier.getNamespace(), identifier.getPath() + suffix);
	}

	public static ResourceLocation prependToPath(ResourceLocation identifier, String prefix) {
		return new ResourceLocation(identifier.getNamespace(), prefix + identifier.getPath());
	}

	public static ResourceLocation appendAndPrependToPath(ResourceLocation identifier, String prefix, String suffix) {
		return new ResourceLocation(identifier.getNamespace(), prefix + identifier.getPath() + suffix);
	}

	public static double dist(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
	}

	public static boolean checkBitFlag(int toCheck, int flag) {
		return (toCheck & flag) == flag;
	}

}
