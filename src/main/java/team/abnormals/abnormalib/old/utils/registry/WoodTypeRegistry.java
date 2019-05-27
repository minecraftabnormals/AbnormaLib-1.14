package team.abnormals.abnormalib.old.utils.registry;

import net.minecraft.util.StringIdentifiable;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class WoodTypeRegistry implements StringIdentifiable {
    public static ArrayList<WoodType> woodTypes = new ArrayList<>();

    private static Queue<ModdedTypeListener> listeners = new ConcurrentLinkedQueue<>();

    static WoodType registerVanilla(WoodType woodType) {
        woodTypes.add(woodType);
        return woodType;
    }

    public static WoodType registerModded(WoodType woodType, float hardness, float resistance) {
        registerVanilla(woodType);

        listeners.forEach(listener -> listener.onModdedWoodTypeRegistered(woodType, hardness, resistance));
        return woodType;
    }

    public static void registerModdedTypeListener(ModdedTypeListener listener) {
        listeners.add(listener);
    }

    public interface ModdedTypeListener {
        void onModdedWoodTypeRegistered(WoodType woodType, float hardness, float resistance);
    }
}