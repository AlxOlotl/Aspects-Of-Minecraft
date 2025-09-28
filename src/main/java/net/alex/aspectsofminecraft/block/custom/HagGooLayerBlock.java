package net.alex.aspectsofminecraft.block.custom;

import net.alex.aspectsofminecraft.effect.ModEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class HagGooLayerBlock extends SnowLayerBlock {

    public HagGooLayerBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    // ✅ No collision
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return net.minecraft.world.phys.shapes.Shapes.empty();
    }

    // ✅ Apply Hagged effect when inside
    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(ModEffects.HAGGED.get(), 200, 0)); // 200 ticks = 10s
        }
        super.entityInside(state, level, pos, entity);
    }
}
