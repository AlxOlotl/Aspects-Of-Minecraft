package net.alex.aspectsofminecraft.entity.ai.goal;

import net.alex.aspectsofminecraft.entity.custom.RootMoleEntity;
import net.alex.aspectsofminecraft.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class RootMoleDigGoal extends Goal {
    private final RootMoleEntity mole;
    private int timer = 0;
    private DigPhase phase = DigPhase.NONE;

    private enum DigPhase {
        NONE,
        DIG_DOWN,
        UNDERGROUND,
        DIG_UP
    }

    public RootMoleDigGoal(RootMoleEntity mole) {
        this.mole = mole;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        if (mole.isUnderground() || mole.isDiggingDown() || mole.isDiggingUp()) return false;

        if (mole.getRandom().nextInt(300) != 0) return false;

        BlockPos below = mole.blockPosition().below();
        BlockState belowState = mole.level().getBlockState(below);
        return belowState.is(ModTags.Blocks.ROOT_MOLE_DIGGABLE);
    }

    @Override
    public boolean canContinueToUse() {
        return phase != DigPhase.NONE;
    }

    @Override
    public void start() {
        phase = DigPhase.DIG_DOWN;
        mole.setDiggingDown(true);
        mole.getNavigation().stop();
        mole.triggerAnim("controller", "dig_down");
        timer = 0;
    }

    @Override
    public void stop() {
        mole.setDiggingDown(false);
        mole.setUnderground(false);
        mole.setDiggingUp(false);
        mole.setInvisible(false);
        mole.noPhysics = false;
        mole.setNoGravity(false);
        phase = DigPhase.NONE;
        timer = 0;
    }

    @Override
    public void tick() {
        timer++;

        switch (phase) {
            case DIG_DOWN -> handleDigDown();
            case UNDERGROUND -> handleUnderground();
            case DIG_UP -> handleDigUp();
            default -> {}
        }
    }

    // -----------------------
    // --- Phase Handlers ---
    // -----------------------

    private void handleDigDown() {
        if (timer >= 20) {
            mole.setDiggingDown(false);
            mole.setUnderground(true);
            mole.setInvisible(true);
            mole.noPhysics = true;
            mole.setNoGravity(true);
            phase = DigPhase.UNDERGROUND;
            timer = 0;
        }
    }

    private void handleUnderground() {
        if (mole.getRandom().nextInt(10) == 0) {
            mole.setDeltaMovement(
                    (mole.getRandom().nextDouble() - 0.5) * 0.15,
                    0,
                    (mole.getRandom().nextDouble() - 0.5) * 0.15
            );
        }

        double surfaceY = mole.level().getHeightmapPos(
                net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                mole.blockPosition()
        ).getY();
        mole.setPos(mole.getX(), surfaceY - 0.25D, mole.getZ());

        if (mole.level() instanceof ServerLevel server && mole.tickCount % 4 == 0) {
            BlockState belowState = mole.level().getBlockState(mole.blockPosition().below());
            server.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, belowState),
                    mole.getX(), mole.getY() + 0.1, mole.getZ(),
                    4, 0.25, 0.1, 0.25, 0.02);
        }

        if (timer > 100 + mole.getRandom().nextInt(400)) {
            BlockPos above = mole.blockPosition().above();
            BlockState aboveState = mole.level().getBlockState(above);

            if (aboveState.isAir() || !aboveState.canOcclude()) {
                mole.setUnderground(false);
                mole.setDiggingUp(true);
                mole.setInvisible(false);
                mole.noPhysics = false;
                mole.setNoGravity(false);
                mole.triggerAnim("controller", "dig_up");
                phase = DigPhase.DIG_UP;
                timer = 0;
            }
        }
    }

    private void handleDigUp() {
        if (timer == 1) {
            mole.setPos(mole.getX(), mole.getY() + 1.0D, mole.getZ());
        }

        if (timer >= 80) {
            mole.setDiggingUp(false);
            mole.setInvisible(false);
            mole.noPhysics = false;
            mole.setNoGravity(false);
            phase = DigPhase.NONE;
            timer = 0;

            mole.clearAnimationCache();
            mole.triggerAnim(RootMoleEntity.CONTROLLER_NAME, "reset_idle");
        }
    }
}
