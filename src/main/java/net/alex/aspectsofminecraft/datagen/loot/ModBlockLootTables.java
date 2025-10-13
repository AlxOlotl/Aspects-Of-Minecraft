package net.alex.aspectsofminecraft.datagen.loot;

import net.alex.aspectsofminecraft.block.ModBlocks;
import net.alex.aspectsofminecraft.block.custom.SpecklereyCropBlock;
import net.alex.aspectsofminecraft.item.ModItems;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
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
        this.dropSelf(ModBlocks.BOEBO_PLANKS.get());
        this.dropSelf(ModBlocks.BOEBO_STAIRS.get());
        this.dropSelf(ModBlocks.BOEBO_FENCE.get());
        this.dropSelf(ModBlocks.BOEBO_FENCE_GATE.get());
        this.dropSelf(ModBlocks.BOEBO_TRAPDOOR.get());
        this.dropSelf(ModBlocks.BOEBO_PRESSURE_PLATE.get());
        this.dropSelf(ModBlocks.BOEBO_BUTTON.get());
        this.dropSelf(ModBlocks.BOEBO_WOOD.get());
        this.dropSelf(ModBlocks.STRIPPED_BOEBO_WOOD.get());
        this.dropSelf(ModBlocks.CHARRED_PLANKS.get());
        this.dropSelf(ModBlocks.CHARRED_STAIRS.get());
        this.dropSelf(ModBlocks.CHARRED_FENCE.get());
        this.dropSelf(ModBlocks.CHARRED_FENCE_GATE.get());
        this.dropSelf(ModBlocks.CHARRED_TRAPDOOR.get());
        this.dropSelf(ModBlocks.CHARRED_PRESSURE_PLATE.get());
        this.dropSelf(ModBlocks.CHARRED_BUTTON.get());
        this.dropSelf(ModBlocks.BAMBOO_CORAL_BLOCK.get());
        this.dropSelf(ModBlocks.DEAD_BAMBOO_CORAL_BLOCK.get());
        this.dropSelf(ModBlocks.SHELF_CORAL_BLOCK.get());
        this.dropSelf(ModBlocks.DEAD_SHELF_CORAL_BLOCK.get());
        this.dropSelf(ModBlocks.HAG_GOO_BLOCK.get());
        this.dropSelf(ModBlocks.NAUTILUS_BLOCK.get());

        this.add(ModBlocks.CHARRED_SLABS.get(), block -> createSlabItemTable(ModBlocks.CHARRED_SLABS.get()));
        this.add(ModBlocks.CHARRED_DOOR.get(), block -> createDoorTable(ModBlocks.CHARRED_DOOR.get()));
        this.add(ModBlocks.BOEBO_SLABS.get(), block -> createSlabItemTable(ModBlocks.BOEBO_SLABS.get()));
        this.add(ModBlocks.BOEBO_DOOR.get(), block -> createDoorTable(ModBlocks.BOEBO_DOOR.get()));

        //Drops Different Stuff
        this.add(ModBlocks.HAG_GOO_LAYER.get(), block -> createOreDrop(ModBlocks.HAG_GOO_LAYER.get(), ModItems.HAG_GOO.get()));
        this.add(ModBlocks.COBALT_ORE.get(), block -> createOreDrop(ModBlocks.COBALT_ORE.get(), ModItems.RAW_COBALT.get()));
        this.add(ModBlocks.DEEPSLATE_COBALT_ORE.get(), block -> createOreDrop(ModBlocks.DEEPSLATE_COBALT_ORE.get(), ModItems.RAW_COBALT.get()));

        //Crops
        LootItemCondition.Builder lootitemcondition$builder1 = LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.SPECKLEREY_CROP.get())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SpecklereyCropBlock.AGE, 4));
        this.add(ModBlocks.SPECKLEREY_CROP.get(), this.createCropDrops(ModBlocks.SPECKLEREY_CROP.get(),
                ModItems.SPECKLEREY.get(), ModItems.SPECKLEREY_SEEDS.get(), lootitemcondition$builder1));

        //Plants
        this.add(ModBlocks.BUBBLECUP.get(),
                block -> createSingleItemTable(ModBlocks.BUBBLECUP.get()));
        this.add(ModBlocks.BUBBLECUP_BLOSSOM.get(), block ->
                LootTable.lootTable().withPool(
                        applyExplosionCondition(block,
                                LootPool.lootPool()
                                        .setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(ModBlocks.BUBBLECUP_BLOSSOM.get())
                                                .when(MatchTool.toolMatches(
                                                        net.minecraft.advancements.critereon.ItemPredicate.Builder.item()
                                                                .of(net.minecraft.world.item.Items.SHEARS)
                                                )))
                        )
                ));

    }

    private LootTable.Builder createConditionalTable(
            Block block,
            LootItemCondition.Builder blooming,
            LootItemCondition.Builder usingShears
    ) {
        return LootTable.lootTable()
                .withPool(applyExplosionCondition(block,
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))

                                .add(LootItem.lootTableItem(ModItems.BUBBLECUP_BLOSSOM.get())
                                        .when(AllOfCondition.allOf(blooming, usingShears)))

                                .add(LootItem.lootTableItem(ModItems.BUBBLECUP.get())
                                        .when(AnyOfCondition.anyOf(
                                                InvertedLootItemCondition.invert(blooming),
                                                InvertedLootItemCondition.invert(usingShears)
                                        )))
                ));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;

    }

}
