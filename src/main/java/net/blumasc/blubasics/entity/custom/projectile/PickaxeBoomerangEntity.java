package net.blumasc.blubasics.entity.custom.projectile;


import net.blumasc.blubasics.entity.BaseModEntities;
import net.blumasc.blubasics.sound.BaseModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class PickaxeBoomerangEntity extends ThrowableProjectile {

    private static final EntityDataAccessor<ItemStack> DATA_PICKAXE =
            SynchedEntityData.defineId(PickaxeBoomerangEntity.class, EntityDataSerializers.ITEM_STACK);

    private static final EntityDataAccessor<Boolean> DATA_RETURNING =
            SynchedEntityData.defineId(PickaxeBoomerangEntity.class, EntityDataSerializers.BOOLEAN);
    private Player owner;
    private Vec3 originPos;
    private double maxDistance;

    public void setPickaxeStack(ItemStack stack) {
        this.entityData.set(DATA_PICKAXE, stack);
    }

    public ItemStack getPickaxeStack() {
        return this.entityData.get(DATA_PICKAXE);
    }

    public void setReturning(boolean returning) {
        this.entityData.set(DATA_RETURNING, returning);
    }

    public boolean isReturning() {
        return this.entityData.get(DATA_RETURNING);
    }

    public PickaxeBoomerangEntity(EntityType<? extends PickaxeBoomerangEntity> type, Level world) {
        super(type, world);
    }

    public PickaxeBoomerangEntity(Level world, Player owner, ItemStack pickaxeStack, double maxDistance) {
        super(BaseModEntities.PICKAXE_BOOMERANG.get(), world);
        this.owner = owner;
        this.setPickaxeStack(pickaxeStack);
        this.originPos = owner.position();
        this.maxDistance = maxDistance;
        this.setPos(owner.getX(), owner.getY() + owner.getEyeHeight(), owner.getZ());
        this.setDeltaMovement(owner.getLookAngle().scale(1.5));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_PICKAXE, ItemStack.EMPTY);
        builder.define(DATA_RETURNING, false);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide) return;

        double traveled = this.position().distanceTo(originPos);
        if (getPickaxeStack().isEmpty()) {
            this.discard();
            return;
        }
        if ((isReturning() && traveled >= maxDistance) || owner == null) {
            dropPickaxe();
            this.discard();
            return;
        }
        if (!isReturning() && traveled >= maxDistance) {
            startReturn();
        }
        if (this.getDeltaMovement().length() < 0.3) {
            startReturn();
        }

        if (isReturning() && owner != null) {
            Vec3 toOwner = new Vec3(owner.getX(), owner.getEyeY(), owner.getZ()).subtract(this.position()).normalize();
            this.setDeltaMovement(toOwner.scale(1.5));
        }

        if (isReturning() && this.position().distanceTo(owner.position()) < 2.5) {
            if (!owner.addItem(getPickaxeStack().copy())) {
                dropPickaxe();
            }
            this.discard();
        }
        if(tickCount%5==0){
            ServerLevel level = (ServerLevel) this.level();
            level.playSound(
                    null,
                    this.blockPosition(),
                    BaseModSounds.PICKERANG.get(),
                    SoundSource.PLAYERS,
                    1.0F,
                    random.nextFloat()+0.5f
            );
        }
    }

    private void startReturn() {
        originPos = this.position();
        setReturning(true);
    }

    private void dropPickaxe() {
        this.spawnAtLocation(getPickaxeStack().copy());
    }

    private void hurtAndBreak(int damage)
    {
        if(this.owner.hasInfiniteMaterials()) return;
        ItemStack particleStack = getPickaxeStack().copy();
        getPickaxeStack().hurtAndBreak(damage, (ServerLevel)this.level(), null, p -> {
            ServerLevel level = (ServerLevel) this.level();
            level.playSound(
                    null,
                    this.blockPosition(),
                    SoundEvents.ITEM_BREAK,
                    SoundSource.PLAYERS,
                    1.0F,
                    1.0F
            );
            ItemParticleOption particle =
                    new ItemParticleOption(ParticleTypes.ITEM, particleStack);

            level.sendParticles(
                    particle,
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    20,
                    0.25, 0.25, 0.25,
                    0.05
            );
        });
    }
    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);

        nbt.put("PickaxeStack", getPickaxeStack().save(this.level().registryAccess()));

        nbt.putBoolean("Returning", isReturning());

        nbt.putDouble("OriginX", originPos.x);
        nbt.putDouble("OriginY", originPos.y);
        nbt.putDouble("OriginZ", originPos.z);

        if (owner != null) {
            nbt.putUUID("OwnerUUID", owner.getUUID());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);

        if (nbt.contains("PickaxeStack") && this.level() != null) {
            RegistryAccess access = this.level().registryAccess();
            CompoundTag stackTag = nbt.getCompound("PickaxeStack");
            setPickaxeStack(ItemStack.parse(access, stackTag).get());
        }

        setReturning(nbt.getBoolean("Returning"));

        originPos = new Vec3(
                nbt.getDouble("OriginX"),
                nbt.getDouble("OriginY"),
                nbt.getDouble("OriginZ")
        );

        if (nbt.hasUUID("OwnerUUID")) {
            if (this.level() instanceof ServerLevel serverLevel) {
                owner = serverLevel.getPlayerByUUID(nbt.getUUID("OwnerUUID"));
            }
        }
    }

    @Override
    protected void onHit(HitResult result) {
        if(this.level().isClientSide)return;
        if (result.getType() == HitResult.Type.BLOCK) {
            BlockPos blockPos = ((BlockHitResult)result).getBlockPos();
            BlockState state = level().getBlockState(blockPos);
            if (owner instanceof ServerPlayer serverPlayer) {
                ItemStack original = serverPlayer.getMainHandItem();
                serverPlayer.setItemInHand(InteractionHand.MAIN_HAND, getPickaxeStack());

                if (state.canHarvestBlock(level(), blockPos, serverPlayer)) {
                    state.getBlock().playerDestroy(
                            level(),
                            serverPlayer,
                            blockPos,
                            state,
                            level().getBlockEntity(blockPos),
                            getPickaxeStack()
                    );
                    level().setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
                    hurtAndBreak(1);
                }
                serverPlayer.setItemInHand(InteractionHand.MAIN_HAND, original);
            }

            startReturn();
            this.setDeltaMovement(Vec3.ZERO);
        } else if (result.getType() == HitResult.Type.ENTITY) {
            Entity living = ((EntityHitResult)result).getEntity();
            if(living.getUUID().equals(this.owner.getUUID())) return;
            ItemStack original = owner.getMainHandItem();
            owner.setItemInHand(InteractionHand.MAIN_HAND, getPickaxeStack());
            owner.attack(living);
            owner.setItemInHand(InteractionHand.MAIN_HAND, original);
            hurtAndBreak(2);
            startReturn();
        }
    }

    @Override
    protected double getDefaultGravity() {
        return 0.0;
    }
}
