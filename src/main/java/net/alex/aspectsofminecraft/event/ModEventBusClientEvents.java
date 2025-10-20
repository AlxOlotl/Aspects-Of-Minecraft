package net.alex.aspectsofminecraft.event;

import net.alex.aspectsofminecraft.Aspects;
import net.alex.aspectsofminecraft.entity.client.HagfishRenderer;
import net.alex.aspectsofminecraft.entity.ModEntities;
import net.alex.aspectsofminecraft.entity.client.MantroodonRenderer;
import net.alex.aspectsofminecraft.entity.client.RootMoleRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Aspects.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {

    //Passive
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.HAGFISH.get(), HagfishRenderer::new);
        event.registerEntityRenderer(ModEntities.ROOT_MOLE.get(), RootMoleRenderer::new);
        event.registerEntityRenderer(ModEntities.MANTROODON.get(), MantroodonRenderer::new);
    }


    //Hostile

    //Bosses

}
