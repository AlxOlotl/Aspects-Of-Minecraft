package net.alex.aspectsofminecraft.item;

import net.alex.aspectsofminecraft.Aspects;
import net.alex.aspectsofminecraft.block.ModBlocks;
import net.alex.aspectsofminecraft.item.custom.HagGooItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item>ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Aspects.MOD_ID);

    public static final RegistryObject<Item> ASPECT = ITEMS.register("aspect",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> OF = ITEMS.register("of",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MINECRAFT = ITEMS.register("minecraft",
            () -> new Item(new Item.Properties()));

    //ores
    public static final RegistryObject<Item> RAW_COBALT = ITEMS.register("raw_cobalt",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> COBALT_INGOT = ITEMS.register("cobalt_ingot",
            () -> new Item(new Item.Properties()));

    //mob drops
    public static final RegistryObject<Item> LUMINOUS_TENDRIL = ITEMS.register("luminous_tendril",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> YETI_CRAB_LEG = ITEMS.register("yeti_crab_leg",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> YETI_CRAB_SETAE = ITEMS.register("yeti_crab_setae",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HAG_GOO = ITEMS.register("hag_goo",
            () -> new HagGooItem(new Item.Properties().stacksTo(32)));


    //tools

    //armor

    //eggs
    public static final RegistryObject<Item> MAMMOTITAN_SPAWN_EGG = ITEMS.register("mammotitan_spawn_egg",
            () -> new Item(new Item.Properties()));

    //blocks
    public static final RegistryObject<Item> HAG_GOO_LAYER = ITEMS.register("hag_goo_layer",
            () -> new BlockItem(ModBlocks.HAG_GOO_LAYER.get(),
                    new Item.Properties()));
    public static final RegistryObject<Item> NAUTILUS_BLOCK_ITEM = ITEMS.register("nautilus_block",
            () -> new BlockItem(ModBlocks.NAUTILUS_BLOCK.get(), new Item.Properties()));


    //foods
    public static final RegistryObject<Item> SPECKLEREY = ITEMS.register("specklerey",
            () -> new Item(new Item.Properties().food(ModFoodProperties.SPECKLEREY)));
    public static final RegistryObject<Item> SPECKLEREY_SEEDS = ITEMS.register("specklerey_seeds",
            () -> new Item(new Item.Properties()));



    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
