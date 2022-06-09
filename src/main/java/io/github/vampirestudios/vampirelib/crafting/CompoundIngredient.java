package io.github.vampirestudios.vampirelib.crafting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import io.github.tropheusj.serialization_hooks.ingredient.CustomIngredient;
import io.github.tropheusj.serialization_hooks.ingredient.IngredientDeserializer;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntComparators;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.network.FriendlyByteBuf;

// todo reevaluate existence, is likely unnecessary
/**
 * Ingredient that matches if any of the child ingredients match
 */
public class CompoundIngredient extends AbstractIngredient {
	private List<Ingredient> children;
	private ItemStack[] stacks;
	private IntList itemIds;
	private Value[] values;

	protected CompoundIngredient(List<Ingredient> children) {
		this.children = Collections.unmodifiableList(children);
	}

	/**
	 * Creates a compound ingredient from the given list of ingredients
	 */
	public static Ingredient of(Ingredient... children) {
		// if 0 or 1 ingredient, can save effort
		if (children.length == 0)
			throw new IllegalArgumentException("Cannot create a compound ingredient with no children, use Ingredient.of() to create an empty ingredient");
		if (children.length == 1)
			return children[0];

		// need to merge vanilla ingredients, as otherwise the JSON produced by this ingredient could be invalid
		List<Ingredient> vanillaIngredients = new ArrayList<>();
		List<Ingredient> allIngredients = new ArrayList<>();
		for (Ingredient child : children) {
			if (!(child instanceof CustomIngredient))
				vanillaIngredients.add(child);
			else
				allIngredients.add(child);
		}
		if (!vanillaIngredients.isEmpty())
			allIngredients.add(CraftingHelper.merge(vanillaIngredients));
		if (allIngredients.size() == 1)
			return allIngredients.get(0);
		return new CompoundIngredient(allIngredients);
	}

	@Override
	@Nonnull
	public ItemStack[] getItems() {
		if (stacks == null) {
			List<ItemStack> tmp = Lists.newArrayList();
			for (Ingredient child : children)
				Collections.addAll(tmp, child.getItems());
			stacks = tmp.toArray(new ItemStack[tmp.size()]);

		}
		return stacks;
	}

	@Override
	@Nonnull
	public IntList getStackingIds() {
		if (this.itemIds == null) {
			this.itemIds = new IntArrayList();
			for (Ingredient child : children)
				this.itemIds.addAll(child.getStackingIds());
			this.itemIds.sort(IntComparators.NATURAL_COMPARATOR);
		}

		return this.itemIds;
	}

	@Override
	public boolean test(@Nullable ItemStack target) {
		if (target == null)
			return false;

		return children.stream().anyMatch(c -> c.test(target));
	}

	@Override
	public IngredientDeserializer getDeserializer() {
		return Serializer.INSTANCE;
	}

	@Override
	public Value[] getValues() {
		if (values == null) {
			List<Value> values = new ArrayList<>();
			for (Ingredient child : children) {
				Collections.addAll(values, CustomIngredient.getValues(child));
			}
			this.values = values.toArray(Value[]::new);
		}
		return values;
	}

	@Nonnull
	public Collection<Ingredient> getChildren() {
		return this.children;
	}

	@Override
	public JsonElement toJson() {
		if (this.children.size() == 1) {
			return this.children.get(0).toJson();
		} else {
			JsonArray json = new JsonArray();
			this.children.stream().forEach(e -> json.add(e.toJson()));
			return json;
		}
	}

	@Override
	public boolean isEmpty() {
		return children.stream().allMatch(Ingredient::isEmpty);
	}

	@Override
	public void toNetwork(FriendlyByteBuf buffer) {
		buffer.writeVarInt(children.size());
		children.forEach(c -> c.toNetwork(buffer));
	}

	public static class Serializer implements IngredientDeserializer {
		public static final Serializer INSTANCE = new Serializer();

		@Override
		public Ingredient fromNetwork(FriendlyByteBuf buffer) {
			return new CompoundIngredient(Stream.generate(() -> Ingredient.fromNetwork(buffer)).limit(buffer.readVarInt()).collect(Collectors.toList()));
		}

		@Nullable
		@Override
		public Ingredient fromJson(JsonObject object) {
			throw new JsonSyntaxException("CompoundIngredient should not be directly referenced in json, just use an array of ingredients.");
		}
	}
}