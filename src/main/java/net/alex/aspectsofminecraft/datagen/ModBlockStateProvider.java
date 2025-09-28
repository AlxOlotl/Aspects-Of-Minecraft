package net.alex.aspectsofminecraft.datagen;

import net.alex.aspectsofminecraft.Aspects;
import net.alex.aspectsofminecraft.block.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraftforge.client.model.generators.BlockStateProvider;
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

        blockWithItem(ModBlocks.BAMBOO_CORAL_BLOCK);
        blockWithItem(ModBlocks.DEAD_BAMBOO_CORAL_BLOCK);
        blockWithItem(ModBlocks.SHELF_CORAL_BLOCK);
        blockWithItem(ModBlocks.DEAD_SHELF_CORAL_BLOCK);

        blockWithItem(ModBlocks.HAG_GOO_BLOCK);
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject){
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}
