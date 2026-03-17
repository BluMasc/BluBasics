package net.blumasc.blubasics.datagen;

import net.blumasc.blubasics.BluBasicsMod;
import net.blumasc.blubasics.util.BaseModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biomes;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBiomeTagProvider extends BiomeTagsProvider {
    public ModBiomeTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, BluBasicsMod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BaseModTags.Biomes.SOLAR_BEETLE_SPAWNABLE)
                .addTag(BiomeTags.HAS_PILLAGER_OUTPOST)
                .addTag(BiomeTags.HAS_VILLAGE_PLAINS)
                .addTag(BiomeTags.HAS_WOODLAND_MANSION);

    }
}
