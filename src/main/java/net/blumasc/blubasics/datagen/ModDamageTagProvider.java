package net.blumasc.blubasics.datagen;

import net.blumasc.blubasics.BluBasicsMod;
import net.blumasc.blubasics.damage.BaseModDamageTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModDamageTagProvider extends DamageTypeTagsProvider {
    public ModDamageTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, BluBasicsMod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(DamageTypeTags.NO_KNOCKBACK)
                .add(BaseModDamageTypes.SPIKE_DAMAGE);
        tag(DamageTypeTags.BYPASSES_SHIELD)
                .add(BaseModDamageTypes.SPIKE_DAMAGE);
    }
}