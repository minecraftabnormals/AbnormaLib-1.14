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

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;

/**
 * Register an instance of the class with {@link FabricDataGenerator.Pack#addProvider} in a {@link net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint}.
 */
public abstract class FabricSoundProvider implements DataProvider {
	protected final FabricDataOutput dataOutput;
	protected final String modId;

	protected FabricSoundProvider(FabricDataOutput dataOutput) {
		this.dataOutput = dataOutput;
		this.modId = dataOutput.getModId();
	}

	/**
	 * Implement this method to register sounds.
	 *
	 * <p>Call {@link SoundGenerator#add(SoundEvent, SoundBuilder...)} to add a list of sound entries
	 * for a given {@link SoundEvent}.
	 */
	public abstract void generateSounds(SoundGenerator soundGenerator);

	@Override
	public CompletableFuture<?> run(@NotNull CachedOutput cache) {
		HashMap<String, JsonObject> soundEvents = new HashMap<>();

		generateSounds(((sound, replace, subtitle, entries) -> {
			Objects.requireNonNull(sound);
			Objects.requireNonNull(entries);

			List<ResourceLocation> keys = Arrays.stream(entries).map(SoundBuilder::getName).toList();

			if (!keys.stream().filter(i -> Collections.frequency(keys, i) > 1).toList().isEmpty()) {
				throw new RuntimeException("Entries for sound event " + sound.location() + " contain duplicate sound names. Event will be omitted.");
			}

			JsonObject soundEventData = new JsonObject();
			JsonArray soundEntries = new JsonArray();

			Arrays.asList(entries).forEach(s -> soundEntries.add(s.build()));
			soundEventData.add("sounds", soundEntries);

			if (replace) {
				soundEventData.addProperty("replace", true);
			}

			if (subtitle != null) {
				soundEventData.addProperty("subtitle", subtitle);
			}

			soundEvents.put(sound.location().getPath(), soundEventData);
		}));

		JsonObject soundsJson = new JsonObject();

		for (Map.Entry<String, JsonObject> entry : soundEvents.entrySet()) {
			soundsJson.add(entry.getKey(), entry.getValue());
		}

		Path soundsPath = dataOutput
				.createPathProvider(PackOutput.Target.RESOURCE_PACK, ".")
				.json(ResourceLocation.fromNamespaceAndPath(dataOutput.getModId(), "sounds"));
		return DataProvider.saveStable(cache, soundsJson, soundsPath.normalize());
	}


	@Override
	public String getName() {
		return "Sounds";
	}

	@ApiStatus.NonExtendable
	@FunctionalInterface
	public interface SoundGenerator {
		/**
		 * Adds an individual {@link SoundEvent} and its respective sounds to your mod's <code>sounds.json</code> file.
		 *
		 * @param sound The {@link SoundEvent} to add an entry for.
		 * @param replace Set this to <code>true</code> if this entry corresponds to a sound event from vanilla
		 *                Minecraft or some other mod's namespace, in order to replace the default sounds from the
		 *                original namespace's sounds file via your own namespace's resource pack.
		 * @param subtitle An optional subtitle to use for the event, given as a translation key for the subtitle.
		 * @param sounds A list of {@link SoundBuilder} instances from which to generate individual sound entry data for
		 *                this event.
		 */
		void add(SoundEvent sound, boolean replace, @Nullable String subtitle, SoundBuilder... sounds);

		/**
		 * Adds an individual {@link SoundEvent} and its respective sounds to your mod's <code>sounds.json</code> file.
		 *
		 * @param sound The {@link SoundEvent} to add an entry for.
		 * @param replace Set this to <code>true</code> if this entry corresponds to a sound event from vanilla
		 *                Minecraft or some other mod's namespace, in order to replace the default sounds from the
		 *                original namespace's sounds file via your own namespace's resource pack.
		 * @param sounds A list of {@link SoundBuilder} instances from which to generate individual sound entry data for
		 *                this event.
		 */
		default void add(SoundEvent sound, boolean replace, SoundBuilder... sounds) {
			add(sound, replace, null, sounds);
		}

		/**
		 * Adds an individual {@link SoundEvent} and its respective sounds to your mod's <code>sounds.json</code> file.
		 *
		 * @param sound The {@link SoundEvent} to add an entry for.
		 * @param subtitle An optional subtitle to use for the event, given as a translation key for the subtitle.
		 * @param sounds A list of {@link SoundBuilder} instances from which to generate individual sound entry data for
		 *                this event.
		 */
		default void add(SoundEvent sound, @Nullable String subtitle, SoundBuilder... sounds) {
			add(sound, false, subtitle, sounds);
		}

		/**
		 * Adds an individual {@link SoundEvent} and its respective sounds to your mod's <code>sounds.json</code> file.
		 *
		 * @param sound The {@link SoundEvent} to add an entry for.
		 * @param sounds A list of {@link SoundBuilder} instances from which to generate individual sound entry data for
		 *                this event.
		 */
		default void add(SoundEvent sound, SoundBuilder... sounds) {
			add(sound, false, null, sounds);
		}
	}
}
