package net.alex.aspectsofminecraft.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

import java.util.function.Supplier;

public class BubblecupBlock extends FlowerBlock {
    public static final BooleanProperty BLOOMING = BooleanProperty.create("blooming");
    private static final int CHECK_TICKS = 40;

    public BubblecupBlock(Supplier<MobEffect> effect, int duration, BlockBehaviour.Properties properties) {
        super(effect, duration, properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(BLOOMING, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BLOOMING);
    }

    /* ---------- Periodic bloom check ---------- */

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (!level.isClientSide) {
            level.scheduleTick(pos, this, CHECK_TICKS);
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        boolean blooming = state.getValue(BLOOMING);
        boolean raining = level.isRainingAt(pos.above());

        if (!blooming && raining) {
            level.setBlock(pos, state.setValue(BLOOMING, true), 3);

            for (int i = 0; i < 12; i++) {
                double x = pos.getX() + 0.5;
                double y = pos.getY() + 0.4 + random.nextDouble() * 0.4;
                double z = pos.getZ() + 0.5;

                double vx = (random.nextDouble() - 0.5) * 0.4;
                double vy = random.nextDouble() * 0.1;
                double vz = (random.nextDouble() - 0.5) * 0.4;
                level.sendParticles(ParticleTypes.DRIPPING_WATER, x, y, z, 0, vx, vy, vz, 1.0);
            }
        }

        else if (blooming && !raining) {
            level.setBlock(pos, state.setValue(BLOOMING, false), 3);

            for (int i = 0; i < 6; i++) {
                double x = pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.4;
                double y = pos.getY() + 0.7 + random.nextDouble() * 0.4;
                double z = pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.4;
                level.sendParticles(ParticleTypes.SPLASH, x, y, z, 1, 0, -0.05, 0, 0.02);
            }
        }

        level.scheduleTick(pos, this, CHECK_TICKS);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return !state.getValue(BLOOMING);
    }

    /* ---------- Player interaction: bucket to reset ---------- */

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos,
                                 Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack held = player.getItemInHand(hand);

        if (state.getValue(BLOOMING) && held.is(Items.BUCKET)) {
            if (!level.isClientSide) {
                if (!player.getAbilities().instabuild) {
                    held.shrink(1);
                    player.addItem(new ItemStack(Items.WATER_BUCKET));
                }

                level.setBlock(pos, state.setValue(BLOOMING, false), 3);
                RandomSource random = level.random;
                for (int i = 0; i < 6; i++) {
                    double x = pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.4;
                    double y = pos.getY() + 0.7 + random.nextDouble() * 0.4;
                    double z = pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.4;
                    ((ServerLevel) level).sendParticles(ParticleTypes.SPLASH, x, y, z, 1, 0, -0.05, 0, 0.02);
                }
                level.scheduleTick(pos, this, CHECK_TICKS);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }
}