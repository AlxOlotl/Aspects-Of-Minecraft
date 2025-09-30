package net.alex.aspectsofminecraft.datagen.loot;

import net.alex.aspectsofminecraft.block.ModBlocks;
import net.alex.aspectsofminecraft.item.ModItems;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        // Drop themselves
        this.dropSelf(ModBlocks.COBALT_BLOCK.get());
        this.dropSelf(ModBlocks.RAW_COBALT_BLOCK.get());
        this.dropSelf(ModBlocks.CHARRED_PLANKS.get());
        this.dropSelf(ModBlocks.CHARRED_STAIRS.get());
        this.dropSelf(ModBlocks.CHARRED_PRESSURE_PLATE.get());
        this.dropSelf(ModBlocks.CHARRED_BUTTON.get());
        this.dropSelf(ModBlocks.BOEBO_PLANKS.get());
        this.dropSelf(ModBlocks.BOEBO_STAIRS.get());
        this.dropSelf(ModBlocks.BOEBO_PRESSURE_PLATE.get());
        this.dropSelf(ModBlocks.BOEBO_BUTTON.get());
        this.dropSelf(ModBlocks.BOEBO_WOOD.get());
        this.dropSelf(ModBlocks.STRIPPED_BOEBO_WOOD.get());
        this.dropSelf(ModBlocks.BAMBOO_CORAL_BLOCK.get());
        this.dropSelf(ModBlocks.DEAD_BAMBOO_CORAL_BLOCK.get());
        this.dropSelf(ModBlocks.SHELF_CORAL_BLOCK.get());
        this.dropSelf(ModBlocks.DEAD_SHELF_CORAL_BLOCK.get());
        this.dropSelf(ModBlocks.HAG_GOO_BLOCK.get());
        this.dropSelf(ModBlocks.NAUTILUS_BLOCK.get());

        this.add(ModBlocks.CHARRED_SLABS.get(), block -> createSlabItemTable(ModBlocks.CHARRED_SLABS.get()));
        this.add(ModBlocks.BOEBO_SLABS.get(), block -> createSlabItemTable(ModBlocks.BOEBO_SLABS.get()));

        this.add(ModBlocks.HAG_GOO_LAYER.get(), block -> createOreDrop(ModBlocks.HAG_GOO_LAYER.get(), ModItems.HAG_GOO.get()));
        this.add(ModBlocks.COBALT_ORE.get(), block -> createOreDrop(ModBlocks.COBALT_ORE.get(), ModItems.RAW_COBALT.get()));
        this.add(ModBlocks.DEEPSLATE_COBALT_ORE.get(), block -> createOreDrop(ModBlocks.DEEPSLATE_COBALT_ORE.get(), ModItems.RAW_COBALT.get()));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
