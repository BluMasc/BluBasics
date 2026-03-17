package net.blumasc.blubasics.block;

import net.blumasc.blubasics.BluBasicsMod;
import net.blumasc.blubasics.block.entity.SporeMushroomBlockEntity;
import net.blumasc.blubasics.block.entity.VoidBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class BaseModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, BluBasicsMod.MODID);
    public static final Supplier<BlockEntityType<SporeMushroomBlockEntity>> SPORE_MUSHROOM_BE=
            BLOCK_ENTITIES.register("spore_mushroom_be", () -> BlockEntityType.Builder.of(
                    SporeMushroomBlockEntity::new, BaseModBlocks.SPORE_MUSHROOM_BLOCK.get()
            ).build(null));

    public static final Supplier<BlockEntityType<VoidBlockEntity>> VOID_BLOCK_BE=
            BLOCK_ENTITIES.register("void_block_be", () -> BlockEntityType.Builder.of(
                    VoidBlockEntity::new, BaseModBlocks.VOID_BLOCK.get()
            ).build(null));

    public static void register(IEventBus bus){
        BLOCK_ENTITIES.register(bus);
    }
}
