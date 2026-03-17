package net.blumasc.blubasics.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.blumasc.blubasics.entity.custom.projectile.BlockProjectileEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class BlockProjectileEntityRenderer extends EntityRenderer<BlockProjectileEntity> {

    ItemRenderer itemRenderer;

    public BlockProjectileEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        itemRenderer = context.getItemRenderer();
    }

    @Override
    public ResourceLocation getTextureLocation(BlockProjectileEntity blockProjectileEntity) {
        return InventoryMenu.BLOCK_ATLAS;
    }

    @Override
    public void render(BlockProjectileEntity p_entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        ItemStack item = p_entity.getBlock().getBlock().asItem().getDefaultInstance();
        if (item.isEmpty())
            return;

        poseStack.pushPose();

        poseStack.mulPose(Axis.XP.rotationDegrees(90f));
        itemRenderer.renderStatic(
                item,
                ItemDisplayContext.GROUND,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                poseStack,
                bufferSource,
                p_entity.level(),
                p_entity.getId()
        );

        poseStack.popPose();
    }
}
