package net.blumasc.blubasics.entity.client.chimera;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.blumasc.blubasics.entity.custom.ChimeraEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class ChimeraMouthItemLayer extends RenderLayer<ChimeraEntity, ChimeraModel<ChimeraEntity>> {

    private final ItemRenderer itemRenderer;

    public ChimeraMouthItemLayer(RenderLayerParent<ChimeraEntity, ChimeraModel<ChimeraEntity>> parent,
                                 ItemRenderer itemRenderer) {
        super(parent);
        this.itemRenderer = itemRenderer;
    }

    @Override
    public void render(PoseStack poseStack,
                       MultiBufferSource buffer,
                       int packedLight,
                       ChimeraEntity chimera,
                       float limbSwing,
                       float limbSwingAmount,
                       float partialTicks,
                       float ageInTicks,
                       float netHeadYaw,
                       float headPitch) {

        ItemStack item = chimera.getMainHandItem();
        if (item.isEmpty())
            return;

        poseStack.pushPose();

        ChimeraModel<?> model = this.getParentModel();

        model.root().translateAndRotate(poseStack);
        model.getBody().translateAndRotate(poseStack);
        model.getHead().translateAndRotate(poseStack);

        poseStack.translate(0.0F, 0.20F, -0.42F);

        poseStack.mulPose(Axis.XP.rotationDegrees(90f));
        poseStack.mulPose(Axis.ZP.rotationDegrees(180f));

        itemRenderer.renderStatic(
                item,
                ItemDisplayContext.GROUND,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                poseStack,
                buffer,
                chimera.level(),
                chimera.getId()
        );

        poseStack.popPose();
    }
}
