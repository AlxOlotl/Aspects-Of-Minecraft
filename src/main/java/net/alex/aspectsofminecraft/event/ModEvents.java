package net.alex.aspectsofminecraft.event;

import net.alex.aspectsofminecraft.Aspects;
import net.alex.aspectsofminecraft.entity.ModEntities;
import net.alex.aspectsofminecraft.entity.custom.HagfishEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.alex.aspectsofminecraft.item.ModItems;

@Mod.EventBusSubscriber(modid = Aspects.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvents {

    @SubscribeEvent
    public static void onFoodEaten(LivingEntityUseItemEvent.Finish event) {
        ItemStack stack = event.getItem();
        LivingEntity entity = event.getEntity();
        if (stack.is(ModItems.SPECKLEREY.get())) {
            entity.hurt(entity.damageSources().generic(), 1.0F); // 4 damage using magic damage
        }
    }

}