package io.github.vampirestudios.vampirelib.utils.modifications.selection.selectors;

import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.teamabnormals.blueprint.core.util.modification.selection.*;
import net.minecraftforge.common.crafting.conditions.FalseCondition;
import net.minecraftforge.common.crafting.conditions.ICondition;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

import io.github.vampirestudios.vampirelib.crafting.CraftingHelper;
import io.github.vampirestudios.vampirelib.utils.modifications.selection.ConditionedResourceSelector;
import io.github.vampirestudios.vampirelib.utils.modifications.selection.ResourceSelector;
import io.github.vampirestudios.vampirelib.utils.modifications.selection.ResourceSelectorSerializers;
import io.github.vampirestudios.vampirelib.utils.modifications.selection.SelectionSpace;

/**
 * A {@link ResourceSelector} implementation that picks a {@link ConditionedResourceSelector} if a condition is met or picks another {@link ConditionedResourceSelector} if the condition is not met.
 *
 * @author SmellyModder (Luke Tonon)
 */
public record ChoiceResourceSelector(ConditionedResourceSelector first, ConditionedResourceSelector second, ICondition condition) implements ResourceSelector<ChoiceResourceSelector> {

	public ChoiceResourceSelector(ResourceSelector<?> first, ResourceSelector<?> second, ICondition condition) {
		this(new ConditionedResourceSelector(first), new ConditionedResourceSelector(second), condition);
	}

	@Override
	public List<ResourceLocation> select(SelectionSpace space) {
		return this.condition.test(ICondition.IContext.EMPTY) ? this.first.select(space) : this.second.select(space);
	}

	@Override
	public Serializer getSerializer() {
		return ResourceSelectorSerializers.CHOICE;
	}

	/**
	 * The serializer class for the {@link ChoiceResourceSelector}.
	 *
	 * @author SmellyModder (Luke Tonon)
	 */
	public static final class Serializer implements ResourceSelector.Serializer<ChoiceResourceSelector> {
		@Override
		public JsonElement serialize(ChoiceResourceSelector selector) {
			JsonObject jsonObject = new JsonObject();
			jsonObject.add("first", selector.first.serialize());
			jsonObject.add("second", selector.second.serialize());
			jsonObject.add("condition", CraftingHelper.serialize(selector.condition));
			return jsonObject;
		}

		@Override
		public ChoiceResourceSelector deserialize(JsonElement element) {
			JsonObject jsonObject = element.getAsJsonObject();
			JsonObject conditionObject = GsonHelper.convertToJsonObject(jsonObject.get("condition"), "condition");
			ICondition condition;
			try {
				condition = CraftingHelper.getCondition(conditionObject);
			} catch (JsonSyntaxException e) {
				//Support for conditions that may not exist under certain circumstances
				return new ChoiceResourceSelector(ConditionedResourceSelector.EMPTY, ConditionedResourceSelector.deserialize("second", GsonHelper.convertToJsonObject(jsonObject.get("second"), "second")), FalseCondition.INSTANCE);
			}
			return new ChoiceResourceSelector(ConditionedResourceSelector.deserialize("first", jsonObject.get("first")), ConditionedResourceSelector.deserialize("second", jsonObject.get("second")), condition);
		}
	}
}