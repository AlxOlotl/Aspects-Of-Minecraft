package net.alex.aspectsofminecraft.datagen;

import net.alex.aspectsofminecraft.Aspects;
import net.alex.aspectsofminecraft.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {
    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                                @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Aspects.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.RAW_COBALT_BLOCK.get(),
                        ModBlocks.COBALT_BLOCK.get(),
                        ModBlocks.COBALT_ORE.get(),
                        ModBlocks.DEEPSLATE_COBALT_ORE.get(),
                        ModBlocks.BAMBOO_CORAL_BLOCK.get(),
                        ModBlocks.DEAD_BAMBOO_CORAL_BLOCK.get(),
                        ModBlocks.SHELF_CORAL_BLOCK.get(),
                        ModBlocks.DEAD_SHELF_CORAL_BLOCK.get());

        this.tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.RAW_COBALT_BLOCK.get(),
                        ModBlocks.COBALT_BLOCK.get(),
                        ModBlocks.COBALT_ORE.get(),
                        ModBlocks.DEEPSLATE_COBALT_ORE.get(),
                        ModBlocks.BAMBOO_CORAL_BLOCK.get(),
                        ModBlocks.DEAD_BAMBOO_CORAL_BLOCK.get(),
                        ModBlocks.SHELF_CORAL_BLOCK.get(),
                        ModBlocks.DEAD_SHELF_CORAL_BLOCK.get(),
                        ModBlocks.HAG_GOO_BLOCK.get());

        this.tag(BlockTags.MINEABLE_WITH_AXE)
                .add(ModBlocks.BOEBO_PLANKS.get(),
                        ModBlocks.BOEBO_WOOD.get(),
                        ModBlocks.STRIPPED_BOEBO_WOOD.get());

        this.tag(BlockTags.MINEABLE_WITH_HOE)
                .add(ModBlocks.HAG_GOO_BLOCK.get());
    }

    @Override
    public String getName() {
        return "Block Tage";
    }
}
