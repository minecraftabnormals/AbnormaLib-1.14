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

package io.github.vampirestudios.vampirelib.mixins.item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.WritableBookItem;
import net.minecraft.world.item.WrittenBookItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.state.BlockState;

@Mixin({WritableBookItem.class, WrittenBookItem.class})
public abstract class MixinBookOnLectern {

	@Inject(method = "useOn", at = @At(value = "HEAD"), cancellable = true)
	public void useOnBlock(UseOnContext usageContext, CallbackInfoReturnable<InteractionResult> cir) {
		Level world = usageContext.getLevel();
		BlockPos blockPos = usageContext.getClickedPos();
		BlockState blockState = world.getBlockState(blockPos);
		if (blockState.getBlock() instanceof LecternBlock) {
			cir.setReturnValue(LecternBlock.tryPlaceBook(usageContext.getPlayer(), world, blockPos, blockState,
					usageContext.getItemInHand()) ? InteractionResult.SUCCESS : InteractionResult.PASS);
		} else {
			cir.setReturnValue(InteractionResult.PASS);
		}
	}
}
