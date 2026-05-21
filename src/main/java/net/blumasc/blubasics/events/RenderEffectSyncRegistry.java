package net.blumasc.blubasics.events;

import net.minecraft.resources.ResourceLocation;

import java.util.HashSet;
import java.util.Set;

public class RenderEffectSyncRegistry {

    private static final Set<ResourceLocation> SYNCED_EFFECTS = new HashSet<>();

    public static void register(ResourceLocation effectId) {
        SYNCED_EFFECTS.add(effectId);
    }

    public static boolean isSynced(ResourceLocation effectId) {
        return SYNCED_EFFECTS.contains(effectId);
    }

    public static Set<ResourceLocation> all() {
        return SYNCED_EFFECTS;
    }
}