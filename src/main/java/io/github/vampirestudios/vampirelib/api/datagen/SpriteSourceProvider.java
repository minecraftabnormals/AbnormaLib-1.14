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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import net.minecraft.client.renderer.texture.atlas.SpriteSources;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

public abstract class SpriteSourceProvider extends JsonCodecProvider<List<SpriteSource>> {
	protected static final ResourceLocation BLOCKS_ATLAS = new ResourceLocation("blocks");
	protected static final ResourceLocation BANNER_PATTERNS_ATLAS = new ResourceLocation("banner_patterns");
	protected static final ResourceLocation BEDS_ATLAS = new ResourceLocation("beds");
	protected static final ResourceLocation CHESTS_ATLAS = new ResourceLocation("chests");
	protected static final ResourceLocation SHIELD_PATTERNS_ATLAS = new ResourceLocation("shield_patterns");
	protected static final ResourceLocation SHULKER_BOXES_ATLAS = new ResourceLocation("shulker_boxes");
	protected static final ResourceLocation SIGNS_ATLAS = new ResourceLocation("signs");
	protected static final ResourceLocation MOB_EFFECTS_ATLAS = new ResourceLocation("mob_effects");
	protected static final ResourceLocation PAINTINGS_ATLAS = new ResourceLocation("paintings");
	protected static final ResourceLocation PARTICLES_ATLAS = new ResourceLocation("particles");

	private final Map<ResourceLocation, SourceList> atlases = new HashMap<>();

	public SpriteSourceProvider(PackOutput output) {
		super(output, PackOutput.Target.RESOURCE_PACK, "atlases", SpriteSources.FILE_CODEC);
	}

	@Override
	public String getName() {
		return "Sprite Source Provider";
	}

	@Override
	protected void configure(BiConsumer<ResourceLocation, List<SpriteSource>> provider) {
		addSources();
		atlases.forEach((atlas, srcList) -> provider.accept(atlas, srcList.sources));
	}

	protected abstract void addSources();

	/**
	 * Get or create a {@link SourceList} for the given atlas
	 *
	 * @param atlas The texture atlas the sources should be added to, see constants at the top for the format
	 *              and the vanilla atlases
	 * @return an existing {@code SourceList} for the given atlas or a new one if not present yet
	 */
	protected final SourceList atlas(ResourceLocation atlas) {
		return atlases.computeIfAbsent(atlas, $ -> new SourceList());
	}

	protected static final class SourceList {
		private final List<SpriteSource> sources = new ArrayList<>();

		/**
		 * Add the given {@link SpriteSource} to this atlas configuration
		 *
		 * @param source The {@code SpriteSource} to be added
		 */
		public SourceList addSource(SpriteSource source) {
			sources.add(source);
			return this;
		}
	}
}
