package net.alex.aspectsofminecraft.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ModFoodProperties {


    public static final FoodProperties SPECKLEREY = new FoodProperties.Builder()
            .nutrition(2)
            .saturationMod(0.5f)
            .build();

}
