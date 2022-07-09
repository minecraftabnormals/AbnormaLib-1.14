package io.github.vampirestudios.vampirelib;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.teamabnormals.blueprint.core.Blueprint;
import com.teamabnormals.blueprint.core.util.DataUtil;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.chunk.ChunkGenerator;

import io.github.vampirestudios.vampirelib.utils.modifications.ObjectModificationManager;
import io.github.vampirestudios.vampirelib.utils.modifications.ObjectModifier;
import io.github.vampirestudios.vampirelib.utils.modifications.ObjectModifierGroup;

/**
 * The data manager class for the {@link ChunkGeneratorModifier} system.
 *
 * @author SmellyModder (Luke Tonon)
 */
//TODO: Possibly rework this class in the future
public final class ChunkGeneratorModificationManager extends SimpleJsonResourceReloadListener {
	public static final String PATH = "dimension/chunk_generator";
	private final EnumMap<EventPriority, LinkedList<ObjectModifierGroup<ChunkGenerator, RegistryOps<JsonElement>, RegistryOps<JsonElement>>>> prioritizedModifiers = new EnumMap<>(EventPriority.class);
	private static ChunkGeneratorModificationManager INSTANCE;
	private final RegistryOps<JsonElement> registryOps;

	public ChunkGeneratorModificationManager(RegistryOps<JsonElement> registryOps) {
		super(new Gson(), ObjectModificationManager.MAIN_PATH + "/" + PATH);
		this.registryOps = registryOps;
	}

	static {
		for (EventPriority priority : EventPriority.values()) {
			MinecraftForge.EVENT_BUS.addListener((ServerAboutToStartEvent event) -> {
				if (INSTANCE == null) return;
				var prioritizedModifiers = INSTANCE.prioritizedModifiers;
				if (prioritizedModifiers == null) return;
				var modifierGroups = prioritizedModifiers.get(priority);
				if (modifierGroups == null) return;
				//Because dimensions don't exist till the server is ready to start, we must process the selectors right before the server starts
				var dimensions = event.getServer().getWorldData().worldGenSettings().dimensions();
				var keySet = dimensions.keySet();
				HashMap<ResourceLocation, LinkedList<ObjectModifier<ChunkGenerator, RegistryOps<JsonElement>, RegistryOps<JsonElement>, ?>>> assignedModifiers = new HashMap<>();
				for (var modifierGroup : modifierGroups) {
					modifierGroup.selector().select(keySet::forEach).forEach(location -> {
						assignedModifiers.computeIfAbsent(location, __ -> new LinkedList<>()).addAll(modifierGroup.modifiers());
					});
				}
				for (var entry : dimensions.entrySet()) {
					var modifiers = assignedModifiers.get(entry.getKey().location());
					if (modifiers == null) continue;
					ChunkGenerator chunkGenerator = entry.getValue().generator();
					modifiers.forEach(configured -> configured.modify(chunkGenerator));
				}
			});
		}
	}

	@SubscribeEvent
	public static void onReloadListener(AddReloadListenerEvent event) {
		try {
			event.addListener(INSTANCE = new ChunkGeneratorModificationManager(DataUtil.createRegistryOps(event.getServerResources())));
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
		RegistryOps<JsonElement> registryOps = this.registryOps;
		int groupsLoaded = 0;
		for (Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
			ResourceLocation location = entry.getKey();
			try {
				ObjectModifierGroup<ChunkGenerator, RegistryOps<JsonElement>, RegistryOps<JsonElement>> modifierGroup = ObjectModifierGroup.deserialize(location.toString(), entry.getValue().getAsJsonObject(), registryOps, ChunkGeneratorModifierSerializers.REGISTRY, true, true);
				this.prioritizedModifiers.computeIfAbsent(modifierGroup.priority(), __ -> new LinkedList<>()).add(modifierGroup);
				groupsLoaded++;
			} catch (IllegalArgumentException | JsonParseException exception) {
				VampireLib.INSTANCE.getLogger().error("Parsing error loading Chunk Generator Modifier Group: {}", location, exception);
			}
		}
		VampireLib.INSTANCE.getLogger().info("Chunk Generator Modification Manager has loaded {} modifier groups", groupsLoaded);
	}
}