package net.alex.aspectsofminecraft.item;

import net.alex.aspectsofminecraft.Aspects;
import net.alex.aspectsofminecraft.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Aspects.MODID);

    public static final RegistryObject<CreativeModeTab> AOM_BLOCKS = CREATIVE_MODE_TAB.register("aom_blocks",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.ASPECT.get()))
                    .title(Component.translatable("creativetab.aspects_blocks_tab"))
                    .displayItems((parameters, output) ->{
                        output.accept(new ItemStack(ModBlocks.COBALT_ORE.get().asItem()));
                        output.accept(new ItemStack(ModBlocks.DEEPSLATE_COBALT_ORE.get().asItem()));
                        output.accept(new ItemStack(ModBlocks.RAW_COBALT_BLOCK.get().asItem()));
                        output.accept(new ItemStack(ModBlocks.COBALT_BLOCK.get().asItem()));
                        output.accept(new ItemStack(ModBlocks.BAMBOO_CORAL_BLOCK.get().asItem()));
                        output.accept(new ItemStack(ModBlocks.DEAD_BAMBOO_CORAL_BLOCK.get().asItem()));
                        output.accept(new ItemStack(ModBlocks.SHELF_CORAL_BLOCK.get().asItem()));
                        output.accept(new ItemStack(ModBlocks.DEAD_SHELF_CORAL_BLOCK.get().asItem()));
                        output.accept(new ItemStack(ModBlocks.BOEBO_PLANKS.get().asItem()));
                        output.accept(new ItemStack(ModBlocks.BOEBO_WOOD.get().asItem()));
                        output.accept(new ItemStack(ModBlocks.STRIPPED_BOEBO_WOOD.get().asItem()));
                        output.accept(new ItemStack(ModBlocks.HAG_GOO_BLOCK.get().asItem()));

                    } ).build());

    public static final RegistryObject<CreativeModeTab> AOM_ITEMS = CREATIVE_MODE_TAB.register("aom_items",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.ASPECT.get()))
                    .title(Component.translatable("creativetab.aspects_items_tab"))
                    .displayItems((parameters, output) ->{
                        output.accept(new ItemStack(ModItems.RAW_COBALT.get()));
                        output.accept(new ItemStack(ModItems.COBALT_INGOT.get()));
                        output.accept(new ItemStack(ModItems.SPECKLEREY_SEEDS.get()));
                        output.accept(new ItemStack(ModItems.SPECKLEREY.get()));
                        output.accept(new ItemStack(ModItems.LUMINOUS_TENDRIL.get()));
                        output.accept(new ItemStack(ModItems.YETI_CRAB_LEG.get()));
                        output.accept(new ItemStack(ModItems.YETI_CRAB_SETAE.get()));
                        output.accept(new ItemStack(ModItems.HAG_GOO.get()));


                    } ).build());

    public static final RegistryObject<CreativeModeTab> AOM_MOBS = CREATIVE_MODE_TAB.register("aom_mobs",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.ASPECT.get()))
                    .title(Component.translatable("creativetab.aspects_mobs_tab"))
                    .displayItems((parameters, output) ->{

                    } ).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
