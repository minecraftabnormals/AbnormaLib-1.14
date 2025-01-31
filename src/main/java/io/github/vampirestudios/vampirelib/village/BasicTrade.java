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

package io.github.vampirestudios.vampirelib.village;

import org.jetbrains.annotations.Nullable;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;

/**
 * A default, exposed implementation of ITrade.  All of the other implementations of ITrade (in VillagerTrades) are not public.
 * This class contains everything needed to make a MerchantOffer, the actual "trade" object shown in trading guis.
 */
public class BasicTrade implements VillagerTrades.ItemListing {

	protected final ItemStack price;
	protected final ItemStack price2;
	protected final ItemStack forSale;
	protected final int maxTrades;
	protected final int xp;
	protected final float priceMult;

	public BasicTrade(ItemStack price, ItemStack price2, ItemStack forSale, int maxTrades, int xp, float priceMult) {
		this.price = price;
		this.price2 = price2;
		this.forSale = forSale;
		this.maxTrades = maxTrades;
		this.xp = xp;
		this.priceMult = priceMult;
	}

	public BasicTrade(ItemStack price, ItemStack forSale, int maxTrades, int xp, float priceMult) {
		this(price, ItemStack.EMPTY, forSale, maxTrades, xp, priceMult);
	}

	public BasicTrade(int emeralds, ItemStack forSale, int maxTrades, int xp, float mult) {
		this(new ItemStack(Items.EMERALD, emeralds), forSale, maxTrades, xp, mult);
	}

	public BasicTrade(int emeralds, ItemStack forSale, int maxTrades, int xp) {
		this(new ItemStack(Items.EMERALD, emeralds), forSale, maxTrades, xp, 1);
	}

	@Override
	@Nullable
	public MerchantOffer getOffer(Entity merchant, RandomSource rand) {
		return new MerchantOffer(price, price2, forSale, maxTrades, xp, priceMult);
	}

}