package net.blumasc.blubasics.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.blumasc.blubasics.entity.custom.projectile.SpikeEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DripstoneThickness;

public class SpikeEntityRenderer extends EntityRenderer<SpikeEntity> {

    private final BlockRenderDispatcher blockRenderer;

    public SpikeEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.blockRenderer = context.getBlockRenderDispatcher();
    }

    @Override
    public void render(
            SpikeEntity entity,
            float entityYaw,
            float partialTicks,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight
    ) {
        float height = entity.getCurrentHeight();

        int maxHeight = entity.getMaxHeight();
        if (maxHeight <= 0) return;
        float topOffset = height - maxHeight;

        poseStack.pushPose();
        poseStack.translate(-0.5, topOffset+1, -0.5);

        int lightX = entity.getBlockX();
        int lightY = entity.getBlockY() + 1;
        int lightZ = entity.getBlockZ();

        int blockLight = entity.level().getBrightness(LightLayer.BLOCK, new BlockPos(lightX, lightY, lightZ));
        int skyLight = entity.level().getBrightness(LightLayer.SKY, new BlockPos(lightX, lightY, lightZ));
        int lightLevel = (skyLight << 20) | (blockLight << 4);
        for (int i = 0; i < maxHeight; i++) {
            DripstoneThickness thickness = getThickness(i + 1, maxHeight);
            renderDripstoneBlock(poseStack, buffer, lightLevel, thickness);
            poseStack.translate(0, 1, 0);
        }

        poseStack.popPose();

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    private void renderDripstoneBlock(PoseStack poseStack, MultiBufferSource buffer, int light, DripstoneThickness thickness) {
        BlockState state = Blocks.POINTED_DRIPSTONE.defaultBlockState()
                .setValue(PointedDripstoneBlock.TIP_DIRECTION, Direction.UP)
                .setValue(PointedDripstoneBlock.THICKNESS, thickness);

        blockRenderer.renderSingleBlock(state, poseStack, buffer, light, OverlayTexture.NO_OVERLAY);
    }

    private DripstoneThickness getThickness(int yIndex, int maxHeight) {
        if (yIndex == maxHeight) return DripstoneThickness.TIP;
        if (yIndex == maxHeight - 1) return DripstoneThickness.FRUSTUM;
        if (yIndex == 1 && maxHeight >= 3) return DripstoneThickness.BASE;
        return DripstoneThickness.MIDDLE;
    }

    @Override
    public boolean shouldRender(SpikeEntity entity, net.minecraft.client.renderer.culling.Frustum frustum, double x, double y, double z) {
        return true;
    }

    @Override
    public net.minecraft.resources.ResourceLocation getTextureLocation(SpikeEntity entity) {
        return null;
    }
}
