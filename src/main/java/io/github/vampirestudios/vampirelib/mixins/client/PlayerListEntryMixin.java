/*
 * MIT License
 *
 * Copyright (c) 2020 Vampire Studios
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.vampirestudios.vampirelib.mixins.client;

import com.mojang.authlib.GameProfile;
import io.github.vampirestudios.vampirelib.VampireLib;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerListEntry.class)
public abstract class PlayerListEntryMixin {

    @Shadow @Final
    private GameProfile profile;

    @Inject(method = "getCapeTexture", at = @At("RETURN"), cancellable = true)
    public void getCapeTexture(CallbackInfoReturnable<Identifier> callbackInfoReturnable) {
        if (VampireLib.DEV_UUID.containsKey(profile.getId().toString())) {
            callbackInfoReturnable.setReturnValue(VampireLib.DEV_UUID.get(profile.getId().toString()));
        }
    }

    @Inject(method = "getElytraTexture", at = @At("RETURN"), cancellable = true)
    public void getElytraTexture(CallbackInfoReturnable<Identifier> callbackInfoReturnable) {
        if (VampireLib.DEV_UUID.containsKey(profile.getId().toString())) {
            callbackInfoReturnable.setReturnValue(VampireLib.DEV_UUID.get(profile.getId().toString()));
        }
    }

}
