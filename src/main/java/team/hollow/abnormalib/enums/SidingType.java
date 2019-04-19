package team.hollow.abnormalib.enums;

import net.minecraft.util.StringRepresentable;

public enum SidingType implements StringRepresentable {
	SINGLE("single"),
	DOUBLE("double");

	private final String name;

	private SidingType(String name) {
		this.name = name;
	}

	public String toString() {
		return this.name;
	}

	public String asString() {
		return this.name;
	}
}