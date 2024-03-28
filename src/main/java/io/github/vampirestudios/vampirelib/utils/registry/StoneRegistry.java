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

package io.github.vampirestudios.vampirelib.utils.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

public class StoneRegistry {

	public ResourceLocation name;

	private Block raw;
	private Block slab;
	private Block stairs;
	private Block wall;
	private Block pillar;

	private Block cobblestone;
	private Block cobblestoneSlab;
	private Block cobblestoneStairs;
	private Block cobblestoneWall;

	private Block polished;
	private Block polishedSlab;
	private Block polishedStairs;
	private Block polishedWall;

	private Block bricks;
	private Block brickSlab;
	private Block brickStairs;
	private Block brickWall;

	private Block crackedBricks;
	private Block crackedBricksWall;
	private Block crackedBricksStairs;
	private Block crackedBricksSlab;

	private Block mossyBricks;
	private Block mossyBricksWall;
	private Block mossyBricksStairs;
	private Block mossyBricksSlab;

	private Block chiseled;

	private Block smallBricks;
	private Block smallBricksSlab;
	private Block smallBricksStairs;
	private Block smallBricksWall;

	private Block tinyBricks;
	private Block tinyBricksSlab;
	private Block tinyBricksStairs;
	private Block tinyBricksWall;

	private Block herringboneBricks;
	private Block herringboneBricksSlab;
	private Block herringboneBricksStairs;
	private Block herringboneBricksWall;

	private Block tiles;
	private Block tilesSlab;
	private Block tilesStairs;
	private Block tilesWall;

	private Block smallTiles;
	private Block smallTilesSlab;
	private Block smallTilesStairs;
	private Block smallTilesWall;

	public StoneRegistry(ResourceLocation name) {
		this.name = name;
	}

	public static StoneRegistry.Builder of(ResourceLocation name) {
		return new Builder().of(name);
	}

	public Block getRaw() {
		return raw;
	}

	public Block getSlab() {
		return slab;
	}

	public Block getStairs() {
		return stairs;
	}

	public Block getWall() {
		return wall;
	}

	public Block getPillar() {
		return this.pillar;
	}

	public Block getCobblestone() {
		return cobblestone;
	}

	public Block getCobblestoneSlab() {
		return cobblestoneSlab;
	}

	public Block getCobblestoneStairs() {
		return cobblestoneStairs;
	}

	public Block getCobblestoneWall() {
		return cobblestoneWall;
	}

	public Block getPolished() {
		return polished;
	}

	public Block getPolishedSlab() {
		return polishedSlab;
	}

	public Block getPolishedStairs() {
		return polishedStairs;
	}

	public Block getPolishedWall() {
		return polishedWall;
	}

	public Block getBricks() {
		return bricks;
	}

	public Block getBrickSlab() {
		return brickSlab;
	}

	public Block getBrickStairs() {
		return brickStairs;
	}

	public Block getBrickWall() {
		return brickWall;
	}

	public Block getCrackedBricks() {
		return crackedBricks;
	}

	public Block getCrackedBricksWall() {
		return crackedBricksWall;
	}

	public Block getCrackedBricksStairs() {
		return crackedBricksStairs;
	}

	public Block getCrackedBricksSlab() {
		return crackedBricksSlab;
	}

	public Block getMossyBricks() {
		return mossyBricks;
	}

	public Block getMossyBricksWall() {
		return mossyBricksWall;
	}

	public Block getMossyBricksStairs() {
		return mossyBricksStairs;
	}

	public Block getMossyBricksSlab() {
		return mossyBricksSlab;
	}

	public Block getChiseled() {
		return chiseled;
	}

	public Block getSmallBricks() {
		return smallBricks;
	}

	public Block getSmallBricksSlab() {
		return smallBricksSlab;
	}

	public Block getSmallBricksStairs() {
		return smallBricksStairs;
	}

	public Block getSmallBricksWall() {
		return smallBricksWall;
	}

	public Block getTinyBricks() {
		return tinyBricks;
	}

	public Block getTinyBricksSlab() {
		return tinyBricksSlab;
	}

	public Block getTinyBricksStairs() {
		return tinyBricksStairs;
	}

	public Block getTinyBricksWall() {
		return tinyBricksWall;
	}

