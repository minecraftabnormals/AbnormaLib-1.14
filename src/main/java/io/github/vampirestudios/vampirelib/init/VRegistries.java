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

package io.github.vampirestudios.vampirelib.init;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;

import io.github.vampirestudios.vampirelib.modules.FeatureManager;

public class VRegistries {
	public static final Registry<FeatureManager> FEATURE_MANAGERS = FabricRegistryBuilder.createSimple(
			FeatureManager.class, ResourceLocation.fromNamespaceAndPath("vampirelib", "feature_managers")
	).buildAndRegister();
}
