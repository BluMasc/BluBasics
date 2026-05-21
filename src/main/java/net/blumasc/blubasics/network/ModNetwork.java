package net.blumasc.blubasics.network;

import net.blumasc.blubasics.BluBasicsMod;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

public class ModNetwork {

    public static void register(final RegisterPayloadHandlersEvent event) {

        event.registrar(BluBasicsMod.MODID).playToClient(
                SyncEntityEffectsPacket.TYPE,
                SyncEntityEffectsPacket.STREAM_CODEC,
                SyncEntityEffectsPacket::handle
        );
    }
}