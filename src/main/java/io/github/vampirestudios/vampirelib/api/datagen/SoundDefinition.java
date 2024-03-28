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

package io.github.vampirestudios.vampirelib.api.datagen;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Nullable;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

/**
 * Contains all the data to completely define a sound event.
 */
public final class SoundDefinition {
	private final String soundId;
	private final List<SoundBuilder> sounds;
	private String subtitle;

	private SoundDefinition(String soundId) {
		this.soundId = soundId;
		this.sounds = new ArrayList<>();
	}

	/**
	 * Creates a new sound definition for the specified sound event. Also adds a sound file to the definition based on the name of the sound
	 * <b><i>NOTE: THE NAMESPACE OF THE SOUND IS IGNORED AND WILL BE ASSIGNED TO THE ONE IN THE {@link LegacyFabricSoundProvider}</i></b>
	 *
	 * @param sound The sound to create a full definition for
	 *
	 * @return this
	 */
	public static SoundDefinition forSound(Supplier<SoundEvent> sound) {
		return definition(sound).addSound(SoundDefinition.sound(sound));
	}

	/**
	 * Creates a new sound definition for the specified sound event.
	 * <b><i>NOTE: THE NAMESPACE OF THE SOUND IS IGNORED AND WILL BE ASSIGNED TO THE ONE IN THE {@link LegacyFabricSoundProvider}</i></b>
	 *
	 * @param sound The sound to create a definition for
	 *
	 * @return this
	 */
	public static SoundDefinition definition(Supplier<SoundEvent> sound) {
		return new SoundDefinition(sound.get().getLocation().getPath());
	}

	/**
	 * Creates a new sound definition for the specified sound.
	 *
	 * @param sound The sound to create a definition for
	 *
	 * @return this
	 */
	public static SoundDefinition definition(String sound) {
		return new SoundDefinition(sound);
	}

	/**
	 * Creates a new sound file definition for the specified sound. The file path is <code>namespace:sounds/id/of/sound.ogg</code>
	 *
	 * @param sound The sound to get a file for
	 *
	 * @return this
	 */
	public static SoundBuilder sound(Supplier<SoundEvent> sound) {
		return new SoundBuilder(new ResourceLocation(sound.get().getLocation().getNamespace(),
				sound.get().getLocation().getPath().replaceAll("\\.", "/")));
	}

	/**
	 * Creates a new sound file definition for file at the specified path.
	 *
	 * @param path The path to the sound file to add, excluding <code>sounds/</code>
	 *
	 * @return this
	 */
	public static SoundBuilder sound(ResourceLocation path) {
		return new SoundDefinition.SoundBuilder(path);
	}

	/**
	 * @return A new JSON representing this sound
	 */
	public JsonObject toJson() {
		Validate.isTrue(!this.sounds.isEmpty(), "At least one sound file must be defined");

		JsonObject json = new JsonObject();
		if (this.subtitle != null) json.addProperty("subtitle", this.subtitle);

		JsonArray soundsJson = new JsonArray();
		this.sounds.forEach(sound -> soundsJson.add(sound.toJson()));
		json.add("sounds", soundsJson);

		return json;
	}

	/**
	 * @return The id of this sound. The namespace is retrieved from the location of the <code>sounds.json</code> file
	 */
	public String getSoundId() {
		return this.soundId;
	}

	/**
	 * @return All the sound files to associate with this sound instance
	 */
	public List<SoundBuilder> getSounds() {
		return this.sounds;
	}

	/**
	 * @return The subtitle to display when this sound is played
	 */
	@Nullable
	public String getSubtitle() {
		return this.subtitle;
	}

	/**
	 * Adds the specified sound file to this instance.
	 *
	 * @param sound The sound to add
	 *
	 * @return this
	 */
	public SoundDefinition addSound(SoundBuilder sound) {
		this.sounds.add(sound);
		return this;
	}

	/**
	 * Sets the subtitle to display when this sound is played.
	 *
	 * @param subtitle The subtitle or <code>null</code> to remove the text
	 *
	 * @return this
	 */
	public SoundDefinition subtitle(@Nullable String subtitle) {
		this.subtitle = subtitle;
		return this;
	}

	/**
	 * Defines how a sound is interpreted by Minecraft.
	 *
	 * @author Ocelot
	 * @since 1.0.0
	 */
	public enum SoundType {
		/**
		 * <code>file</code> causes the value of <code>name</code> in {@link SoundBuilder} to be interpreted as the path to a literal sound file.
		 */
		FILE,
		/**
		 * <code>event</code> causes the value of <code>name</code> in {@link SoundBuilder} to be interpreted as the id of a registered sound event.
		 */
		EVENT
	}

	/**
	 * Constructs sound files for {@link SoundDefinition}.
	 *
	 * @author Ocelot
	 * @since 1.0.0
	 */
	public static class SoundBuilder {
		private final ResourceLocation path;
		private float volume;
		private float pitch;
		private int weight;
		private SoundType type;
		private boolean preload;
		private boolean stream;
		private int attenuationDistance;

		public SoundBuilder(ResourceLocation path) {
			this.path = path;
			this.volume = 1.0F;
			this.pitch = 1.0F;
			this.weight = 1;
			this.type = SoundType.FILE;
			this.preload = false;
			this.stream = false;
			this.attenuationDistance = 16;
		}

