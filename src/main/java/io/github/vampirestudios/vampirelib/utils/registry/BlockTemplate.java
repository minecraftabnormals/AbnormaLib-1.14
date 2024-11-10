package io.github.vampirestudios.vampirelib.utils.registry;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * BlockTemplate
 */
public interface BlockTemplate {

    public static BlockTemplate of(Block block) {
        return () -> BlockBehaviour.Properties.ofLegacyCopy(block);
    }

    public static BlockTemplate of(BlockBehaviour.Properties properties) {
        return () -> properties;
    }

    BlockTemplate EMPTY = BlockBehaviour.Properties::of;

    BlockBehaviour.Properties getProperties();

}
