package net.alex.aspectsofminecraft.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;

public class HagGooLayerBlock extends SnowLayerBlock {

    public HagGooLayerBlock() {
        super(Properties.of()
                .strength(0.1F)
                .noCollission()
                .friction(0.1F) // Very slippery (optional)
                .speedFactor(0.4F) // Slow movement speed
                .jumpFactor(0.8F) // Optional: reduce jump height slightly
                .isValidSpawn((state, level, pos, type) -> false)); // Prevent mob spawning
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        super.stepOn(level, pos, state, entity);
        // Additional logic if needed
    }
}
