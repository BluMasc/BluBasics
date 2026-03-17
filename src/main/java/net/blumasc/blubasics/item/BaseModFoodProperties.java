package net.blumasc.blubasics.item;

import net.blumasc.blubasics.effect.BaseModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class BaseModFoodProperties {

    public static final FoodProperties MUSHROOM_SKEWER = new FoodProperties.Builder().nutrition(4).saturationModifier(3.4f)
            .effect(new MobEffectInstance(MobEffects.LEVITATION, 240, 0,
                    false, true, true), 1.0f).build();

    public static final FoodProperties COOKED_MUSHROOM_SKEWER = new FoodProperties.Builder().nutrition(4).saturationModifier(3.4f)
            .effect(new MobEffectInstance(BaseModEffects.FALL_IMMUNITY_EFFECT, 120, 1,
                    false, true, true), 1.0f).build();
}
