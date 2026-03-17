package net.blumasc.blubasics.entity.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class ChimeraEntity extends TamableAnimal {

    public final AnimationState attackAnimationState = new AnimationState();
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState sittingAnimationState = new AnimationState();
    public final AnimationState flapAnimationState = new AnimationState();
    private LivingEntity clientSideCachedAttackTarget;
    private int clientSideAttackTime;
    protected int shootRechargeTimer;
    private static final EntityDataAccessor<Boolean> CHARGING =
            SynchedEntityData.defineId(ChimeraEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> DATA_ID_ATTACK_TARGET =
            SynchedEntityData.defineId(ChimeraEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> SITTING =
            SynchedEntityData.defineId(ChimeraEntity.class, EntityDataSerializers.BOOLEAN);

    public ChimeraEntity(EntityType<? extends TamableAnimal> entityType, Level level) {

        super(entityType, level);
        this.setPathfindingMalus(PathType.WATER, 0.0F);
    }

    public static AttributeSupplier.Builder createAttributes(){
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 30d)
                .add(Attributes.ARMOR, 5)
                .add(Attributes.ATTACK_DAMAGE, 3d)
                .add(Attributes.MOVEMENT_SPEED, 0.1f)
                .add(Attributes.FOLLOW_RANGE, 30D);
    }

    public boolean isCharging() {
        return this.entityData.get(CHARGING);
    }

    public void setCharging(boolean charging) {
        this.entityData.set(CHARGING, charging);
    }

    @Override
    public boolean isOrderedToSit() {
        return this.entityData.get(SITTING);
    }

    @Override
    public void setOrderedToSit(boolean sit) {
        this.entityData.set(SITTING, sit);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(CHARGING, false);
        builder.define(DATA_ID_ATTACK_TARGET, 0);
        builder.define(SITTING, false);
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return false;
    }

    @Override
    protected int decreaseAirSupply(int currentAir) {
        return currentAir;
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }

    public float getAttackAnimationScale(float partialTick) {
        return ((float)this.clientSideAttackTime + partialTick) / (float)70;
    }

    private void setupAnimationStates() {

        if(!onGround()){
            this.idleAnimationState.stop();
            this.sittingAnimationState.stop();
            this.flapAnimationState.startIfStopped(this.tickCount);
        }else {
            this.flapAnimationState.stop();
            if (isOrderedToSit()) {
                this.idleAnimationState.stop();
                this.sittingAnimationState.startIfStopped(this.tickCount);
            } else {
                this.sittingAnimationState.stop();
                this.idleAnimationState.startIfStopped(this.tickCount);
            }
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        Vec3 vec3 = this.getDeltaMovement();
        if (!this.onGround() && vec3.y < (double)0.0F) {
            this.setDeltaMovement(vec3.multiply((double)1.0F, 0.6, (double)1.0F));
        }
    }

    @Override
    public void tick(){
        super.tick();

        if(this.level().isClientSide()){
            this.setupAnimationStates();

            if (this.hasActiveAttackTarget()) {
                if (this.clientSideAttackTime < 70) {
                    ++this.clientSideAttackTime;
                }

                LivingEntity livingentity = this.getActiveAttackTarget();
                if (livingentity != null) {
                    this.getLookControl().setLookAt(livingentity, 90.0F, 90.0F);
                    this.getLookControl().tick();
                }
            }
        }
        else {
            if (this.hasActiveAttackTarget()) {
                this.setYRot(this.yHeadRot);
            }
            if (this.shootRechargeTimer > 0) {
                this.shootRechargeTimer--;
            }
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("ShootRechargeTimer", shootRechargeTimer);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        if (tag.contains("ShootRechargeTimer")) {
            shootRechargeTimer = tag.getInt("ShootRechargeTimer");
        }
    }



    void setActiveAttackTarget(int activeAttackTargetId) {
        this.entityData.set(DATA_ID_ATTACK_TARGET, activeAttackTargetId);
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return SoundEvents.FOX_AMBIENT;
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.FOX_HURT;
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return SoundEvents.FOX_DEATH;
    }

    public boolean hasActiveAttackTarget() {
        return (Integer)this.entityData.get(DATA_ID_ATTACK_TARGET) != 0;
    }

    @javax.annotation.Nullable
    public LivingEntity getActiveAttackTarget() {
        if (!this.hasActiveAttackTarget()) {
            return null;
        } else if (this.level().isClientSide) {
            if (this.clientSideCachedAttackTarget != null) {
                return this.clientSideCachedAttackTarget;
            } else {
                Entity entity = this.level().getEntity((Integer)this.entityData.get(DATA_ID_ATTACK_TARGET));
                if (entity instanceof LivingEntity) {
                    this.clientSideCachedAttackTarget = (LivingEntity)entity;
                    return this.clientSideCachedAttackTarget;
                } else {
                    return null;
                }
            }
        } else {
            return this.getTarget();
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (this.level().isClientSide) {
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        if (!this.isTame() || !this.isOwnedBy(player)) {
            return InteractionResult.PASS;
        }

        ItemStack heldStack = player.getItemInHand(hand);
        ItemStack chimeraItem = this.getMainHandItem();

        if (!chimeraItem.isEmpty() && (heldStack.isEmpty())) {
            this.spawnAtLocation(chimeraItem);
            this.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
            this.playSound(SoundEvents.ITEM_PICKUP, 1.0F, 0.8F);
            return InteractionResult.CONSUME;
        }

        if (!heldStack.isEmpty() && chimeraItem.isEmpty()) {
            ItemStack copy = heldStack.copy();
            copy.setCount(1);
            this.setItemInHand(InteractionHand.MAIN_HAND, copy);

            if (!player.getAbilities().instabuild) {
                heldStack.shrink(1);
            }

            this.playSound(SoundEvents.ITEM_PICKUP, 1.0F, 1.0F);
            return InteractionResult.CONSUME;
        }

        this.setOrderedToSit(!this.isOrderedToSit());
        this.navigation.stop();
        return InteractionResult.CONSUME;
    }


    @Override
    public boolean canTakeItem(ItemStack stack) {
        return false;
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float damageMultiplier, DamageSource source) {
        return false;
    }

    @Override
    protected int calculateFallDamage(float fallDistance, float damageMultiplier) {
        return 0;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new ChimeraShootGoal(this));
        this.goalSelector.addGoal(3, new ChimeraMeleeAttackGoal(this, 4.4, true));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.goalSelector.addGoal(4,new FollowOwnerGoal(this,4.0,6,1));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 4.0f));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
    }

    static class ChimeraMeleeAttackGoal extends MeleeAttackGoal
    {
        private final ChimeraEntity chimera;

        public ChimeraMeleeAttackGoal(ChimeraEntity mob, double speedModifier, boolean followingTargetEvenIfNotSeen) {
            super(mob, speedModifier, followingTargetEvenIfNotSeen);
            this.chimera = mob;
        }

        @Override
        public boolean canUse() {
            return !chimera.isOrderedToSit() && super.canUse();
        }

        @Override
        public void start() {
            chimera.setCharging(true);
            super.start();
        }

        @Override
        public void stop() {
            chimera.setCharging(false);
            super.stop();
        }
    }

    static class ChimeraShootGoal extends Goal {
        private final ChimeraEntity chimera;
        private int attackTime;

        public ChimeraShootGoal(ChimeraEntity chimera) {
            this.chimera= chimera;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        public boolean canUse() {
            if (chimera.isOrderedToSit()) return false;
            LivingEntity livingentity = this.chimera.getTarget();
            return livingentity != null && livingentity.isAlive() && this.chimera.hasLineOfSight(livingentity) && this.chimera.shootRechargeTimer<=0;
        }

        public boolean canContinueToUse() {
            LivingEntity livingentity = this.chimera.getTarget();
            return super.canContinueToUse() && (this.chimera.getTarget() != null && this.chimera.distanceToSqr(this.chimera.getTarget()) > (double)9.0F) && this.chimera.hasLineOfSight(livingentity);
        }

        public void start() {
            this.attackTime = -10;
            this.chimera.getNavigation().stop();
            LivingEntity livingentity = this.chimera.getTarget();
            if (livingentity != null) {
                this.chimera.getLookControl().setLookAt(livingentity, 90.0F, 90.0F);
            }

            this.chimera.hasImpulse = true;
        }

        public void stop() {
            this.chimera.setActiveAttackTarget(0);
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity livingentity = this.chimera.getTarget();
            if (livingentity != null) {
                this.chimera.getNavigation().stop();
                this.chimera.getLookControl().setLookAt(livingentity, 90.0F, 90.0F);
                ++this.attackTime;
                if (this.attackTime == 0) {
                    this.chimera.setActiveAttackTarget(livingentity.getId());
                } else if (this.attackTime >= 70) {
                    float f = 1.0F;
                    if (this.chimera.level().getDifficulty() == Difficulty.HARD) {
                        f += 2.0F;
                    }

                    livingentity.hurt(this.chimera.damageSources().indirectMagic(this.chimera, this.chimera), f);
                    this.chimera.doHurtTarget(livingentity);
                    this.chimera.shootRechargeTimer = 800;
                    this.chimera.setTarget((LivingEntity) null);
                }

                super.tick();
            }

        }
    }
}
