package net.blumasc.blubasics.datagen;

import net.blumasc.blubasics.BluBasicsMod;
import net.blumasc.blubasics.entity.BaseModEntities;
import net.blumasc.blubasics.util.BaseModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModEntityTagProvider extends EntityTypeTagsProvider {
    public ModEntityTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, BluBasicsMod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BaseModTags.EntityTypes.CHIMERA_LIKE_FOX)
                .add(EntityType.FOX);
        tag(BaseModTags.EntityTypes.CHIMERA_LIKE_CHICKEN)
                .add(EntityType.CHICKEN);
        tag(BaseModTags.EntityTypes.CHIMERA_LIKE_GOAT)
                .add(EntityType.GOAT);
        tag(BaseModTags.EntityTypes.CHIMERA_LIKE_GUARDIAN)
                .add(EntityType.ELDER_GUARDIAN)
                .add(EntityType.GUARDIAN);
        tag(BaseModTags.EntityTypes.CHIMERA_LIKE_PHANTOM)
                .add(EntityType.PHANTOM);
        tag(BaseModTags.EntityTypes.CHIMERA_LIKE_RABBIT)
                .add(EntityType.RABBIT);
        tag(BaseModTags.EntityTypes.CHIMERA_LIKE_HOGLIN)
                .add(EntityType.HOGLIN)
                .add(EntityType.ZOGLIN);
        tag(BaseModTags.EntityTypes.CHIMERA_LIKE)
                .addTag(BaseModTags.EntityTypes.CHIMERA_LIKE_FOX)
                .addTag(BaseModTags.EntityTypes.CHIMERA_LIKE_CHICKEN)
                .addTag(BaseModTags.EntityTypes.CHIMERA_LIKE_GOAT)
                .addTag(BaseModTags.EntityTypes.CHIMERA_LIKE_GUARDIAN)
                .addTag(BaseModTags.EntityTypes.CHIMERA_LIKE_PHANTOM)
                .addTag(BaseModTags.EntityTypes.CHIMERA_LIKE_RABBIT)
                .addTag(BaseModTags.EntityTypes.CHIMERA_LIKE_HOGLIN);
        tag(EntityTypeTags.ARTHROPOD)
                .add(BaseModEntities.SOLAR_BEETLE.get());
        tag(EntityTypeTags.SENSITIVE_TO_BANE_OF_ARTHROPODS)
                .add(BaseModEntities.SOLAR_BEETLE.get());
        tag(BaseModTags.EntityTypes.SAND_IMMUNE)
                .add(EntityType.HUSK)
                .add(EntityType.CAMEL);
    }
}
