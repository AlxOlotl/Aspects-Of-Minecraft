package net.alex.aspectsofminecraft.util;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.Set;

public enum DigLevel {
    SURFACE(Set.of(
            Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.SAND, Blocks.RED_SAND, Blocks.GRAVEL
    )),
    INNER(Set.of(
            Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.SAND, Blocks.GRAVEL,
            Blocks.STONE, Blocks.COBBLESTONE, Blocks.ANDESITE, Blocks.DIORITE, Blocks.GRANITE
    )),
    DEEP(Set.of(
            Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.SAND, Blocks.GRAVEL,
            Blocks.STONE, Blocks.COBBLESTONE, Blocks.ANDESITE, Blocks.DIORITE, Blocks.GRANITE,
            Blocks.DEEPSLATE, Blocks.COBBLED_DEEPSLATE, Blocks.TUFF
    ));

    private final Set<Block> breakableBlocks;

    DigLevel(Set<Block> blocks) {
        this.breakableBlocks = blocks;
    }

    public boolean canDig(Block block) {
        return breakableBlocks.contains(block);
    }

    public boolean includes(DigLevel other) {
        return this.ordinal() >= other.ordinal();
    }
}