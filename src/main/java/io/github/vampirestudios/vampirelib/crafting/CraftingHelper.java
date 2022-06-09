package io.github.vampirestudios.vampirelib.crafting;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import io.github.tropheusj.serialization_hooks.ingredient.IngredientDeserializer;
import io.github.tropheusj.serialization_hooks.value.ValueDeserializer;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;

import io.github.vampirestudios.vampirelib.crafting.NbtItemValue.NbtItemValueDeserializer;

import static net.fabricmc.fabric.api.datagen.v1.provider.FabricLootTableProvider.GSON;

public class CraftingHelper {
	public static void init() {
		register(new ResourceLocation("forge", "compound"), CompoundIngredient.Serializer.INSTANCE);
		register(new ResourceLocation("forge", "nbt"), NBTIngredient.Serializer.INSTANCE);
		Registry.register(ValueDeserializer.REGISTRY, NbtItemValueDeserializer.ID, NbtItemValueDeserializer.INSTANCE);
	}

	public static IngredientDeserializer register(ResourceLocation key, IngredientDeserializer serializer) {
		return Registry.register(IngredientDeserializer.REGISTRY, key, serializer);
	}

	@Nullable
	public static ResourceLocation getID(IngredientDeserializer serializer) {
		return IngredientDeserializer.REGISTRY.getKey(serializer);
	}

	public static ItemStack getItemStack(JsonObject json, boolean readNBT) {
		return getItemStack(json, readNBT, false);
	}

	public static Item getItem(String itemName, boolean disallowsAirInRecipe) {
		Item item = tryGetItem(itemName, disallowsAirInRecipe);
		if (item == null) {
			if (!Registry.ITEM.containsKey(new ResourceLocation(itemName)))
				throw new JsonSyntaxException("Unknown item '" + itemName + "'");
			if (disallowsAirInRecipe && item == Items.AIR)
				throw new JsonSyntaxException("Invalid item: " + itemName);
		}
		return Objects.requireNonNull(item);
	}

	@Nullable
	public static Item tryGetItem(String itemName, boolean disallowsAirInRecipe) {
		ResourceLocation itemKey = new ResourceLocation(itemName);
		if (!Registry.ITEM.containsKey(itemKey))
			return null;

		Item item = Registry.ITEM.get(itemKey);
		if (disallowsAirInRecipe && item == Items.AIR)
			return null;
		return item;
	}

	public static CompoundTag getNBT(JsonElement element) {
		try {
			if (element.isJsonObject())
				return TagParser.parseTag(GSON.toJson(element));
			else
				return TagParser.parseTag(GsonHelper.convertToString(element, "nbt"));
		} catch (CommandSyntaxException e) {
			throw new JsonSyntaxException("Invalid NBT Entry: " + e);
		}
	}

	@Nullable
	public static CompoundTag tryGetNBT(JsonElement element) {
		try {
			if (element.isJsonObject())
				return TagParser.parseTag(GSON.toJson(element));
			else
				return TagParser.parseTag(GsonHelper.convertToString(element, "nbt"));
		} catch (CommandSyntaxException e) {
			return null;
		}
	}

	public static ItemStack getItemStack(JsonObject json, boolean readNBT, boolean disallowsAirInRecipe) {
		String itemName = GsonHelper.getAsString(json, "item");
		Item item = getItem(itemName, disallowsAirInRecipe);
		if (readNBT && json.has("nbt")) {
			CompoundTag nbt = getNBT(json.get("nbt"));
			CompoundTag tmp = new CompoundTag();
			if (nbt.contains("ForgeCaps")) { // TODO: should we keep this?
				tmp.put("ForgeCaps", nbt.get("ForgeCaps"));
				nbt.remove("ForgeCaps");
			}

			tmp.put("tag", nbt);
			tmp.putString("id", itemName);
			tmp.putInt("Count", GsonHelper.getAsInt(json, "count", 1));

			return ItemStack.of(tmp);
		}

		return new ItemStack(item, GsonHelper.getAsInt(json, "count", 1));
	}

	@Nullable
	public static ItemStack tryGetItemStack(JsonObject json, boolean readNBT, boolean disallowsAirInRecipe) {
		JsonElement nameElement = json.get("name");
		if (nameElement == null || !nameElement.isJsonPrimitive())
			return null;
		String itemName = nameElement.getAsString();
		Item item = tryGetItem(itemName, disallowsAirInRecipe);
		if (readNBT && json.has("nbt")) {
			CompoundTag nbt = tryGetNBT(json.get("nbt"));
			if (nbt == null)
				return null;
			CompoundTag tmp = new CompoundTag();
			if (nbt.contains("ForgeCaps")) { // TODO: should we keep this?
				tmp.put("ForgeCaps", nbt.get("ForgeCaps"));
				nbt.remove("ForgeCaps");
			}

			tmp.put("tag", nbt);
			tmp.putString("id", itemName);
			tmp.putInt("Count", GsonHelper.getAsInt(json, "count", 1));

			return ItemStack.of(tmp);
		}

		return new ItemStack(item, GsonHelper.getAsInt(json, "count", 1));
	}

	//Merges several vanilla Ingredients together. As a quirk of how the json is structured, we can't tell if its a single Ingredient type or multiple so we split per item and re-merge here.
	//Only public for internal use, so we can access a private field in here.
	public static Ingredient merge(Collection<Ingredient> parts) {
		return Ingredient.fromValues(parts.stream().flatMap(i -> Arrays.stream(i.values)));
	}

	/**
	 * Modeled after ItemStack.areItemStackTagsEqual
	 * Uses Item.getNBTShareTag for comparison instead of NBT and capabilities.
	 * Only used for comparing itemStacks that were transferred from server to client using Item.getNBTShareTag.
	 */
	public static boolean areShareTagsEqual(ItemStack stack, ItemStack other) {
		CompoundTag shareTagA = stack.getTag();
		CompoundTag shareTagB = other.getTag();
		if (shareTagA == null)
			return shareTagB == null;
		else
			return shareTagB != null && shareTagA.equals(shareTagB);
	}

	public static Predicate<JsonObject> getConditionPredicate(JsonObject json) {
		return ResourceConditions.get(new ResourceLocation(GsonHelper.getAsString(json, ResourceConditions.CONDITION_ID_KEY)));
	}
}