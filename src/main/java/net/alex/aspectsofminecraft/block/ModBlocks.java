package net.alex.aspectsofminecraft.block;

import net.alex.aspectsofminecraft.Aspects;
import net.alex.aspectsofminecraft.block.custom.HagGooLayerBlock;
import net.alex.aspectsofminecraft.item.ModItems;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Aspects.MODID);

    //aboveground
    public static final RegistryObject<Block> BOEBO_PLANKS = registerBlock("boebo_planks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));

    public static final RegistryObject<Block> BOEBO_WOOD = registerBlock("boebo_wood",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD)));

    public static final RegistryObject<Block> STRIPPED_BOEBO_WOOD = registerBlock("stripped_boebo_wood",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD)));

    //underwater
    public static final RegistryObject<Block> BAMBOO_CORAL_BLOCK = registerBlock("bamboo_coral_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.BUBBLE_CORAL_BLOCK)));

    public static final RegistryObject<Block> DEAD_BAMBOO_CORAL_BLOCK = registerBlock("dead_bamboo_coral_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEAD_BUBBLE_CORAL_BLOCK)));

    public static final RegistryObject<Block> SHELF_CORAL_BLOCK = registerBlock("shelf_coral_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.BUBBLE_CORAL_BLOCK)));

    public static final RegistryObject<Block> DEAD_SHELF_CORAL_BLOCK = registerBlock("dead_shelf_coral_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEAD_BUBBLE_CORAL_BLOCK)));

    //underground
    public static final RegistryObject<Block> COBALT_BLOCK = registerBlock("cobalt_block",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLUE).instrument(NoteBlockInstrument.BASS)
                    .requiresCorrectToolForDrops().strength(4.0F, 5.0F)));

    public static final RegistryObject<Block> RAW_COBALT_BLOCK = registerBlock("raw_cobalt_block",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                    .requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL)));

    public static final RegistryObject<Block> COBALT_ORE = registerBlock("cobalt_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.COPPER_ORE)
                    , UniformInt.of( 0, 5)));

    public static final RegistryObject<Block> DEEPSLATE_COBALT_ORE = registerBlock("deepslate_cobalt_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_COPPER_ORE)
                    , UniformInt.of( 0, 5)));
    //nether

    //end

    //entities
    public static final RegistryObject<Item> HAG_GOO_LAYER_ITEM = ModItems.ITEMS.register("hag_goo_layer",
            () -> new BlockItem(ModBlocks.HAG_GOO_LAYER.get(), new Item.Properties()));

    public static final RegistryObject<Block> HAG_GOO_LAYER = BLOCKS.register("hag_goo_layer",
            HagGooLayerBlock::new);
    //misc





    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block){
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;

    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem (String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    
    public static final RegistryObject<Block> HAG_GOO_BLOCK = BLOCKS.register("hag_goo_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .strength(0.3F)
                    .sound(SoundType.SLIME_BLOCK)
                    .noOcclusion()
                    .isRedstoneConductor((state, level, pos) -> false)));
public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
