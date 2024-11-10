package io.github.vampirestudios.vampirelib.utils.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import com.mojang.datafixers.util.Pair;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.properties.WoodType;

/**
 * WoodSet
 */
public class WoodSet {

    private Map<WoodBlockType, Block> blocks;
    private Map<WoodBlockType, Item> items;

    WoodSet(Map<WoodBlockType, Block> blocks, Map<WoodBlockType, Item> items) {
        this.blocks = blocks;
        this.items = items;
    }

    public List<Block> getBlockSuppliers() {
        return List.copyOf(blocks.values());
    }

    public Optional<Block> getBlockSafe(WoodBlockType type) {
        return Optional.ofNullable(blocks.get(type));
    }

    public Block getBlock(WoodBlockType type) {
        return getBlockSafe(type).orElseThrow();
    }

    public List<Item> getItemSuppliers() {
        return List.copyOf(items.values());
    }

    public Optional<Item> getItemSafe(WoodBlockType type) {
        return Optional.ofNullable(items.get(type));
    }

    public static Builder overworldBuilder(String name, WoodType woodType) {
        return Builder.forWood(name, woodType)
               .addTypes(WoodBlockType.DEFAULT)
               .addTypes(WoodBlockType.LOGS)
               .noItem(
                   WoodBlockType.SIGN,
                   WoodBlockType.WALL_SIGN,
                   WoodBlockType.HANGING_SIGN,
                   WoodBlockType.WALL_HANGING_SIGN
               );
    }

    public static Builder netherBuilder(String name, WoodType woodType) {
        return Builder.forWood(name, woodType)
               .addTypes(WoodBlockType.DEFAULT)
               .addTypes(WoodBlockType.STEMS)
               .noItem(
                   WoodBlockType.SIGN,
                   WoodBlockType.WALL_SIGN,
                   WoodBlockType.HANGING_SIGN,
                   WoodBlockType.WALL_HANGING_SIGN
               );
    }

    public static class Builder {

        private String name;

        private final List<WoodBlockType> types = new ArrayList<>();
        private final Map<WoodBlockType, BlockTemplate> templates = new HashMap<>();
        private final Map<WoodBlockType, Function<String, String>> nameModifiers = new HashMap<>();
        private final Map<WoodBlockType, Consumer<BlockBehaviour.Properties>> propertiesModifiers = new HashMap<>();
        private final Set<WoodBlockType> noItem = new HashSet<>();

        private WoodType woodType;
		private RegistryHelper registryHelper;

        private Consumer<Properties> propertiesModifier = p -> {};

        public Builder(String name, WoodType type) {
            this.name = name;
            this.woodType = type;
        }

        public static Builder forWood(String name, WoodType type) {
            return new Builder(name, type);
        }

		public Builder modId(String modId) {
			this.registryHelper = new RegistryHelper(modId);
			return this;
		}

        public Builder addTypes(List<Pair<WoodBlockType, BlockTemplate>> types) {
            types.forEach(type -> {
                this.types.add(type.getFirst());
                this.templates.put(type.getFirst(), type.getSecond());
            });
            return this;
        }

        public Builder removeTypes(WoodBlockType... types) {
            for (WoodBlockType type : types) {
                this.types.remove(type);
            }
            return this;
        }

        public Builder withNameModifier(WoodBlockType type, Function<String, String> modifier) {
            nameModifiers.put(type, modifier);
            return this;
        }

        public Builder withPropertiesModifier(WoodBlockType type, Consumer<BlockBehaviour.Properties> modifier) {
            propertiesModifiers.put(type, modifier);
            return this;
        }

        public Builder withPropertiesModifier(Consumer<BlockBehaviour.Properties> modifier) {
            this.propertiesModifier = modifier;
            return this;
        }

        public Builder noItem(WoodBlockType... types) {
            for (WoodBlockType type : types) {
                noItem.add(type);
            }
            return this;
        }

        public WoodSet build() {
            if (this.types.contains(WoodBlockType.STAIRS) && !this.types.contains(WoodBlockType.PLANKS)) {
                throw new RuntimeException(new IllegalStateException("To register Stairs, Planks need to be registered too!"));
            }
            Map<WoodBlockType, Block> blocks = new LinkedHashMap<>();
            Map<WoodBlockType, Item> items = new LinkedHashMap<>();
            WoodSet woodSet = new WoodSet(blocks, items);
            types.forEach(type -> {
                String id = nameModifiers.getOrDefault(type, s -> s).apply(type.getName(name));

				BlockBehaviour.Properties properties = templates.get(type).getProperties();
				propertiesModifier.accept(properties);
				propertiesModifiers.getOrDefault(type, t -> {}).accept(properties);
				Block block1 = type.make(properties.setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.parse(id))), woodType, woodSet.getBlockSafe(WoodBlockType.PLANKS));
                Block block = registryHelper.blocks().registerBlockWithoutItem(id, block1);
                if (!noItem.contains(type)) {
                    items.put(type, registryHelper.items().registerItem(id, new BlockItem(block, new Item.Properties().setId(ResourceKey.create(Registries.ITEM, ResourceLocation.parse(id))))));
                }
                blocks.put(type, block);
            });
            return woodSet;
        }

    }

}
