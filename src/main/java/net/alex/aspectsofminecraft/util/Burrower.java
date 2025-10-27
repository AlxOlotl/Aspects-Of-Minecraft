package net.alex.aspectsofminecraft.util;

public interface Burrower {
    DigLevel getDigLevel();
    int getDigDelayTicks();

    boolean isBurrowing();
    void setBurrowing(boolean burrowing);

    default void onDigStart() {}
    default void onDigging() {}
    default void onDigEnd() {}

    default String getDigDownAnimation() { return "animation.dig_down"; }
    default String getDigLoopAnimation() { return "animation.digging"; }
    default String getDigUpAnimation() { return "animation.dig_up"; }
}