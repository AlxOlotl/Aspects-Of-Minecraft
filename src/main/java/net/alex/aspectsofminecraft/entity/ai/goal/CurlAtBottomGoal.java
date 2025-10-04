package net.alex.aspectsofminecraft.entity.ai.goal;

import net.alex.aspectsofminecraft.entity.custom.HagfishEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class CurlAtBottomGoal extends Goal {
    private final HagfishEntity hagfish;
    private int duration;

    public CurlAtBottomGoal(HagfishEntity hagfish) {
        this.hagfish = hagfish;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return hagfish.isInWater() && !hagfish.isCurled() && hagfish.getRandom().nextInt(2000) == 0;
    }

    @Override
    public void start() {
        hagfish.getNavigation().stop();
        hagfish.setCurled(true);
        hagfish.curlTicks = 0;
        duration = 20 * (180 + hagfish.getRandom().nextInt(120));
    }

    @Override
    public boolean canContinueToUse() {
        return hagfish.isCurled() && hagfish.curlTicks < duration;
    }

    @Override
    public void tick() {
        hagfish.setDeltaMovement(0, -0.01D, 0);
        hagfish.curlTicks++;
    }

    @Override
    public void stop() {
        hagfish.setCurled(false);
    }
}
