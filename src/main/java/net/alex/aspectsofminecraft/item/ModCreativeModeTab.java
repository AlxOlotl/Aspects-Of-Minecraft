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
            () -> CreativeModeTab.builder().icon(()->new ItemStack(ModItems.ASPECT.get()))
                    .title(Component.translatable("creativetab.aspects_blocks_tab"))
                    .displayItems((parameters, output) ->{
                        output.accept(ModBlocks.COBALT_ORE.get());
                        output.accept(ModBlocks.DEEPSLATE_COBALT_ORE.get());
                        output.accept(ModBlocks.RAW_COBALT_BLOCK.get());
                        output.accept(ModBlocks.COBALT_BLOCK.get());
                        output.accept(ModBlocks.BAMBOO_CORAL_BLOCK.get());
                        output.accept(ModBlocks.SHELF_CORAL_BLOCK.get());
                        output.accept(ModBlocks.BOEBO_PLANK.get());
                        output.accept(ModBlocks.BOEBO_WOOD.get());

                    } ).build());

    public static final RegistryObject<CreativeModeTab> AOM_ITEMS = CREATIVE_MODE_TAB.register("aom_items",
            () -> CreativeModeTab.builder().icon(()->new ItemStack(ModItems.ASPECT.get()))
                    .title(Component.translatable("creativetab.aspects_items_tab"))
                    .displayItems((parameters, output) ->{
                        output.accept(ModItems.RAW_COBALT.get());
                        output.accept(ModItems.COBALT_INGOT.get());
                        output.accept(ModItems.LUMINOUS_TENDRIL.get());
                        output.accept(ModItems.YETI_CRAB_LEG.get());
                        output.accept(ModItems.YETI_CRAB_SETAE.get());
                        output.accept(ModItems.HAG_GOO.get());

                    } ).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
