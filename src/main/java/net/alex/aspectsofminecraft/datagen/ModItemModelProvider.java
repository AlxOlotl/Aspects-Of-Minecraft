package net.alex.aspectsofminecraft.datagen;

import net.alex.aspectsofminecraft.Aspects;
import net.alex.aspectsofminecraft.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
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
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> itemRegistryObject){
        return withExistingParent(itemRegistryObject.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(Aspects.MOD_ID,"item/" +itemRegistryObject.getId().getPath()));
    }
}
