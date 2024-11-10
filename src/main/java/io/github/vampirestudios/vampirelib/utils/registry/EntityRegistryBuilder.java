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

package io.github.vampirestudios.vampirelib.utils.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;

public class EntityRegistryBuilder<E extends Entity> {

	private static ResourceLocation name;

	private EntityType.EntityFactory<E> entityFactory;

	private MobCategory category;

	private int trackingDistance;
	private int updateIntervalTicks;
	private boolean alwaysUpdateVelocity;

	private int primaryColor;
	private int secondaryColor;
	private boolean hasEgg;
	private boolean fireImmune;

	private EntityDimensions dimensions;

	public static <E extends Entity> EntityRegistryBuilder<E> createBuilder(ResourceLocation nameIn) {
		name = nameIn;
		return new EntityRegistryBuilder<>();
	}

	public EntityRegistryBuilder<E> entity(EntityType.EntityFactory<E> entityFactory) {
		this.entityFactory = entityFactory;
		return this;
	}

	@Deprecated
	public EntityRegistryBuilder<E> category(MobCategory category) {
		this.category = category;
		return this;
	}

	public EntityRegistryBuilder<E> group(MobCategory category) {
		this.category = category;
		return this;
	}

	public EntityRegistryBuilder<E> trackingDistance(int trackingDistance) {
		this.trackingDistance = trackingDistance;
		return this;
	}

	public EntityRegistryBuilder<E> updateIntervalTicks(int updateIntervalTicks) {
		this.updateIntervalTicks = updateIntervalTicks;
		return this;
	}

	public EntityRegistryBuilder<E> alwaysUpdateVelocity(boolean alwaysUpdateVelocity) {
		this.alwaysUpdateVelocity = alwaysUpdateVelocity;
		return this;
	}

	@Deprecated
	public EntityRegistryBuilder<E> tracker(int trackingDistance, int updateIntervalTicks, boolean alwaysUpdateVelocity) {
		this.trackingDistance = trackingDistance;
		this.updateIntervalTicks = updateIntervalTicks;
		this.alwaysUpdateVelocity = alwaysUpdateVelocity;
		return this;
	}

	public EntityRegistryBuilder<E> egg(int primaryColor, int secondaryColor) {
		this.primaryColor = primaryColor;
		this.secondaryColor = secondaryColor;
		return this;
	}

	public EntityRegistryBuilder<E> hasEgg(boolean hasEgg) {
		this.hasEgg = hasEgg;
		return this;
	}

	public EntityRegistryBuilder<E> makeFireImmune() {
		this.fireImmune = true;
		return this;
	}

	public EntityRegistryBuilder<E> dimensions(EntityDimensions size) {
		this.dimensions = size;
		return this;
	}

	public EntityType<E> build() {
		FabricEntityTypeBuilder<E> entityBuilder = FabricEntityTypeBuilder.create(this.category, this.entityFactory)
				.dimensions(this.dimensions);
		if (fireImmune) {
			entityBuilder.fireImmune();
		}
		if (this.trackingDistance != 0) {
			entityBuilder.trackRangeBlocks(this.trackingDistance);
		}
		if (this.updateIntervalTicks != 0) {
			entityBuilder.trackedUpdateRate(this.updateIntervalTicks);
		}
		if (this.updateIntervalTicks != 0) {
			entityBuilder.forceTrackedVelocityUpdates(this.alwaysUpdateVelocity);
		}

		EntityType<E> entityType = Registry.register(BuiltInRegistries.ENTITY_TYPE, name, entityBuilder.build(ResourceKey.create(Registries.ENTITY_TYPE, name)));

		if (hasEgg) {
			RegistryHelper.createRegistryHelper(name.getNamespace()).items()
					.registerSpawnEgg(name.getPath(), (EntityType<? extends Mob>) entityType, primaryColor,
							secondaryColor);
		}

		return entityType;
	}

}
