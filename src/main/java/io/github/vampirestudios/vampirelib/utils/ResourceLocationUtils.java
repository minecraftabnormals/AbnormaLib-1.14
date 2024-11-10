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

package io.github.vampirestudios.vampirelib.utils;

import java.util.Locale;

import net.minecraft.resources.ResourceLocation;

import io.github.vampirestudios.vampirelib.api.BasicModClass;

public class ResourceLocationUtils {

	private static String modId;

	public static void setModInstance(BasicModClass instanceIn) {
		modId = instanceIn.modId();
	}

	public static void setModId(String modIdIn) {
		modId = modIdIn;
	}

	public enum IdType {
		BLOCK,
		ITEM,
		ENTITY,
		MOB,
		WOOD_SETS,
		GUI,
		MISC,
		MOB_EFFECTS,
		PAINTING,
		PARTICLE,
		SOUNDS,
		MODELS_BLOCK,
		MODELS_ITEM,
		PARTICLE_JSON,
		BLOCKSTATES,
		LANG,
		MUSIC,
		WOOD_TYPES
	}

	public static ResourceLocation vanillaSpecialId(IdType idType, String path) {
		return vanillaId(idType.name().toLowerCase(Locale.ROOT) + path);
	}

	public static ResourceLocation modSpecialId(IdType idType, String path) {
		return modId(idType.name().toLowerCase(Locale.ROOT) + "/" + path);
	}

	public static ResourceLocation vanillaPrefixId(String prefix, String path) {
		return vanillaId(prefix + path);
	}

	public static ResourceLocation modPrefixId(String prefix, String path) {
		return modId(prefix + path);
	}

	public static ResourceLocation vanillaSuffixId(String path, String suffix) {
		return vanillaId(path + suffix);
	}

	public static ResourceLocation modSuffixId(String path, String suffix) {
		return modId(path + suffix);
	}

	public static ResourceLocation vanillaPrefixSuffixId(String prefix, String path, String suffix) {
		return vanillaId(prefix + path + suffix);
	}

	public static ResourceLocation modPrefixSuffixId(String prefix, String path, String suffix) {
		return modId(prefix + path + suffix);
	}

	public static ResourceLocation modId(String path) {
		return id(modId, path);
	}

	public static ResourceLocation vanillaId(String path) {
		return ResourceLocation.withDefaultNamespace(path);
	}

	public static ResourceLocation id(String namespace, String path) {
		return ResourceLocation.fromNamespaceAndPath(namespace, path);
	}

}
