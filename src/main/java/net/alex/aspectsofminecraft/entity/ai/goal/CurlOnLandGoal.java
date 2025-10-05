package net.alex.aspectsofminecraft.entity.ai.goal;

import net.alex.aspectsofminecraft.entity.custom.HagfishEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class CurlOnLandGoal extends Goal {
    private final HagfishEntity hagfish;

    public CurlOnLandGoal(HagfishEntity hagfish) {
        this.hagfish = hagfish;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return !hagfish.isInWaterRainOrBubble() && !hagfish.isCurled();
    }

    @Override
    public void start() {
        hagfish.getNavigation().stop();
        hagfish.setCurled(true);
    }

    @Override
    public void tick() {
        var motion = hagfish.getDeltaMovement();
        hagfish.setDeltaMovement(0, motion.y - 0.08D, 0);
        hagfish.curlTicks++;
    }


    @Override
    public boolean canContinueToUse() {
        return !hagfish.isInWaterRainOrBubble();
    }

    @Override
    public void stop() {
        hagfish.setCurled(false);
    }
}
