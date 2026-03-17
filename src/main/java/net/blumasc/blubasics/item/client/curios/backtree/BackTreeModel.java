package net.blumasc.blubasics.item.client.curios.backtree;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.blumasc.blubasics.BluBasicsMod;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class BackTreeModel<T extends LivingEntity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(BluBasicsMod.MODID, "backtreemodel"), "main");
    public final ModelPart body;

    public BackTreeModel(ModelPart root) {
        this.body = root.getChild("body");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(3.0F, -9.0F, 4.0F, 7.0F, 4.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(18, 26).addBox(10.0F, -8.0F, 4.0F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(0, 26).addBox(5.0F, -10.0F, 5.0F, 5.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 20).addBox(2.0F, -8.0F, 8.0F, 7.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(32, 28).addBox(7.0F, -5.0F, 8.0F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(32, 26).addBox(4.0F, -6.0F, 3.0F, 7.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(14, 37).addBox(2.0F, -8.0F, 4.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(14, 35).addBox(-2.0F, 2.0F, 2.0F, 5.0F, 0.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(36, 36).addBox(3.0F, 2.0F, 2.0F, 0.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r1 = body.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(22, 37).addBox(0.0F, -3.0F, -1.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, -3.0F, 8.0F, -0.658F, 0.3715F, 0.4391F));

        PartDefinition cube_r2 = body.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(8, 38).addBox(-1.0F, -3.0F, 0.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, -3.0F, 5.0F, 0.045F, 0.123F, 0.7882F));

        PartDefinition cube_r3 = body.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(28, 36).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(5.0F, -3.0F, 6.0F, 0.0F, 0.0F, -0.3491F));

        PartDefinition cube_r4 = body.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(26, 12).addBox(-1.0F, -2.0F, -2.0F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, 0.0F, 6.0F, 1.1442F, 0.4114F, 0.2546F));

        PartDefinition cube_r5 = body.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(26, 5).addBox(-1.0F, -2.0F, -2.0F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 3.0F, 3.0F, 0.577F, 0.4114F, 0.2546F));

        PartDefinition leaves = body.addOrReplaceChild("leaves", CubeListBuilder.create().texOffs(0, 10).addBox(3.0F, -9.0F, 4.0F, 7.0F, 4.0F, 6.0F, new CubeDeformation(0.2F))
                .texOffs(0, 31).addBox(10.0F, -8.0F, 4.0F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.2F))
                .texOffs(26, 0).addBox(5.0F, -10.0F, 5.0F, 5.0F, 1.0F, 4.0F, new CubeDeformation(0.2F))
                .texOffs(22, 20).addBox(2.0F, -8.0F, 8.0F, 7.0F, 2.0F, 4.0F, new CubeDeformation(0.2F))
                .texOffs(32, 32).addBox(7.0F, -5.0F, 8.0F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.2F))
                .texOffs(14, 33).addBox(4.0F, -6.0F, 3.0F, 7.0F, 1.0F, 1.0F, new CubeDeformation(0.2F))
                .texOffs(0, 38).addBox(2.0F, -8.0F, 4.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        body.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }
}
