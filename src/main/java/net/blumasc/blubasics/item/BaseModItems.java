package net.blumasc.blubasics.item;

import net.blumasc.blubasics.BluBasicsMod;
import net.blumasc.blubasics.entity.BaseModEntities;
import net.blumasc.blubasics.item.custom.CurioItem;
import net.blumasc.blubasics.item.custom.LightningInABottleItem;
import net.blumasc.blubasics.item.custom.SkewerItem;
import net.blumasc.blubasics.item.custom.VoidPowderItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BaseModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(BluBasicsMod.MODID);

    public static final DeferredItem<Item> BEETLE_HORN = ITEMS.register("beetle_horn",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> SUN_HORN= ITEMS.register("sun_horn",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> LIGHTNING_IN_A_BOTTLE= ITEMS.register("lightning_bottle",
            () -> new LightningInABottleItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> MUSHROOM_SKEWER= ITEMS.register("mushroom_skewer",
            () -> new SkewerItem(new Item.Properties().food(BaseModFoodProperties.MUSHROOM_SKEWER)));

    public static final DeferredItem<Item> COOKED_MUSHROOM_SKEWER= ITEMS.register("cooked_mushroom_skewer",
            () -> new SkewerItem(new Item.Properties().food(BaseModFoodProperties.COOKED_MUSHROOM_SKEWER)));

    public static final DeferredItem<Item> SPINE_TREE = ITEMS.register("spine_tree",
            () -> new CurioItem(new Item.Properties()));

    public static final DeferredItem<Item> VOID_POWDER = ITEMS.register("void_powder",
            () -> new VoidPowderItem(new Item.Properties().stacksTo(16)));

    public static final DeferredItem<Item> SOLAR_BEETLE_SPAWN_EGG = ITEMS.register("solar_beetle_spawn_egg",
            () -> new DeferredSpawnEggItem(BaseModEntities.SOLAR_BEETLE, 0x721011, 0xfff123, new Item.Properties()));

    public static final DeferredItem<Item> CHIMERA_SPAWN_EGG = ITEMS.register("chimera_spawn_egg",
            () -> new DeferredSpawnEggItem(BaseModEntities.CHIMERA, 0xf0efec, 0xe27c21, new Item.Properties()));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
