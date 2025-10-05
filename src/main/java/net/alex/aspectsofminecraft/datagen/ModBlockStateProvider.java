package net.alex.aspectsofminecraft.datagen;

import net.alex.aspectsofminecraft.Aspects;
import net.alex.aspectsofminecraft.block.ModBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Aspects.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.COBALT_BLOCK);
        blockWithItem(ModBlocks.RAW_COBALT_BLOCK);
        blockWithItem(ModBlocks.COBALT_ORE);
        blockWithItem(ModBlocks.DEEPSLATE_COBALT_ORE);

        blockWithItem(ModBlocks.BOEBO_PLANKS);
        stairsBlock((StairBlock) ModBlocks.BOEBO_STAIRS.get(), blockTexture(ModBlocks.BOEBO_PLANKS.get()));
        slabBlock(((SlabBlock) ModBlocks.BOEBO_SLABS.get()), blockTexture(ModBlocks.BOEBO_PLANKS.get()), blockTexture(ModBlocks.BOEBO_PLANKS.get()));
        buttonBlock((ButtonBlock) ModBlocks.BOEBO_BUTTON.get(), blockTexture(ModBlocks.BOEBO_PLANKS.get()));
        pressurePlateBlock((PressurePlateBlock) ModBlocks.BOEBO_PRESSURE_PLATE.get(), blockTexture(ModBlocks.BOEBO_PLANKS.get()));
        fenceBlock((FenceBlock) ModBlocks.BOEBO_FENCE.get(), blockTexture(ModBlocks.BOEBO_PLANKS.get()));
        fenceGateBlock((FenceGateBlock) ModBlocks.BOEBO_FENCE_GATE.get(), blockTexture(ModBlocks.BOEBO_PLANKS.get()));
        doorBlockWithRenderType((DoorBlock)ModBlocks.BOEBO_DOOR.get(), modLoc("block/boebo_door_bottom"), modLoc("block/boebo_door_top"),"cutout");
        trapdoorBlockWithRenderType((TrapDoorBlock)ModBlocks.BOEBO_TRAPDOOR.get(), modLoc("block/boebo_trapdoor"),true,"cutout");
        axisBlock((RotatedPillarBlock) ModBlocks.BOEBO_WOOD.get(),
                new ResourceLocation("aspects", "block/boebo_log"),
                new ResourceLocation("aspects", "block/boebo_log"));
        itemModels().cubeColumn(ModBlocks.BOEBO_WOOD.getId().getPath(),
                new ResourceLocation("aspects", "block/boebo_log"),
                new ResourceLocation("aspects", "block/boebo_log"));

        axisBlock((RotatedPillarBlock) ModBlocks.STRIPPED_BOEBO_WOOD.get(),
                new ResourceLocation("aspects", "block/stripped_boebo_log"),
                new ResourceLocation("aspects", "block/stripped_boebo_log"));
        itemModels().cubeColumn(ModBlocks.STRIPPED_BOEBO_WOOD.getId().getPath(),
                new ResourceLocation("aspects", "block/stripped_boebo_log"),
                new ResourceLocation("aspects", "block/stripped_boebo_log"));

        blockWithItem(ModBlocks.CHARRED_PLANKS);
        stairsBlock((StairBlock) ModBlocks.CHARRED_STAIRS.get(), blockTexture(ModBlocks.CHARRED_PLANKS.get()));
        slabBlock(((SlabBlock) ModBlocks.CHARRED_SLABS.get()), blockTexture(ModBlocks.CHARRED_PLANKS.get()), blockTexture(ModBlocks.CHARRED_PLANKS.get()));
        buttonBlock((ButtonBlock) ModBlocks.CHARRED_BUTTON.get(), blockTexture(ModBlocks.CHARRED_PLANKS.get()));
        pressurePlateBlock((PressurePlateBlock) ModBlocks.CHARRED_PRESSURE_PLATE.get(), blockTexture(ModBlocks.CHARRED_PLANKS.get()));
        fenceBlock((FenceBlock) ModBlocks.CHARRED_FENCE.get(), blockTexture(ModBlocks.CHARRED_PLANKS.get()));
        fenceGateBlock((FenceGateBlock) ModBlocks.CHARRED_FENCE_GATE.get(), blockTexture(ModBlocks.CHARRED_PLANKS.get()));
        doorBlockWithRenderType((DoorBlock)ModBlocks.CHARRED_DOOR.get(), modLoc("block/charred_door_bottom"), modLoc("block/charred_door_top"),"cutout");
        trapdoorBlockWithRenderType((TrapDoorBlock)ModBlocks.CHARRED_TRAPDOOR.get(), modLoc("block/charred_trapdoor"),true,"cutout");


        blockWithItem(ModBlocks.BAMBOO_CORAL_BLOCK);
        blockWithItem(ModBlocks.DEAD_BAMBOO_CORAL_BLOCK);
        blockWithItem(ModBlocks.SHELF_CORAL_BLOCK);
        blockWithItem(ModBlocks.DEAD_SHELF_CORAL_BLOCK);

        blockItem(ModBlocks.BOEBO_STAIRS);
        blockItem(ModBlocks.BOEBO_SLABS);
        blockItem(ModBlocks.BOEBO_FENCE_GATE);
        blockItem(ModBlocks.BOEBO_TRAPDOOR, "_bottom");
        blockItem(ModBlocks.BOEBO_PRESSURE_PLATE);
        blockItem(ModBlocks.BOEBO_WOOD);
        blockItem(ModBlocks.STRIPPED_BOEBO_WOOD);
        blockItem(ModBlocks.CHARRED_STAIRS);
        blockItem(ModBlocks.CHARRED_SLABS);
        blockItem(ModBlocks.CHARRED_FENCE_GATE);
        blockItem(ModBlocks.CHARRED_TRAPDOOR, "_bottom");
        blockItem(ModBlocks.CHARRED_PRESSURE_PLATE);

        blockWithItem(ModBlocks.HAG_GOO_BLOCK);
        hagGooLayer(ModBlocks.HAG_GOO_LAYER.get());
        nautilusBlock(ModBlocks.NAUTILUS_BLOCK.get());



    }


    private void blockItem(RegistryObject<Block> blockRegistryObject, String appendix) {
        simpleBlockItem(blockRegistryObject.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/" +
                        ForgeRegistries.BLOCKS.getKey(blockRegistryObject.get()).getPath()+appendix)));
    }

    private void blockItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockItem(blockRegistryObject.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/" +
                        ForgeRegistries.BLOCKS.getKey(blockRegistryObject.get()).getPath())));
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject){
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }

    private void hagGooLayer(Block block) {
        String baseName = ForgeRegistries.BLOCKS.getKey(block).getPath();
        String texture = "block/hag_goo_block";
        for (int i = 1; i <= 8; i++) {
            float height = 2.0f * i;
            models().getBuilder(baseName + "_layer_" + i)
                    .parent(models().getExistingFile(mcLoc("block/block"))).texture("all", modLoc(texture)).texture("particle", modLoc(texture)).element().from(0.0f, 0.0f, 0.0f).to(16.0f, height, 16.0f).face(Direction.UP).texture("#all").end().face(Direction.DOWN).texture("#all").end().face(Direction.NORTH).texture("#all").end().face(Direction.SOUTH).texture("#all").end().face(Direction.WEST).texture("#all").end().face(Direction.EAST).texture("#all").end().end();
        }
        getVariantBuilder(block).forAllStates(state -> {
            int layers = state.getValue(BlockStateProperties.LAYERS);
            return ConfiguredModel.builder()
                    .modelFile(models().getExistingFile(modLoc("block/" + baseName + "_layer_" + layers))).build();
        });
        itemModels().getBuilder(baseName)
                .parent(models().getExistingFile(modLoc("block/" + baseName + "_layer_1"))).transforms().transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).rotation(0, -10, 0).translation(0, 3.5F, 1.0F).scale(0.9F).end().transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND).rotation(0, 10, 0).translation(0, 3.5F, 1.0F).scale(0.9F).end().end();

    }


    private void nautilusBlock(Block block) {
        ModelFile nautilusModel = models().getBuilder("nautilus_block")
                .parent(models().getExistingFile(mcLoc("block/cube")))
                .texture("top", modLoc("block/nautilus_top"))
                .texture("bottom", modLoc("block/nautilus_bottom"))
                .texture("north", modLoc("block/nautilus_front"))
                .texture("south", modLoc("block/nautilus_back"))
                .texture("west", modLoc("block/nautilus_horizontal_side"))
                .texture("east", modLoc("block/nautilus_horizontal_side"))
                .texture("particle", modLoc("block/nautilus_horizontal_side"))
                .element()
                .from(0, 0, 0).to(16, 16, 16)
                .face(Direction.NORTH).texture("#north").end()
                .face(Direction.SOUTH).texture("#south").end()
                .face(Direction.WEST).texture("#west").end()
                .face(Direction.EAST).texture("#east").uvs(16, 0, 0, 16).end() // flipped east
                .face(Direction.UP).texture("#top").end()
                .face(Direction.DOWN).texture("#bottom").end()
                .end();

        getVariantBuilder(block).forAllStates(state -> {
            Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            int yRot = switch (facing) {
                case NORTH -> 180;
                case SOUTH -> 0;
                case WEST  -> 90;
                case EAST  -> 270;
                default -> 0;
            };
            return ConfiguredModel.builder()
                    .modelFile(nautilusModel).rotationY(yRot).build();
        });
        itemModels().withExistingParent("nautilus_block", modLoc("block/nautilus_block"));
    }

}
