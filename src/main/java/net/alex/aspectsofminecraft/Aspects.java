package net.alex.aspectsofminecraft;

import com.mojang.logging.LogUtils;
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

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Aspects.MOD_ID)
public class Aspects
{
    public static final String MOD_ID = "aspects";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Aspects() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModCreativeModeTab.register(modEventBus);
        ModEntities.ENTITIES.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModEffects.MOB_EFFECTS.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);


    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
    }

    //putting stuff in vanilla creative tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {


        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(ModBlocks.COBALT_BLOCK);
            event.accept(ModBlocks.BOEBO_WOOD);
            event.accept(ModBlocks.BOEBO_PLANKS);
            event.accept(ModBlocks.BOEBO_STAIRS);
            event.accept(ModBlocks.BOEBO_SLABS);
            event.accept(ModBlocks.BOEBO_PRESSURE_PLATE);
            event.accept(ModBlocks.BOEBO_BUTTON);
            event.accept(ModBlocks.CHARRED_PLANKS);
            event.accept(ModBlocks.CHARRED_STAIRS);
            event.accept(ModBlocks.CHARRED_SLABS);
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
            event.accept(ModBlocks.BAMBOO_CORAL_BLOCK);
            event.accept(ModBlocks.DEAD_BAMBOO_CORAL_BLOCK);
            event.accept(ModBlocks.SHELF_CORAL_BLOCK);
            event.accept(ModBlocks.DEAD_SHELF_CORAL_BLOCK);
            event.accept(ModBlocks.HAG_GOO_BLOCK);
        }

        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {

        }

        if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS) {
            event.accept(ModBlocks.CHARRED_PRESSURE_PLATE);
            event.accept(ModBlocks.CHARRED_BUTTON);
            event.accept(ModBlocks.BOEBO_PRESSURE_PLATE);
            event.accept(ModBlocks.BOEBO_BUTTON);
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
            event.accept(ModItems.LUMINOUS_TENDRIL);
            event.accept(ModItems.YETI_CRAB_LEG);
            event.accept(ModItems.YETI_CRAB_SETAE);
        }
        if(event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.RAW_COBALT);
            event.accept(ModItems.COBALT_INGOT);
            event.accept(ModItems.HAG_GOO);
        }
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS);

    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
    }

    @Mod.EventBusSubscriber(modid = Aspects.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.HAG_GOO_BLOCK.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.HAG_GOO_LAYER.get(), RenderType.translucent());

        }
        @SubscribeEvent
        public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(ModEntities.HAG_GOO_PROJECTILE.get(),
                    ThrownItemRenderer::new);
        }

    }
}
