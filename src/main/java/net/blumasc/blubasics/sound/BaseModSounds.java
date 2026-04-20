package net.blumasc.blubasics.sound;

import net.blumasc.blubasics.BluBasicsMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class BaseModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENT =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, BluBasicsMod.MODID);

    public static final Supplier<SoundEvent> SQUISH_PLUSHY =registerSoundEvent("squish_plushy");
    public static final Supplier<SoundEvent> ELECTRIC = registerSoundEvent("electric");
    public static final Supplier<SoundEvent> BOUNCE =registerSoundEvent("bounce");
    public static final Supplier<SoundEvent> SOLAR_BUG = registerSoundEvent("solar_bug");
    public static final Supplier<SoundEvent> SOLAR_BUG_DEATH = registerSoundEvent("solar_bug_death");
    public static final Supplier<SoundEvent> SOLAR_BUG_HURT = registerSoundEvent("solar_bug_hurt");
    public static final Supplier<SoundEvent> SOLAR_BUG_SCUTTLE = registerSoundEvent("solar_bug_scuttle");
    public static final Supplier<SoundEvent> PICKERANG = registerSoundEvent("pickerang");
    public static final Supplier<SoundEvent> EARTH_RUMBLE = registerSoundEvent("earth_rumble");

    private static Supplier<SoundEvent> registerSoundEvent(String name){
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(BluBasicsMod.MODID, name);
        return SOUND_EVENT.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENT.register(eventBus);
    }
}
