package net.blumasc.blubasics.effect.custom;

import net.blumasc.blubasics.damage.BaseModDamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SpikedEffect extends MobEffect {
    public SpikedEffect() {
        super(MobEffectCategory.HARMFUL, 0x735451);
    }

    static Map<UUID, Vec3> lastPos = new HashMap<>();

    @Override
    public void onEffectAdded(LivingEntity livingEntity, int amplifier) {
        lastPos.put(livingEntity.getUUID(), livingEntity.position());
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide) {
            Vec3 prev = lastPos.get(entity.getUUID());
            if (prev != null) {
                double dx = entity.getX() - prev.x;
                double dy = entity.getY() - prev.y;
                double dz = entity.getZ() - prev.z;
                double speedSqr = dx*dx + dz*dz + dy*dy;
                if(entity.tickCount%20 == 0)
                {
                    if(speedSqr>0.02)
                    {
                        float damageAmount = 1.0F * (amplifier + 1);
                        entity.hurt(BaseModDamageTypes.spikeDamage(entity.level()), damageAmount);
                    }
                }
            }
            lastPos.put(entity.getUUID(), entity.position());
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
