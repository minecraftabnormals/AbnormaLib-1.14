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

package io.github.vampirestudios.vampirelib.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.world.flag.FeatureFlagRegistry;

@Mixin(value = FeatureFlagRegistry.Builder.class, priority = 1001)
public class FeatureFlagBuilderMixin {

	@ModifyExpressionValue(
		method = "create",
		at = @At(
			value = "CONSTANT",
			args = {
				"intValue=64"
			}
		)
	)
	private int increaseMax(int constant) {
		return Math.max(constant, 1024);
	}
}
