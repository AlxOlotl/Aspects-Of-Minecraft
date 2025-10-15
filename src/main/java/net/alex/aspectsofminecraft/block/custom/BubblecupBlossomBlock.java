package net.alex.aspectsofminecraft.block.custom;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Supplier;

public class BubblecupBlossomBlock extends FlowerBlock {
    public BubblecupBlossomBlock(Supplier<MobEffect> effect, int duration, BlockBehaviour.Properties properties) {
        super(effect, duration, properties);
    }
}
