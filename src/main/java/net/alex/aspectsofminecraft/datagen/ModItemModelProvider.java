package net.alex.aspectsofminecraft.datagen;

import net.alex.aspectsofminecraft.Aspects;
import net.alex.aspectsofminecraft.block.ModBlocks;
import net.alex.aspectsofminecraft.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
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

        customSpawnEgg(ModItems.HAGFISH_SPAWN_EGG, "hagfish_spawn_egg");

        simpleItem(ModItems.RAW_COBALT);
        simpleItem(ModItems.COBALT_INGOT);

        simpleItem(ModItems.LUMINOUS_TENDRIL);
        simpleItem(ModItems.YETI_CRAB_LEG);
        simpleItem(ModItems.YETI_CRAB_SETAE);
        simpleItem(ModItems.SCALY_PLUME);
        simpleItem(ModItems.SCORCHED_MANE);
        simpleItem(ModItems.STARRY_SCHNOZ);
        simpleItem(ModItems.SPECKLEREY);
        simpleItem(ModItems.SPECKLEREY_SEEDS);
        simpleItem(ModItems.HAG_GOO);
        simpleItem(ModItems.HAGFISH_BUCKET);
        simpleItem(ModItems.MAMMOTITAN_SPAWN_EGG);
        simpleBlockItem(ModBlocks.BUBBLECUP);
        simpleBlockItem(ModBlocks.BUBBLECUP_BLOSSOM);
        simpleBlockItem(ModBlocks.BOEBO_DOOR);
        simpleBlockItem(ModBlocks.CHARRED_DOOR);
        buttonItem(ModBlocks.CHARRED_BUTTON, ModBlocks.CHARRED_PLANKS);
        fenceItem(ModBlocks.CHARRED_FENCE, ModBlocks.CHARRED_PLANKS);
        buttonItem(ModBlocks.BOEBO_BUTTON, ModBlocks.BOEBO_PLANKS);
        fenceItem(ModBlocks.BOEBO_FENCE, ModBlocks.BOEBO_PLANKS);

        basicItem(ModItems.HAGFISH_SPAWN_EGG.get());
        hagGooLayerItem(ModBlocks.HAG_GOO_LAYER.get());
    }


    public void fenceItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/fence_inventory"))
                .texture("texture",  new ResourceLocation(Aspects.MOD_ID, "block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }

    public void wallItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/wall_inventory"))
                .texture("wall",  new ResourceLocation(Aspects.MOD_ID, "block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }

    public void buttonItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock){
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(),mcLoc("block/button_inventory"))
                .texture("texture", new ResourceLocation(Aspects.MOD_ID,"block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }

    private void hagGooLayerItem(Block block) {
        String baseName = ForgeRegistries.BLOCKS.getKey(block).getPath();

        getBuilder(baseName)
                .parent(getExistingFile(modLoc("block/" + baseName + "_layer_1")))
                .texture("all", modLoc("block/hag_goo_block"))
                .transforms()
                .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND)
                .rotation(0, 45, 0)
                .translation(0, 4, 0)
                .scale(0.4f, 0.4f, 0.4f)
                .end()
                .transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND)
                .rotation(0, 45, 0)
                .translation(0, 4, 0)
                .scale(0.4f, 0.4f, 0.4f)
                .end()
                .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)
                .rotation(75, 45, 0)
                .translation(0, 2.75f, 2.25f)
                .scale(0.375F, 0.375F, 0.375F)
                .end()
                .transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND)
                .rotation(75, 45, 0)
                .translation(0, 2.75f, 2.25f)
                .scale(0.375F, 0.375F, 0.375F)
                .end()
                .end();
    }

    private ItemModelBuilder handHeldItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(Aspects.MOD_ID, "item/" + item.getId().getPath()));
    }
    private ItemModelBuilder simpleBlockItem(RegistryObject<Block> blockRegistryObject) {
        return withExistingParent(
                ForgeRegistries.BLOCKS.getKey(blockRegistryObject.get()).getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(Aspects.MOD_ID, "item/" + ForgeRegistries.BLOCKS.getKey(blockRegistryObject.get()).getPath()));
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item){
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(Aspects.MOD_ID,"item/" +item.getId().getPath()));
    }

    private void customSpawnEgg(RegistryObject<Item> item, String textureName) {
        getBuilder(item.getId().getPath())
                .parent(getExistingFile(mcLoc("item/generated")))
                .texture("layer0", modLoc("item/" + textureName));
    }

}
