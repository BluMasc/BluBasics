package net.blumasc.blubasics.block.custom;

import net.blumasc.blubasics.effect.BaseModEffects;
import net.blumasc.blubasics.sound.BaseModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Vector3f;

public class JumpingMushroomBlock extends Block {

    public static final BooleanProperty SPAWNED = BooleanProperty.create("spawned");

    public JumpingMushroomBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(SPAWNED, false));
    }

    @Override
    public VoxelShape getCollisionShape(
            BlockState state,
            BlockGetter level,
            BlockPos pos,
            CollisionContext context
    ) {
        return Shapes.empty();
    }

    @Override
    public VoxelShape getOcclusionShape(
            BlockState state,
            BlockGetter level,
            BlockPos pos
    ) {
        return Shapes.empty();
    }

    @Override
    public boolean isOcclusionShapeFullBlock(BlockState state, BlockGetter level, BlockPos pos) {
        return false;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return  Block.box((double)0.0F, (double)0.0F, (double)0.0F, (double)16.0F, (double)12.0F, (double)16.0F);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (level.isClientSide) return;
        if (!(entity instanceof LivingEntity living)) return;

        boolean spawned = state.getValue(SPAWNED);

        double bounceHeight = spawned ? 20.0D : 10.0D;

        double velocityY = Math.sqrt(2 * 0.08D * bounceHeight);

        Vec3 motion = entity.getDeltaMovement();
        entity.setDeltaMovement(motion.x, velocityY, motion.z);
        entity.hurtMarked = true;

        level.playSound(
                null,
                pos,
                BaseModSounds.BOUNCE.get(),
                SoundSource.BLOCKS,
                1.0F,
                (entity.getRandom().nextFloat()*0.6f)+0.5f
        );

        DustParticleOptions dust = new DustParticleOptions(
                new Vector3f(0.7F, 0.9F, 0.7F),
                1.4F
        );

        int count = 32;

        ((ServerLevel) level).sendParticles(
                dust,
                pos.getX() + 0.5D,
                pos.getY() + 0.2D,
                pos.getZ() + 0.5D,
                count,
                0.6D,
                0.15D,
                0.6D,
                0.05D
        );
        living.addEffect(new MobEffectInstance(
                BaseModEffects.FALL_IMMUNITY_EFFECT,
                spawned?120:60,
                0,
                false,
                false,
                false
        ));
        if(spawned){
            goAway(state, (ServerLevel) level, pos);
        }
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

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean moved) {
        if (level.isClientSide) return;
        if (!state.getValue(SPAWNED)) return;

        RandomSource random = level.getRandom();
        int delay = 100 + random.nextInt(201);

        level.scheduleTick(pos, this, delay);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        goAway(state, level, pos);
    }

    public void goAway(BlockState b, ServerLevel l, BlockPos pos){
        if (b.getValue(SPAWNED)) {
            l.removeBlock(pos, false);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SPAWNED);
    }

}
