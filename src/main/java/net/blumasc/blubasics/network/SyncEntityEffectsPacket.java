package net.blumasc.blubasics.network;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import net.blumasc.blubasics.client.ClientEffectData;

import java.util.HashSet;
import java.util.List;

public record SyncEntityEffectsPacket(
        int entityId,
        List<ResourceLocation> effects
) implements CustomPacketPayload {

    public static final Type<SyncEntityEffectsPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("blubasics", "sync_effects"));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncEntityEffectsPacket> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT, SyncEntityEffectsPacket::entityId,
                    ByteBufCodecs.collection(java.util.ArrayList::new, ResourceLocation.STREAM_CODEC),
                    SyncEntityEffectsPacket::effects,
                    SyncEntityEffectsPacket::new
            );

    public static void handle(SyncEntityEffectsPacket msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            var level = Minecraft.getInstance().level;
            if (level == null) return;

            Entity entity = level.getEntity(msg.entityId());
            if (entity == null) return;

            ClientEffectData.setEffects(entity.getUUID(), new HashSet<>(msg.effects()));
        });
    }
}