	public Block getHerringboneBricks() {
		return herringboneBricks;
	}

	public Block getHerringboneBricksSlab() {
		return herringboneBricksSlab;
	}

	public Block getHerringboneBricksStairs() {
		return herringboneBricksStairs;
	}

	public Block getHerringboneBricksWall() {
		return herringboneBricksWall;
	}

	public Block getTiles() {
		return tiles;
	}

	public Block getTilesSlab() {
		return tilesSlab;
	}

	public Block getTilesStairs() {
		return tilesStairs;
	}

	public Block getTilesWall() {
		return tilesWall;
	}

	public Block getSmallTiles() {
		return smallTiles;
	}

	public Block getSmallTilesSlab() {
		return smallTilesSlab;
	}

	public Block getSmallTilesStairs() {
		return smallTilesStairs;
	}

	public Block getSmallTilesWall() {
		return smallTilesWall;
	}

	public static class Builder {

		public ResourceLocation name;
		private StoneRegistry stoneRegistry;
		private RegistryHelper registryHelper;

		public Builder of(ResourceLocation name) {
			this.name = name;
			stoneRegistry = new StoneRegistry(name);
			registryHelper = RegistryHelper.createRegistryHelper(name.getNamespace());
			return this;
		}

		public Builder raw() {
			stoneRegistry.raw = registryHelper.blocks()
					.registerBlock(new Block(BlockBehaviour.Properties.ofLegacyCopy(Blocks.STONE)),
							name.getPath());
			return this;
		}

		public Builder raw(Block raw) {
			stoneRegistry.raw = raw;
			return this;
		}

