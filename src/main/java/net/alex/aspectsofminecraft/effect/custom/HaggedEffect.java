package net.alex.aspectsofminecraft.effect.custom;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientMobEffectExtensions;

import java.util.UUID;
import java.util.function.Consumer;

public class HaggedEffect extends MobEffect {
    private static final ResourceLocation ICON = new ResourceLocation("aspects", "textures/mob_effect/hagged.png");
    private static final UUID MOVEMENT_SPEED_MODIFIER = UUID.fromString("91AEAA56-376B-4498-935B-2F7F68070635");
    private static final UUID ATTACK_DAMAGE_MODIFIER = UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3");

    public HaggedEffect() {
        super(MobEffectCategory.HARMFUL, 0x5E006A); // Purple-ish color

        // Attribute modifiers are applied while the effect is active
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, MOVEMENT_SPEED_MODIFIER.toString(), -0.5D, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE, ATTACK_DAMAGE_MODIFIER.toString(), -0.3D, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity instanceof Player player) {
            ItemStack mainHand = player.getMainHandItem();
            if (!mainHand.isEmpty()) {
                RandomSource random = entity.getRandom();
                if (random.nextFloat() < 0.1f) {
                    player.drop(mainHand, true, false);
                    player.getInventory().removeItem(mainHand);
                }
            }
        }
    }

    // 🔹 This is the correct Forge 1.20 way to add custom icons
    @Override
    public void initializeClient(Consumer<IClientMobEffectExtensions> consumer) {
        consumer.accept(new IClientMobEffectExtensions() {
            public ResourceLocation getIcon() {
                return ICON;
            }
        });
    }
}
