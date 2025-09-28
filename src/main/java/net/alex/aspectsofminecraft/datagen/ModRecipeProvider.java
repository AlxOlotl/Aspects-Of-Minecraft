package net.alex.aspectsofminecraft.datagen;

import net.alex.aspectsofminecraft.block.ModBlocks;
import net.alex.aspectsofminecraft.item.ModItems;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.List;
import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    private static final List<ItemLike> COBALT_INGOT_SMELTABLES = List.of(ModItems.RAW_COBALT.get(),
             ModBlocks.COBALT_ORE.get(), ModBlocks.DEEPSLATE_COBALT_ORE.get());

    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.COBALT_BLOCK.get())
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', ModItems.COBALT_INGOT.get())
                .unlockedBy("has_cobalt", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(ModItems.COBALT_INGOT.get()).build()))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.COBALT_INGOT.get(), 9)
                .requires(ModBlocks.COBALT_BLOCK.get())
                .unlockedBy("has_cobalt_block", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(ModBlocks.COBALT_BLOCK.get()).build()))
                .save(pWriter);

        nineBlockStorageRecipes(pWriter, RecipeCategory.MISC, ModItems.RAW_COBALT.get(), RecipeCategory.MISC, ModBlocks.RAW_COBALT_BLOCK.get());
        oreSmelting(pWriter, COBALT_INGOT_SMELTABLES, RecipeCategory.MISC, ModItems.COBALT_INGOT.get(), 0.25f, 200, "cobalt");
        oreBlasting(pWriter, COBALT_INGOT_SMELTABLES, RecipeCategory.MISC, ModItems.COBALT_INGOT.get(), 0.25f, 100, "cobalt");
    }
}
