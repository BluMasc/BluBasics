package net.blumasc.blubasics.worldgen;

import net.blumasc.blubasics.BluBasicsMod;
import net.blumasc.blubasics.entity.BaseModEntities;
import net.blumasc.blubasics.util.BaseModTags;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.List;

public class ModBiomeModifiers {
    public static final ResourceKey<BiomeModifier> SPAWN_SOLAR_BEETLE = registerKey("spawn_solar_beetle");

    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        var biomes = context.lookup(Registries.BIOME);

        context.register(SPAWN_SOLAR_BEETLE, new BiomeModifiers.AddSpawnsBiomeModifier(
                biomes.getOrThrow(BaseModTags.Biomes.SOLAR_BEETLE_SPAWNABLE),
                List.of(new MobSpawnSettings.SpawnerData(BaseModEntities.SOLAR_BEETLE.get(), 12, 2, 4))
        ));


    }

    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ResourceLocation.fromNamespaceAndPath(BluBasicsMod.MODID, name));
    }
}
