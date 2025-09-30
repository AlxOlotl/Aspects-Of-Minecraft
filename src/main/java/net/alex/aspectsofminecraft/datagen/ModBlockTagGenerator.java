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
                        ModBlocks.BOEBO_STAIRS.get(),
                        ModBlocks.BOEBO_SLABS.get(),
                        ModBlocks.BOEBO_FENCE.get(),
                        ModBlocks.BOEBO_FENCE_GATE.get(),
                        ModBlocks.BOEBO_DOOR.get(),
                        ModBlocks.BOEBO_TRAPDOOR.get(),
                        ModBlocks.BOEBO_WOOD.get(),
                        ModBlocks.STRIPPED_BOEBO_WOOD.get(),
                        ModBlocks.CHARRED_PLANKS.get(),
                        ModBlocks.CHARRED_STAIRS.get(),
                        ModBlocks.CHARRED_SLABS.get(),
                        ModBlocks.CHARRED_FENCE.get(),
                        ModBlocks.CHARRED_FENCE_GATE.get(),
                        ModBlocks.CHARRED_DOOR.get(),
                        ModBlocks.CHARRED_TRAPDOOR.get());

        this.tag(BlockTags.MINEABLE_WITH_HOE)
                .add(ModBlocks.HAG_GOO_BLOCK.get(),
                        ModBlocks.HAG_GOO_LAYER.get());

        this.tag(BlockTags.FENCES)
                .add(ModBlocks.CHARRED_FENCE.get(),
                        ModBlocks.BOEBO_FENCE.get());

        this.tag(BlockTags.WALLS);


        this.tag(BlockTags.FENCE_GATES)
                .add(ModBlocks.CHARRED_FENCE_GATE.get(),
                        ModBlocks.BOEBO_FENCE_GATE.get());
    }

    @Override
    public String getName() {
        return "Block Tage";
    }
}
