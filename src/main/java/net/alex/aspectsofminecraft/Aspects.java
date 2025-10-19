package net.alex.aspectsofminecraft;

import com.mojang.logging.LogUtils;
import net.alex.aspectsofminecraft.enchantment.ModEnchantments;
import net.alex.aspectsofminecraft.entity.client.HagfishRenderer;
import net.alex.aspectsofminecraft.entity.client.RootMoleRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.level.block.ComposterBlock;
import software.bernie.geckolib.GeckoLib;
import net.alex.aspectsofminecraft.block.ModBlocks;
import net.alex.aspectsofminecraft.effect.ModEffects;
import net.alex.aspectsofminecraft.entity.ModEntities;
import net.alex.aspectsofminecraft.item.ModCreativeModeTab;
import net.alex.aspectsofminecraft.item.ModItems;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Aspects.MOD_ID)
public class Aspects
{
    public static final String MOD_ID = "aspects";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Aspects() {
        GeckoLib.initialize();
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModEntities.register(modEventBus);
        ModItems.register(modEventBus);
        ModCreativeModeTab.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModEffects.register(modEventBus);
        ModEnchantments.register(modEventBus);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() ->{
            ComposterBlock.COMPOSTABLES.put(ModItems.SPECKLEREY.get(),0.25f);
            ComposterBlock.COMPOSTABLES.put(ModItems.SPECKLEREY_SEEDS.get(),0.4f);
        });
    }


    //putting stuff in vanilla creative tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {


        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(ModBlocks.COBALT_BLOCK);
            event.accept(ModBlocks.BOEBO_WOOD);
            event.accept(ModBlocks.BOEBO_PLANKS);
            event.accept(ModBlocks.BOEBO_STAIRS);
            event.accept(ModBlocks.BOEBO_SLABS);
            event.accept(ModBlocks.BOEBO_FENCE);
            event.accept(ModBlocks.BOEBO_FENCE_GATE);
            event.accept(ModBlocks.BOEBO_PRESSURE_PLATE);
            event.accept(ModBlocks.BOEBO_BUTTON);
            event.accept(ModBlocks.CHARRED_PLANKS);
            event.accept(ModBlocks.CHARRED_STAIRS);
            event.accept(ModBlocks.CHARRED_SLABS);
            event.accept(ModBlocks.CHARRED_FENCE);
            event.accept(ModBlocks.CHARRED_FENCE_GATE);
            event.accept(ModBlocks.CHARRED_PRESSURE_PLATE);
            event.accept(ModBlocks.CHARRED_BUTTON);
            event.accept(ModBlocks.STRIPPED_BOEBO_WOOD);
            event.accept(ModBlocks.NAUTILUS_BLOCK);


        }

        if (event.getTabKey() == CreativeModeTabs.COLORED_BLOCKS) {

        }

        if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
            event.accept(ModBlocks.COBALT_ORE);
            event.accept(ModBlocks.DEEPSLATE_COBALT_ORE);
            event.accept(ModBlocks.RAW_COBALT_BLOCK);
            event.accept(ModItems.SPECKLEREY_SEEDS);
            event.accept(ModBlocks.BUBBLECUP);
            event.accept(ModBlocks.BUBBLECUP_BLOSSOM);
            event.accept(ModBlocks.BAMBOO_CORAL_BLOCK);
            event.accept(ModBlocks.DEAD_BAMBOO_CORAL_BLOCK);
            event.accept(ModBlocks.SHELF_CORAL_BLOCK);
            event.accept(ModBlocks.DEAD_SHELF_CORAL_BLOCK);
            event.accept(ModBlocks.HAG_GOO_BLOCK);
            event.accept(ModBlocks.HAG_GOO_LAYER);
        }

        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {

        }

        if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS) {
            event.accept(ModBlocks.BOEBO_FENCE_GATE);
            event.accept(ModBlocks.BOEBO_DOOR);
            event.accept(ModBlocks.BOEBO_TRAPDOOR);
            event.accept(ModBlocks.BOEBO_PRESSURE_PLATE);
            event.accept(ModBlocks.BOEBO_BUTTON);
            event.accept(ModBlocks.CHARRED_FENCE_GATE);
            event.accept(ModBlocks.CHARRED_DOOR);
            event.accept(ModBlocks.CHARRED_TRAPDOOR);
            event.accept(ModBlocks.CHARRED_PRESSURE_PLATE);
            event.accept(ModBlocks.CHARRED_BUTTON);
            event.accept(ModBlocks.HAG_GOO_BLOCK);
            event.accept(ModBlocks.NAUTILUS_BLOCK);

        }

        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {

        }

        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(ModItems.HAG_GOO);

        }

        if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
            event.accept(ModItems.SPECKLEREY);
            event.accept(ModItems.BUBBLECUP_DEWDROP);
            event.accept(ModItems.LUMINOUS_TENDRIL);
            event.accept(ModItems.YETI_CRAB_LEG);
            event.accept(ModItems.YETI_CRAB_SETAE);
        }
        if(event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.RAW_COBALT);
            event.accept(ModItems.COBALT_INGOT);
            event.accept(ModItems.BUBBLECUP_DEWDROP);
            event.accept(ModItems.HAG_GOO);
            event.accept(ModItems.SCALY_PLUME);
            event.accept(ModItems.SCORCHED_MANE);
            event.accept(ModItems.STARRY_SCHNOZ);

        }
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS){
            event.accept(ModItems.HAGFISH_SPAWN_EGG);

        }

    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
    }

    @Mod.EventBusSubscriber(modid = Aspects.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {


                ItemBlockRenderTypes.setRenderLayer(ModBlocks.HAG_GOO_BLOCK.get(), RenderType.translucent());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.HAG_GOO_LAYER.get(), RenderType.translucent());
                EntityRenderers.register(ModEntities.ROOT_MOLE.get(), RootMoleRenderer::new);
                EntityRenderers.register(ModEntities.HAGFISH.get(), HagfishRenderer::new);
                EntityRenderers.register(ModEntities.HAG_GOO_PROJECTILE.get(), ThrownItemRenderer::new);
            });
        }
    }

}
