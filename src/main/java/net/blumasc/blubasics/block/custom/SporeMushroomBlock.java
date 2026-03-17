package net.blumasc.blubasics.block.custom;

import com.mojang.serialization.MapCodec;
import net.blumasc.blubasics.block.BaseModBlockEntities;
import net.blumasc.blubasics.block.entity.SporeMushroomBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class SporeMushroomBlock extends BaseEntityBlock {

    public static final MapCodec<SporeMushroomBlock> CODEC = simpleCodec(SporeMushroomBlock::new);

    public SporeMushroomBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return  Block.box((double)1.0F, (double)0.0F, (double)1.0F, (double)15.0F, (double)10.0F, (double)15.0F);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new SporeMushroomBlockEntity(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, BaseModBlockEntities.SPORE_MUSHROOM_BE.get(),
                (nlevel, blockPos, blockState, blockEntity) -> blockEntity.tick(nlevel, blockPos, blockState));
    }
    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos below = pos.below();
        BlockState ground = level.getBlockState(below);

        return ground.isFaceSturdy(level, below, Direction.UP);
    }

    @Override
    protected BlockState updateShape(BlockState state,
                                     Direction dir,
                                     BlockState neighborState,
                                     LevelAccessor level,
                                     BlockPos pos,
                                     BlockPos neighborPos) {

        if (dir == Direction.DOWN && !this.canSurvive(state, level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }

        return super.updateShape(state, dir, neighborState, level, pos, neighborPos);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.canSurvive(this.defaultBlockState(), ctx.getLevel(), ctx.getClickedPos())
                ? this.defaultBlockState()
                : null;
    }
}