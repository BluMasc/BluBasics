package net.blumasc.blubasics.entity.client.solarbeetle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.blumasc.blubasics.BluBasicsMod;
import net.blumasc.blubasics.entity.custom.SolarBeetleEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class SolarBeetleModel <T extends SolarBeetleEntity> extends HierarchicalModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(BluBasicsMod.MODID, "sunbeetle"), "main");
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart mandible1;
    private final ModelPart mandible2;
    private final ModelPart horn1;
    private final ModelPart lowerPart1;
    private final ModelPart upperPart1;
    private final ModelPart spike1;
    private final ModelPart horn2;
    private final ModelPart lowerPart2;
    private final ModelPart upperPart2;
    private final ModelPart spike2;
    private final ModelPart wing1;
    private final ModelPart wing2;
    private final ModelPart legs;
    private final ModelPart leg1;
    private final ModelPart leg2;
    private final ModelPart leg3;
    private final ModelPart leg4;
    private final ModelPart leg5;
    private final ModelPart leg6;

    public SolarBeetleModel (ModelPart root) {
        this.root = root.getChild("root");
        this.body = this.root.getChild("body");
        this.head = this.body.getChild("head");
        this.mandible1 = this.head.getChild("mandible1");
        this.mandible2 = this.head.getChild("mandible2");
        this.horn1 = this.head.getChild("horn1");
        this.lowerPart1 = this.horn1.getChild("lowerPart1");
        this.upperPart1 = this.lowerPart1.getChild("upperPart1");
        this.spike1 = this.upperPart1.getChild("spike1");
        this.horn2 = this.head.getChild("horn2");
        this.lowerPart2 = this.horn2.getChild("lowerPart2");
        this.upperPart2 = this.lowerPart2.getChild("upperPart2");
        this.spike2 = this.upperPart2.getChild("spike2");
        this.wing1 = this.body.getChild("wing1");
        this.wing2 = this.body.getChild("wing2");
        this.legs = this.root.getChild("legs");
        this.leg1 = this.legs.getChild("leg1");
        this.leg2 = this.legs.getChild("leg2");
        this.leg3 = this.legs.getChild("leg3");
        this.leg4 = this.legs.getChild("leg4");
        this.leg5 = this.legs.getChild("leg5");
        this.leg6 = this.legs.getChild("leg6");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 23.0F, 0.0F));

        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -4.0F, -5.0F, 6.0F, 4.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(28, 26).addBox(-2.0F, -4.0F, 4.0F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(28, 13).addBox(-2.0F, -2.0F, -4.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(28, 31).addBox(-1.0F, 0.0F, -5.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, -5.0F));

        PartDefinition mandible1 = head.addOrReplaceChild("mandible1", CubeListBuilder.create().texOffs(12, 37).addBox(-0.5F, 0.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 0.0F, -4.5F, 0.0F, 0.0F, 0.48F));

        PartDefinition mandible2 = head.addOrReplaceChild("mandible2", CubeListBuilder.create().texOffs(16, 37).addBox(-1.5F, 0.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 0.0F, -4.5F, 0.0F, 0.0F, -0.48F));

        PartDefinition horn1 = head.addOrReplaceChild("horn1", CubeListBuilder.create().texOffs(28, 34).addBox(-1.0F, -1.0F, -1.5F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -1.0F, -2.0F, 0.0F, 0.0F, 0.6109F));

        PartDefinition lowerPart1 = horn1.addOrReplaceChild("lowerPart1", CubeListBuilder.create().texOffs(24, 35).addBox(-0.5F, -3.0F, -1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition upperPart1 = lowerPart1.addOrReplaceChild("upperPart1", CubeListBuilder.create().texOffs(36, 31).addBox(-0.5F, -3.0F, -1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, 0.0F, 0.0F, -0.5236F));

        PartDefinition spike1 = upperPart1.addOrReplaceChild("spike1", CubeListBuilder.create().texOffs(20, 37).addBox(-1.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, -1.5F, -0.5F));

        PartDefinition horn2 = head.addOrReplaceChild("horn2", CubeListBuilder.create().texOffs(0, 35).addBox(-1.0F, -1.0F, -1.5F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -1.0F, -2.0F, 0.0F, 0.0F, -0.6109F));

        PartDefinition lowerPart2 = horn2.addOrReplaceChild("lowerPart2", CubeListBuilder.create().texOffs(36, 35).addBox(-0.5F, -3.0F, -1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition upperPart2 = lowerPart2.addOrReplaceChild("upperPart2", CubeListBuilder.create().texOffs(8, 37).addBox(-0.5F, -3.0F, -1.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, 0.0F, 0.0F, 0.5236F));

        PartDefinition spike2 = upperPart2.addOrReplaceChild("spike2", CubeListBuilder.create().texOffs(28, 37).addBox(0.0F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, -1.5F, -0.5F));

        PartDefinition wing1 = body.addOrReplaceChild("wing1", CubeListBuilder.create().texOffs(0, 13).addBox(-3.0F, -1.0F, -1.0F, 4.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(8, 35).addBox(-2.0F, -1.0F, 9.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -4.0F, -4.0F, 0.0436F, 0.0F, -0.1745F));

        PartDefinition wing2 = body.addOrReplaceChild("wing2", CubeListBuilder.create().texOffs(0, 24).addBox(-1.0F, -1.0F, -1.0F, 4.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(16, 35).addBox(-1.0F, -1.0F, 9.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -4.0F, -4.0F, 0.0436F, 0.0F, 0.1745F));

        PartDefinition legs = root.addOrReplaceChild("legs", CubeListBuilder.create().texOffs(28, 21).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition leg1 = legs.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(30, 0).addBox(-1.0F, -1.0F, 0.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -1.0F, -2.0F, 0.0F, 0.0F, 0.5236F));

        PartDefinition leg2 = legs.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(30, 2).addBox(-1.0F, -1.0F, -1.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -1.0F, 1.0F, 0.054F, -0.3892F, 0.513F));

        PartDefinition leg3 = legs.addOrReplaceChild("leg3", CubeListBuilder.create().texOffs(30, 4).addBox(-5.0F, -1.0F, 0.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -1.0F, -2.0F, 0.0F, 0.0F, -0.5236F));

        PartDefinition leg4 = legs.addOrReplaceChild("leg4", CubeListBuilder.create().texOffs(30, 6).addBox(-5.0F, -1.0F, -1.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -1.0F, 1.0F, 0.054F, 0.3892F, -0.513F));

        PartDefinition leg5 = legs.addOrReplaceChild("leg5", CubeListBuilder.create().texOffs(30, 8).addBox(-5.0F, -1.0F, -1.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -1.0F, -2.0F, 0.0F, -0.6109F, -0.6545F));

        PartDefinition leg6 = legs.addOrReplaceChild("leg6", CubeListBuilder.create().texOffs(30, 10).addBox(-1.0F, -1.0F, -1.0F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -1.0F, -2.0F, 0.0F, 0.6109F, 0.6545F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(SolarBeetleEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotation(netHeadYaw,headPitch);
        int hornGrowth = entity.getCharge();
        float lowGrowth = Math.min(hornGrowth/25f,1f);
        this.horn1.xScale = lowGrowth;
        this.horn1.yScale = lowGrowth;
        this.horn1.zScale = lowGrowth;
        this.horn2.xScale = lowGrowth;
        this.horn2.yScale = lowGrowth;
        this.horn2.zScale = lowGrowth;
        float upperGrowth = Math.min(Math.max((hornGrowth - 25f) / 25f, 0f), 1f);
        this.upperPart1.xScale = upperGrowth;
        this.upperPart1.yScale = upperGrowth;
        this.upperPart1.zScale = upperGrowth;
        this.upperPart2.xScale = upperGrowth;
        this.upperPart2.yScale = upperGrowth;
        this.upperPart2.zScale = upperGrowth;
        float spikeGrowth = Math.min(Math.max((hornGrowth - 50f) / 25f, 0f), 1f);
        this.spike1.xScale = spikeGrowth;
        this.spike1.yScale = spikeGrowth;
        this.spike1.zScale = spikeGrowth;
        this.spike2.xScale = spikeGrowth;
        this.spike2.yScale = spikeGrowth;
        this.spike2.zScale = spikeGrowth;

        this.animateWalk(SolarBeetleAnimation.walking, limbSwing, limbSwingAmount, 2f, 2.5f);
        this.animate(entity.idleAnimationState, SolarBeetleAnimation.idle, ageInTicks, 1f);
        this.animate(entity.wingshakeAnimationState, SolarBeetleAnimation.wingshake, ageInTicks, 1f);
        this.animate(entity.closeWingsAnimationState, SolarBeetleAnimation.closeWings, ageInTicks, 1f);
        this.animate(entity.spreadWingsAnimationState, SolarBeetleAnimation.spreadWings, ageInTicks, 1f);
    }

    private void applyHeadRotation(float headYaw, float headPitch) {
        headYaw = Mth.clamp(headYaw, -30f, 30f);
        headPitch = Mth.clamp(headPitch, -25f, 45);

        this.head.yRot = headYaw * ((float)Math.PI / 180f);
        this.head.xRot = headPitch *  ((float)Math.PI / 180f);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }

    @Override
    public ModelPart root() {
        return root;
    }
}
