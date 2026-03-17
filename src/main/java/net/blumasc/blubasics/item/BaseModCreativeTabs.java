package net.blumasc.blubasics.item;

import net.blumasc.blubasics.BluBasicsMod;
import net.blumasc.blubasics.block.BaseModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class BaseModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BluBasicsMod.MODID);

    public static final Supplier<CreativeModeTab> SELECTIVE_POWERS_TAB = CREATIVE_MODE_TAB.register("blu_basics_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(BaseModItems.LIGHTNING_IN_A_BOTTLE.get())).title(Component.translatable("itemGroup.blubasics"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(BaseModItems.BEETLE_HORN);
                        output.accept(BaseModItems.SUN_HORN);
                        output.accept(BaseModItems.LIGHTNING_IN_A_BOTTLE);
                        output.accept(BaseModItems.MUSHROOM_SKEWER);
                        output.accept(BaseModItems.COOKED_MUSHROOM_SKEWER);
                        output.accept(BaseModItems.VOID_POWDER);
                        output.accept(BaseModItems.SPINE_TREE);
                        output.accept(BaseModBlocks.JUMP_MUSHROOM);
                        output.accept(BaseModBlocks.SPORE_MUSHROOM_BLOCK);
                        output.accept(BaseModItems.SOLAR_BEETLE_SPAWN_EGG);
                        output.accept(BaseModItems.CHIMERA_SPAWN_EGG);
                        output.accept(BaseModBlocks.BLUBOTT_PLUSH);
                        output.accept(BaseModBlocks.RIKARASHI_PLUSH);
                        output.accept(BaseModBlocks.BLUMASC_PLUSH);
                        output.accept(BaseModBlocks.VOID_BLOCK);
                    }).build());

    public static void register(IEventBus eventBus)
    {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
