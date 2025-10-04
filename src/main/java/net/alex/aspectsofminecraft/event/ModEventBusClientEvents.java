package net.alex.aspectsofminecraft.event;

import net.alex.aspectsofminecraft.Aspects;
import net.alex.aspectsofminecraft.entity.client.HagfishModel;
import net.alex.aspectsofminecraft.entity.client.ModModelLayers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Aspects.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {

    //Passive
    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event){
        event.registerLayerDefinition(ModModelLayers.HAGFISH_LAYER, HagfishModel::createBodyLayer);
    }


    //Hostile

    //Bosses
}
