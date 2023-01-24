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

package io.github.vampirestudios.vampirelib.modules.api;

import net.minecraft.resources.ResourceLocation;

public abstract class Feature {

	private final ResourceLocation registryName;
	private final String name;
	private boolean enabled = true;

	public Feature(ResourceLocation registryName, String name) {
		this.registryName = registryName;
		this.name = name;
	}

	public final boolean isEnabled() {
		return enabled;
	}

	public final void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public ResourceLocation getRegistryName() {
		return registryName;
	}

	public String getName() {
		return name;
	}

}
