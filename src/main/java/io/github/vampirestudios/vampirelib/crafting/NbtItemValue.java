package io.github.vampirestudios.vampirelib.crafting;

import com.google.gson.JsonObject;
import io.github.tropheusj.serialization_hooks.value.CustomValue;
import io.github.tropheusj.serialization_hooks.value.ValueDeserializer;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient.ItemValue;
import net.minecraft.world.item.crafting.Ingredient.Value;

import io.github.vampirestudios.vampirelib.VampireLib;

public class NbtItemValue extends ItemValue implements CustomValue {
	public NbtItemValue(ItemStack itemStack) {
		super(itemStack);
	}

	@Override
	public JsonObject serialize() {
		JsonObject jsonObject = super.serialize();
		jsonObject.addProperty("count", item.getCount());
		jsonObject.addProperty("value_deserializer", NbtItemValueDeserializer.ID.toString());
		CompoundTag nbt = item.getTag();
		if (nbt != null) {
			jsonObject.addProperty("nbt", nbt.toString());
		}
		return jsonObject;
	}

	@Override
	public ValueDeserializer getDeserializer() {
		return NbtItemValueDeserializer.INSTANCE;
	}

	public static class NbtItemValueDeserializer implements ValueDeserializer {
		public static final NbtItemValueDeserializer INSTANCE = new NbtItemValueDeserializer();
		public static final ResourceLocation ID = VampireLib.INSTANCE.identifier("nbt_item_value_deserializer");

		@Override
		public Value fromJson(JsonObject object) {
			return new NbtItemValue(CraftingHelper.getItemStack(object, true));
		}
	}
}