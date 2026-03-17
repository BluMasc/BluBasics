package net.blumasc.blubasics.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.blumasc.blubasics.entity.custom.helper.MeteorSphere;
import net.blumasc.blubasics.entity.custom.projectile.MeteoriteEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class MeteoriteRenderer extends EntityRenderer<MeteoriteEntity> {

    public MeteoriteRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(
            MeteoriteEntity entity,
            float yaw,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int light
    ) {
        poseStack.pushPose();
        poseStack.translate(0, entity.getRadius(), 0);

        poseStack.mulPose(Axis.XP.rotationDegrees(entity.rotationX));
        poseStack.mulPose(Axis.YP.rotationDegrees(entity.rotationY));
        poseStack.mulPose(Axis.ZP.rotationDegrees(entity.rotationZ));



        BlockRenderDispatcher dispatcher =
                Minecraft.getInstance().getBlockRenderer();

        for (BlockPos pos : MeteorSphere.shell(entity.getRadius())) {
            poseStack.pushPose();
            poseStack.translate(pos.getX(), pos.getY(), pos.getZ());

            BlockState state = entity.getRenderBlock();
            dispatcher.renderSingleBlock(
                    state,
                    poseStack,
                    buffer,
                    light,
                    OverlayTexture.NO_OVERLAY
            );

            poseStack.popPose();
        }

        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(MeteoriteEntity entity) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}