package net.alex.aspectsofminecraft.event;

import net.alex.aspectsofminecraft.Aspects;

import net.alex.aspectsofminecraft.entity.ModEntities;
import net.alex.aspectsofminecraft.entity.custom.HagfishEntity;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Aspects.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

    //Passive
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.HAGFISH.get(), HagfishEntity.createAttributes().build());
    }



    //Hostile

    //Bosses
}
