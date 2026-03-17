package net.blumasc.blubasics.entity.custom.projectile;

import net.blumasc.blubasics.entity.BaseModEntities;
import net.blumasc.blubasics.sound.BaseModSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class LightningArcEntity extends Entity {
    private static final EntityDataAccessor<Vector3f> START =
            SynchedEntityData.defineId(LightningArcEntity.class, EntityDataSerializers.VECTOR3);

    private static final EntityDataAccessor<Vector3f> END =
            SynchedEntityData.defineId(LightningArcEntity.class, EntityDataSerializers.VECTOR3);
    private int life = 5;
    public long seed;

    public LightningArcEntity(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(START, Vec3.ZERO.toVector3f());
        builder.define(END, Vec3.ZERO.toVector3f());
    }

    public LightningArcEntity(Level level, Vec3 start, Vec3 end) {
        this(BaseModEntities.LIGHTNING_ARC.get(), level);
        entityData.set(START,start.toVector3f());
        entityData.set(END,end.toVector3f());
        this.setPos(start);
        this.seed = this.random.nextLong();
    }

    public LightningArcEntity(EntityType<?> type, Level level, Vec3 start, Vec3 end) {
        this(type, level);
        entityData.set(START,start.toVector3f());
        entityData.set(END,end.toVector3f());
        this.setPos(start);
        this.seed = this.random.nextLong();
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        entityData.set(START, new Vector3f(tag.getFloat("sx"), tag.getFloat("sy"), tag.getFloat("sz")));
        entityData.set(END, new Vector3f(tag.getFloat("ex"), tag.getFloat("ey"), tag.getFloat("ez")));
        life  = tag.getInt("life");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        var start = entityData.get(START);
        var end = entityData.get(END);
        tag.putFloat("sx", start.x);
        tag.putFloat("sy", start.y);
        tag.putFloat("sz", start.z);
        tag.putFloat("ex", end.x);
        tag.putFloat("ey", end.y);
        tag.putFloat("ez", end.z);
        tag.putInt("life", life);
    }

    @Override
    public void tick() {
        if (!level().isClientSide) {
            if (life-- <= 0) {
                discard();
            }
            if (life % 20 == 0) {
                level().playSound(null, this.position().x, this.position().y, this.position().z, BaseModSounds.ELECTRIC.get(), SoundSource.WEATHER);
            }
        }
    }


    @Override
    public AABB getBoundingBoxForCulling() {
        Vector3f s = entityData.get(START);
        Vector3f e = entityData.get(END);

        Vec3 start = new Vec3(s.x, s.y, s.z);
        Vec3 end   = new Vec3(e.x, e.y, e.z);

        return new AABB(start, end).inflate(1.0);
    }

    public Vector3f getStart() { return entityData.get(START); }
    public Vec3 getDStart() {
        var s =  entityData.get(START);
        return new Vec3(s.x,s.y,s.z);
    }
    public Vector3f getEnd() { return entityData.get(END); }
}
