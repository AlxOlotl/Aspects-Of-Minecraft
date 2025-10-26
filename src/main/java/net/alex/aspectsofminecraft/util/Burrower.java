package net.alex.aspectsofminecraft.util;

public interface Burrower {
    DigLevel getDigLevel();

    int getDigDelayTicks();

    default boolean isBurrowing() {
        return false;
    }

    default void onBurrowTick() {
    }
}
