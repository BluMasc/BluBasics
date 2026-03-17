package net.blumasc.blubasics.effect.custom;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class FallImmunityEffect extends MobEffect {
    public FallImmunityEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xffffff);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return false;
    }

}
