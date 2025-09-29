package net.alex.aspectsofminecraft.datagen;

import net.alex.aspectsofminecraft.Aspects;
import net.alex.aspectsofminecraft.block.ModBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
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



        blockWithItem(ModBlocks.BAMBOO_CORAL_BLOCK);
        blockWithItem(ModBlocks.DEAD_BAMBOO_CORAL_BLOCK);
        blockWithItem(ModBlocks.SHELF_CORAL_BLOCK);
        blockWithItem(ModBlocks.DEAD_SHELF_CORAL_BLOCK);

        blockWithItem(ModBlocks.HAG_GOO_BLOCK);
        hagGooLayer(ModBlocks.HAG_GOO_LAYER.get());
        nautilusBlock(ModBlocks.NAUTILUS_BLOCK.get());



    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject){
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }

    private void hagGooLayer(Block block) {
        simpleBlock(block, models().getBuilder("hag_goo_layer")
                .parent(models().getExistingFile(mcLoc("block/block")))
                .texture("all", modLoc("block/hag_goo_block"))
                .texture("particle", modLoc("block/hag_goo_block"))
                .element()
                .from(0, 0, 0).to(16, 2, 16)
                .face(Direction.UP).texture("#all").end()
                .face(Direction.DOWN).texture("#all").end()
                .face(Direction.NORTH).texture("#all").cullface(Direction.NORTH).end()
                .face(Direction.SOUTH).texture("#all").cullface(Direction.SOUTH).end()
                .face(Direction.WEST).texture("#all").cullface(Direction.WEST).end()
                .face(Direction.EAST).texture("#all").cullface(Direction.EAST).end()
                .end()
        );

        itemModels().getBuilder(ForgeRegistries.BLOCKS.getKey(block).getPath())
                .parent(models().getExistingFile(modLoc("block/hag_goo_layer")));
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
