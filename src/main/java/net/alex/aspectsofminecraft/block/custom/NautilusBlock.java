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
        // prevents water overlay
        return state.getValue(BlockStateProperties.WATERLOGGED)
                ? Fluids.WATER.getSource(false)
                : super.getFluidState(state);
    }

    @Override
    public boolean skipRendering(BlockState state, BlockState adjacentState, Direction side) {
        // hide faces when next to another NautilusBlock
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
        if (below.is(Blocks.FIRE) || below.is(Blocks.SOUL_FIRE)) {
            Direction facing = state.getValue(FACING);
            double x = pos.getX() + 0.5 + facing.getStepX() * 0.6;
            double y = pos.getY() + 0.5;
            double z = pos.getZ() + 0.5 + facing.getStepZ() * 0.6;
            double spread = 0.2;
            double speed = 0.1 + random.nextDouble() * 0.15;

            for (int i = 0; i < 5; i++) {
                double offsetX = x + (random.nextDouble() - 0.5) * spread;
                double offsetY = y + (random.nextDouble() - 0.5) * spread;
                double offsetZ = z + (random.nextDouble() - 0.5) * spread;

                if (level.getFluidState(pos).isEmpty()) {
                    level.addParticle(ParticleTypes.CLOUD,
                            offsetX, offsetY, offsetZ,
                            facing.getStepX() * speed, 0, facing.getStepZ() * speed);
                } else {
                    level.addParticle(ParticleTypes.BUBBLE,
                            offsetX, offsetY, offsetZ,
                            facing.getStepX() * speed, 0, facing.getStepZ() * speed);
                }
            }
        }
    }
}
