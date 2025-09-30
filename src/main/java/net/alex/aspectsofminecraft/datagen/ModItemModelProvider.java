package net.alex.aspectsofminecraft.datagen;

import net.alex.aspectsofminecraft.Aspects;
import net.alex.aspectsofminecraft.block.ModBlocks;
import net.alex.aspectsofminecraft.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Aspects.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ModItems.ASPECT);
        simpleItem(ModItems.OF);
        simpleItem(ModItems.MINECRAFT);
        simpleItem(ModItems.RAW_COBALT);
        simpleItem(ModItems.COBALT_INGOT);
        simpleItem(ModItems.BOEBO_DOOR);
        simpleItem(ModItems.LUMINOUS_TENDRIL);
        simpleItem(ModItems.YETI_CRAB_LEG);
        simpleItem(ModItems.YETI_CRAB_SETAE);
        simpleItem(ModItems.SPECKLEREY);
        simpleItem(ModItems.SPECKLEREY_SEEDS);
        simpleItem(ModItems.HAG_GOO);
        simpleItem(ModItems.MAMMOTITAN_SPAWN_EGG);
        buttonItem(ModBlocks.CHARRED_BUTTON, ModBlocks.CHARRED_PLANKS);
        buttonItem(ModBlocks.BOEBO_BUTTON, ModBlocks.BOEBO_PLANKS);
    }
    public void buttonItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock){
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(),mcLoc("block/button_inventory"))
                .texture("texture", new ResourceLocation(Aspects.MOD_ID,"block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> itemRegistryObject){
        return withExistingParent(itemRegistryObject.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(Aspects.MOD_ID,"item/" +itemRegistryObject.getId().getPath()));
    }
}
