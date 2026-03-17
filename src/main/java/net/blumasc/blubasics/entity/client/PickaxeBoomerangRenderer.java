package net.blumasc.blubasics.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.blumasc.blubasics.entity.custom.projectile.PickaxeBoomerangEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class PickaxeBoomerangRenderer extends EntityRenderer<PickaxeBoomerangEntity> {

    private final ItemRenderer itemRenderer;

    public PickaxeBoomerangRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(PickaxeBoomerangEntity entity, float yaw, float partialTicks, PoseStack stack,
                       MultiBufferSource buffer, int packedLight) {

        stack.pushPose();

        float spin = (entity.tickCount + partialTicks) * 20f;
        stack.mulPose(Axis.YP.rotationDegrees(spin));

        stack.mulPose(Axis.XP.rotationDegrees(90));

        stack.scale(1.25f, 1.25f, 1.25f);

        ItemStack stackToRender = entity.getPickaxeStack();
        if (!stackToRender.isEmpty()) {
            itemRenderer.renderStatic(stackToRender, ItemDisplayContext.NONE , packedLight, OverlayTexture.NO_OVERLAY, stack, buffer, entity.level(), 0);
        }

        stack.popPose();

        super.render(entity, yaw, partialTicks, stack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(PickaxeBoomerangEntity pickaxeBoomerangEntity) {
        return null;
    }

    @Override
    public float getShadowRadius(PickaxeBoomerangEntity entity) {
        return 0.0f;
    }
}