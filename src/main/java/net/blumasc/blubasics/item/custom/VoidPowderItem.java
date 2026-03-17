package net.blumasc.blubasics.item.custom;

import net.blumasc.blubasics.block.BaseModBlocks;
import net.blumasc.blubasics.block.entity.VoidBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class VoidPowderItem extends Item {
    public VoidPowderItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos pos = context.getClickedPos();
        Level world = context.getLevel();
        if(world.isClientSide()) return super.useOn(context);
        BlockState state = world.getBlockState(pos);
        ItemStack stack = context.getItemInHand();

        if (state.isAir()) return super.useOn(context);

        BlockEntity tile = world.getBlockEntity(pos);
        if (tile != null) return super.useOn(context);

        if (state.getLightEmission() > 0) {
            world.setBlock(pos, BaseModBlocks.VOID_BLOCK.get().defaultBlockState(), 3);
            BlockEntity vtile = world.getBlockEntity(pos);
            if (vtile instanceof VoidBlockEntity timedTile) {
                timedTile.setOriginalBlock(state, 300);
            }
            stack.shrink(1);
            return InteractionResult.CONSUME;
        }
        return super.useOn(context);
    }
}
