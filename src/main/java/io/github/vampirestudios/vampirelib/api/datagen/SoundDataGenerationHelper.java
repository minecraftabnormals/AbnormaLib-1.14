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

package io.github.vampirestudios.vampirelib.api.datagen;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.sounds.SoundEvent;

import io.github.vampirestudios.vampirelib.api.datagen.FabricSoundProvider.SoundGenerator;
import io.github.vampirestudios.vampirelib.utils.ResourceLocationUtils;

public class SoundDataGenerationHelper {

	public static void createMultipleSoundsBlockSoundDefinitionWithSubtitle(SoundGenerator registry, SoundEvent soundEvent, String name, String subtitle, String type, int amount) {
		createMultipleSoundsSoundDefinitionWithSubtitle(registry, ResourceLocationUtils.IdType.BLOCK, soundEvent, name, subtitle, type, amount);
	}

	public static void createMultipleSoundsBlockSoundDefinition(SoundGenerator registry, SoundEvent soundEvent, String name, String type, int amount) {
		createMultipleSoundsSoundDefinition(registry, ResourceLocationUtils.IdType.BLOCK, soundEvent, name, type, amount);
	}

	public static void createSimpleBlockSoundDefinitionWithSubtitle(SoundGenerator registry, SoundEvent soundEvent, String name, String subtitle, String type) {
		createSimpleSoundDefinitionWithSubtitle(registry, ResourceLocationUtils.IdType.BLOCK, soundEvent, name, subtitle, type);
	}

	public static void createSimpleBlockSoundDefinition(SoundGenerator registry, SoundEvent soundEvent, String name, String type) {
		createSimpleSoundDefinition(registry, ResourceLocationUtils.IdType.BLOCK, soundEvent, name, type);
	}

	public static void createMultipleSoundsItemSoundDefinitionWithSubtitle(SoundGenerator registry, SoundEvent soundEvent, String name, String subtitle, String type, int amount) {
		createMultipleSoundsSoundDefinitionWithSubtitle(registry, ResourceLocationUtils.IdType.ITEM, soundEvent, name, subtitle, type, amount);
	}

	public static void createMultipleSoundsItemSoundDefinition(SoundGenerator registry, SoundEvent soundEvent, String name, String type, int amount) {
		createMultipleSoundsSoundDefinition(registry, ResourceLocationUtils.IdType.ITEM, soundEvent, name, type, amount);
	}

	public static void createSimpleItemSoundDefinitionWithSubtitle(SoundGenerator registry, SoundEvent soundEvent, String name, String subtitle, String type) {
		createSimpleSoundDefinitionWithSubtitle(registry, ResourceLocationUtils.IdType.ITEM, soundEvent, name, subtitle, type);
	}

	public static void createSimpleItemSoundDefinition(SoundGenerator registry, SoundEvent soundEvent, String name, String type) {
		createSimpleSoundDefinition(registry, ResourceLocationUtils.IdType.ITEM, soundEvent, name, type);
	}

	public static void createMultipleSoundsParticleSoundDefinitionWithSubtitle(SoundGenerator registry, SoundEvent soundEvent, String name, String subtitle, String type, int amount) {
		createMultipleSoundsSoundDefinitionWithSubtitle(registry, ResourceLocationUtils.IdType.PARTICLE, soundEvent, name, subtitle, type, amount);
	}

	public static void createMultipleSoundsParticleSoundDefinition(SoundGenerator registry, SoundEvent soundEvent, String name, String type, int amount) {
		createMultipleSoundsSoundDefinition(registry, ResourceLocationUtils.IdType.PARTICLE, soundEvent, name, type, amount);
	}

	public static void createSimpleParticleSoundDefinitionWithSubtitle(SoundGenerator registry, SoundEvent soundEvent, String name, String subtitle, String type) {
		createSimpleSoundDefinitionWithSubtitle(registry, ResourceLocationUtils.IdType.PARTICLE, soundEvent, name, subtitle, type);
	}

