package net.blumasc.blubasics.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.blumasc.blubasics.BluBasicsMod;
import net.blumasc.blubasics.entity.custom.projectile.ShardProjectileEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

public class ShardProjectileRenderer
        extends EntityRenderer<ShardProjectileEntity> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(BluBasicsMod.MODID, "textures/entity/shard/texture.png");

    public ShardProjectileRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(
            ShardProjectileEntity entity,
            float entityYaw,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight
    ) {
        poseStack.pushPose();

        poseStack.mulPose(Axis.YP.rotationDegrees(
                Mth.lerp(partialTick, entity.yRotO, entity.getYRot()) - 90.0F
        ));
        poseStack.mulPose(Axis.ZP.rotationDegrees(
                Mth.lerp(partialTick, entity.xRotO, entity.getXRot())
        ));

        poseStack.scale(0.5F, 0.5F, 0.5F);

        int color = entity.getColor();
        float r = ((color >> 16) & 255) / 255.0F;
        float g = ((color >> 8) & 255) / 255.0F;
        float b = (color & 255) / 255.0F;

        VertexConsumer vc = buffer.getBuffer(
                RenderType.entityCutoutNoCull(TEXTURE)
        );

        PoseStack.Pose pose = poseStack.last();

        renderQuad(pose, vc, r, g, b, packedLight);

        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        pose = poseStack.last();
        renderQuad(pose, vc, r, g, b, packedLight);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }

    private static void renderQuad(
            PoseStack.Pose pose,
            VertexConsumer vc,
            float r, float g, float b,
            int light
    ) {
        float halfWidth = 6.0F / 16.0F;
        float halfHeight = 2.5F / 16.0F;

        Matrix4f mat = pose.pose();

        vc.addVertex(mat, -halfWidth, -halfHeight, 0.0F)
                .setColor(r, g, b, 1.0F)
                .setUv(0.0F, 1.0F)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setUv2(light & 0xFFFF, light >> 16 & 0xFFFF)
                .setNormal(pose, 0, 0, 1);

        vc.addVertex(mat, halfWidth, -halfHeight, 0.0F)
                .setColor(r, g, b, 1.0F)
                .setUv(1.0F, 1.0F)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setUv2(light & 0xFFFF, light >> 16 & 0xFFFF)
                .setNormal(pose, 0, 0, 1);

        vc.addVertex(mat, halfWidth, halfHeight, 0.0F)
                .setColor(r, g, b, 1.0F)
                .setUv(1.0F, 0.0F)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setUv2(light & 0xFFFF, light >> 16 & 0xFFFF)
                .setNormal(pose, 0, 0, 1);

        vc.addVertex(mat, -halfWidth, halfHeight, 0.0F)
                .setColor(r, g, b, 1.0F)
                .setUv(0.0F, 0.0F)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setUv2(light & 0xFFFF, light >> 16 & 0xFFFF)
                .setNormal(pose, 0, 0, 1);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull ShardProjectileEntity entity) {
        return TEXTURE;
    }
}
