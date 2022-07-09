package io.github.vampirestudios.vampirelib.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import io.github.vampirestudios.vampirelib.VampireLib;

public class LootTableIdCondition implements LootItemCondition {
	// TODO Forge Registry at some point?
	public static final LootItemConditionType LOOT_TABLE_ID = new LootItemConditionType(new Serializer());
	public static final ResourceLocation UNKNOWN_LOOT_TABLE = VampireLib.INSTANCE.identifier("unknown_loot_table");

	private final ResourceLocation targetLootTableId;

	private LootTableIdCondition(final ResourceLocation targetLootTableId) {
		this.targetLootTableId = targetLootTableId;
	}

	public static Builder builder(final ResourceLocation targetLootTableId) {
		return new Builder(targetLootTableId);
	}

	@Override
	public LootItemConditionType getType() {
		return LOOT_TABLE_ID;
	}

	@Override
	public boolean test(LootContext lootContext) {
		return false;//lootContext.getQueriedLootTableId().equals(this.targetLootTableId); // TODO: PORT
	}

	public static class Builder implements LootItemCondition.Builder {
		private final ResourceLocation targetLootTableId;

		public Builder(ResourceLocation targetLootTableId) {
			if (targetLootTableId == null) throw new IllegalArgumentException("Target loot table must not be null");
			this.targetLootTableId = targetLootTableId;
		}

		@Override
		public LootItemCondition build() {
			return new LootTableIdCondition(this.targetLootTableId);
		}
	}

	public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<LootTableIdCondition> {
		@Override
		public void serialize(JsonObject object, LootTableIdCondition instance, JsonSerializationContext ctx) {
			object.addProperty("loot_table_id", instance.targetLootTableId.toString());
		}

		@Override
		public LootTableIdCondition deserialize(JsonObject object, JsonDeserializationContext ctx) {
			return new LootTableIdCondition(new ResourceLocation(GsonHelper.getAsString(object, "loot_table_id")));
		}
	}
}