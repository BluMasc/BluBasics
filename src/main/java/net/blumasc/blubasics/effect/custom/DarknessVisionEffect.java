package net.blumasc.blubasics.effect.custom;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class DarknessVisionEffect extends MobEffect {
    public DarknessVisionEffect() {
        super(MobEffectCategory.HARMFUL, 0xFFFFFF);
    }
}