	public static void createSimpleParticleSoundDefinition(SoundGenerator registry, SoundEvent soundEvent, String name, String type) {
		createSimpleSoundDefinition(registry, ResourceLocationUtils.IdType.PARTICLE, soundEvent, name, type);
	}

	public static void createMultipleSoundsEntitySoundDefinitionWithSubtitle(SoundGenerator registry, SoundEvent soundEvent, String name, String subtitle, String type, int amount) {
		createMultipleSoundsSoundDefinitionWithSubtitle(registry, ResourceLocationUtils.IdType.MOB, soundEvent, name, subtitle, type, amount);
	}

	public static void createMultipleSoundsEntitySoundDefinition(SoundGenerator registry, SoundEvent soundEvent, String name, String type, int amount) {
		createMultipleSoundsSoundDefinition(registry, ResourceLocationUtils.IdType.MOB, soundEvent, name, type, amount);
	}

	public static void createSimpleEntitySoundDefinitionWithSubtitle(SoundGenerator registry, SoundEvent soundEvent, String name, String subtitle, String type) {
		createSimpleSoundDefinitionWithSubtitle(registry, ResourceLocationUtils.IdType.MOB, soundEvent, name, subtitle, type);
	}

	public static void createSimpleEntitySoundDefinition(SoundGenerator registry, SoundEvent soundEvent, String name, String type) {
		createSimpleSoundDefinition(registry, ResourceLocationUtils.IdType.MOB, soundEvent, name, type);
	}

	public static void createMusicDiscSoundDefinition(SoundGenerator registry, SoundEvent soundEvent, String name) {
		registry.add(soundEvent, SoundBuilder.sound(ResourceLocationUtils.modId("records/" + name)));
	}

	public static void createMultipleSoundsSoundDefinitionWithSubtitle(SoundGenerator registry, ResourceLocationUtils.IdType idType, SoundEvent soundEvent, String name, String subtitle, String type, int amount) {
		List<SoundBuilder> soundBuilders = new ArrayList<>();
		for (int i = 1; i < amount; i++) {
			soundBuilders.add(SoundBuilder.sound(ResourceLocationUtils.modSpecialId(idType, name + "/" + type + i)));
		}
		registry.add(soundEvent, subtitle, soundBuilders.toArray(new SoundBuilder[0]));
	}

	public static void createMultipleSoundsSoundDefinition(SoundGenerator registry, ResourceLocationUtils.IdType idType, SoundEvent soundEvent, String name, String type, int amount) {
		List<SoundBuilder> soundBuilders = new ArrayList<>();
		for (int i = 1; i < amount; i++) {
			soundBuilders.add(SoundBuilder.sound(ResourceLocationUtils.modSpecialId(idType, name + "/" + type + i)));
		}
		registry.add(soundEvent, soundBuilders.toArray(new SoundBuilder[0]));
	}

	public static void createSimpleSoundDefinitionWithSubtitle(SoundGenerator registry, ResourceLocationUtils.IdType idType, SoundEvent soundEvent, String name, String subtitle, String type) {
		registry.add(soundEvent, subtitle, SoundBuilder.sound(ResourceLocationUtils.modSpecialId(idType, name + "/" + type)));
	}

	public static void createSimpleSoundDefinitionWithSubtitle(SoundGenerator registry, SoundEvent soundEvent, String name, String subtitle, String type) {
		registry.add(soundEvent, subtitle, SoundBuilder.sound(ResourceLocationUtils.modId(name + "/" + type)));
	}

	public static void createSimpleSoundDefinition(SoundGenerator registry, ResourceLocationUtils.IdType idType, SoundEvent soundEvent, String name, String type) {
		registry.add(soundEvent, SoundBuilder.sound(ResourceLocationUtils.modSpecialId(idType, name + "/" + type)));
	}

	public static void createSimpleSoundDefinition(SoundGenerator registry, SoundEvent soundEvent, String name, String type) {
		registry.add(soundEvent, SoundBuilder.sound(ResourceLocationUtils.modId(name + "/" + type)));
	}

}
