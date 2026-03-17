package net.blumasc.blubasics.entity.client.chimera;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.blumasc.blubasics.BluBasicsMod;
import net.blumasc.blubasics.entity.custom.ChimeraEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class ChimeraModel<T extends ChimeraEntity> extends HierarchicalModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(BluBasicsMod.MODID, "chimera"), "main");
    private final ModelPart main;
    private final ModelPart body;
    private final ModelPart tailHead;
    private final ModelPart eye;
    private final ModelPart head;

    public ChimeraModel(ModelPart root) {
        this.main = root.getChild("main");
        this.body = this.main.getChild("body");
        ModelPart tailpart0 = this.body.getChild("tailpart0");
        ModelPart tailpart1 = tailpart0.getChild("tailpart1");
        ModelPart tailpart2 = tailpart1.getChild("tailpart2");
        this.tailHead = tailpart2.getChild("tailHead");
        this.eye = this.tailHead.getChild("eye");
        this.head = this.body.getChild("head");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition leg0 = main.addOrReplaceChild("leg0", CubeListBuilder.create().texOffs(70, 66).addBox(-1.0F, 0.0F, -3.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -5.0F, -3.0F));

        PartDefinition leg1 = main.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(0, 71).addBox(-1.0F, 0.0F, -3.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, -5.0F, -3.0F));

        PartDefinition leg2 = main.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(12, 71).addBox(-1.0F, 0.0F, -3.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -5.0F, 7.0F));

        PartDefinition leg3 = main.addOrReplaceChild("leg3", CubeListBuilder.create().texOffs(40, 71).addBox(-1.0F, 0.0F, -3.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, -5.0F, 7.0F));

        PartDefinition body = main.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -18.0F, -8.0F, 9.0F, 11.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(0, 27).addBox(-5.0F, -19.0F, -9.0F, 11.0F, 14.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, 2.0F));

        PartDefinition tailpart0 = body.addOrReplaceChild("tailpart0", CubeListBuilder.create().texOffs(50, 14).addBox(-2.0F, -1.0F, 0.0F, 4.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -12.0F, 6.0F, 0.6109F, 0.0F, 0.0F));

        PartDefinition tailpart1 = tailpart0.addOrReplaceChild("tailpart1", CubeListBuilder.create().texOffs(0, 52).addBox(-1.5F, -1.0F, -2.0F, 3.0F, 3.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 8.0F, 0.9163F, 0.0F, 0.0F));

        PartDefinition tailpart2 = tailpart1.addOrReplaceChild("tailpart2", CubeListBuilder.create().texOffs(24, 52).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 7.0F, 0.6981F, 0.0F, 0.0F));

        PartDefinition tailHead = tailpart2.addOrReplaceChild("tailHead", CubeListBuilder.create().texOffs(50, 0).addBox(-3.0F, -8.0F, -1.0F, 6.0F, 8.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(42, 59).addBox(-4.0F, -7.0F, -1.0F, 1.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(56, 59).addBox(3.0F, -7.0F, -1.0F, 1.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 64).addBox(-3.0F, -7.0F, 5.0F, 6.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(70, 59).addBox(-3.0F, -7.0F, -2.0F, 6.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 5.0F, -0.6981F, 0.0F, 0.0F));

        PartDefinition eye = tailHead.addOrReplaceChild("eye", CubeListBuilder.create().texOffs(20, 64).addBox(-2.5F, -7.25F, 4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -1.0F, -2.0F));

        PartDefinition wing1 = body.addOrReplaceChild("wing1", CubeListBuilder.create(), PartPose.offset(5.0F, -16.0F, -3.0F));

        PartDefinition wingtip0_r1 = wing1.addOrReplaceChild("wingtip0_r1", CubeListBuilder.create().texOffs(44, 27).addBox(0.0F, -1.0F, -5.0F, 13.0F, 1.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.6981F));

        PartDefinition wing2 = body.addOrReplaceChild("wing2", CubeListBuilder.create(), PartPose.offset(-5.0F, -16.0F, -3.0F));

        PartDefinition wingtip1_r1 = wing2.addOrReplaceChild("wingtip1_r1", CubeListBuilder.create().texOffs(44, 37).mirror().addBox(-12.0F, 0.0F, -5.0F, 13.0F, 1.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.6545F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(44, 47).addBox(-4.0F, -2.0F, -6.0F, 8.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(52, 71).addBox(-2.0F, 2.0F, -9.0F, 4.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -12.0F, -8.0F));

        PartDefinition head_r1 = head.addOrReplaceChild("head_r1", CubeListBuilder.create().texOffs(32, 61).addBox(0.0F, -8.0F, -2.0F, 2.0F, 11.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(24, 61).addBox(-10.0F, -8.0F, -2.0F, 2.0F, 11.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 0.0F, -4.0F, 1.1781F, 0.0F, 0.0F));

        PartDefinition earLeft = head.addOrReplaceChild("earLeft", CubeListBuilder.create().texOffs(14, 64).addBox(0.5F, -9.0F, -1.0F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, -1.0F, 0.0F, 0.2618F, 0.0F));

        PartDefinition earRight = head.addOrReplaceChild("earRight", CubeListBuilder.create().texOffs(72, 47).addBox(-2.5F, -9.0F, -1.0F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, -1.0F, 0.0F, -0.2618F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(ChimeraEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotation(netHeadYaw, headPitch);
        this.animate(entity.sittingAnimationState, ChimeraAnimations.sittingIdle, ageInTicks, 1f);
        this.animate(entity.idleAnimationState, ChimeraAnimations.idle, ageInTicks, 1f);
        this.animate(entity.flapAnimationState, ChimeraAnimations.flap, ageInTicks, 2f);
        if(entity.isCharging()) {
            this.animateWalk(ChimeraAnimations.charging, limbSwing, limbSwingAmount, 2f, 2.5f);
        }else{
            this.animateWalk(ChimeraAnimations.walking, limbSwing, limbSwingAmount, 2f, 2.5f);
        }
        this.animate(entity.attackAnimationState, ChimeraAnimations.shoot, ageInTicks, 1f);
    }

    private void applyHeadRotation(float headYaw, float headPitch) {
        headYaw = Mth.clamp(headYaw, -30f, 30f);
        headPitch = Mth.clamp(headPitch, -25f, 45);

        this.head.yRot = headYaw * ((float)Math.PI / 180f);
        this.head.xRot = headPitch *  ((float)Math.PI / 180f);

        this.eye.x = 2.0f-(headYaw/30*1.5f);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        main.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }

    @Override
    public ModelPart root() {
        return main;
    }

    public ModelPart getTailHead() {
        return this.tailHead;
    }

    public ModelPart getHead() {
        return this.head;
    }

    public ModelPart getBody() {
        return this.body;
    }
}

