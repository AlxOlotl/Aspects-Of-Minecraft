package net.alex.aspectsofminecraft;

import com.mojang.logging.LogUtils;
import net.alex.aspectsofminecraft.block.ModBlocks;
import net.alex.aspectsofminecraft.item.ModCreativeModeTab;
import net.alex.aspectsofminecraft.item.ModItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
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
@Mod(Aspects.MODID)
public class Aspects
{
    public static final String MODID = "aspects";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Aspects() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModCreativeModeTab.register(modEventBus);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);

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
            event.accept(ModBlocks.BOEBO_PLANKS);
            event.accept(ModBlocks.BOEBO_WOOD);

        }

        if (event.getTabKey() == CreativeModeTabs.COLORED_BLOCKS) {

        }

        if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
            event.accept(ModBlocks.COBALT_ORE);
            event.accept(ModBlocks.DEEPSLATE_COBALT_ORE);
            event.accept(ModBlocks.RAW_COBALT_BLOCK);
            event.accept(ModBlocks.BAMBOO_CORAL_BLOCK);
            event.accept(ModBlocks.SHELF_CORAL_BLOCK);
        }

        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {

        }

        if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS) {

        }

        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {

        }

        if (event.getTabKey() == CreativeModeTabs.COMBAT) {

        }

        if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
            event.accept(ModItems.LUMINOUS_TENDRIL);
            event.accept(ModItems.YETI_CRAB_LEG);
            event.accept(ModItems.YETI_CRAB_SETAE);
            event.accept(ModItems.HAG_GOO);
        }
        if(event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.RAW_COBALT);
            event.accept(ModItems.COBALT_INGOT);
        }
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS);

    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
        }
    }
}
