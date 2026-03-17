package net.blumasc.blubasics.item.client.curios.backtree;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.blumasc.blubasics.BluBasicsMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class BackTreeRenderer implements ICurioRenderer {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(BluBasicsMod.MODID, "textures/entity/curio/back_tree.png");
    private static final ResourceLocation TEXTURE_CHERRY = ResourceLocation.fromNamespaceAndPath(BluBasicsMod.MODID, "textures/entity/curio/back_tree_cherry.png");

    public static BackTreeModel<LivingEntity> MODEL;

    public BackTreeRenderer() {
        super();
        ModelPart part = Minecraft.getInstance().getEntityModels().bakeLayer(BackTreeModel.LAYER_LOCATION);
        MODEL = new BackTreeModel<>(part);
    }

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(
            ItemStack stack, SlotContext slotContext, PoseStack matrixStack,
            RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer,
            int light, float limbSwing, float limbSwingAmount, float partialTicks,
            float ageInTicks, float netHeadYaw, float headPitch) {

        if (MODEL == null) return;

        matrixStack.pushPose();

        if (renderLayerParent.getModel() instanceof HumanoidModel<?> humanoidModel) {
            humanoidModel.body.translateAndRotate(matrixStack);
        }

        VertexConsumer buffer = renderTypeBuffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        if(stack.getHoverName().getString().toLowerCase().contains("cherry"))
        {
            buffer = renderTypeBuffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE_CHERRY));
        }
        MODEL.body.render(matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);

        matrixStack.popPose();
    }
}
