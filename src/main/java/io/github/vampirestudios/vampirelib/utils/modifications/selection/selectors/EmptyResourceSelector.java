package io.github.vampirestudios.vampirelib.utils.modifications.selection.selectors;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.resources.ResourceLocation;

import io.github.vampirestudios.vampirelib.utils.modifications.selection.ResourceSelector;
import io.github.vampirestudios.vampirelib.utils.modifications.selection.ResourceSelectorSerializers;
import io.github.vampirestudios.vampirelib.utils.modifications.selection.SelectionSpace;

/**
 * A {@link ResourceSelector} implementation that always returns an empty list of target names.
 *
 * @author SmellyModder (Luke Tonon)
 */
public record EmptyResourceSelector() implements ResourceSelector<EmptyResourceSelector> {
	private static final ImmutableList<ResourceLocation> EMPTY = ImmutableList.of();

	@Override
	public List<ResourceLocation> select(SelectionSpace space) {
		return EMPTY;
	}

	@Override
	public Serializer getSerializer() {
		return ResourceSelectorSerializers.EMPTY;
	}

	/**
	 * The serializer class for the {@link EmptyResourceSelector}.
	 *
	 * @author SmellyModder (Luke Tonon)
	 */
	public static final class Serializer implements ResourceSelector.Serializer<EmptyResourceSelector> {
		@Override
		public JsonElement serialize(EmptyResourceSelector selector) {
			return new JsonObject();
		}

		@Override
		public EmptyResourceSelector deserialize(JsonElement element) {
			return new EmptyResourceSelector();
		}
	}