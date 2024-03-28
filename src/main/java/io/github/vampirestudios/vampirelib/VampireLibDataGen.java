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

package io.github.vampirestudios.vampirelib;

import static io.github.vampirestudios.vampirelib.VampireLib.TEST_CONTENT_ENABLED;

import java.io.File;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;

import io.github.vampirestudios.vampirelib.api.datagen.CustomTagProviders;
import io.github.vampirestudios.vampirelib.utils.registry.WoodRegistry;

public class VampireLibDataGen implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator dataGenerator) {
		FabricDataGenerator.Pack pack = dataGenerator.createPack();
		if (TEST_CONTENT_ENABLED) {
			FabricDataGenerator.Pack pack1 = dataGenerator.createBuiltinResourcePack(VampireLib.INSTANCE.identifier("wood_types"));
			pack1.addProvider(WoodTypeBlockStateDefinitionProvider::new);
			pack1.addProvider((FabricDataGenerator.Pack.Factory<DataProvider>) output -> new WoodTypeTranslationProvider(output, "en_us"));
			pack1.addProvider((FabricDataGenerator.Pack.Factory<DataProvider>) output -> new WoodTypeTranslationProvider(output, "fr_fr"));
			pack1.addProvider((FabricDataGenerator.Pack.Factory<DataProvider>) output -> new WoodTypeTranslationProvider(output, "no_no"));
			pack1.addProvider((FabricDataGenerator.Pack.Factory<DataProvider>) output -> new WoodTypeTranslationProvider(output, "da_dk"));
			pack1.addProvider((FabricDataGenerator.Pack.Factory<DataProvider>) output -> new WoodTypeTranslationProvider(output, "de_de"));
			pack1.addProvider((FabricDataGenerator.Pack.Factory<DataProvider>) output -> new WoodTypeTranslationProvider(output, "fi_fi"));
			pack1.addProvider((FabricDataGenerator.Pack.Factory<DataProvider>) output -> new WoodTypeTranslationProvider(output, "enws"));
			pack1.addProvider((FabricDataGenerator.Pack.Factory<DataProvider>) output -> new WoodTypeTranslationProvider(output, "lol_us"));
			pack1.addProvider((FabricDataGenerator.Pack.Factory<DataProvider>) output -> new WoodTypeTranslationProvider(output, "nl_nl"));
			pack1.addProvider(WoodTypeRecipeProvider::new);
			WoodTypeBlockTagProvider blockTagsProvider = pack1.addProvider(WoodTypeBlockTagProvider::new);
			pack1.addProvider((output, registriesFuture) -> new WoodTypeItemTagProvider(output, blockTagsProvider, registriesFuture));
			pack1.addProvider(WoodTypeBlockLootTableProvider::new);
		}
		VBlockTagsProvider blockTagsProvider = pack.addProvider(VBlockTagsProvider::new);
		pack.addProvider((output, registriesFuture) -> new VItemTagsProvider(output, registriesFuture, blockTagsProvider));
	}

	//Wood Type Test Generation
	private static class WoodTypeBlockStateDefinitionProvider extends FabricModelProvider {
		private WoodTypeBlockStateDefinitionProvider(FabricDataOutput generator) {
			super(generator);
		}

		@Override
		public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
			generateWoodTypeAssets(blockStateModelGenerator, VampireLib.TEST_WOOD);
			generateWoodTypeAssets(blockStateModelGenerator, VampireLib.TEST_WOOD1);
			generateWoodTypeAssets(blockStateModelGenerator, VampireLib.TEST_WOOD2);
			generateWoodTypeAssets(blockStateModelGenerator, VampireLib.TEST_WOOD3);
			generateWoodTypeAssets(blockStateModelGenerator, VampireLib.TEST_WOOD4);
			generateWoodTypeAssets(blockStateModelGenerator, VampireLib.TEST_WOOD5);
			generateWoodTypeAssets(blockStateModelGenerator, VampireLib.TEST_WOOD6);
			generateWoodTypeAssets(blockStateModelGenerator, VampireLib.TEST_WOOD7);
			generateWoodTypeAssets(blockStateModelGenerator, VampireLib.TEST_WOOD8);
			generateWoodTypeAssets(blockStateModelGenerator, VampireLib.TEST_WOOD9);
			generateWoodTypeAssets(blockStateModelGenerator, VampireLib.TEST_WOOD10);
			generateWoodTypeAssets(blockStateModelGenerator, VampireLib.TEST_WOOD11);
			generateWoodTypeAssets(blockStateModelGenerator, VampireLib.TEST_WOOD12);
			generateWoodTypeAssets(blockStateModelGenerator, VampireLib.TEST_WOOD13);
			generateWoodTypeAssets(blockStateModelGenerator, VampireLib.TEST_WOOD14);
			generateWoodTypeAssets(blockStateModelGenerator, VampireLib.TEST_WOOD15);
			generateWoodTypeAssets(blockStateModelGenerator, VampireLib.TEST_WOOD16);

			generateWoodTypeAssets(blockStateModelGenerator, VampireLib.TEST_NETHER_WOOD);
			generateWoodTypeAssets(blockStateModelGenerator, VampireLib.TEST_NETHER_WOOD1);
			generateWoodTypeAssets(blockStateModelGenerator, VampireLib.TEST_NETHER_WOOD2);
			generateWoodTypeAssets(blockStateModelGenerator, VampireLib.TEST_NETHER_WOOD3);
			generateWoodTypeAssets(blockStateModelGenerator, VampireLib.TEST_NETHER_WOOD4);
			generateWoodTypeAssets(blockStateModelGenerator, VampireLib.TEST_NETHER_WOOD5);
			generateWoodTypeAssets(blockStateModelGenerator, VampireLib.TEST_NETHER_WOOD6);
			generateWoodTypeAssets(blockStateModelGenerator, VampireLib.TEST_NETHER_WOOD7);
			generateWoodTypeAssets(blockStateModelGenerator, VampireLib.TEST_NETHER_WOOD8);
			generateWoodTypeAssets(blockStateModelGenerator, VampireLib.TEST_NETHER_WOOD9);
			generateWoodTypeAssets(blockStateModelGenerator, VampireLib.TEST_NETHER_WOOD10);
			generateWoodTypeAssets(blockStateModelGenerator, VampireLib.TEST_NETHER_WOOD11);
			generateWoodTypeAssets(blockStateModelGenerator, VampireLib.TEST_NETHER_WOOD12);
			generateWoodTypeAssets(blockStateModelGenerator, VampireLib.TEST_NETHER_WOOD13);
		}

		@Override
		public void generateItemModels(ItemModelGenerators itemModelGenerator) {

		}

		private void generateWoodTypeAssets(BlockModelGenerators blockStateModelGenerator, WoodRegistry woodRegistry) {
			woodRegistry.generateModels(blockStateModelGenerator);
		}
	}

	private static class WoodTypeTranslationProvider extends FabricLanguageProvider {
		private Map<String, String> lang;

		private WoodTypeTranslationProvider(FabricDataOutput dataGenerator, String langCode) {
			super(dataGenerator, langCode);
			File file = new File("translations/" + langCode + ".json");
			try (Reader reader = Files.newBufferedReader(Paths.get(file.toURI()))) {
				lang = VampireLib.GSON.<Map<String, String>>fromJson(reader, Map.class);
			} catch (Exception ignored) {
				lang = new HashMap<>();
			}
		}

		@Override
		public void generateTranslations(TranslationBuilder translationBuilder) {
			addWoodTypeLang(VampireLib.TEST_WOOD, translationBuilder);
			addWoodTypeLang(VampireLib.TEST_WOOD1, translationBuilder);
			addWoodTypeLang(VampireLib.TEST_WOOD2, translationBuilder);
			addWoodTypeLang(VampireLib.TEST_WOOD3, translationBuilder);
			addWoodTypeLang(VampireLib.TEST_WOOD4, translationBuilder);
			addWoodTypeLang(VampireLib.TEST_WOOD5, translationBuilder);
			addWoodTypeLang(VampireLib.TEST_WOOD6, translationBuilder);
			addWoodTypeLang(VampireLib.TEST_WOOD7, translationBuilder);
			addWoodTypeLang(VampireLib.TEST_WOOD8, translationBuilder);
			addWoodTypeLang(VampireLib.TEST_WOOD9, translationBuilder);
			addWoodTypeLang(VampireLib.TEST_WOOD10, translationBuilder);
			addWoodTypeLang(VampireLib.TEST_WOOD11, translationBuilder);
			addWoodTypeLang(VampireLib.TEST_WOOD12, translationBuilder);
			addWoodTypeLang(VampireLib.TEST_WOOD13, translationBuilder);
			addWoodTypeLang(VampireLib.TEST_WOOD14, translationBuilder);
			addWoodTypeLang(VampireLib.TEST_WOOD15, translationBuilder);
			addWoodTypeLang(VampireLib.TEST_WOOD16, translationBuilder);

			addWoodTypeLang(VampireLib.TEST_NETHER_WOOD, translationBuilder);
			addWoodTypeLang(VampireLib.TEST_NETHER_WOOD1, translationBuilder);
			addWoodTypeLang(VampireLib.TEST_NETHER_WOOD2, translationBuilder);
			addWoodTypeLang(VampireLib.TEST_NETHER_WOOD3, translationBuilder);
			addWoodTypeLang(VampireLib.TEST_NETHER_WOOD4, translationBuilder);
			addWoodTypeLang(VampireLib.TEST_NETHER_WOOD5, translationBuilder);
			addWoodTypeLang(VampireLib.TEST_NETHER_WOOD6, translationBuilder);
			addWoodTypeLang(VampireLib.TEST_NETHER_WOOD7, translationBuilder);
			addWoodTypeLang(VampireLib.TEST_NETHER_WOOD8, translationBuilder);
			addWoodTypeLang(VampireLib.TEST_NETHER_WOOD9, translationBuilder);
			addWoodTypeLang(VampireLib.TEST_NETHER_WOOD10, translationBuilder);
			addWoodTypeLang(VampireLib.TEST_NETHER_WOOD11, translationBuilder);
			addWoodTypeLang(VampireLib.TEST_NETHER_WOOD12, translationBuilder);
			addWoodTypeLang(VampireLib.TEST_NETHER_WOOD13, translationBuilder);
		}

		private void addWoodTypeLang(WoodRegistry woodRegistry, FabricLanguageProvider.TranslationBuilder translationBuilder) {
			woodRegistry.generateLang(translationBuilder, lang);
		}
	}

	private static class WoodTypeRecipeProvider extends FabricRecipeProvider {
		private WoodTypeRecipeProvider(FabricDataOutput dataGenerator) {
			super(dataGenerator);
		}

		@Override
		public void buildRecipes(RecipeOutput exporter) {
			generateWoodTypeRecipes(exporter, VampireLib.TEST_WOOD);
			generateWoodTypeRecipes(exporter, VampireLib.TEST_WOOD1);
			generateWoodTypeRecipes(exporter, VampireLib.TEST_WOOD2);
			generateWoodTypeRecipes(exporter, VampireLib.TEST_WOOD3);
			generateWoodTypeRecipes(exporter, VampireLib.TEST_WOOD4);
			generateWoodTypeRecipes(exporter, VampireLib.TEST_WOOD5);
			generateWoodTypeRecipes(exporter, VampireLib.TEST_WOOD6);
			generateWoodTypeRecipes(exporter, VampireLib.TEST_WOOD7);
			generateWoodTypeRecipes(exporter, VampireLib.TEST_WOOD8);
			generateWoodTypeRecipes(exporter, VampireLib.TEST_WOOD9);
			generateWoodTypeRecipes(exporter, VampireLib.TEST_WOOD10);
			generateWoodTypeRecipes(exporter, VampireLib.TEST_WOOD11);
			generateWoodTypeRecipes(exporter, VampireLib.TEST_WOOD12);
			generateWoodTypeRecipes(exporter, VampireLib.TEST_WOOD13);
			generateWoodTypeRecipes(exporter, VampireLib.TEST_WOOD14);
			generateWoodTypeRecipes(exporter, VampireLib.TEST_WOOD15);
			generateWoodTypeRecipes(exporter, VampireLib.TEST_WOOD16);

			generateWoodTypeRecipes(exporter, VampireLib.TEST_NETHER_WOOD);
			generateWoodTypeRecipes(exporter, VampireLib.TEST_NETHER_WOOD1);
			generateWoodTypeRecipes(exporter, VampireLib.TEST_NETHER_WOOD2);
			generateWoodTypeRecipes(exporter, VampireLib.TEST_NETHER_WOOD3);
			generateWoodTypeRecipes(exporter, VampireLib.TEST_NETHER_WOOD4);
			generateWoodTypeRecipes(exporter, VampireLib.TEST_NETHER_WOOD5);
			generateWoodTypeRecipes(exporter, VampireLib.TEST_NETHER_WOOD6);
			generateWoodTypeRecipes(exporter, VampireLib.TEST_NETHER_WOOD7);
			generateWoodTypeRecipes(exporter, VampireLib.TEST_NETHER_WOOD8);
			generateWoodTypeRecipes(exporter, VampireLib.TEST_NETHER_WOOD9);
			generateWoodTypeRecipes(exporter, VampireLib.TEST_NETHER_WOOD10);
			generateWoodTypeRecipes(exporter, VampireLib.TEST_NETHER_WOOD11);
			generateWoodTypeRecipes(exporter, VampireLib.TEST_NETHER_WOOD12);
			generateWoodTypeRecipes(exporter, VampireLib.TEST_NETHER_WOOD13);
		}

		private void generateWoodTypeRecipes(RecipeOutput exporter, WoodRegistry woodRegistry) {
			woodRegistry.generateRecipes(exporter);
		}
	}

	private static class WoodTypeBlockTagProvider extends CustomTagProviders.CustomBlockTagProvider {
		private WoodTypeBlockTagProvider(FabricDataOutput dataGenerator, CompletableFuture<HolderLookup.Provider> completableFuture) {
			super(dataGenerator, completableFuture);
		}

		@Override
		protected void addTags(HolderLookup.Provider provider) {
			this.generateWoodTypeBlockTags(VampireLib.TEST_WOOD);
			this.generateWoodTypeBlockTags(VampireLib.TEST_WOOD1);
			this.generateWoodTypeBlockTags(VampireLib.TEST_WOOD2);
			this.generateWoodTypeBlockTags(VampireLib.TEST_WOOD3);
			this.generateWoodTypeBlockTags(VampireLib.TEST_WOOD4);
			this.generateWoodTypeBlockTags(VampireLib.TEST_WOOD5);
			this.generateWoodTypeBlockTags(VampireLib.TEST_WOOD6);
			this.generateWoodTypeBlockTags(VampireLib.TEST_WOOD7);
			this.generateWoodTypeBlockTags(VampireLib.TEST_WOOD8);
			this.generateWoodTypeBlockTags(VampireLib.TEST_WOOD9);
			this.generateWoodTypeBlockTags(VampireLib.TEST_WOOD10);
			this.generateWoodTypeBlockTags(VampireLib.TEST_WOOD11);
			this.generateWoodTypeBlockTags(VampireLib.TEST_WOOD12);
			this.generateWoodTypeBlockTags(VampireLib.TEST_WOOD13);
			this.generateWoodTypeBlockTags(VampireLib.TEST_WOOD14);
			this.generateWoodTypeBlockTags(VampireLib.TEST_WOOD15);
			this.generateWoodTypeBlockTags(VampireLib.TEST_WOOD16);

			this.generateWoodTypeBlockTags(VampireLib.TEST_NETHER_WOOD);
			this.generateWoodTypeBlockTags(VampireLib.TEST_NETHER_WOOD1);
			this.generateWoodTypeBlockTags(VampireLib.TEST_NETHER_WOOD2);
			this.generateWoodTypeBlockTags(VampireLib.TEST_NETHER_WOOD3);
			this.generateWoodTypeBlockTags(VampireLib.TEST_NETHER_WOOD4);
			this.generateWoodTypeBlockTags(VampireLib.TEST_NETHER_WOOD5);
			this.generateWoodTypeBlockTags(VampireLib.TEST_NETHER_WOOD6);
			this.generateWoodTypeBlockTags(VampireLib.TEST_NETHER_WOOD7);
			this.generateWoodTypeBlockTags(VampireLib.TEST_NETHER_WOOD8);
			this.generateWoodTypeBlockTags(VampireLib.TEST_NETHER_WOOD9);
			this.generateWoodTypeBlockTags(VampireLib.TEST_NETHER_WOOD10);
			this.generateWoodTypeBlockTags(VampireLib.TEST_NETHER_WOOD11);
			this.generateWoodTypeBlockTags(VampireLib.TEST_NETHER_WOOD12);
			this.generateWoodTypeBlockTags(VampireLib.TEST_NETHER_WOOD13);
		}

		private void generateWoodTypeBlockTags(WoodRegistry woodRegistry) {
			woodRegistry.generateBlockTags(this);
		}
	}

	private static class WoodTypeItemTagProvider extends CustomTagProviders.CustomItemTagProvider {
		private WoodTypeItemTagProvider(FabricDataOutput dataGenerator, CustomBlockTagProvider blockTagProvider, CompletableFuture<HolderLookup.Provider> registriesFuture) {
			super(dataGenerator, registriesFuture, blockTagProvider);
		}

		@Override
		protected void addTags(HolderLookup.Provider arg) {
			this.generateWoodTypeItemTags(VampireLib.TEST_WOOD);
			this.generateWoodTypeItemTags(VampireLib.TEST_WOOD1);
			this.generateWoodTypeItemTags(VampireLib.TEST_WOOD2);
			this.generateWoodTypeItemTags(VampireLib.TEST_WOOD3);
			this.generateWoodTypeItemTags(VampireLib.TEST_WOOD4);
			this.generateWoodTypeItemTags(VampireLib.TEST_WOOD5);
			this.generateWoodTypeItemTags(VampireLib.TEST_WOOD6);
			this.generateWoodTypeItemTags(VampireLib.TEST_WOOD7);
			this.generateWoodTypeItemTags(VampireLib.TEST_WOOD8);
			this.generateWoodTypeItemTags(VampireLib.TEST_WOOD9);
			this.generateWoodTypeItemTags(VampireLib.TEST_WOOD10);
			this.generateWoodTypeItemTags(VampireLib.TEST_WOOD11);
			this.generateWoodTypeItemTags(VampireLib.TEST_WOOD12);
			this.generateWoodTypeItemTags(VampireLib.TEST_WOOD13);
			this.generateWoodTypeItemTags(VampireLib.TEST_WOOD14);
			this.generateWoodTypeItemTags(VampireLib.TEST_WOOD15);
			this.generateWoodTypeItemTags(VampireLib.TEST_WOOD16);

			this.generateWoodTypeItemTags(VampireLib.TEST_NETHER_WOOD);
			this.generateWoodTypeItemTags(VampireLib.TEST_NETHER_WOOD1);
			this.generateWoodTypeItemTags(VampireLib.TEST_NETHER_WOOD2);
			this.generateWoodTypeItemTags(VampireLib.TEST_NETHER_WOOD3);
			this.generateWoodTypeItemTags(VampireLib.TEST_NETHER_WOOD4);
			this.generateWoodTypeItemTags(VampireLib.TEST_NETHER_WOOD5);
			this.generateWoodTypeItemTags(VampireLib.TEST_NETHER_WOOD6);
			this.generateWoodTypeItemTags(VampireLib.TEST_NETHER_WOOD7);
			this.generateWoodTypeItemTags(VampireLib.TEST_NETHER_WOOD8);
			this.generateWoodTypeItemTags(VampireLib.TEST_NETHER_WOOD9);
			this.generateWoodTypeItemTags(VampireLib.TEST_NETHER_WOOD10);
			this.generateWoodTypeItemTags(VampireLib.TEST_NETHER_WOOD11);
			this.generateWoodTypeItemTags(VampireLib.TEST_NETHER_WOOD12);
			this.generateWoodTypeItemTags(VampireLib.TEST_NETHER_WOOD13);
		}

		private void generateWoodTypeItemTags(WoodRegistry woodRegistry) {
			woodRegistry.generateItemTags(this);
		}
	}

	private static class WoodTypeBlockLootTableProvider extends FabricBlockLootTableProvider {

		protected WoodTypeBlockLootTableProvider(FabricDataOutput dataOutput) {
			super(dataOutput);
		}

		@Override
		public void generate() {
			this.generateWoodTypeLoot(VampireLib.TEST_WOOD);
			this.generateWoodTypeLoot(VampireLib.TEST_WOOD1);
			this.generateWoodTypeLoot(VampireLib.TEST_WOOD2);
			this.generateWoodTypeLoot(VampireLib.TEST_WOOD3);
			this.generateWoodTypeLoot(VampireLib.TEST_WOOD4);
			this.generateWoodTypeLoot(VampireLib.TEST_WOOD5);
			this.generateWoodTypeLoot(VampireLib.TEST_WOOD6);
			this.generateWoodTypeLoot(VampireLib.TEST_WOOD7);
			this.generateWoodTypeLoot(VampireLib.TEST_WOOD8);
			this.generateWoodTypeLoot(VampireLib.TEST_WOOD9);
			this.generateWoodTypeLoot(VampireLib.TEST_WOOD10);
			this.generateWoodTypeLoot(VampireLib.TEST_WOOD11);
			this.generateWoodTypeLoot(VampireLib.TEST_WOOD12);
			this.generateWoodTypeLoot(VampireLib.TEST_WOOD13);
			this.generateWoodTypeLoot(VampireLib.TEST_WOOD14);
			this.generateWoodTypeLoot(VampireLib.TEST_WOOD15);
			this.generateWoodTypeLoot(VampireLib.TEST_WOOD16);

			this.generateWoodTypeLoot(VampireLib.TEST_NETHER_WOOD);
			this.generateWoodTypeLoot(VampireLib.TEST_NETHER_WOOD1);
			this.generateWoodTypeLoot(VampireLib.TEST_NETHER_WOOD2);
			this.generateWoodTypeLoot(VampireLib.TEST_NETHER_WOOD3);
			this.generateWoodTypeLoot(VampireLib.TEST_NETHER_WOOD4);
			this.generateWoodTypeLoot(VampireLib.TEST_NETHER_WOOD5);
			this.generateWoodTypeLoot(VampireLib.TEST_NETHER_WOOD6);
			this.generateWoodTypeLoot(VampireLib.TEST_NETHER_WOOD7);
			this.generateWoodTypeLoot(VampireLib.TEST_NETHER_WOOD8);
			this.generateWoodTypeLoot(VampireLib.TEST_NETHER_WOOD9);
			this.generateWoodTypeLoot(VampireLib.TEST_NETHER_WOOD10);
			this.generateWoodTypeLoot(VampireLib.TEST_NETHER_WOOD11);
			this.generateWoodTypeLoot(VampireLib.TEST_NETHER_WOOD12);
			this.generateWoodTypeLoot(VampireLib.TEST_NETHER_WOOD13);
		}

		private void generateWoodTypeLoot(WoodRegistry woodRegistry) {
			woodRegistry.generateLoot(this);
		}

	}

	//VampireLib generators
	private static class VBlockTagsProvider extends CustomTagProviders.CustomBlockTagProvider {
		private VBlockTagsProvider(FabricDataOutput dataGenerator, CompletableFuture<HolderLookup.Provider> completableFuture) {
			super(dataGenerator, completableFuture);
		}

		@Override
		protected void addTags(HolderLookup.Provider arg) {}
	}

	private static class VItemTagsProvider extends CustomTagProviders.CustomItemTagProvider {
		private VItemTagsProvider(FabricDataOutput dataGenerator, CompletableFuture<HolderLookup.Provider> completableFuture, CustomBlockTagProvider blockTagProvider) {
			super(dataGenerator, completableFuture, blockTagProvider);
		}

		@Override
		protected void addTags(HolderLookup.Provider arg) {
			copyCobblestoneTags();
		}

		private void copyCobblestoneTags() {
			getOrCreateTagBuilder(ItemTags.STONE_CRAFTING_MATERIALS)
					.add(Items.ANDESITE, Items.DIORITE, Items.GRANITE);
			getOrCreateTagBuilder(ItemTags.STONE_TOOL_MATERIALS)
					.addTag(ItemTags.STONE_CRAFTING_MATERIALS);
		}
	}

}
