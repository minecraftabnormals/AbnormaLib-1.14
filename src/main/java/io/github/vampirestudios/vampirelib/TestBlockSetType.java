package io.github.vampirestudios.vampirelib;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockSetType;

import net.fabricmc.fabric.api.object.builder.v1.block.type.BlockSetTypeBuilder;

public class TestBlockSetType {

    public static final BlockSetType TEST_NETHER_WOOD_1 = registerBlockSetType("test_nether_wood_1",
            SoundType.NETHER_WOOD,
            SoundEvents.NETHER_WOOD_DOOR_CLOSE,
            SoundEvents.NETHER_WOOD_DOOR_OPEN,
            SoundEvents.NETHER_WOOD_TRAPDOOR_CLOSE,
            SoundEvents.NETHER_WOOD_TRAPDOOR_OPEN,
            SoundEvents.NETHER_WOOD_PRESSURE_PLATE_CLICK_OFF,
            SoundEvents.NETHER_WOOD_PRESSURE_PLATE_CLICK_ON,
            SoundEvents.NETHER_WOOD_BUTTON_CLICK_OFF,
            SoundEvents.NETHER_WOOD_BUTTON_CLICK_ON
    );

    private static BlockSetType registerBlockSetType(String name, SoundType soundType, SoundEvent doorClose, SoundEvent doorOpen, SoundEvent trapdoorClose, SoundEvent trapdoorOpen, SoundEvent pressurePlateClickOff, SoundEvent pressurePlateClickOn, SoundEvent buttonClickOff, SoundEvent buttonClickOn) {
        return BlockSetTypeBuilder.copyOf(BlockSetType.CRIMSON)
			.soundGroup(soundType)
			.doorCloseSound(doorClose)
			.doorOpenSound(doorOpen)
			.trapdoorCloseSound(trapdoorClose)
			.trapdoorOpenSound(trapdoorOpen)
			.pressurePlateClickOffSound(pressurePlateClickOff)
			.pressurePlateClickOnSound(pressurePlateClickOn)
			.buttonClickOffSound(buttonClickOff)
			.buttonClickOnSound(buttonClickOn)
			.register(ResourceLocation.fromNamespaceAndPath(VampireLib.INSTANCE.modId(), name));
    }
}
