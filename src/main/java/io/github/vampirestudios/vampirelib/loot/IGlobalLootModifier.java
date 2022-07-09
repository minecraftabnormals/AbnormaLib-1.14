package io.github.vampirestudios.vampirelib.loot;

import javax.annotation.Nonnull;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

public interface IGlobalLootModifier {
	@Nonnull
	ObjectArrayList<ItemStack> apply(ObjectArrayList<ItemStack> generatedLoot, LootContext context);
}