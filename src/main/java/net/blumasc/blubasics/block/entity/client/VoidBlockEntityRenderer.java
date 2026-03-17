package net.blumasc.blubasics.block.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.blumasc.blubasics.BluBasicsMod;
import net.blumasc.blubasics.block.entity.VoidBlockEntity;
import net.blumasc.blubasics.datagen.ModItemTagProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class VoidBlockEntityRenderer implements BlockEntityRenderer<VoidBlockEntity> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(BluBasicsMod.MODID, "textures/block/void_block.png");

    public VoidBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(VoidBlockEntity entity,  float partialTick,
                       PoseStack poseStack,
                       MultiBufferSource buffer,
                       int light,
                       int overlay) {

        var camera = Minecraft.getInstance().gameRenderer.getMainCamera();

        poseStack.pushPose();

        poseStack.translate(0.5, 0.5, 0.5);

        poseStack.mulPose(camera.rotation());

        VertexConsumer consumer = buffer.getBuffer(
                RenderType.entityTranslucent(TEXTURE)
        );


        PoseStack.Pose pose = poseStack.last();

        float r = 1f, g = 1f, b = 1f;

        consumer.addVertex(pose, -0.5F, -0.5F, 0.0F)
                .setColor((int)(r*255), (int)(g*255), (int)(b*255), 255)
                .setUv(0.0F, 1.0F)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(pose, 0.0F, 0.0F, -1.0F);

        consumer.addVertex(pose, 0.5F, -0.5F, 0.0F)
                .setColor((int)(r*255), (int)(g*255), (int)(b*255), 255)
                .setUv(1.0F, 1.0F)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(pose, 0.0F, 0.0F, -1.0F);

        consumer.addVertex(pose, 0.5F, 0.5F, 0.0F)
                .setColor((int)(r*255), (int)(g*255), (int)(b*255), 255)
                .setUv(1.0F, 0.0F)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(pose, 0.0F, 0.0F, -1.0F);

        consumer.addVertex(pose, -0.5F, 0.5F, 0.0F)
                .setColor((int)(r*255), (int)(g*255), (int)(b*255), 255)
                .setUv(0.0F, 0.0F)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(pose, 0.0F, 0.0F, -1.0F);

        poseStack.popPose();
    }
}