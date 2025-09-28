package net.alex.aspectsofminecraft.datagen;

import net.alex.aspectsofminecraft.Aspects;
import net.alex.aspectsofminecraft.block.ModBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraftforge.client.model.generators.BlockStateProvider;
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


    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject){
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }

    private void hagGooLayer(Block block) {
        simpleBlock(block, models().getBuilder("hag_goo_layer")
                .parent(models().getExistingFile(mcLoc("block/block")))
                .texture("all", modLoc("block/hag_goo_block"))
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






}
