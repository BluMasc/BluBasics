package net.blumasc.blubasics.entity.custom.projectile;

import net.blumasc.blubasics.entity.BaseModEntities;
import net.blumasc.blubasics.entity.custom.helper.MeteorSphere;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MeteoriteEntity extends ThrowableProjectile {
    private static final EntityDataAccessor<Integer> RADIUS =
            SynchedEntityData.defineId(MeteoriteEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<BlockState> RENDER_BLOCK=
            SynchedEntityData.defineId(MeteoriteEntity.class, EntityDataSerializers.BLOCK_STATE);
    private static final EntityDataAccessor<BlockState> SHELL_BLOCK =
            SynchedEntityData.defineId(MeteoriteEntity.class, EntityDataSerializers.BLOCK_STATE);
    private static final EntityDataAccessor<String> CORE_BLOCK_TAG =
            SynchedEntityData.defineId(MeteoriteEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> EXPLODES =
            SynchedEntityData.defineId(MeteoriteEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> REPLACES =
            SynchedEntityData.defineId(MeteoriteEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> EVACUATES =
            SynchedEntityData.defineId(MeteoriteEntity.class, EntityDataSerializers.BOOLEAN);

    private static String blockStateToString(BlockState state) {
        return BuiltInRegistries.BLOCK.getKey(state.getBlock()).toString();
    }

    private static BlockState stringToBlockState(String id) {
        Block block = BuiltInRegistries.BLOCK.get(ResourceLocation.parse(id));
        return block.defaultBlockState();
    }

    public int getRadius() {
        return this.entityData.get(RADIUS);
    }

    public void setRadius(int radius) {
        this.entityData.set(RADIUS, radius);
    }

    public boolean getExplodes() {
        return this.entityData.get(EXPLODES);
    }

    public void setExplodes(boolean explodes) {
        this.entityData.set(EXPLODES, explodes);
    }

    public boolean getReplaces() {
        return this.entityData.get(REPLACES);
    }

    public void setReplaces(boolean replaces) {
        this.entityData.set(REPLACES, replaces);
    }

    public boolean getEvacuates() {
        return this.entityData.get(EVACUATES);
    }

    public void setEvacuates(boolean evecuates) {
        this.entityData.set(EVACUATES, evecuates);
    }

    public BlockState getRenderBlock() {
        return this.entityData.get(RENDER_BLOCK);
    }

    public BlockState getShellBlock() {
        return this.entityData.get(SHELL_BLOCK);
    }

    public TagKey<Block> coreBlock() {

        return BlockTags.create(ResourceLocation.parse(this.entityData.get(CORE_BLOCK_TAG)));
    }

    public void setRenderBlock(BlockState state) {
        this.entityData.set(RENDER_BLOCK, state);
    }

    public void setShellBlock(BlockState state) {
        this.entityData.set(SHELL_BLOCK, state);
    }

    public void setCoreBlockTag(TagKey<Block> tag) {
        this.entityData.set(CORE_BLOCK_TAG, tag.location().toString());
    }

    private BlockState getRandomCoreBlock() {
        TagKey<Block> tag = coreBlock();
        List<Block> blocks = BuiltInRegistries.BLOCK
                .getTag(tag)
                .map(holderSet -> holderSet.stream().map(holder -> holder.value()).toList())
                .orElse(List.of());

        if (blocks.isEmpty())
            return Blocks.STONE.defaultBlockState();

        Block randomBlock = blocks.get(level().random.nextInt(blocks.size()));
        return randomBlock.defaultBlockState();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(RADIUS, 5);
        builder.define(RENDER_BLOCK, Blocks.MAGMA_BLOCK.defaultBlockState());
        builder.define(SHELL_BLOCK, Blocks.OBSIDIAN.defaultBlockState());
        builder.define(CORE_BLOCK_TAG, "c:ores");
        builder.define(EXPLODES, true);
        builder.define(REPLACES, false);
        builder.define(EVACUATES, true);
    }



    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Radius", getRadius());
        tag.putString("RenderBlock", blockStateToString(getRenderBlock()));
        tag.putString("ShellBlock", blockStateToString(getShellBlock()));
        tag.putString("CoreTag", this.entityData.get(CORE_BLOCK_TAG));
        tag.putBoolean("Explodes", getExplodes());
        tag.putBoolean("Replaces", getReplaces());
        tag.putBoolean("Evacuates", getEvacuates());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains("Radius")) {
            setRadius(tag.getInt("Radius"));
        }
        if (tag.contains("Explodes")) {
            setExplodes(tag.getBoolean("Explodes"));
        }
        if (tag.contains("Replaces")) {
            setReplaces(tag.getBoolean("Replaces"));
        }
        if (tag.contains("Evacuates")) {
            setEvacuates(tag.getBoolean("Evacuates"));
        }
        if (tag.contains("RenderBlock"))
            setRenderBlock(stringToBlockState(tag.getString("RenderBlock")));

        if (tag.contains("ShellBlock"))
            setShellBlock(stringToBlockState(tag.getString("ShellBlock")));

        if (tag.contains("CoreTag"))
            this.entityData.set(CORE_BLOCK_TAG, tag.getString("CoreTag"));
    }

    public float rotationX;
    public float rotationY;
    public float rotationZ;

    public MeteoriteEntity(Level level) {
        super(BaseModEntities.METEOR.get(), level);
        initClientRotation();
    }

    public MeteoriteEntity(EntityType<? extends MeteoriteEntity> type, Level level) {
        super(type, level);
        initClientRotation();
    }

    public MeteoriteEntity(Level level, LivingEntity shooter) {
        super(BaseModEntities.METEOR.get(), shooter, level);
        initClientRotation();
    }

    private void initClientRotation() {
        RandomSource rand = level().random;
        rotationX = rand.nextFloat() * 360f;
        rotationY = rand.nextFloat() * 360f;
        rotationZ = rand.nextFloat() * 360f;
    }

    private void tickRotation() {
        float speed = 0.5f;
        rotationX = (rotationX + speed) % 360f;
        rotationY = (rotationY + speed * 1.2f) % 360f;
        rotationZ = (rotationZ + speed * 0.8f) % 360f;
    }

    public void shootFrom(LivingEntity shooter, float velocity) {
        Vec3 look = shooter.getLookAngle();
        this.setPos(shooter.getX(), shooter.getEyeY() - 0.1, shooter.getZ());
        this.setDeltaMovement(look.scale(velocity));
    }

    @Override
    public void tick() {
        super.tick();

        if (level().isClientSide) {
            if (level().random.nextInt(2) == 0) {
                spawnFallingParticles();
                spawnBottomFlames();
            }
            tickRotation();
        }
    }

    @Override
    protected void onHit(net.minecraft.world.phys.HitResult result) {
        super.onHit(result);

        if (!level().isClientSide) {
            impact(BlockPos.containing(result.getLocation()));
        }
    }


    private void spawnFallingParticles() {
        Vec3 motion = getDeltaMovement();
        if (motion.y > -0.05) return;

        RandomSource rand = level().random;
        int count = Mth.clamp((int) (-motion.y * 6), 1, 5);

        for (int i = 0; i < count; i++) {
            double angle = rand.nextDouble() * Math.PI * 2;
            double radius = 3.0 + rand.nextDouble() * 2.0;

            double ox = Math.cos(angle) * radius;
            double oz = Math.sin(angle) * radius;
            double oy = rand.nextDouble() * 2.0 - 1.0;

            level().addParticle(
                    ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    getX() + ox,
                    getY() + oy,
                    getZ() + oz,
                    0.0,
                    0.07 + rand.nextDouble() * 0.04,
                    0.0
            );
        }
    }

    private void spawnBottomFlames() {
        Vec3 motion = getDeltaMovement();
        if (motion.y > -0.1) return;

        RandomSource rand = level().random;
        int count = Mth.clamp((int) (-motion.y * 4), 1, 4);

        for (int i = 0; i < count; i++) {
            double angle = rand.nextDouble() * Math.PI * 2;
            double radius = 2.5 + rand.nextDouble() * 2.0;

            double ox = Math.cos(angle) * radius;
            double oz = Math.sin(angle) * radius;
            double oy = -3.0 + rand.nextDouble() * 0.5;

            level().addParticle(
                    ParticleTypes.FLAME,
                    getX() + ox,
                    getY() + oy,
                    getZ() + oz,
                    motion.x * -0.05,
                    -0.02,
                    motion.z * -0.05
            );
        }
    }

    private void impact(BlockPos center) {
        if(getExplodes()) {
            level().explode(
                    this,
                    center.getX() + 0.5,
                    center.getY(),
                    center.getZ() + 0.5,
                    getRadius() + 2,
                    true,
                    Level.ExplosionInteraction.TNT
            );
        }

        if(getEvacuates()) {
            evacuateEntities(center.below(getRadius()));
        }

        Map<BlockPos, BlockState> changes = new HashMap<>();
        for (BlockPos offset : MeteorSphere.positions(getRadius())) {
            BlockPos pos = center.offset(offset);

            if(level().getBlockState(pos).canBeReplaced() || (getReplaces() && !level().getBlockState(pos).is(BlockTags.WITHER_IMMUNE) )) {

                if (level().getBlockEntity(pos) != null) continue;

                BlockState state = isShell(offset)
                        ? getShellBlock()
                        : getRandomCoreBlock();

                changes.put(pos, state);
            }
        }

        changes.forEach((pos, state) -> level().setBlock(pos, state, 18));
        discard();
    }

    private void evacuateEntities(BlockPos center) {
        AABB meteorBox = new AABB(
                center.offset(-getRadius(), -getRadius(), -getRadius()).getCenter(),
                center.offset(getRadius() + 1, getRadius() + 1, getRadius() + 1).getCenter()
        );

        List<LivingEntity> entities = level().getEntitiesOfClass(LivingEntity.class, meteorBox);

        for (LivingEntity entity : entities) {
            Vec3 dir = entity.position().subtract(center.getX() + 0.5, center.getY() + 0.5, center.getZ() + 0.5);
            if (dir.lengthSqr() == 0) dir = new Vec3(0, 1, 0);

            dir = dir.normalize().scale(getRadius());
            entity.move(MoverType.SELF, dir);
            entity.setDeltaMovement(entity.getDeltaMovement().add(dir.x * 0.2, dir.y * 0.2, dir.z * 0.2));
            entity.hurt(level().damageSources().inWall(), getRadius());
        }
    }

    private boolean isShell(BlockPos p) {
        int d = p.getX() * p.getX() + p.getY() * p.getY() + p.getZ() * p.getZ();
        return d > (getRadius() - 1) * (getRadius() - 1);
    }
}
