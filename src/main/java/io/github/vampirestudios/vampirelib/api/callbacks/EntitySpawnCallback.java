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

package io.github.vampirestudios.vampirelib.api.callbacks;

import java.util.concurrent.atomic.AtomicReference;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import io.github.vampirestudios.vampirelib.utils.EntitySpawnImpl;

/**
 * Collection of events for entity spawning.
 */
public interface EntitySpawnCallback {
	/**
	 * Callback for before the entity spawns. Use this to cancel, force succeed, or alter the entity spawning.
	 */
	Event<EntitySpawnCallback.Pre> PRE = EventFactory.createArrayBacked(EntitySpawnCallback.Pre.class, listeners ->
			(original, entity, world, reason) -> EntitySpawnImpl.eventPre(original, entity, world, reason, listeners));

	/**
	 * Callback for after the entity succeeds in spawning. Use this for general functions after an entity has spawned.
	 */
	Event<EntitySpawnCallback.Post> POST = EventFactory.createArrayBacked(EntitySpawnCallback.Post.class, listeners ->
			(entity, level, pos, reason) -> {
				for (EntitySpawnCallback.Post callback : listeners) {
					callback.onEntitySpawnPost(entity, level, pos, reason);
				}
			});

	/**
	 * Callback for before the entity spawns. Use this to cancel, force succeed, or alter the entity spawning.
	 *
	 * @author Valoeghese
	 */
	@FunctionalInterface
	interface Pre {
		/**
		 * @param original the entity that was originally going to spawn.
		 * @param entity   the entity that is going to spawn. If this is different to {@code original}, then a mod has modified the entity to spawn.
		 * @param world    the world in which the entity is to spawn.
		 * @param reason   the cause of the spawning.
		 *
		 * @return <ul>
		 * <li>{@code SUCCESS} or {@code CONSUME} to instantly succeed in spawning the entity in the world at its specified position.<br/>
		 * <li>{@code PASS} to leave SUCCESS/FAIL handling to subsequent events. If all events PASS, the action is determined to be a SUCCESS.<br/>
		 * <li>{@code FAIL} cancel spawning the entity.
		 * </ul>
		 */
		InteractionResult onEntitySpawnPre(Entity original, AtomicReference<Entity> entity, ServerLevelAccessor world, EntitySpawnReason reason);
	}

	/**
	 * Callback for after the entity succeeds in spawning. Use this for general functions after an entity has spawned.
	 */
	@FunctionalInterface
	interface Post {
		/**
		 * @param entity the entity that has spawned.
		 * @param world  the world in which the entity spawned.
		 * @param pos    the position at which the entity spawned.
		 * @param reason the cause for the entity spawn.
		 */
		void onEntitySpawnPost(Entity entity, ServerLevelAccessor world, Vec3 pos, EntitySpawnReason reason);
	}
}
