package net.alex.aspectsofminecraft.datagen;

import net.alex.aspectsofminecraft.Aspects;
import net.alex.aspectsofminecraft.block.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
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
        itemModels().withExistingParent("hag_goo_layer", "item/generated")
                .texture("layer0", modLoc("block/hag_goo_block"));

    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject){
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
    private void hagGooLayer(Block block) {
        getVariantBuilder(block).forAllStates(state -> {
            int layers = state.getValue(SnowLayerBlock.LAYERS);

            ModelFile parent = new ModelFile.UncheckedModelFile("minecraft:block/snow_height" + layers);

            ModelFile model = models().getBuilder("hag_goo_height" + layers)
                    .parent(parent)
                    .texture("texture", modLoc("block/hag_goo_block"));



            return ConfiguredModel.builder()
                    .modelFile(model)
                    .build();
        });

        // Item model
        itemModels().withExistingParent("hag_goo_layer", "item/generated")
                .texture("layer0", modLoc("block/hag_goo_block"));
    }


}
