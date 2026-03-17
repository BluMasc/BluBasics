package net.blumasc.blubasics.block.entity;

import net.blumasc.blubasics.block.BaseModBlockEntities;
import net.blumasc.blubasics.effect.BaseModEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class SporeMushroomBlockEntity extends BlockEntity {
    public SporeMushroomBlockEntity(BlockPos pos, BlockState blockState) {
        super(BaseModBlockEntities.SPORE_MUSHROOM_BE.get(), pos, blockState);
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState){
        if (level == null || level.isClientSide) return;

        AABB box = new AABB(worldPosition).inflate(8);
        for (LivingEntity e : level.getEntitiesOfClass(LivingEntity.class, box)) {
            e.addEffect(new MobEffectInstance(BaseModEffects.HALLUCINATION, 300, 0, false, true));
        }
    }
}
