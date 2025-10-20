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

    public RootMoleDigGoal(RootMoleEntity mole) {
        this.mole = mole;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        if (mole.isDiggingDown() || mole.isDiggingUp() || mole.isUnderground())
            return false;

        BlockPos below = mole.blockPosition().below();
        BlockState belowState = mole.level().getBlockState(below);

        return belowState.is(ModTags.Blocks.DIGGABLE) && mole.getRandom().nextInt(400) == 0;
    }

    @Override
    public boolean canContinueToUse() {
        return mole.isDiggingDown() || mole.isUnderground() || mole.isDiggingUp();
    }

    @Override
    public void start() {
        mole.setDiggingDown(true);
        mole.setDiggingUp(false);
        mole.setUnderground(false);
        mole.noPhysics = true;
        mole.setNoGravity(true);
        mole.setDeltaMovement(Vec3.ZERO);
        mole.triggerAnim("controller", "dig_down");
        mole.setInvisible(false);
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
        mole.setDeltaMovement(Vec3.ZERO);
        timer = 0;
    }

    @Override
    public void tick() {
        if (mole.isDiggingDown()) {
            timer++;

            double sinkSpeed = 0.015D;
            mole.setPos(mole.getX(), mole.getY() - sinkSpeed, mole.getZ());

            if (mole.level() instanceof ServerLevel server && timer % 5 == 0) {
                BlockState below = mole.level().getBlockState(mole.blockPosition().below());
                server.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, below),
                        mole.getX(), mole.getY(), mole.getZ(),
                        3, 0.2, 0.1, 0.2, 0.03);
            }

            if (timer >= 40) {
                mole.setDiggingDown(false);
                mole.setUnderground(true);
                mole.setInvisible(true);
                mole.triggerAnim("controller", "dig");
                timer = 0;
            }
        }

        else if (mole.isUnderground()) {
            timer++;

            if (mole.getRandom().nextInt(10) == 0) {
                Vec3 move = new Vec3(
                        (mole.getRandom().nextDouble() - 0.5) * 0.1,
                        0,
                        (mole.getRandom().nextDouble() - 0.5) * 0.1
                );
                mole.setDeltaMovement(move);
            }

            if (mole.level() instanceof ServerLevel server && timer % 6 == 0) {
                BlockState below = mole.level().getBlockState(mole.blockPosition().below());
                server.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, below),
                        mole.getX(), mole.getY() + 0.2, mole.getZ(),
                        5, 0.25, 0.1, 0.25, 0.02);
            }

            if (timer > 100 + mole.getRandom().nextInt(200)) {
                BlockPos above = mole.blockPosition().above();
                if (mole.level().getBlockState(above).isAir()) {
                    mole.setUnderground(false);
                    mole.setDiggingUp(true);
                    mole.setInvisible(false);
                    mole.triggerAnim("controller", "dig_up");
                    timer = 0;
                }
            }
        }

        else if (mole.isDiggingUp()) {
            timer++;

            double riseSpeed = 0.015D;
            mole.setPos(mole.getX(), mole.getY() + riseSpeed, mole.getZ());

            if (timer >= 80) {
                mole.setDiggingUp(false);
                mole.noPhysics = false;
                mole.setNoGravity(false);
                timer = 0;
            }
        }
    }

}
