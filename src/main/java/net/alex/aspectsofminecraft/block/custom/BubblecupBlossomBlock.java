package net.alex.aspectsofminecraft.block.custom;

import net.alex.aspectsofminecraft.block.ModBlocks;
import net.alex.aspectsofminecraft.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.effect.MobEffect;

import java.util.function.Supplier;

public class BubblecupBlossomBlock extends FlowerBlock {
    public static final BooleanProperty PERMANENT = BooleanProperty.create("permanent");

    public BubblecupBlossomBlock(Supplier<MobEffect> effect, int duration, BlockBehaviour.Properties properties) {
        super(effect, duration, properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(PERMANENT, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PERMANENT);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(PERMANENT, true);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(PERMANENT)) return;

        if (!level.isRainingAt(pos.above())) {
            level.scheduleTick(pos, this, 600); // 30 seconds
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(PERMANENT)) return;

        if (!level.isRainingAt(pos.above())) {
            for (int i = 0; i < 8; i++) {
                double x = pos.getX() + 0.3 + random.nextDouble() * 0.4;
                double y = pos.getY() + 0.2 + random.nextDouble() * 0.3;
                double z = pos.getZ() + 0.3 + random.nextDouble() * 0.4;
                level.sendParticles(ParticleTypes.SPLASH, x, y, z, 1, 0.0, 0.05, 0.0, 0.05);
            }

            level.setBlock(pos, ModBlocks.BUBBLECUP.get().defaultBlockState(), 3);
        }
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return !state.getValue(PERMANENT);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos,
                                 Player player, InteractionHand hand, net.minecraft.world.phys.BlockHitResult hit) {
        ItemStack held = player.getItemInHand(hand);

        if (held.is(Items.SHEARS)) {
            if (!level.isClientSide) {
                ServerLevel server = (ServerLevel) level;
                RandomSource random = server.random;
                for (int i = 0; i < 6; i++) {
                    double x = pos.getX() + 0.3 + random.nextDouble() * 0.4;
                    double y = pos.getY() + 0.2 + random.nextDouble() * 0.3;
                    double z = pos.getZ() + 0.3 + random.nextDouble() * 0.4;
                    server.sendParticles(ParticleTypes.SPLASH, x, y, z, 1, 0.0, 0.05, 0.0, 0.05);
                    server.sendParticles(ParticleTypes.GLOW, x, y, z, 1, 0.0, 0.05, 0.0, 0.05);
                }

                Block.popResource(level, pos, new ItemStack(ModItems.BUBBLECUP_DEWDROP.get()));
                level.setBlock(pos, ModBlocks.BUBBLECUP.get().defaultBlockState(), 3);
                held.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return InteractionResult.PASS;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(PERMANENT)) {
            if (random.nextInt(10) == 0) {
                double x = pos.getX() + 0.3 + random.nextDouble() * 0.4;
                double y = pos.getY() + 0.5 + random.nextDouble() * 0.2;
                double z = pos.getZ() + 0.3 + random.nextDouble() * 0.4;
                level.addParticle(ParticleTypes.GLOW, x, y, z, 0.0, 0.01, 0.0);
            }
        }
    }
}