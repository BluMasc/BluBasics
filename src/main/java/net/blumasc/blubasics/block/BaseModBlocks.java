package net.blumasc.blubasics.block;

import net.blumasc.blubasics.BluBasicsMod;
import net.blumasc.blubasics.block.custom.JumpingMushroomBlock;
import net.blumasc.blubasics.block.custom.PlushBlock;
import net.blumasc.blubasics.block.custom.SporeMushroomBlock;
import net.blumasc.blubasics.block.custom.VoidBlock;
import net.blumasc.blubasics.item.BaseModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class BaseModBlocks {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(BluBasicsMod.MODID);

    public static  final DeferredBlock<Block> BLUMASC_PLUSH = registerBlock("blumasc_plush",
            () -> new PlushBlock(BlockBehaviour.Properties.of().noOcclusion().strength(0f).sound(SoundType.WOOL)));

    public static  final DeferredBlock<Block> RIKARASHI_PLUSH = registerBlock("rikarashi_plush",
            () -> new PlushBlock(BlockBehaviour.Properties.of().noOcclusion().strength(0f).sound(SoundType.WOOL)));

    public static  final DeferredBlock<Block> BLUBOTT_PLUSH = registerBlock("blubott_plush",
            () -> new PlushBlock(BlockBehaviour.Properties.of().noOcclusion().strength(0f).sound(SoundType.WOOL)));

    public static final DeferredBlock<VoidBlock> VOID_BLOCK = registerBlock("void_block",
            () -> new VoidBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BLACK_WOOL).noOcclusion().noCollission()));

    public static final DeferredBlock<Block> JUMP_MUSHROOM = registerBlock("jump_mushroom",
            () -> new JumpingMushroomBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MUSHROOM_STEM).noCollission().noOcclusion().mapColor(MapColor.COLOR_LIGHT_GREEN)));

    public static final DeferredBlock<SporeMushroomBlock> SPORE_MUSHROOM_BLOCK = registerBlock("spore_mushroom",
            () -> new SporeMushroomBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MUSHROOM_STEM).noOcclusion().mapColor(MapColor.COLOR_BLACK)));

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block){
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block)
    {
        BaseModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}
