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

import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.resources.ResourceLocation;

/**
 * Utility class for building a sound entry with a given set of properties, without necessarily passing them all as parameters.
 */
public class SoundBuilder {
	private final ResourceLocation name;
	private final boolean event;

	private float volume = 1;
	private float pitch = 1;
	private int weight = 1;
	private boolean stream = false;
	private int attenuationDistance = 16;
	private boolean preload = false;

	private SoundBuilder(ResourceLocation name, boolean event) {
		this.name = name;
		this.event = event;
	}

	public ResourceLocation getName() {
		return name;
	}

	/**
	 * Build an entry corresponding to a sound file.
	 *
	 * @param name The name of the sound as a namespaced ID with relative folder path.
	 */
	public static SoundBuilder sound(ResourceLocation name) {
		return new SoundBuilder(name, false);
	}

	/**
	 * Build an entry corresponding to an existing {@link net.minecraft.sounds.SoundEvent}.
	 *
	 * @param name The ID of the sound event.
	 */
	public static SoundBuilder event(ResourceLocation name) {
		return new SoundBuilder(name, true);
	}

	/**
	 * Sets the volume that the sound should play at as a number between <code>0.0</code> and <code>1.0</code>. Defaults
	 * to <code>1.0</code>.
	 */
	public SoundBuilder setVolume(float volume) {
		Preconditions.checkArgument(volume >= 0 && volume <= 1);
		this.volume = volume;
		return this;
	}

	/**
	 * Sets the pitch that the sound should play at. Note that this is internally clamped in-game between 0.5 and 2.0.
	 */
	public SoundBuilder setPitch(float pitch) {
		Preconditions.checkArgument(pitch > 0);
		this.pitch = pitch;
		return this;
	}

	/**
	 * Sets how much likelier it should be for this sound to play. For example, setting this to 2 will mean that this
	 * sound is twice as likely to play for this event.
	 */
	public SoundBuilder setWeight(int weight) {
		Preconditions.checkArgument(weight >= 0);
		this.weight = weight;
		return this;
	}

	/**
	 * Dictates that this sound should be streamed from its file. Recommended for sounds with a much longer play time
	 * than a couple of seconds, such as music tracks, in order to minimise lag. If set, only 4 instances of this sound
	 * can play in-game at once.
	 */
	public SoundBuilder stream() {
		this.stream = true;
		return this;
	}

	/**
	 * Sets the reduction rate of this sound depending on distance from the source. Defaults to 16.
	 */
	public SoundBuilder setAttenuationDistance(int attenuationDistance) {
		Preconditions.checkArgument(attenuationDistance >= 0);
		this.attenuationDistance = attenuationDistance;
		return this;
	}

	/**
	 * Dictates that this sound should be loaded in advance when loading the resource pack containing it rather than
	 * when the sound itself plays.
	 */
	public SoundBuilder preload() {
		this.preload = true;
		return this;
	}

	private boolean allDefaults() {
		return volume == 1 && pitch == 1 && weight == 1 && attenuationDistance == 16 && !stream && !preload && !event;
	}

	public JsonElement build() {
		if (allDefaults()) {
			return new JsonPrimitive(name.toString());
		} else {
			JsonObject soundEntry = new JsonObject();
			soundEntry.addProperty("name", name.toString());

			if (volume != 1) {
				soundEntry.addProperty("volume", volume);
			}

			if (pitch != 1) {
				soundEntry.addProperty("pitch", pitch);
			}

			if (weight != 1) {
				soundEntry.addProperty("weight", weight);
			}

			if (stream) {
				soundEntry.addProperty("stream", true);
			}

			if (attenuationDistance != 16) {
				soundEntry.addProperty("attenuation_distance", attenuationDistance);
			}

			if (preload) {
				soundEntry.addProperty("preload", true);
			}

			if (event) {
				soundEntry.addProperty("type", "event");
			}

			return soundEntry;
		}
	}
}
