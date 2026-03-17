package net.blumasc.blubasics.block.custom;

import com.mojang.serialization.MapCodec;
import net.blumasc.blubasics.sound.BaseModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PlushBlock extends HorizontalDirectionalBlock implements Equipable{
    public static final MapCodec<PlushBlock> CODEC = simpleCodec(PlushBlock::new);
    private static final VoxelShape SHAPE = Block.box(3.0, 0.0, 3.0, 13.0, 15.0, 13.0);
    public static final EnumProperty<SquishType> SQUISH_TYPE = EnumProperty.create(
            "squish_type", SquishType.class
    );

    public enum SquishType implements StringRepresentable {
        NONE("none"),
        TYPE1("type1"),
        TYPE2("type2");

        private final String name;
        SquishType(String name) { this.name = name; }
        @Override
        public String getSerializedName() { return name; }
    }

    public PlushBlock(Properties properties){
        super(properties);

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(SQUISH_TYPE, SquishType.NONE));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return SHAPE;
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context){
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder){
        builder.add(FACING, SQUISH_TYPE);
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.HEAD;
    }

    @Override
    public Holder<SoundEvent> getEquipSound() {
        return Holder.direct(BaseModSounds.SQUISH_PLUSHY.get());
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player,
                                 BlockHitResult hit) {

        world.playSound(player, pos, BaseModSounds.SQUISH_PLUSHY.get(), SoundSource.BLOCKS);

        SquishType type = world.getRandom().nextBoolean() ? SquishType.TYPE1 : SquishType.TYPE2;

        state = state.setValue(SQUISH_TYPE, type);

        world.setBlock(pos, state, 10);

        world.scheduleTick(pos, this, 5);

        return InteractionResult.SUCCESS;
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        state = state.setValue(SQUISH_TYPE, SquishType.NONE);

        world.setBlock(pos, state, 10);
    }
}
