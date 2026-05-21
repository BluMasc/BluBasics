package net.blumasc.blubasics.client;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ClientEffectData {

    private static final Map<UUID, Set<String>> EFFECTS = new HashMap<>();

    public static void setEffects(UUID id, Collection<ResourceLocation> effects) {
        Set<String> normalized = effects.stream()
                .map(ResourceLocation::toString)
                .collect(Collectors.toSet());

        EFFECTS.put(id, Set.copyOf(normalized));
    }

    public static boolean hasEffect(UUID id, ResourceLocation effect) {
        Set<String> set = EFFECTS.get(id);
        if (set == null) return false;

        return set.contains(effect.toString());
    }

    public static void clear(UUID id) {
        EFFECTS.remove(id);
    }
}
