package net.alex.aspectsofminecraft.block;

import net.alex.aspectsofminecraft.Aspects;
import net.alex.aspectsofminecraft.block.custom.*;
import net.alex.aspectsofminecraft.effect.ModEffects;
import net.alex.aspectsofminecraft.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Aspects.MOD_ID);


    //Boebo set
    public static final RegistryObject<Block> BOEBO_PLANKS = registerBlock("boebo_planks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> BOEBO_STAIRS = registerBlock("boebo_stairs",
            () -> new StairBlock(() -> ModBlocks.BOEBO_PLANKS.get().defaultBlockState(),
                    BlockBehaviour.Properties.copy(Blocks.OAK_STAIRS)));
    public static final RegistryObject<Block> BOEBO_SLABS = registerBlock("boebo_slabs",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.OAK_SLAB)));
    public static final RegistryObject<Block> BOEBO_PRESSURE_PLATE = registerBlock("boebo_pressure_plate",
            () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING,
                    BlockBehaviour.Properties.copy(Blocks.OAK_PRESSURE_PLATE), BlockSetType.OAK));
    public static final RegistryObject<Block> BOEBO_BUTTON = registerBlock("boebo_button",
            () -> new ButtonBlock(BlockBehaviour.Properties.copy(Blocks.OAK_BUTTON), BlockSetType.OAK, 30, true));
    public static final RegistryObject<Block> BOEBO_FENCE = registerBlock("boebo_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE)));
    public static final RegistryObject<Block> BOEBO_FENCE_GATE = registerBlock("boebo_fence_gate",
            () -> new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE_GATE), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
    public static final RegistryObject<Block> BOEBO_DOOR = registerBlock("boebo_door",
            () -> new DoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_DOOR), BlockSetType.OAK));
    public static final RegistryObject<Block> BOEBO_TRAPDOOR = registerBlock("boebo_trapdoor",
            () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_DOOR), BlockSetType.OAK));
    public static final RegistryObject<Block> BOEBO_WOOD = registerBlock("boebo_wood",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD)));
    public static final RegistryObject<Block> STRIPPED_BOEBO_WOOD = registerBlock("stripped_boebo_wood",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD)));

    //Charred set
    public static final RegistryObject<Block> CHARRED_PLANKS = registerBlock("charred_planks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> CHARRED_STAIRS = registerBlock("charred_stairs",
            () -> new StairBlock(() -> ModBlocks.CHARRED_PLANKS.get().defaultBlockState(),
                    BlockBehaviour.Properties.copy(Blocks.OAK_STAIRS)));
    public static final RegistryObject<Block> CHARRED_SLABS = registerBlock("charred_slabs",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.OAK_SLAB)));
    public static final RegistryObject<Block> CHARRED_PRESSURE_PLATE = registerBlock("charred_pressure_plate",
            () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING,
                    BlockBehaviour.Properties.copy(Blocks.OAK_PRESSURE_PLATE), BlockSetType.OAK));
    public static final RegistryObject<Block> CHARRED_BUTTON = registerBlock("charred_button",
            () -> new ButtonBlock(BlockBehaviour.Properties.copy(Blocks.OAK_BUTTON), BlockSetType.OAK, 30, true));
    public static final RegistryObject<Block> CHARRED_FENCE = registerBlock("charred_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE)));
    public static final RegistryObject<Block> CHARRED_FENCE_GATE = registerBlock("charred_fence_gate",
            () -> new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE_GATE), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
    public static final RegistryObject<Block> CHARRED_DOOR = registerBlock("charred_door",
            () -> new DoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_DOOR), BlockSetType.OAK));
    public static final RegistryObject<Block> CHARRED_TRAPDOOR = registerBlock("charred_trapdoor",
            () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_DOOR), BlockSetType.OAK));

    //Crops
    public static final RegistryObject<Block> SPECKLEREY_CROP = BLOCKS.register("specklerey_crop",
            () -> new SpecklereyCropBlock(BlockBehaviour.Properties.copy(Blocks.BEETROOTS).noCollission().noOcclusion()));

    //Plants
    public static final RegistryObject<Block> BUBBLECUP = registerBlock("bubblecup",
            () -> new BubblecupBlock(() -> MobEffects.WATER_BREATHING, 5, BlockBehaviour.Properties.copy(Blocks.DANDELION).noOcclusion().instabreak()));
    public static final RegistryObject<Block> BUBBLECUP_BLOSSOM = registerBlock("bubblecup_blossom",
            () -> new BubblecupBlossomBlock(() -> MobEffects.REGENERATION, 5, BlockBehaviour.Properties.copy(Blocks.DANDELION).noCollission().instabreak()));

    //Corals
    public static final RegistryObject<Block> BAMBOO_CORAL_BLOCK = registerBlock("bamboo_coral_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.BUBBLE_CORAL_BLOCK)));
    public static final RegistryObject<Block> DEAD_BAMBOO_CORAL_BLOCK = registerBlock("dead_bamboo_coral_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEAD_BUBBLE_CORAL_BLOCK)));

    public static final RegistryObject<Block> SHELF_CORAL_BLOCK = registerBlock("shelf_coral_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.BUBBLE_CORAL_BLOCK)));
    public static final RegistryObject<Block> DEAD_SHELF_CORAL_BLOCK = registerBlock("dead_shelf_coral_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEAD_BUBBLE_CORAL_BLOCK)));

    //Ores
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

    //Nether

    //End

    //Special
    public static final RegistryObject<Block> NAUTILUS_BLOCK = BLOCKS.register("nautilus_block",
            () -> new NautilusBlock(BlockBehaviour.Properties.of()
                    .strength(1.5f).requiresCorrectToolForDrops().noOcclusion()));

    //Misc
    public static final RegistryObject<Block> HAG_GOO_BLOCK = registerBlock("hag_goo_block",
            () -> new HagGooBlock(BlockBehaviour.Properties.of()
                    .strength(0.5f).noOcclusion().noCollission().sound(SoundType.SLIME_BLOCK).isRedstoneConductor((s,g,p) -> false)) {
                @Override
                public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
                    if (entity instanceof LivingEntity livingEntity) {
                        livingEntity.addEffect(new MobEffectInstance(ModEffects.HAGGED.get(), 200, 0, false, true));
                    }
                    super.entityInside(state, level, pos, entity);
                }});
    public static final RegistryObject<Block> HAG_GOO_LAYER = BLOCKS.register("hag_goo_layer",
            () -> new HagGooLayerBlock(BlockBehaviour.Properties.of()
                    .strength(0.1f).noOcclusion().noCollission().sound(SoundType.SLIME_BLOCK)));

//===================================================================================================================================

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block){
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem (String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}