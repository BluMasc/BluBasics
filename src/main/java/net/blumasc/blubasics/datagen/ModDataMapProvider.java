package net.blumasc.blubasics.datagen;

import net.blumasc.blubasics.item.BaseModItems;
import net.blumasc.blubasics.util.BaseModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import java.util.concurrent.CompletableFuture;

public class ModDataMapProvider extends DataMapProvider {
    protected ModDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        this.builder(NeoForgeDataMaps.FURNACE_FUELS)
                .add(BaseModItems.SUN_HORN, new FurnaceFuel(2400), false);

        this.builder(NeoForgeDataMaps.COMPOSTABLES)
                .add(BaseModTags.Items.COMPOSTABLE_SHROOMS, new Compostable(0.40f), false);
    }
}
