package net.alex.aspectsofminecraft.entity.ai.goal;

import net.alex.aspectsofminecraft.util.Burrower;
import net.alex.aspectsofminecraft.util.DigLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.EnumSet;
import java.util.Set;

public class BurrowMoveGoal extends Goal {
    private final PathfinderMob mob;
    private final Burrower burrower;

    private static final Set<Block> SURFACE_BLOCKS = Set.of(
            Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.SAND, Blocks.GRAVEL
    );
    private static final Set<Block> INNER_BLOCKS = Set.of(
            Blocks.STONE, Blocks.ANDESITE, Blocks.DIORITE, Blocks.GRANITE
    );
    private static final Set<Block> DEEP_BLOCKS = Set.of(
            Blocks.DEEPSLATE, Blocks.TUFF
    );

    public BurrowMoveGoal(PathfinderMob mob) {
        this.mob = mob;
        if (!(mob instanceof Burrower b))
            throw new IllegalArgumentException("Mob must implement Burrower!");
        this.burrower = b;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        BlockPos pos = mob.blockPosition();
        BlockState state = mob.level().getBlockState(pos);
        return isDiggable(state.getBlock(), burrower.getDigLevel());
    }

    @Override
    public void tick() {
        BlockPos pos = mob.blockPosition();
        BlockState state = mob.level().getBlockState(pos);

        boolean canBurrow = isDiggable(state.getBlock(), burrower.getDigLevel());
        if (canBurrow) {
            mob.setNoGravity(true);
            mob.noPhysics = true;

            mob.setDeltaMovement(mob.getDeltaMovement().scale(0.6));

            if (mob.level() instanceof ServerLevel server) {
                server.sendParticles(
                        new BlockParticleOption(ParticleTypes.BLOCK, state), // <-- wrap with state
                        mob.getX(), mob.getY(), mob.getZ(),
                        2, 0.1, 0.1, 0.1, 0.02
                );
            }

            burrower.onBurrowTick();
        } else {
            mob.noPhysics = false;
            mob.setNoGravity(false);
        }
    }

    @Override
    public boolean canContinueToUse() {
        BlockPos pos = mob.blockPosition();
        return isDiggable(mob.level().getBlockState(pos).getBlock(), burrower.getDigLevel());
    }

    private boolean isDiggable(Block block, DigLevel level) {
        return switch (level) {
            case SURFACE -> SURFACE_BLOCKS.contains(block);
            case INNER -> SURFACE_BLOCKS.contains(block) || INNER_BLOCKS.contains(block);
            case DEEP -> SURFACE_BLOCKS.contains(block) || INNER_BLOCKS.contains(block) || DEEP_BLOCKS.contains(block);
        };
    }
}