		public Builder slab() {
			stoneRegistry.slab = registryHelper.blocks().registerBlock(
					new SlabBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.STONE_SLAB)),
					name.getPath() + "_slab");
			return this;
		}

		public Builder slab(Block slab) {
			stoneRegistry.slab = slab;
			return this;
		}

		public Builder stairs() {
			stoneRegistry.stairs = registryHelper.blocks().registerBlock(
					new StairBlock(stoneRegistry.raw.defaultBlockState(), FabricBlockSettings.copyOf(Blocks.STONE_STAIRS)),
					name.getPath() + "_stairs");
			return this;
		}

		public Builder stairs(Block stairs) {
			stoneRegistry.stairs = stairs;
			return this;
		}

		public Builder wall() {
			stoneRegistry.wall = registryHelper.blocks().registerBlock(
					new WallBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.COBBLESTONE_WALL)),
					name.getPath() + "_wall");
			return this;
		}

		public Builder wall(Block wall) {
			stoneRegistry.wall = wall;
			return this;
		}

		public StoneRegistry.Builder pillar() {
			this.stoneRegistry.pillar = registryHelper.blocks().registerBlock(
					new RotatedPillarBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.COBBLESTONE_WALL)),
					this.name.getPath() + "_pillar");
			return this;
		}

		public StoneRegistry.Builder pillar(Block pillar) {
			this.stoneRegistry.pillar = pillar;
			return this;
		}

		public Builder cobblestone() {
			stoneRegistry.cobblestone = registryHelper.blocks().registerBlock(
					new Block(BlockBehaviour.Properties.ofLegacyCopy(Blocks.COBBLESTONE)),
					name.getPath() + "_cobblestone");
			return this;
		}

		public Builder cobblestone(Block cobblestone) {
			stoneRegistry.cobblestone = cobblestone;
			return this;
		}

		public Builder cobblestoneSlab() {
			stoneRegistry.cobblestoneSlab = registryHelper.blocks().registerBlock(
					new SlabBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.COBBLESTONE_SLAB)),
					name.getPath() + "_cobblestone_slab");
			return this;
		}

		public Builder cobblestoneSlab(Block cobblestoneSlab) {
			stoneRegistry.cobblestoneSlab = cobblestoneSlab;
			return this;
		}

		public Builder cobblestoneStairs() {
			stoneRegistry.cobblestoneStairs = registryHelper.blocks().registerBlock(
					new StairBlock(stoneRegistry.cobblestone.defaultBlockState(), FabricBlockSettings.copyOf(Blocks.COBBLESTONE_STAIRS)),
					name.getPath() + "_cobblestone_stairs");
			return this;
		}

		public Builder cobblestoneStairs(Block cobblestoneStairs) {
			stoneRegistry.cobblestoneStairs = cobblestoneStairs;
			return this;
		}

		public Builder cobblestoneWall() {
			stoneRegistry.cobblestoneWall = registryHelper.blocks().registerBlock(
					new WallBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.COBBLESTONE_WALL)),
					name.getPath() + "_cobblestone_wall");
			return this;
		}

		public Builder cobblestoneWall(Block cobblestoneWall) {
			stoneRegistry.cobblestoneWall = cobblestoneWall;
			return this;
		}

		public Builder polished(Block polished) {
			stoneRegistry.polished = polished;
			return this;
		}

		public Builder polished() {
			stoneRegistry.polished = registryHelper.blocks().registerBlock(
					new Block(BlockBehaviour.Properties.ofLegacyCopy(Blocks.COBBLESTONE)),
					"polished_" + name.getPath());
			return this;
		}

		public Builder polishedSlab(Block polishedSlab) {
			stoneRegistry.polishedSlab = polishedSlab;
			return this;
		}

		public Builder polishedSlab() {
			stoneRegistry.polishedSlab = registryHelper.blocks().registerBlock(
					new SlabBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.STONE_SLAB)),
					"polished_" + name.getPath() + "_slab");
			return this;
		}

		public Builder polishedStairs(Block polishedStairs) {
			stoneRegistry.polishedStairs = polishedStairs;
			return this;
		}

		public Builder polishedStairs() {
			stoneRegistry.polishedStairs = registryHelper.blocks().registerBlock(
					new StairBlock(stoneRegistry.polished.defaultBlockState(), FabricBlockSettings.copyOf(Blocks.POLISHED_GRANITE_STAIRS)),
					"polished_" + name.getPath() + "_stairs");
			return this;
		}

		public Builder polishedWall(Block polishedWall) {
			stoneRegistry.polishedWall = polishedWall;
			return this;
		}

		public Builder polishedWall() {
			stoneRegistry.polishedWall = registryHelper.blocks().registerBlock(
					new WallBlock(BlockBehaviour.Properties.ofLegacyCopy(stoneRegistry.cobblestone)),
					"polished_" + name.getPath() + "_wall");
			return this;
		}

		public Builder bricks() {
			stoneRegistry.bricks = registryHelper.blocks().registerBlock(
					new Block(BlockBehaviour.Properties.ofLegacyCopy(Blocks.COBBLESTONE)),
					name.getPath() + "_bricks");
			return this;
		}

		public Builder bricks(Block bricks) {
			stoneRegistry.bricks = bricks;
			return this;
		}

		public Builder brickSlab(Block brickSlab) {
			stoneRegistry.brickSlab = brickSlab;
			return this;
		}

		public Builder brickSlab() {
			stoneRegistry.brickSlab = registryHelper.blocks().registerBlock(
					new SlabBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.STONE_SLAB)),
					name.getPath() + "_brick_slab");
			return this;
		}

		public Builder brickStairs(Block brickStairs) {
			stoneRegistry.brickStairs = brickStairs;
			return this;
		}

		public Builder brickStairs() {
			stoneRegistry.brickStairs = registryHelper.blocks().registerBlock(
					new StairBlock(stoneRegistry.bricks.defaultBlockState(), FabricBlockSettings.copyOf(Blocks.STONE_BRICK_STAIRS)),
					name.getPath() + "_brick_stairs");
			return this;
		}

		public Builder brickWall(Block brickWall) {
			stoneRegistry.brickWall = brickWall;
			return this;
		}

		public Builder brickWall() {
			stoneRegistry.brickWall = registryHelper.blocks().registerBlock(
					new WallBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.BRICK_WALL)),
					name.getPath() + "_brick_wall");
			return this;
		}

		public Builder crackedBricks(Block crackedBricks) {
			stoneRegistry.crackedBricks = crackedBricks;
			return this;
		}

		public Builder crackedBricks() {
			stoneRegistry.crackedBricks = registryHelper.blocks().registerBlock(
					new Block(BlockBehaviour.Properties.ofLegacyCopy(Blocks.COBBLESTONE)),
					"cracked_" + name.getPath() + "_bricks");
			return this;
		}

		public Builder crackedBricksWall(Block crackedBricksWall) {
			stoneRegistry.crackedBricksWall = crackedBricksWall;
			return this;
		}

		public Builder crackedBricksWall() {
			stoneRegistry.crackedBricksWall = registryHelper.blocks().registerBlock(
					new WallBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.COBBLESTONE_WALL)),
					"cracked_" + name.getPath() + "_bricks_wall");
			return this;
		}

		public Builder crackedBricksStairs(Block crackedBricksStairs) {
			stoneRegistry.crackedBricksStairs = crackedBricksStairs;
			return this;
		}

		public Builder crackedBricksStairs() {
			stoneRegistry.crackedBricksStairs = registryHelper.blocks().registerBlock(
					new StairBlock(stoneRegistry.crackedBricks.defaultBlockState(), FabricBlockSettings.copyOf(Blocks.CRACKED_STONE_BRICKS)),
					"cracked_" + name.getPath() + "_bricks_stairs");
			return this;
		}

		public Builder crackedBricksSlab(Block crackedBricksSlab) {
			stoneRegistry.crackedBricksSlab = crackedBricksSlab;
			return this;
		}

		public Builder crackedBricksSlab() {
			stoneRegistry.crackedBricksSlab = registryHelper.blocks().registerBlock(
					new SlabBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.STONE_SLAB)),
					"cracked_" + name.getPath() + "_bricks_slab");
			return this;
		}

		public Builder mossyBricks(Block mossyBricks) {
			stoneRegistry.mossyBricks = mossyBricks;
			return this;
		}

		public Builder mossyBricks() {
			stoneRegistry.bricks = registryHelper.blocks().registerBlock(
					new Block(BlockBehaviour.Properties.ofLegacyCopy(Blocks.COBBLESTONE)),
					"mossy_" + name.getPath() + "_bricks");
			return this;
		}

		public Builder mossyBricksSlab(Block mossyBricksSlab) {
			stoneRegistry.mossyBricksSlab = mossyBricksSlab;
			return this;
		}

		public Builder mossyBricksSlab() {
			stoneRegistry.mossyBricksSlab = registryHelper.blocks().registerBlock(
					new SlabBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.STONE_SLAB)),
					"mossy_" + name.getPath() + "_bricks_slab");
			return this;
		}

		public Builder mossyBricksStairs(Block mossyBricksStairs) {
			stoneRegistry.mossyBricksStairs = mossyBricksStairs;
			return this;
		}

		public Builder mossyBricksStairs() {
			stoneRegistry.mossyBricksStairs = registryHelper.blocks().registerBlock(
					new StairBlock(stoneRegistry.cobblestone.defaultBlockState(), FabricBlockSettings.copyOf(Blocks.MOSSY_STONE_BRICK_STAIRS)),
					"mossy_" + name.getPath() + "_bricks_stairs");
			return this;
		}

		public Builder mossyBricksWall(Block mossyBricksWall) {
			stoneRegistry.mossyBricksWall = mossyBricksWall;
			return this;
		}

		public Builder mossyBricksWall() {
			stoneRegistry.mossyBricksWall = registryHelper.blocks().registerBlock(
					new WallBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.COBBLESTONE_WALL)),
					"mossy_" + name.getPath() + "_bricks_wall");
			return this;
		}


		public StoneRegistry.Builder chiseled(Block chiseledBricks) {
			this.stoneRegistry.chiseled = chiseledBricks;
			return this;
		}

		public StoneRegistry.Builder chiseled() {
			this.stoneRegistry.chiseled = registryHelper.blocks().registerBlock(
					new Block(BlockBehaviour.Properties.ofLegacyCopy(Blocks.COBBLESTONE)), "chiseled_" + this.name.getPath());
			return this;
		}

		public StoneRegistry.Builder smallBricks() {
			this.stoneRegistry.smallBricks = registryHelper.blocks().registerBlock(
					new Block(BlockBehaviour.Properties.ofLegacyCopy(Blocks.COBBLESTONE)),
					"small_" + this.name.getPath() + "_bricks");
			return this;
		}

		public StoneRegistry.Builder smallBricks(Block bricks) {
			this.stoneRegistry.smallBricks = bricks;
			return this;
		}

		public StoneRegistry.Builder smallBricksSlab(Block smallBricksSlab) {
			this.stoneRegistry.smallBricksSlab = smallBricksSlab;
			return this;
		}

		public StoneRegistry.Builder smallBricksSlab() {
			this.stoneRegistry.smallBricksSlab = registryHelper.blocks().registerBlock(
					new SlabBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.STONE_SLAB)),
					"small_" + this.name.getPath() + "_bricks_slab");
			return this;
		}

		public StoneRegistry.Builder smallBricksStairs(Block brickStairs) {
			this.stoneRegistry.smallBricksStairs = brickStairs;
			return this;
		}

		public StoneRegistry.Builder smallBricksStairs() {
			this.stoneRegistry.smallBricksStairs = registryHelper.blocks().registerBlock(
					new StairBlock(this.stoneRegistry.smallBricks.defaultBlockState(), FabricBlockSettings.copyOf(Blocks.STONE_BRICK_STAIRS)),
					"small_" + this.name.getPath() + "_bricks_stairs");
			return this;
		}

		public StoneRegistry.Builder smallBricksWall(Block smallBricksWall) {
			this.stoneRegistry.smallBricksWall = smallBricksWall;
			return this;
		}

		public StoneRegistry.Builder smallBricksWall() {
			this.stoneRegistry.smallBricksWall = registryHelper.blocks().registerBlock(
					new WallBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.BRICK_WALL)),
					"small_" + this.name.getPath() + "_bricks_wall");
			return this;
		}

		public StoneRegistry.Builder tinyBricks() {
			this.stoneRegistry.tinyBricks = registryHelper.blocks().registerBlock(
					new Block(BlockBehaviour.Properties.ofLegacyCopy(Blocks.COBBLESTONE)),
					"tiny_" + this.name.getPath() + "_bricks");
			return this;
		}

		public StoneRegistry.Builder tinyBricks(Block tinyBricks) {
			this.stoneRegistry.tinyBricks = tinyBricks;
			return this;
		}

		public StoneRegistry.Builder tinyBricksSlab(Block tinyBricksSlab) {
			this.stoneRegistry.tinyBricksSlab = tinyBricksSlab;
			return this;
		}

		public StoneRegistry.Builder tinyBricksSlab() {
			this.stoneRegistry.tinyBricksSlab = registryHelper.blocks().registerBlock(
					new SlabBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.STONE_SLAB)),
					"tiny_" + this.name.getPath() + "_bricks_slab");
			return this;
		}

		public StoneRegistry.Builder tinyBricksStairs(Block tinyBricksStairs) {
			this.stoneRegistry.tinyBricksStairs = tinyBricksStairs;
			return this;
		}

		public StoneRegistry.Builder tinyBricksStairs() {
			this.stoneRegistry.tinyBricksStairs = registryHelper.blocks().registerBlock(
					new StairBlock(this.stoneRegistry.tinyBricks.defaultBlockState(), FabricBlockSettings.copyOf(Blocks.STONE_BRICK_STAIRS)),
					"tiny_" + this.name.getPath() + "_bricks_stairs");
			return this;
		}

		public StoneRegistry.Builder tinyBricksWall(Block tinyBricksWall) {
			this.stoneRegistry.tinyBricksWall = tinyBricksWall;
			return this;
		}

		public StoneRegistry.Builder tinyBricksWall() {
			this.stoneRegistry.tinyBricksWall = registryHelper.blocks().registerBlock(
					new WallBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.BRICK_WALL)),
					"tiny_" + this.name.getPath() + "_bricks_wall");
			return this;
		}

		public StoneRegistry.Builder herringboneBricks() {
			this.stoneRegistry.herringboneBricks = registryHelper.blocks().registerBlock(
					new Block(BlockBehaviour.Properties.ofLegacyCopy(Blocks.COBBLESTONE)),
					this.name.getPath() + "_herringbone_bricks");
			return this;
		}

		public StoneRegistry.Builder herringboneBricks(Block tinyBricks) {
			this.stoneRegistry.tinyBricks = tinyBricks;
			return this;
		}

		public StoneRegistry.Builder herringboneBricksSlab(Block herringboneBricksSlab) {
			this.stoneRegistry.herringboneBricksSlab = herringboneBricksSlab;
			return this;
		}

		public StoneRegistry.Builder herringboneBricksSlab() {
			this.stoneRegistry.herringboneBricksSlab = registryHelper.blocks().registerBlock(
					new SlabBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.STONE_SLAB)),
					this.name.getPath() + "_herringbone_bricks_slab");
			return this;
		}

		public StoneRegistry.Builder herringboneBricksStairs(Block herringboneBricksStairs) {
			this.stoneRegistry.herringboneBricksStairs = herringboneBricksStairs;
			return this;
		}

		public StoneRegistry.Builder herringboneBricksStairs() {
			this.stoneRegistry.herringboneBricksStairs = registryHelper.blocks().registerBlock(
					new StairBlock(this.stoneRegistry.herringboneBricks.defaultBlockState(), FabricBlockSettings.copyOf(Blocks.STONE_BRICK_STAIRS)),
					this.name.getPath() + "_herringbone_bricks_stairs");
			return this;
		}

		public StoneRegistry.Builder herringboneBricksWall(Block herringboneBricksWall) {
			this.stoneRegistry.herringboneBricksWall = herringboneBricksWall;
			return this;
		}

		public StoneRegistry.Builder herringboneBricksWall() {
			this.stoneRegistry.herringboneBricksWall = registryHelper.blocks().registerBlock(
					new WallBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.BRICK_WALL)),
					this.name.getPath() + "_herringbone_bricks_wall");
			return this;
		}

		public StoneRegistry.Builder tiles() {
			this.stoneRegistry.tiles = registryHelper.blocks().registerBlock(
					new Block(BlockBehaviour.Properties.ofLegacyCopy(Blocks.COBBLESTONE)), this.name.getPath() + "_tiles");
			return this;
		}

		public StoneRegistry.Builder tiles(Block tiles) {
			this.stoneRegistry.tiles = tiles;
			return this;
		}

		public StoneRegistry.Builder smallTiles() {
			this.stoneRegistry.smallTiles = registryHelper.blocks().registerBlock(
					new Block(BlockBehaviour.Properties.ofLegacyCopy(Blocks.COBBLESTONE)),
					"small_" + this.name.getPath() + "_tiles");
			return this;
		}

		public StoneRegistry.Builder smallTiles(Block smallTiles) {
			this.stoneRegistry.smallTiles = smallTiles;
			return this;
		}

		public StoneRegistry.Builder stoneVariants() {
			return this.raw().slab().stairs().wall();
		}

		public StoneRegistry.Builder cobblestoneVariants() {
			return this.cobblestone().cobblestoneSlab().cobblestoneStairs().cobblestoneWall();
		}

		public StoneRegistry.Builder brickVariants() {
			return this.bricks().brickSlab().brickStairs().brickWall();
		}

		public StoneRegistry.Builder crackedBrickVariants() {
			return this.bricks().brickSlab().brickStairs().brickWall();
		}

		public StoneRegistry.Builder mossyBrickVariants() {
			return this.bricks().brickSlab().brickStairs().brickWall();
		}

		public StoneRegistry.Builder defaultBricksBlocks() {
			return this.brickVariants().crackedBrickVariants().mossyBrickVariants();
		}

		public StoneRegistry.Builder herringboneBrickVariants() {
			return this.herringboneBricks().herringboneBricksSlab().herringboneBricksStairs().herringboneBricksWall();
		}

		public StoneRegistry.Builder all() {
			return this
					.raw().slab().stairs().wall().pillar()
					.cobblestone().cobblestoneSlab().cobblestoneStairs().cobblestoneWall()
					.polished().polishedSlab().polishedStairs().polishedWall()
					.bricks().brickSlab().brickStairs().brickWall()
					.crackedBricks().crackedBricksSlab().crackedBricksStairs().crackedBricksWall()
					.mossyBricks().mossyBricksSlab().mossyBricksStairs().mossyBricksWall()
					.smallBricks().smallBricksSlab().smallBricksStairs().smallBricksWall()
					.tinyBricks().tinyBricksSlab().tinyBricksStairs().tinyBricksWall()
					.herringboneBricks().herringboneBricksSlab().herringboneBricksStairs().herringboneBricksWall()
					.tiles()
					.smallTiles();
		}

		public StoneRegistry build() {
			return stoneRegistry;
		}

	}

}
