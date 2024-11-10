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

package io.github.vampirestudios.vampirelib.mixins.entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import io.github.vampirestudios.vampirelib.village.VillagerTypeRegistry;

@Mixin(Villager.class)
public abstract class VillagerEntityMixin extends AbstractVillager {
	protected VillagerEntityMixin(EntityType<? extends AbstractVillager> entityType, Level level) {
		super(entityType, level);
	}

	@Shadow
	public abstract VillagerData getVillagerData();

	@Shadow
	public abstract void setVillagerData(VillagerData villagerData);

	@Inject(method = "finalizeSpawn", at = @At(value = "INVOKE",
											   target = "Lnet/minecraft/world/entity/npc/Villager;setVillagerData(Lnet/minecraft/world/entity/npc/VillagerData;)V",
											   ordinal = 1, shift = At.Shift.AFTER))
	public void onInitialize(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, EntitySpawnReason entitySpawnReason, SpawnGroupData spawnGroupData, CallbackInfoReturnable<SpawnGroupData> cir) {
		if (serverLevelAccessor.getBiome(this.blockPosition()).unwrapKey().isPresent()) {
			setVillagerData(getVillagerData().setType(
					VillagerTypeRegistry.getVillagerTypeForBiome(serverLevelAccessor.getBiome(this.blockPosition()))));
		} else {
			setVillagerData(getVillagerData().setType(VillagerType.PLAINS));
		}
	}

}
