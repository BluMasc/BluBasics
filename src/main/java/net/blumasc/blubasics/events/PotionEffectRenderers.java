package net.blumasc.blubasics.events;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.datafixers.util.Unit;
import net.blumasc.blubasics.BluBasicsMod;
import net.blumasc.blubasics.effect.BaseModEffects;
import net.blumasc.blubasics.effect.custom.helper.ClientHallucinationHandler;
import net.blumasc.blubasics.entity.client.fakeMob.FakeMob;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@EventBusSubscriber(modid = BluBasicsMod.MODID, value = Dist.CLIENT)
public class PotionEffectRenderers {
    private static final ResourceLocation HEART_OVERLAY =
            ResourceLocation.fromNamespaceAndPath(BluBasicsMod.MODID, "textures/misc/rage_icon.png");

    private static void renderCustomHearts(GuiGraphics gui, Player player) {

        Minecraft mc = Minecraft.getInstance();

        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();

        int left = width / 2 - 91;
        int top = height - 39;

        int hearts = (int) Math.ceil(player.getHealth()/2);

        for (int i = 0; i < hearts; i++) {
            int x = left + ((i%10)*8);
            int overlayX = x + 4;
            int overlayY = top - 1-((i/10)*10);
            gui.pose().pushPose();
            gui.pose().translate(0, 0, 200);
            gui.blit(HEART_OVERLAY , overlayX, overlayY, 0,0,5, 5, 5, 5);
            gui.pose().popPose();
        }
    }
    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_ENTITIES) return;

        Minecraft mc = Minecraft.getInstance();
        EntityRenderDispatcher dispatcher = mc.getEntityRenderDispatcher();
        MultiBufferSource.BufferSource buffer =
                mc.renderBuffers().bufferSource();

        Vec3 camPos = event.getCamera().getPosition();
        PoseStack poseStack = event.getPoseStack();

        for (FakeMob fake : ClientHallucinationHandler.getActiveMobs()) {

            Entity mob = fake.getEntity();

            double x = mob.getX() - camPos.x;
            double y = mob.getY() - camPos.y;
            double z = mob.getZ() - camPos.z;

            BlockPos mobPos = new BlockPos((int) mob.getX(), (int) mob.getY(), (int) mob.getZ());
            int blockLight = mob.level().getBrightness(LightLayer.BLOCK, mobPos);
            int skyLight   = mob.level().getBrightness(LightLayer.SKY, mobPos);
            int packedLight = LightTexture.pack(blockLight, skyLight);
            dispatcher.render(
                    mob,
                    x, y, z,
                    fake.getEntity().getYRot(),
                    event.getPartialTick().getGameTimeDeltaPartialTick(true),
                    poseStack,
                    buffer,
                    packedLight
            );
        }
    }
    @SubscribeEvent
    public static void onClientPlayerTick(PlayerTickEvent.Pre event) {
        if (!(event.getEntity() instanceof LocalPlayer player)) return;
        if (!player.level().isClientSide) return;

        ClientHallucinationHandler.tickPlayer(player);
    }

}
