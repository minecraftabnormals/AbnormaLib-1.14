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

import java.util.concurrent.atomic.AtomicReference;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.ServerLevelAccessor;

import io.github.vampirestudios.vampirelib.api.callbacks.EntitySpawnCallback;

public final class EntitySpawnImpl {
	private EntitySpawnImpl() {
	}

	/**
	 * @author Valoeghese
	 */
	public static boolean spawnEntityZ(ServerLevel self, Entity entity) {
		AtomicReference<Entity> currentEntity = new AtomicReference<>(entity);
		InteractionResult result = EntitySpawnCallback.PRE.invoker().onEntitySpawnPre(entity, currentEntity, self,
				MobSpawnType.COMMAND);
		entity = currentEntity.get();

		if (result != InteractionResult.FAIL) {
			if (self.tryAddFreshEntityWithPassengers(entity)) {
				EntitySpawnCallback.POST.invoker()
						.onEntitySpawnPost(entity, self, entity.position(), MobSpawnType.COMMAND);
				return true;
			}
		}

		return false;
	}

	/**
	 * @author Valoeghese
	 */
	public static void spawnEntityV(ServerLevelAccessor self, Entity entity) {
		AtomicReference<Entity> currentEntity = new AtomicReference<>(entity);
		InteractionResult result = EntitySpawnCallback.PRE.invoker().onEntitySpawnPre(entity, currentEntity, self,
				MobSpawnType.NATURAL);
		entity = currentEntity.get();

		if (result != InteractionResult.FAIL) {
			self.addFreshEntityWithPassengers(entity);
			EntitySpawnCallback.POST.invoker().onEntitySpawnPost(entity, self, entity.position(), MobSpawnType.NATURAL);
		}
	}

	/**
	 * @author Valoeghese
	 */
	public static Entity spawnEntityE(Entity entity, ServerLevel serverWorld, MobSpawnType spawnReason) {
		if (entity != null) {
			AtomicReference<Entity> currentEntity = new AtomicReference<>(entity);
			InteractionResult result = EntitySpawnCallback.PRE.invoker()
					.onEntitySpawnPre(entity, currentEntity, serverWorld,
							spawnReason);
			entity = result != InteractionResult.FAIL ? currentEntity.get() : null;
		}

		if (entity != null) {
			serverWorld.addFreshEntityWithPassengers(entity);
			EntitySpawnCallback.POST.invoker().onEntitySpawnPost(entity, serverWorld, entity.position(), spawnReason);
		}

		return entity;
	}

	/**
	 * @author Valoeghese
	 */
	public static InteractionResult eventPre(Entity original, AtomicReference<Entity> entity, ServerLevelAccessor world, MobSpawnType reason, EntitySpawnCallback.Pre[] listeners) {
		for (EntitySpawnCallback.Pre callback : listeners) {
			InteractionResult result = callback.onEntitySpawnPre(original, entity, world, reason);

			if (result != InteractionResult.PASS) {
				return result;
			}
		}

		return InteractionResult.PASS;
	}
}
