package net.blumasc.blubasics.block.entity;

import net.blumasc.blubasics.block.BaseModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public class VoidBlockEntity extends BlockEntity {
    private BlockState originalBlock = Blocks.AIR.defaultBlockState();
    private int timer = 100;
    public VoidBlockEntity(BlockPos pos, BlockState blockState) {
        super(BaseModBlockEntities.VOID_BLOCK_BE.get(), pos, blockState);
    }

    public void setOriginalBlock(BlockState state, int timerTicks) {
        this.originalBlock = state;
        this.timer = timerTicks;
        setChanged();
    }

    public void tick() {
        if (level == null || level.isClientSide) return;

        timer--;
        if (timer <= 0) {
            revertBlock();
        }
    }

    public void revertBlock() {
        if (level == null) return;

        level.setBlockAndUpdate(worldPosition, originalBlock);

        level.removeBlockEntity(worldPosition);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        timer = tag.getInt("timer");
        if (tag.contains("originalBlock")) {
            BlockState.CODEC.parse(
                            registries.createSerializationContext(NbtOps.INSTANCE),
                            tag.get("originalBlock")
                    ).resultOrPartial(System.err::println)
                    .ifPresent(state -> originalBlock = state);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag,registries);
        tag.putInt("timer", timer);
        BlockState.CODEC.encodeStart(
                        registries.createSerializationContext(NbtOps.INSTANCE),
                        originalBlock
                ).resultOrPartial(System.err::println)
                .ifPresent(nbt -> tag.put("originalBlock", nbt));
    }

    private static <T extends Comparable<T>> BlockState setProperty(BlockState state, Property<T> property, String value) {
        T parsed = property.getValue(value).orElse(null);
        if (parsed != null) state = state.setValue(property, parsed);
        return state;
    }



}
