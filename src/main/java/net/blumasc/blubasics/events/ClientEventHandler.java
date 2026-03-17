package net.blumasc.blubasics.events;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.blumasc.blubasics.BluBasicsMod;
import net.blumasc.blubasics.block.BaseModBlocks;
import net.blumasc.blubasics.block.entity.client.extension.VoidClientExtension;
import net.blumasc.blubasics.effect.BaseModEffects;
import net.blumasc.blubasics.shader.VoidVisionRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

import java.io.IOException;

@EventBusSubscriber(modid = BluBasicsMod.MODID, value = Dist.CLIENT)
public class ClientEventHandler {
    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerBlock(
                new VoidClientExtension(),
                BaseModBlocks.VOID_BLOCK.get()
        );
    }
    @SubscribeEvent
    public static void onRenderRainbow(RenderGuiEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
            if (mc.player.hasEffect(BaseModEffects.VOID_EFFECT)) {
                VoidVisionRenderer.apply(event.getPartialTick().getGameTimeDeltaTicks());
                mc.getMainRenderTarget().bindWrite(true);
            }
    }
    @SubscribeEvent
    public static void onRegisterReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener((ResourceManagerReloadListener) rm -> VoidVisionRenderer.load());
    }
}
