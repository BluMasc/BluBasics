package net.blumasc.blubasics.entity.client.chimera;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.blumasc.blubasics.BluBasicsMod;
import net.blumasc.blubasics.entity.custom.ChimeraEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class ChimeraRenderer extends MobRenderer<ChimeraEntity, ChimeraModel<ChimeraEntity>> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(BluBasicsMod.MODID,"textures/entity/chimera/texture.png");

    private static final ResourceLocation BEAM_TEXTURE =
            ResourceLocation.parse("textures/entity/guardian_beam.png");

    public ChimeraRenderer(EntityRendererProvider.Context context) {
        super(context, new ChimeraModel<>(context.bakeLayer(ChimeraModel.LAYER_LOCATION)), 0.4f);
        this.addLayer(new ChimeraMouthItemLayer(this, context.getItemRenderer()));
    }

    @Override
    public void render(
            ChimeraEntity chimera,
            float entityYaw,
            float partialTicks,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight
    ) {
        super.render(chimera, entityYaw, partialTicks, poseStack, buffer, packedLight);

        LivingEntity target = chimera.getActiveAttackTarget();
        if (target != null) {
            renderBeam(chimera, target, partialTicks, poseStack, buffer);
            if (chimera.getAttackAnimationScale(partialTicks) > 0.1F) {
                playBeamHum(chimera, target);
            }
        }
    }

    private void renderBeam(
            ChimeraEntity chimera,
            LivingEntity target,
            float partialTicks,
            PoseStack poseStack,
            MultiBufferSource buffer
    ) {
        float attackScale = chimera.getAttackAnimationScale(partialTicks);
        float beamProgress = attackScale * attackScale;

        double dx = target.getX() - chimera.getX();
        double dy = target.getEyeY() - (chimera.getEyeY()+0.5);
        double dz = target.getZ() - chimera.getZ();

        float length = (float)Math.sqrt(dx * dx + dy * dy + dz * dz);

        poseStack.pushPose();

        poseStack.translate(0.0D, chimera.getEyeHeight()+0.5, 0.0D);

        float yaw = (float)(Math.atan2(dx, dz));
        float pitch = (float)(-(Math.atan2(dy, Math.sqrt(dx * dx + dz * dz))));

        poseStack.mulPose(Axis.YP.rotation(yaw));
        poseStack.mulPose(Axis.XP.rotation(pitch));

        VertexConsumer vc = buffer.getBuffer(RenderType.entityCutoutNoCull(BEAM_TEXTURE));
        PoseStack.Pose pose = poseStack.last();

        float vStart = -beamProgress;
        float vEnd = length * 0.5F + vStart;

        renderBeamSegment(vc, pose, length, vStart, vEnd);

        poseStack.popPose();
    }



    private static void renderBeamSegment(
            VertexConsumer vc,
            PoseStack.Pose pose,
            float length,
            float vStart,
            float vEnd
    ) {
        float radius = 0.2F;
        int light = 15728880;

        for (int i = 0; i < 4; ++i) {
            float angle0 = (float)(Math.PI / 2F * i);
            float angle1 = (float)(Math.PI / 2F * (i + 1));

            float x0 = Mth.cos(angle0) * radius;
            float y0 = Mth.sin(angle0) * radius;
            float x1 = Mth.cos(angle1) * radius;
            float y1 = Mth.sin(angle1) * radius;

            vc.addVertex(pose.pose(), x0, y0, 0.0F)
                    .setColor(255, 255, 255, 255)
                    .setUv(0.0F, vStart)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setUv2(light, light)
                    .setNormal(pose, 0.0F, 0.0F, 1.0F);

            vc.addVertex(pose.pose(), x0, y0, length)
                    .setColor(255, 255, 255, 255)
                    .setUv(0.0F, vEnd)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setUv2(light, light)
                    .setNormal(pose, 0.0F, 0.0F, 1.0F);

            vc.addVertex(pose.pose(), x1, y1, length)
                    .setColor(255, 255, 255, 255)
                    .setUv(1.0F, vEnd)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setUv2(light, light)
                    .setNormal(pose, 0.0F, 0.0F, 1.0F);

            vc.addVertex(pose.pose(), x1, y1, 0.0F)
                    .setColor(255, 255, 255, 255)
                    .setUv(1.0F, vStart)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setUv2(light, light)
                    .setNormal(pose, 0.0F, 0.0F, 1.0F);
        }
    }

    private void playBeamHum(ChimeraEntity chimera, Entity target) {
        if (!chimera.level().isClientSide) return;

        if (chimera.tickCount % 20 == 0) {
            chimera.level().playLocalSound(
                    chimera.getX(),
                    chimera.getY(),
                    chimera.getZ(),
                    SoundEvents.GUARDIAN_ATTACK,
                    chimera.getSoundSource(),
                    0.6F,
                    1.2F,
                    false
            );
            chimera.level().playLocalSound(
                    target.getX(),
                    target.getY(),
                    target.getZ(),
                    SoundEvents.GUARDIAN_ATTACK,
                    chimera.getSoundSource(),
                    0.6F,
                    1.2F,
                    false
            );
        }
    }





    @Override
    public ResourceLocation getTextureLocation(ChimeraEntity chimeraEntity) {
        return TEXTURE;
    }
    public static ResourceLocation getStaticTextureLocation() {
        return TEXTURE;
    }
}