		/**
		 * @return A new JSON representing this sound file
		 */
		public JsonElement toJson() {
			Validate.isTrue(this.volume > 0.0F, "Invalid volume");
			Validate.isTrue(this.pitch > 0.0F, "Invalid pitch");
			Validate.isTrue(this.weight > 0, "Invalid weight");

			if (this.volume == 1.0F && this.pitch == 1.0F && this.weight == 1 && this.type == SoundType.FILE
					&& !this.preload && !this.stream && this.attenuationDistance == 16) {
				return new JsonPrimitive(this.path.toString());
			}

			JsonObject json = new JsonObject();
			json.addProperty("name", this.path.toString());
			if (this.volume != 1.0F) json.addProperty("volume", this.volume);
			if (this.pitch != 1.0F) json.addProperty("pitch", this.pitch);
			if (this.weight != 1) json.addProperty("weight", this.weight);
			if (this.type != SoundType.FILE) json.addProperty("type", this.type.name().toLowerCase(Locale.ROOT));
			if (this.preload) json.addProperty("preload", true);
			if (this.stream) json.addProperty("stream", true);
			if (this.attenuationDistance != 16) json.addProperty("attenuation_distance", this.attenuationDistance);

			return json;
		}

		/**
		 * @return The path to the actual <code>ogg</code> file, excluding <code>sounds/</code>
		 */
		public ResourceLocation getPath() {
			return this.path;
		}

		/**
		 * @return The volume factor of the sound. Default is 1.0
		 */
		public float getVolume() {
			return this.volume;
		}

		/**
		 * @return The pitch factor of the sound. Default is 1.0
		 */
		public float getPitch() {
			return this.pitch;
		}

		/**
		 * @return The weight of this sound playing. This is only used when multiple files are defined to set probability for each file. Default is 1
		 */
		public int getWeight() {
			return this.weight;
		}

		/**
		 * @return The type of file this is. Default is {@link SoundType#EVENT}
		 */
		public SoundType getType() {
			return this.type;
		}

		/**
		 * @return Whether to preload this file when the game loads. Default is false
		 */
		public boolean isPreload() {
			return this.preload;
		}

		/**
		 * @return Whether to stream this file. Default is false
		 */
		public boolean isStream() {
			return this.stream;
		}

		/**
		 * @return The distance the sound is able to be heard from. Default is 16
		 */
		public int getAttenuationDistance() {
			return this.attenuationDistance;
		}

		/**
		 * Sets the volume factor for this sound.
		 *
		 * @param volume The new volume multiplier
		 *
		 * @return this
		 */
		public SoundBuilder volume(float volume) {
			this.volume = volume;
			return this;
		}

		/**
		 * Sets the pitch factor for this sound.
		 *
		 * @param pitch The new pitch multiplier
		 *
		 * @return this
		 */
		public SoundBuilder pitch(float pitch) {
			this.pitch = pitch;
			return this;
		}

		/**
		 * Sets the weight for this file. Higher weights increase the chance of this being chosen when multiple sounds files are defined.
		 *
		 * @param weight The new weight value
		 *
		 * @return this
		 */
		public SoundBuilder weight(int weight) {
			this.weight = weight;
			return this;
		}

		/**
		 * Sets how the value of <code>name</code> is interpreted by Minecraft.
		 *
		 * @param type The new type
		 *
		 * @return this
		 *
		 * @see SoundType
		 */
		public SoundBuilder type(SoundType type) {
			this.type = type;
			return this;
		}

		/**
		 * Causes this sound to be loaded when resources reload.
		 *
		 * @return this
		 */
		public SoundBuilder preload() {
			this.preload = true;
			return this;
		}

		/**
		 * Sets whether to load this sound when resources reload.
		 *
		 * @param preload Whether to preload or not
		 *
		 * @return this
		 */
		public SoundBuilder preload(boolean preload) {
			this.preload = preload;
			return this;
		}

		/**
		 * Causes this sound to be streamed from disc instead of loading the entire file.
		 * <b><i>NOTE: THIS WILL NOT ALLOW MODIFICATION OF THE SOUND THROUGH SOUND INSTANCES</i></b>
		 * To be able to modify the sound, use {@link #preload()} instead
		 *
		 * @return this
		 */
		public SoundBuilder stream() {
			this.stream = true;
			return this;
		}

		/**
		 * Sets whether to stream this sound from disc instead of loading the entire file.
		 * <b><i>NOTE: THIS WILL NOT ALLOW MODIFICATION OF THE SOUND THROUGH SOUND INSTANCES</i></b>
		 * To be able to modify the sound, use {@link #preload()} instead.
		 *
		 * @param stream Whether to stream the file or not
		 *
		 * @return this
		 */
		public SoundBuilder stream(boolean stream) {
			this.stream = stream;
			return this;
		}

		/**
		 * Sets the maximum distance to be able to hear the sound from.
		 * Used by nether portals and redstone components to decrease listening distance.
		 *
		 * @param attenuationDistance The new distance to hear sounds from
		 *
		 * @return this
		 */
		public SoundBuilder attenuationDistance(int attenuationDistance) {
			this.attenuationDistance = attenuationDistance;
			return this;
		}
	}
}
