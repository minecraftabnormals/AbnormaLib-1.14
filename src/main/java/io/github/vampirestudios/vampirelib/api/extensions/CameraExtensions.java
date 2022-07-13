package io.github.vampirestudios.vampirelib.api.extensions;

import net.minecraft.world.level.block.state.BlockState;

public interface CameraExtensions {
	default void setAnglesInternal(float yaw, float pitch) {
		throw new RuntimeException("this should be overridden via mixin. what?");
	}

	default BlockState getBlockAtCamera() {
		throw new RuntimeException("this should be overridden via mixin. what?");
	}
}