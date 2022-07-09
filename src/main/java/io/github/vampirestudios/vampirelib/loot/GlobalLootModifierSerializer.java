package io.github.vampirestudios.vampirelib.loot;

import com.google.gson.JsonObject;

import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

/**
 * Abstract base deserializer for LootModifiers. Takes care of Forge registry things.<br/>
 * Modders should extend this class to return their modifier and implement the abstract
 * <code>read</code> method to deserialize from json.
 *
 * @param <T> the Type to deserialize
 */
public abstract class GlobalLootModifierSerializer<T extends IGlobalLootModifier> {
	private final ResourceLocation registryName = null;

	//Helpers

	@SuppressWarnings("unchecked") // Need this wrapper, because generics
	private static <G> Class<G> castClass(Class<?> cls) {
		return (Class<G>) cls;
	}

	/**
	 * Most mods will likely not need more than<br/>
	 * <code>return new MyModifier(conditionsIn)</code><br/>
	 * but any additional properties that are needed will need to be deserialized here.
	 *
	 * @param location        The resource location (if needed)
	 * @param json            The full json object (including ILootConditions)
	 * @param ailootcondition An already deserialized list of ILootConditions
	 */
	public abstract T read(ResourceLocation location, JsonObject json, LootItemCondition[] ailootcondition);

	/**
	 * Write the serializer to json.
	 * <p>
	 * Most serializers won't have to do anything else than {@link #makeConditions}
	 * Which simply creates the JsonObject from an array of ILootConditions.
	 */
	public abstract JsonObject write(T instance);

	/**
	 * Helper to create the json object from the conditions.
	 * Add any extra properties to the returned json.
	 */
	public JsonObject makeConditions(LootItemCondition[] conditions) {
		JsonObject json = new JsonObject();
		json.add("conditions", SerializationContext.INSTANCE.serializeConditions(conditions));
		return json;
	}
}