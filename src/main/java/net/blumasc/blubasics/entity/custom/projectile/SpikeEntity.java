package net.blumasc.blubasics.entity.custom.projectile;

import net.blumasc.blubasics.damage.BaseModDamageTypes;
import net.blumasc.blubasics.effect.BaseModEffects;
import net.blumasc.blubasics.entity.BaseModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DripstoneThickness;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.List;

public class SpikeEntity extends Entity {
    private BlockPos basePos;
    private static final EntityDataAccessor<Float> DATA_HEIGHT =
            SynchedEntityData.defineId(SpikeEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> DATA_MAX_HEIGHT =
            SynchedEntityData.defineId(SpikeEntity.class, EntityDataSerializers.INT);
    private Player owner;
    private boolean basePosInitialized = false;
    private int effectDuration=0;

    public float getCurrentHeight() {
        return this.entityData.get(DATA_HEIGHT);
    }

    private void setCurrentHeight(float height) {
        this.entityData.set(DATA_HEIGHT, height);
        this.refreshDimensions();
    }

    public int getMaxHeight() {
        return this.entityData.get(DATA_MAX_HEIGHT);
    }

    public void setMaxHeight(int height) {
        this.entityData.set(DATA_MAX_HEIGHT, height);
    }

    public SpikeEntity(EntityType<? extends SpikeEntity> type, Level world) {
        super(type, world);
    }

    public SpikeEntity(Level world, BlockPos basePos, int maxHeight, int effectDuration, @Nullable Player owner) {
        super(BaseModEntities.DRIPSTONE_SPIKE.get(), world);
        this.basePos = basePos;
        this.basePosInitialized=true;
        setMaxHeight(maxHeight);
        this.owner = owner;
        this.effectDuration = effectDuration;
        this.setPos(basePos.getX()+0.5, basePos.getY(), basePos.getZ()+0.5);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_HEIGHT, 0.0f);
        builder.define(DATA_MAX_HEIGHT, 3);
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return EntityDimensions.fixed(1.0f, Math.max(0.1f, getCurrentHeight()));
    }

    @Override
    public void tick() {
        super.tick();
        if (!basePosInitialized) {
            BlockPos pos = this.blockPosition();
            if (!pos.equals(BlockPos.ZERO)) {
                this.basePos = pos;
                basePosInitialized = true;
            } else {
                return;
            }
        }
        if (level().isClientSide) return;


        if (getCurrentHeight() < getMaxHeight()) {
            BlockPos nextPos = basePos.above((int) Math.floor(getCurrentHeight()) + 1);
            if (level().getBlockState(nextPos).canBeReplaced()) {
                setCurrentHeight(getCurrentHeight()+0.3f);
            } else {
                setCurrentHeight((float) Math.floor(getCurrentHeight()));
                setMaxHeight ((int) getCurrentHeight());
            }
        } else {
            for(int i=1;i<=getMaxHeight();i++) {
                BlockPos nextPos = basePos.above(i);
                level().setBlock(nextPos, Blocks.POINTED_DRIPSTONE.defaultBlockState()
                        .setValue(PointedDripstoneBlock.TIP_DIRECTION, Direction.UP)
                        .setValue(PointedDripstoneBlock.THICKNESS, posToThickness(i+(3-getMaxHeight()))), 3);
            }
            this.discard();
            return;
        }

        AABB spikeBox = new AABB(basePos.getX()-0.5, basePos.getY(), basePos.getZ()-0.5,
                basePos.getX()+1.5, basePos.getY()+getCurrentHeight()+1.5, basePos.getZ()+1.5);
        List<Entity> entities = level().getEntities(this, spikeBox, e -> e instanceof LivingEntity);
        for (Entity e : entities) {
            if(tickCount%4==0) {
                e.hurt(BaseModDamageTypes.spikeDamage(level()), (float) 2);
                if (e instanceof LivingEntity living)
                    living.addEffect(new MobEffectInstance(BaseModEffects.SPIKED, effectDuration));
            }
        }

        if (tickCount%2==0) {
            level().playSound(
                    null,
                    basePos.getX() + 0.5,
                    basePos.getY() + 1.0,
                    basePos.getZ() + 0.5,
                    SoundEvents.DRIPSTONE_BLOCK_BREAK,
                    net.minecraft.sounds.SoundSource.BLOCKS,
                    1.0f,
                    1.0f
            );
        }
        BlockPos particlePos = basePos.above(1);
        BlockState dripstoneState = Blocks.POINTED_DRIPSTONE.defaultBlockState();
        ((ServerLevel) level()).sendParticles(
                new BlockParticleOption(ParticleTypes.BLOCK, dripstoneState),
                particlePos.getX() + 0.5, particlePos.getY() + 0.5, particlePos.getZ() + 0.5,
                10,
                0.3, 0.3, 0.3,
                0.05
        );
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        basePos = BlockPos.of(tag.getLong("BasePos"));
        setCurrentHeight(tag.getFloat("VisualHeight"));
        setMaxHeight(tag.getInt("MaxHeight"));
        effectDuration = tag.getInt("EffectDuration");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putLong("BasePos", basePos.asLong());
        tag.putFloat("VisualHeight", getCurrentHeight());
        tag.putInt("MaxHeight", getMaxHeight());
        tag.putInt("EffectDuration", effectDuration);
    }

    private static DripstoneThickness posToThickness(int i){
        switch(i)
        {
            case 1: return DripstoneThickness.MIDDLE;
            case 2: return DripstoneThickness.FRUSTUM;
            case 3: return DripstoneThickness.TIP;
            default:return DripstoneThickness.BASE;
        }
    }

}

