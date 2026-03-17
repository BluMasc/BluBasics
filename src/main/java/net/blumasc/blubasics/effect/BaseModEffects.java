package net.blumasc.blubasics.effect;

import net.blumasc.blubasics.BluBasicsMod;
import net.blumasc.blubasics.effect.custom.DarknessVisionEffect;
import net.blumasc.blubasics.effect.custom.FallImmunityEffect;
import net.blumasc.blubasics.effect.custom.HallucinationEffect;
import net.blumasc.blubasics.effect.custom.SpikedEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BaseModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS= DeferredRegister.create(
            BuiltInRegistries.MOB_EFFECT, BluBasicsMod.MODID
    );

    public static final Holder<MobEffect> SPIKED = MOB_EFFECTS.register("spiked",
            () -> new SpikedEffect());

    public static final Holder<MobEffect> HALLUCINATION =
            MOB_EFFECTS.register("hallucination", HallucinationEffect::new);

    public static final Holder<MobEffect> VOID_EFFECT = MOB_EFFECTS.register("void",
            () -> new DarknessVisionEffect());

    public static final Holder<MobEffect> FALL_IMMUNITY_EFFECT = MOB_EFFECTS.register("fall_immunity",
            () -> new FallImmunityEffect()
                    .addAttributeModifier(Attributes.FALL_DAMAGE_MULTIPLIER, ResourceLocation.fromNamespaceAndPath(BluBasicsMod.MODID, "fall_imunity"), -1.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

    public static void register(IEventBus eventBus)
    {
        MOB_EFFECTS.register(eventBus);
    }
}
