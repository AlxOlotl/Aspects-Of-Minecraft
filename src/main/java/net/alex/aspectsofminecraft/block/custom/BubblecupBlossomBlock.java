package net.alex.aspectsofminecraft.block.custom;

import net.alex.aspectsofminecraft.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.effect.MobEffect;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class BubblecupBlossomBlock extends FlowerBlock {
    private static final Set<BlockPos> PLAYER_PLACED = new HashSet<>();

    public BubblecupBlossomBlock(Supplier<MobEffect> effect, int duration, BlockBehaviour.Properties properties) {
        super(effect, duration, properties);
    }

    public static void markPlayerPlaced(BlockPos pos, Level level) {
        if (!level.isClientSide) {
            PLAYER_PLACED.add(pos.immutable());
        }
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (PLAYER_PLACED.contains(pos)) return;

        if (!level.isRainingAt(pos.above())) {
            level.scheduleTick(pos, this, 600); // check again in 30s
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (PLAYER_PLACED.contains(pos)) return;

        if (!level.isRainingAt(pos.above())) {
            // 💧 splash before reverting
            for (int i = 0; i < 8; i++) {
                double x = pos.getX() + 0.3 + random.nextDouble() * 0.4;
                double y = pos.getY() + 0.2 + random.nextDouble() * 0.3;
                double z = pos.getZ() + 0.3 + random.nextDouble() * 0.4;
                level.sendParticles(ParticleTypes.SPLASH, x, y, z, 1, 0.0, 0.05, 0.0, 0.05);
            }

            level.setBlock(pos, ModBlocks.BUBBLECUP.get().defaultBlockState(), 3);
        }
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }
}