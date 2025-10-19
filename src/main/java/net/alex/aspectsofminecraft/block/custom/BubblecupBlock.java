package net.alex.aspectsofminecraft.block.custom;

import net.alex.aspectsofminecraft.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import java.util.function.Supplier;

public class BubblecupBlock extends FlowerBlock {
    public static final BooleanProperty BLOOMING = BooleanProperty.create("blooming");

    public BubblecupBlock(Supplier<MobEffect> effect, int duration, BlockBehaviour.Properties properties) {
        super(effect, duration, properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(BLOOMING, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BLOOMING);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.isRainingAt(pos.above())) {
            level.setBlock(pos, ModBlocks.BUBBLECUP_BLOSSOM.get().defaultBlockState(), 3);

            for (int i = 0; i < 8; i++) {
                double x = pos.getX() + 0.3 + random.nextDouble() * 0.4;
                double y = pos.getY() + 0.2 + random.nextDouble() * 0.3;
                double z = pos.getZ() + 0.3 + random.nextDouble() * 0.4;
                level.sendParticles(ParticleTypes.SPLASH, x, y, z, 1, 0.0, 0.05, 0.0, 0.05);
            }
        }
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }
}