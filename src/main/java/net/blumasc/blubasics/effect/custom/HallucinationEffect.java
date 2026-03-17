package net.blumasc.blubasics.effect.custom;

import net.blumasc.blubasics.effect.custom.helper.ClientHallucinationHandler;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class HallucinationEffect extends MobEffect {

    public HallucinationEffect() {
        super(MobEffectCategory.HARMFUL, 0x00ff9d);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (!(entity instanceof Player player)) return false;

        if (player.level().isClientSide) {
            ClientHallucinationHandler.tick(player, amplifier);
        }
        return true;
    }
}
