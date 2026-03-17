package net.blumasc.blubasics.damage;

import net.blumasc.blubasics.BluBasicsMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.Level;

public class BaseModDamageTypes {
    public static final ResourceKey<DamageType> SPIKE_DAMAGE =
            ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(BluBasicsMod.MODID, "spike"));


    public static DamageSource spikeDamage(Level level) {
        return new DamageSource(
                level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(SPIKE_DAMAGE));
    }
}
