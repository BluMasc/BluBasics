package net.blumasc.blubasics.events;

import net.blumasc.blubasics.BluBasicsMod;
import net.blumasc.blubasics.network.ModNetwork;
import net.blumasc.blubasics.network.SyncEntityEffectsPacket;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.*;

@EventBusSubscriber(modid = BluBasicsMod.MODID)
public class EntityEffectSyncManager {
    private static final Map<Integer, Set<ResourceLocation>> LAST_SENT = new HashMap<>();

    private static Set<ResourceLocation> collectEffects(LivingEntity entity) {
        Set<ResourceLocation> set = new HashSet<>();

        for (MobEffectInstance inst : entity.getActiveEffects()) {
            ResourceLocation id =
                    BuiltInRegistries.MOB_EFFECT.getKey(inst.getEffect().value());

            if (id != null && RenderEffectSyncRegistry.isSynced(id)) {
                set.add(id);
            }
        }

        return set;
    }

    private static void syncIfChanged(LivingEntity entity, ServerPlayer player) {
        int id = entity.getId();

        Set<ResourceLocation> current = collectEffects(entity);
        Set<ResourceLocation> last = LAST_SENT.get(id);

        if (current.equals(last)) return;

        LAST_SENT.put(id, new HashSet<>(current));

        PacketDistributor.sendToPlayer(player,
                new SyncEntityEffectsPacket(id, new ArrayList<>(current))
        );
    }

    private static void syncToAllTracking(LivingEntity entity) {
        if (!(entity.level() instanceof ServerLevel level)) return;

        int id = entity.getId();
        Set<ResourceLocation> current = collectEffects(entity);

        LAST_SENT.put(id, new HashSet<>(current));

        for (ServerPlayer player : level.getChunkSource().chunkMap.getPlayers(entity.chunkPosition(), false)) {
            PacketDistributor.sendToPlayer(player,
                    new SyncEntityEffectsPacket(id, new ArrayList<>(current))
            );
        }
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        if (!(event.getTarget() instanceof LivingEntity living)) return;
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        syncIfChanged(living, player);
    }

    @SubscribeEvent
    public static void onEffectAdded(MobEffectEvent.Added event) {
        if (!(event.getEntity() instanceof LivingEntity living)) return;

        syncToAllTracking(living);
    }

    @SubscribeEvent
    public static void onEffectRemoved(MobEffectEvent.Remove event) {
        if (!(event.getEntity() instanceof LivingEntity living)) return;

        syncToAllTracking(living);
    }

    @SubscribeEvent
    public static void onEntityUnload(net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent event) {
        if (event.getEntity() instanceof LivingEntity living) {
            LAST_SENT.remove(living.getId());
        }
    }

    private static int tickCounter = 0;
    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        tickCounter++;

        if (tickCounter % 20 != 0) return;

        for (ServerLevel level : event.getServer().getAllLevels()) {
            for (Entity e : level.getAllEntities()) {
                if (e instanceof LivingEntity entity) {
                    syncIfChangedGlobal(entity);
                }
            }
        }
    }
    private static void syncIfChangedGlobal(LivingEntity entity) {
        if (!(entity.level() instanceof ServerLevel level)) return;

        int id = entity.getId();
        Set<ResourceLocation> current = collectEffects(entity);

        Set<ResourceLocation> last = LAST_SENT.get(id);

        if (current.equals(last)) return;

        LAST_SENT.put(id, new HashSet<>(current));

        var packet = new SyncEntityEffectsPacket(id, new ArrayList<>(current));

        for (ServerPlayer player : level.players()) {
            PacketDistributor.sendToPlayer(player, packet);
        }
    }
}