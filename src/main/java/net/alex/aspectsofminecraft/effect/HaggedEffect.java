package net.alex.aspectsofminecraft.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.RandomSource;

public class HaggedEffect extends MobEffect {
    public HaggedEffect() {
        super(MobEffectCategory.HARMFUL, 0x5E006A); // Purple-ish color
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED) != null) {
            entity.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED)
                    .setBaseValue(entity.getAttributeBaseValue(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED) * 0.5);
        }

        if (entity.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE) != null) {
            entity.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE)
                    .setBaseValue(entity.getAttributeBaseValue(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE) * 0.7);
        }

        if (entity instanceof Player player) {
            ItemStack mainHand = player.getMainHandItem();
            if (!mainHand.isEmpty()) {
                RandomSource random = entity.getRandom();
                if (random.nextFloat() < 0.3f) {
                    player.drop(mainHand, true, false);
                    player.getInventory().removeItem(mainHand);
                }
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true; // Run every tick
    }
}
