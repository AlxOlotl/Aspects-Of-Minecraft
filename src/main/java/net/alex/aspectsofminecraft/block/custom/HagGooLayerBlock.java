package net.alex.aspectsofminecraft.block.custom;

import net.alex.aspectsofminecraft.effect.ModEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Collections;
import java.util.List;

public class HagGooLayerBlock extends FallingBlock implements SimpleWaterloggedBlock {
    public static final IntegerProperty LAYERS = BlockStateProperties.LAYERS;
    public static final BooleanProperty PROJECTILE_PLACED = BooleanProperty.create("projectile_placed");

    private static final VoxelShape[] SHAPES = new VoxelShape[8];
    static {
        for (int i = 0; i < 8; i++) {
            SHAPES[i] = Block.box(0, 0, 0, 16, (i + 1) * 2, 16);
        }
    }

    public HagGooLayerBlock(BlockBehaviour.Properties props) {
        super(props.strength(0.2F).noOcclusion().noCollission());
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(LAYERS, 1)
                .setValue(BlockStateProperties.WATERLOGGED, false)
                .setValue(PROJECTILE_PLACED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LAYERS, BlockStateProperties.WATERLOGGED, PROJECTILE_PLACED);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
        return SHAPES[state.getValue(LAYERS) - 1];
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        BlockState existing = level.getBlockState(pos);

        if (existing.is(this)) {
            int current = existing.getValue(LAYERS);
            return existing.setValue(LAYERS, Math.min(8, current + 1));
        }

        FluidState fluid = level.getFluidState(pos);
        return this.defaultBlockState()
                .setValue(BlockStateProperties.WATERLOGGED, fluid.getType() == Fluids.WATER);
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        int layers = state.getValue(LAYERS);
        return context.getItemInHand().is(this.asItem()) && layers < 8;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(BlockStateProperties.WATERLOGGED)
                ? Fluids.WATER.getSource(false)
                : super.getFluidState(state);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction dir, BlockState neighbor, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(BlockStateProperties.WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, dir, neighbor, level, pos, neighborPos);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!level.isClientSide && entity instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(ModEffects.HAGGED.get(), 200));
        }
    }

    @Override
    public void onLand(Level level, BlockPos pos, BlockState fallingState, BlockState hitState, FallingBlockEntity fallingEntity) {
        if (!level.isClientSide && hitState.is(this)) {
            int belowLayers = hitState.getValue(LAYERS);
            int fallingLayers = fallingState.getValue(LAYERS);
            int total = Math.min(8, belowLayers + fallingLayers);

            level.setBlockAndUpdate(pos, hitState.setValue(LAYERS, total));
            fallingEntity.discard();
        } else {
            super.onLand(level, pos, fallingState, hitState, fallingEntity);
        }
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, net.minecraft.world.level.storage.loot.LootParams.Builder builder) {
        if (state.getValue(PROJECTILE_PLACED)) {
            return Collections.emptyList();
        }
        return super.getDrops(state, builder);
    }
}
