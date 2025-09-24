package net.alex.aspectsofminecraft.item.custom;

import net.alex.aspectsofminecraft.entity.ModEntities;
import net.alex.aspectsofminecraft.entity.custom.HagGooProjectile;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class HagGooItem extends Item {
    public HagGooItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        if (!level.isClientSide()) {
            HagGooProjectile goo = new HagGooProjectile(ModEntities.HAG_GOO_PROJECTILE.get(), level, player);
            goo.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F); // speed and inaccuracy
            level.addFreshEntity(goo);
        }

        if (!player.getAbilities().instabuild) {
            itemstack.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }
}
