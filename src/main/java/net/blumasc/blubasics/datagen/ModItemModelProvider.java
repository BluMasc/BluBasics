package net.blumasc.blubasics.datagen;

import net.blumasc.blubasics.BluBasicsMod;
import net.blumasc.blubasics.block.BaseModBlocks;
import net.blumasc.blubasics.item.BaseModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;

public class ModItemModelProvider  extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, BluBasicsMod.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(BaseModItems.BEETLE_HORN.get());
        basicItem(BaseModItems.SUN_HORN.get());
        basicItem(BaseModItems.LIGHTNING_IN_A_BOTTLE.get());
        basicItem(BaseModItems.MUSHROOM_SKEWER.get());
        basicItem(BaseModItems.COOKED_MUSHROOM_SKEWER.get());
        basicItem(BaseModItems.SPINE_TREE.get());
        basicItem(BaseModBlocks.VOID_BLOCK.asItem());
        basicItem(BaseModItems.VOID_POWDER.asItem());
        spawnEggItem(BaseModItems.SOLAR_BEETLE_SPAWN_EGG);
        spawnEggItem(BaseModItems.CHIMERA_SPAWN_EGG);

    }


    private ItemModelBuilder spawnEggItem(DeferredItem<?> item){
        return withExistingParent(item.getId().getPath(), mcLoc("item/template_spawn_egg"));
    }
}
