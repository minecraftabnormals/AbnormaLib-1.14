package io.github.vampirestudios.vampirelib.api;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.github.vampirestudios.vampirelib.utils.ItemStackUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Implementation class of {@link IItemGroupFiller} for filling {@link Item}s after a target {@link Item}.
 *
 * @see IItemGroupFiller
 */
public final class TargetedItemGroupFiller implements IItemGroupFiller {
	private final Supplier<Item> targetItem;
	private final Map<ItemGroup, OffsetValue> offsetMap = Maps.newHashMap();

	public TargetedItemGroupFiller(Supplier<Item> targetItem) {
		this.targetItem = targetItem;
	}

	@Override
	public void fillItem(Item item, ItemGroup group, DefaultedList<ItemStack> items) {
		if (ItemStackUtils.isInGroup(item, group)) {
			OffsetValue offset = this.offsetMap.computeIfAbsent(group, (key) -> new OffsetValue());
			if (offset.itemsProcessed.contains(item)) {
				offset.reset();
			}
			int index = ItemStackUtils.findIndexOfItem(this.targetItem.get(), items);
			if (index != -1) {
				items.add(index + offset.offset, new ItemStack(item));
				offset.itemsProcessed.add(item);
				offset.offset++;
			} else {
				items.add(new ItemStack(item));
			}
		}
	}

	static class OffsetValue {
		private final Set<Item> itemsProcessed = Sets.newHashSet();
		private int offset = 1;

		/**
		 * Vanilla doesn't cache its item group items so we must make sure the offsets are reset when the process is ran again.
		 */
		private void reset() {
			this.offset = 1;
			this.itemsProcessed.clear();
		}
	}
}