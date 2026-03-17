package net.blumasc.blubasics.datagen;

import net.blumasc.blubasics.block.BaseModBlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.Set;

public class ModBlockLootTableProvider  extends BlockLootSubProvider {
    protected ModBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        dropSelf(BaseModBlocks.BLUBOTT_PLUSH.get());
        dropSelf(BaseModBlocks.BLUMASC_PLUSH.get());
        dropSelf(BaseModBlocks.RIKARASHI_PLUSH.get());
        dropSelf(BaseModBlocks.SPORE_MUSHROOM_BLOCK.get());
        dropSelf(BaseModBlocks.JUMP_MUSHROOM.get());
        dropOther(BaseModBlocks.VOID_BLOCK.get(), ItemStack.EMPTY.getItem());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return BaseModBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
