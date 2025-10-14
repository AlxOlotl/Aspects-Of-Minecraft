package net.alex.aspectsofminecraft.item;

import net.alex.aspectsofminecraft.Aspects;
import net.alex.aspectsofminecraft.block.ModBlocks;
import net.alex.aspectsofminecraft.block.custom.BubblecupBlock;
import net.alex.aspectsofminecraft.entity.ModEntities;
import net.alex.aspectsofminecraft.item.custom.HagGooItem;
import net.alex.aspectsofminecraft.item.custom.HagfishBucketItem;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
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
    public static final RegistryObject<Item> SCALY_PLUME = ITEMS.register("scaly_plume",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SCORCHED_MANE = ITEMS.register("scorched_mane",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> STARRY_SCHNOZ = ITEMS.register("starry_schnoz",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> LUMINOUS_TENDRIL = ITEMS.register("luminous_tendril",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> YETI_CRAB_LEG = ITEMS.register("yeti_crab_leg",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> YETI_CRAB_SETAE = ITEMS.register("yeti_crab_setae",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HAG_GOO = ITEMS.register("hag_goo",
            () -> new HagGooItem(new Item.Properties().stacksTo(32)));
    public static final RegistryObject<Item> HAGFISH_BUCKET = ITEMS.register("hagfish_bucket",
            () -> new HagfishBucketItem(
                    new Item.Properties().stacksTo(1)
                            .craftRemainder(Items.BUCKET)
            ));


    //tools

    //armor

    //eggs
    public static final RegistryObject<Item> MAMMOTITAN_SPAWN_EGG = ITEMS.register("mammotitan_spawn_egg",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HAGFISH_SPAWN_EGG = ITEMS.register("hagfish_spawn_egg",
            () -> new CustomSpawnEggItem(ModEntities.HAGFISH, new Item.Properties()));

    //blocks
    public static final RegistryObject<BlockItem> HAG_GOO_LAYER = ITEMS.register("hag_goo_layer",
            () -> new BlockItem(ModBlocks.HAG_GOO_LAYER.get(),
                    new Item.Properties()));
    public static final RegistryObject<Item> NAUTILUS_BLOCK_ITEM = ITEMS.register("nautilus_block",
            () -> new BlockItem(ModBlocks.NAUTILUS_BLOCK.get(), new Item.Properties()));


    //foods
    public static final RegistryObject<Item> SPECKLEREY = ITEMS.register("specklerey",
            () -> new Item(new Item.Properties().food(ModFoodProperties.SPECKLEREY)));
    public static final RegistryObject<Item> SPECKLEREY_SEEDS = ITEMS.register("specklerey_seeds",
            () -> new ItemNameBlockItem(ModBlocks.SPECKLEREY_CROP.get(), new  Item.Properties()));

    //Plants
    public static final RegistryObject<Item> BUBBLECUP =
            ITEMS.register("bubblecup", () ->
                    new BlockItem(ModBlocks.BUBBLECUP.get(),
                            new Item.Properties()));

    public static final RegistryObject<Item> BUBBLECUP_BLOSSOM =
            ITEMS.register("bubblecup_blossom", () ->
                    new BlockItem(ModBlocks.BUBBLECUP_BLOSSOM.get(), new Item.Properties()) {
                        @Override
                        public InteractionResult place(BlockPlaceContext context) {
                            InteractionResult result = super.place(context);
                            if (result.consumesAction()) {
                                context.getLevel().setBlock(context.getClickedPos(),
                                        ModBlocks.BUBBLECUP_BLOSSOM.get().defaultBlockState()
                                                .setValue(BubblecupBlock.BLOOMING, true), 3);
                            }
                            return result;
                        }
                    });



    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
