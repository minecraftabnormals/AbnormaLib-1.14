package io.github.vampirestudios.vampirelib.api;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix3f;
import com.mojang.math.Transformation;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;

import net.minecraft.core.Direction;

public interface TransformationExtensions {
	/**
	 * Apply this transformation to a different origin.
	 * Can be used for switching between coordinate systems.
	 * Parameter is relative to the current origin.
	 */
	default Transformation applyOrigin(Vector3f origin) {
		throw new RuntimeException("this should be overridden via mixin. what?");
	}

	default Matrix3f getNormalMatrix() {
		throw new RuntimeException("this should be overridden via mixin. what?");
	}

	default void push(PoseStack stack) {
		throw new RuntimeException("this should be overridden via mixin. what?");
	}

	default void transformPosition(Vector4f position) {
		throw new RuntimeException("this should be overridden via mixin. what?");
	}

	default Direction rotateTransform(Direction facing) {
		throw new RuntimeException("this should be overridden via mixin. what?");
	}

	default boolean isIdentity() {
		return this.equals(Transformation.identity());
	}

	default void transformNormal(Vector3f normal) {
		normal.transform(getNormalMatrix());
		normal.normalize();
	}

	/**
	 * convert transformation from assuming center-block system to opposing-corner-block system
	 */
	default Transformation blockCenterToCorner() {
		return applyOrigin(new Vector3f(.5f, .5f, .5f));
	}

	/**
	 * convert transformation from assuming opposing-corner-block system to center-block system
	 */
	default Transformation blockCornerToCenter() {
		return applyOrigin(new Vector3f(-.5f, -.5f, -.5f));
	}
}