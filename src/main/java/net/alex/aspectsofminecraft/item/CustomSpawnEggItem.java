package net.alex.aspectsofminecraft.item;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.function.Supplier;

public class CustomSpawnEggItem extends Item {
    private final Supplier<? extends EntityType<? extends Mob>> entityTypeSupplier;

    public CustomSpawnEggItem(Supplier<? extends EntityType<? extends Mob>> entityTypeSupplier, Properties properties) {
        super(properties);
        this.entityTypeSupplier = entityTypeSupplier;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        BlockHitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos pos = hitResult.getBlockPos().relative(hitResult.getDirection());

            if (!level.isClientSide && level instanceof net.minecraft.server.level.ServerLevel serverLevel) {
                EntityType<? extends Mob> type = entityTypeSupplier.get();
                Entity entity = type.spawn(serverLevel, stack, player, pos, MobSpawnType.SPAWN_EGG, true, true);

                if (entity != null) {
                    level.playSound(null, pos, SoundEvents.SLIME_SQUISH, SoundSource.PLAYERS, 0.8F, 1.0F);
                    level.gameEvent(player, GameEvent.ENTITY_PLACE, pos);
                }

                if (!player.isCreative()) stack.shrink(1);
            }

            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
        }

        return InteractionResultHolder.pass(stack);
    }
}
