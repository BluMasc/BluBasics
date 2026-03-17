package net.blumasc.blubasics.entity.custom.projectile;

import net.blumasc.blubasics.effect.BaseModEffects;
import net.blumasc.blubasics.entity.BaseModEntities;
import net.blumasc.blubasics.util.BaseModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class BlockProjectileEntity extends AbstractHurtingProjectile {

    private static final EntityDataAccessor<String> BLOCK =
            SynchedEntityData.defineId(BlockProjectileEntity.class, EntityDataSerializers.STRING);

    public BlockProjectileEntity(EntityType<? extends AbstractHurtingProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public BlockProjectileEntity(LivingEntity owner, Vec3 movement, Level level, BlockState state) {
        super(BaseModEntities.THROWN_BLOCK.get(), owner, movement, level);
        this.entityData.set(BLOCK, blockStateToString(state));
    }

    public BlockProjectileEntity(double x, double y, double z, Vec3 movement, Level level, BlockState state) {
        super(BaseModEntities.THROWN_BLOCK.get(), x, y, z, movement, level);
        this.entityData.set(BLOCK, blockStateToString(state));
    }

    protected BlockProjectileEntity(double x, double y, double z, Level level, BlockState state) {
        super(BaseModEntities.THROWN_BLOCK.get(), x, y, z, level);
        this.entityData.set(BLOCK, blockStateToString(state));
    }

    private static String blockStateToString(BlockState state) {
        return BuiltInRegistries.BLOCK.getKey(state.getBlock()).toString();
    }

    private static BlockState stringToBlockState(String id) {
        Block block = BuiltInRegistries.BLOCK.get(ResourceLocation.parse(id));
        return block.defaultBlockState();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("Block", this.entityData.get(BLOCK));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.entityData.set(BLOCK, tag.getString("Block"));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(BLOCK, blockStateToString(Blocks.SAND.defaultBlockState()));
    }

    public boolean isOnFire() {
        return false;
    }

    public BlockState getBlock() {
        return stringToBlockState(this.entityData.get(BLOCK));
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Level var3 = this.level();
        if (var3 instanceof ServerLevel serverlevel) {
            Entity entity1 = result.getEntity();
            Entity owner = this.getOwner();
            DamageSource damageSource = this.damageSources().fallingBlock(owner !=null? owner: this);
            entity1.hurt(damageSource, 5.0F);
            if(entity1 instanceof LivingEntity le){
                le.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 10, 0));
            }
        }
        this.discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        Level var3 = this.level();
        if(var3 instanceof ServerLevel serverlevel){
            BlockPos p = result.getBlockPos();
            Direction d = result.getDirection();
            BlockPos pos = p.relative(d);
            FallingBlockEntity.fall(serverlevel, pos, getBlock());
        }
        this.discard();
    }
    @Override
    protected boolean canHitEntity(Entity entity) {
        if (entity.getType().is(BaseModTags.EntityTypes.SAND_IMMUNE)) return false;
        return true;
    }
}
