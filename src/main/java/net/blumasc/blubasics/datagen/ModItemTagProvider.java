package net.blumasc.blubasics.datagen;

import net.blumasc.blubasics.BluBasicsMod;
import net.blumasc.blubasics.block.BaseModBlocks;
import net.blumasc.blubasics.item.BaseModItems;
import net.blumasc.blubasics.util.BaseModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosTags;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {

    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, BluBasicsMod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BaseModTags.Items.SOLAR_BEETLE_FOOD)
                .add(Items.QUARTZ)
                .add(BaseModItems.SUN_HORN.get());

        tag(BaseModTags.Items.COMPOSTABLE_SHROOMS)
                .add(BaseModBlocks.JUMP_MUSHROOM.asItem())
                .add(BaseModBlocks.SPORE_MUSHROOM_BLOCK.asItem());

        tag(CuriosTags.BODY)
                .add(BaseModItems.SPINE_TREE.get());

    }
}