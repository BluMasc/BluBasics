package net.blumasc.blubasics.entity.custom;

import net.blumasc.blubasics.entity.BaseModEntities;
import net.blumasc.blubasics.item.BaseModItems;
import net.blumasc.blubasics.sound.BaseModSounds;
import net.blumasc.blubasics.util.BaseModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class SolarBeetleEntity extends Animal implements Shearable {
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState wingshakeAnimationState = new AnimationState();
    public final AnimationState spreadWingsAnimationState = new AnimationState();
    public final AnimationState closeWingsAnimationState = new AnimationState();

    private static final EntityDataAccessor<Integer> CHARGE =
            SynchedEntityData.defineId(SolarBeetleEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> CHARGING =
            SynchedEntityData.defineId(SolarBeetleEntity.class, EntityDataSerializers.BOOLEAN);

    public SolarBeetleEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 14d)
                .add(Attributes.MOVEMENT_SPEED, 0.3f)
                .add(Attributes.FOLLOW_RANGE, 16D)
                .add(Attributes.ARMOR, 4d);
    }

    public void shear(SoundSource category) {
        this.level().playSound((Player)null, this, SoundEvents.SHEEP_SHEAR, category, 1.0F, 1.0F);

        if(this.getCharge()<75)return;
        ItemLike il = BaseModItems.BEETLE_HORN;
        if(this.getCharge()==100) il = BaseModItems.SUN_HORN;
        this.setCharge(0);
        int i = 2;

        for(int j = 0; j < i; ++j) {
            ItemEntity itementity = this.spawnAtLocation(il, 1);
            if (itementity != null) {
                itementity.setDeltaMovement(itementity.getDeltaMovement().add((double)((this.random.nextFloat() - this.random.nextFloat()) * 0.1F), (double)(this.random.nextFloat() * 0.05F), (double)((this.random.nextFloat() - this.random.nextFloat()) * 0.1F)));
            }
        }

    }

    @Override
    public boolean readyForShearing() {
        return getCharge()>=75;
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(BaseModTags.Items.SOLAR_BEETLE_FOOD);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return BaseModEntities.SOLAR_BEETLE.get().create(serverLevel);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, (double)1.25F));
        this.goalSelector.addGoal(3, new BreedGoal(this, (double)1.0F));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.2, (p_335406_) -> p_335406_.is(BaseModTags.Items.SOLAR_BEETLE_FOOD), false));
        this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.1));
        this.goalSelector.addGoal(6, new BeetleSolarChargingGoal(this));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, (double)1.0F));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
    }

    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        player.getItemInHand(hand);
        return super.mobInteract(player, hand);
    }

    @Override
    public void tick() {
        super.tick();
        if(this.level().isClientSide())
        {
            this.setupAnimationState();
        }else{
            particleTick();
            int moonLight = level().getBrightness(LightLayer.SKY, this.blockPosition());
            if(isChargable())
            {
                if(this.level().getRandom().nextInt(1200)<moonLight) {
                    this.setCharge(Math.min(this.getCharge() + 1, 100));
                }
            }
            if ( isDechargable())
            {
                this.setCharge(Math.max(this.getCharge()-1,75));
            }

        }
    }

    private boolean isChargable(){
        boolean isDay = level().isDay();
        boolean skyVisible = level().canSeeSky(this.blockPosition());
        boolean isRaining = level().isRainingAt(this.blockPosition());
        return skyVisible && isDay && !isRaining && this.getCharge()<100;
    }

    private boolean isDechargable(){
        boolean isDay = level().isDay();
        boolean isRaining = level().isRainingAt(this.blockPosition());
        return ((!isDay) || isRaining) && this.getCharge()>76;
    }

    private void setupAnimationState() {

        if(isCharging())
        {
            this.closeWingsAnimationState.stop();
            this.spreadWingsAnimationState.startIfStopped(this.tickCount);
        }else{
            this.spreadWingsAnimationState.stop();
            this.closeWingsAnimationState.startIfStopped(this.tickCount);
        }
        this.idleAnimationState.startIfStopped(this.tickCount);

        if(this.level().getRandom().nextDouble()<0.02 && !isCharging())
        {
            this.wingshakeAnimationState.stop();
            this.wingshakeAnimationState.startIfStopped(this.tickCount);
        }


    }

    @Override
    public void addAdditionalSaveData(net.minecraft.nbt.CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Charge", this.getCharge());
    }

    @Override
    public void readAdditionalSaveData(net.minecraft.nbt.CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setCharge(compound.getInt("Charge"));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(CHARGE, 0);
        builder.define(CHARGING, false);
    }

    public boolean isCharging() {
        return this.entityData.get(CHARGING);
    }

    public void setCharging(boolean delivering) {
        this.entityData.set(CHARGING, delivering);
    }

    public int getCharge() {
        return this.entityData.get(CHARGE);
    }

    public void setCharge(int charge) {
        this.entityData.set(CHARGE, charge);
    }

    protected void playStepSound(BlockPos pos, BlockState block) {
        this.playSound(BaseModSounds.SOLAR_BUG_SCUTTLE.get(), 0.15F, 1.0F);
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return BaseModSounds.SOLAR_BUG.get();
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource damageSource) {
        return BaseModSounds.SOLAR_BUG_HURT.get();
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return BaseModSounds.SOLAR_BUG_DEATH.get();
    }


    public void particleTick() {
        if (getCharge()==100) {
            this.getLookAngle();
            spawnParticle(level(), this.getX(), this.getY()+0.2, this.getZ());
        }
    }

    public static void spawnParticle(Level level, double x, double y, double z) {
        RandomSource rand = level.getRandom();
        if(rand.nextDouble()<0.1 && level instanceof ServerLevel sl) {
            sl.sendParticles(
                    ParticleTypes.ELECTRIC_SPARK,
                    x, y, z,
                    1,
                    0.2, 0.2, 0.2,
                    0.01
            );
        }
    }

    public class BeetleSolarChargingGoal extends Goal {
        private final SolarBeetleEntity beetle;
        private int chargeTime;
        private int solarTickCounter = 0;

        public BeetleSolarChargingGoal(SolarBeetleEntity beetle) {
            this.beetle = beetle;
            this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return beetle.isChargable()
                    && beetle.random.nextFloat() < 0.01f;
        }

        @Override
        public boolean canContinueToUse() {
            return chargeTime > 0 && beetle.isChargable();
        }

        @Override
        public void start() {
            chargeTime = beetle.random.nextInt(60, 120);
            solarTickCounter=0;
            beetle.setCharging(true);
        }

        @Override
        public void stop() {
            chargeTime = 0;
            solarTickCounter=0;
            beetle.setCharging(false);
        }

        @Override
        public void tick() {
            chargeTime--;

            float sunAngle = beetle.level().getSunAngle(1.0F);
            float yaw = sunAngle * 360f - 90f;
            beetle.yBodyRot = yaw;
            beetle.yHeadRot = yaw;

            solarTickCounter++;
            if(solarTickCounter >= 10) {
                solarTickCounter = 0;
                beetle.setCharge(Math.min(beetle.getCharge() + 2, 100));
            }
        }
    }



}
