package net.blumasc.blubasics.item.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InWorld3dBakedModel implements BakedModel {

    private final BakedModel inventoryModel;
    private final BakedModel handModel;

    public InWorld3dBakedModel(BakedModel inventoryModel, BakedModel handModel) {
        this.inventoryModel = inventoryModel;
        this.handModel = handModel;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand) {
        return inventoryModel.getQuads(state, side, rand);
    }

    @Override
    public BakedModel applyTransform(ItemDisplayContext context, PoseStack poseStack, boolean applyLeftHandTransform) {
        return switch (context) {
            case THIRD_PERSON_LEFT_HAND, THIRD_PERSON_RIGHT_HAND, FIRST_PERSON_LEFT_HAND, FIRST_PERSON_RIGHT_HAND -> handModel.applyTransform(context, poseStack, applyLeftHandTransform);
            default -> inventoryModel.applyTransform(context, poseStack, applyLeftHandTransform);
        };
    }
    @Override public boolean useAmbientOcclusion() { return inventoryModel.useAmbientOcclusion(); }
    @Override public boolean isGui3d() { return false; }
    @Override public boolean usesBlockLight() { return inventoryModel.usesBlockLight(); }
    @Override public boolean isCustomRenderer() { return false; }
    @Override public TextureAtlasSprite getParticleIcon() { return inventoryModel.getParticleIcon(); }
    @Override public ItemOverrides getOverrides() { return ItemOverrides.EMPTY; }
}