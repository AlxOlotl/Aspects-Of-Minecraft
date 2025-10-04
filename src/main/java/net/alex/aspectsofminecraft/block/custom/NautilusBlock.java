package net.alex.aspectsofminecraft.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.core.particles.ParticleTypes;

public class NautilusBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public NautilusBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(BlockStateProperties.WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, BlockStateProperties.WATERLOGGED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection())
                .setValue(BlockStateProperties.WATERLOGGED,
                        context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
    }


    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(BlockStateProperties.WATERLOGGED)
                ? Fluids.WATER.getSource(false)
                : super.getFluidState(state);
    }

    @Override
    public boolean skipRendering(BlockState state, BlockState adjacentState, Direction side) {
        if (adjacentState.getBlock() instanceof NautilusBlock) {
            return true;
        }
        return super.skipRendering(state, adjacentState, side);
    }

    @SuppressWarnings("deprecation")
    public static boolean isSolidRender(BlockState state, BlockGetter world, BlockPos pos) {
        return false;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        BlockState below = level.getBlockState(pos.below());
        Direction facing = state.getValue(FACING);

        double x = pos.getX() + 0.5 - facing.getStepX() * 0.6;
        double y = pos.getY() + 0.2;
        double z = pos.getZ() + 0.5 - facing.getStepZ() * 0.6;
        double spread = 0.5;

        if (below.is(Blocks.FIRE) || below.is(Blocks.SOUL_FIRE)) {
            for (int i = 0; i < 5; i++) {
                level.addParticle(ParticleTypes.CLOUD,
                        x + (random.nextDouble() - 0.5) * spread,
                        y + (random.nextDouble() - 0.5) * spread,
                        z + (random.nextDouble() - 0.5) * spread,
                        facing.getStepX() * 0.1, 0, facing.getStepZ() * 0.1);
            }
        } else if (state.getValue(BlockStateProperties.WATERLOGGED)) {
            for (int i = 0; i < 5; i++) {
                level.addParticle(ParticleTypes.BUBBLE,
                        x + (random.nextDouble() - 0.5) * spread,
                        y + (random.nextDouble() - 0.5) * spread,
                        z + (random.nextDouble() - 0.5) * spread,
                        facing.getStepX() * 0.05,
                        random.nextDouble() * 0.02,
                        facing.getStepZ() * 0.05);
            }
        }
    }
}
