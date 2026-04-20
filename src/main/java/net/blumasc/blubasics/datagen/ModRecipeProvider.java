package net.blumasc.blubasics.datagen;

import net.blumasc.blubasics.BluBasicsMod;
import net.blumasc.blubasics.block.BaseModBlocks;
import net.blumasc.blubasics.item.BaseModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        foodSmelting(recipeOutput, Collections.singletonList(BaseModItems.MUSHROOM_SKEWER), RecipeCategory.FOOD, BaseModItems.COOKED_MUSHROOM_SKEWER.get(), 0.25f, 200, "cooked_mushrrom_skewer");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, BaseModItems.MUSHROOM_SKEWER)
                .requires(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c","mushrooms")))
                .requires(BaseModBlocks.JUMP_MUSHROOM.get())
                .requires(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c","mushrooms")))
                .requires(Items.STICK)
                .unlockedBy("has_jump_mushroom", has(BaseModBlocks.JUMP_MUSHROOM.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, Items.ARROW,4)
                .pattern("C")
                .pattern("S")
                .pattern("F")
                .define('C', Items.FLINT)
                .define('S', ItemTags.create(ResourceLocation.fromNamespaceAndPath("c","rods/wooden")))
                .define('F', ItemTags.create(ResourceLocation.fromNamespaceAndPath("c","feathers")))
                .unlockedBy("has_feather", has(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c","feathers")))).save(recipeOutput, BluBasicsMod.MODID+":arrow_any_feather");


        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, Items.BRUSH)
                .pattern("C")
                .pattern("S")
                .pattern("F")
                .define('C', ItemTags.create(ResourceLocation.fromNamespaceAndPath("c","feathers")))
                .define('S', ItemTags.create(ResourceLocation.fromNamespaceAndPath("c","ingots/copper")))
                .define('F', ItemTags.create(ResourceLocation.fromNamespaceAndPath("c","rods/wooden")))
                .unlockedBy("has_feather", has(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c","feathers")))).save(recipeOutput, BluBasicsMod.MODID+":brush_any_feather");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.WRITABLE_BOOK)
                .requires(Items.BOOK)
                .requires(Items.INK_SAC)
                .requires(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c","feathers")))
                .unlockedBy("has_ink_sac", has(Items.INK_SAC))
                .save(recipeOutput, BluBasicsMod.MODID+":written_book_any_feather");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.BONE_MEAL, 5)
                .requires(BaseModItems.BEETLE_HORN)
                .unlockedBy("has_beetle_horn", has(BaseModItems.BEETLE_HORN))
                .save(recipeOutput, BluBasicsMod.MODID+":bone_meal_from_beetle_horn");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, BaseModBlocks.BLUBOTT_PLUSH)
                .requires(Items.ARMOR_STAND)
                .requires(Items.CYAN_WOOL)
                .unlockedBy("has_armor_stand", has(Items.ARMOR_STAND))
                .save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, BaseModBlocks.BLUMASC_PLUSH)
                .requires(Items.ARMOR_STAND)
                .requires(Items.BLUE_WOOL)
                .unlockedBy("has_armor_stand", has(Items.ARMOR_STAND))
                .save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, BaseModBlocks.RIKARASHI_PLUSH)
                .requires(Items.ARMOR_STAND)
                .requires(Items.ORANGE_WOOL)
                .unlockedBy("has_armor_stand", has(Items.ARMOR_STAND))
                .save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, BaseModBlocks.XTHEHEROLP_PLUSH)
                .requires(Items.ARMOR_STAND)
                .requires(Items.GRAY_WOOL)
                .unlockedBy("has_armor_stand", has(Items.ARMOR_STAND))
                .save(recipeOutput);
    }

    protected static void itemSmelting(RecipeOutput recipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult,
                                       float pExperience, int pCookingTIme, String pGroup) {
        itemCooking(recipeOutput, RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, pIngredients, pCategory, pResult,
                pExperience, pCookingTIme, pGroup, "_from_smelting");
    }

    protected static void itemSmoking(RecipeOutput recipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult,
                                      float pExperience, int pCookingTime, String pGroup) {
        itemCooking(recipeOutput, RecipeSerializer.SMOKING_RECIPE, SmokingRecipe::new, pIngredients, pCategory, pResult,
                pExperience, pCookingTime, pGroup, "_from_smoking");
    }
    protected static void itemCampfire(RecipeOutput recipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult,
                                       float pExperience, int pCookingTime, String pGroup) {
        itemCooking(recipeOutput, RecipeSerializer.CAMPFIRE_COOKING_RECIPE, CampfireCookingRecipe::new, pIngredients, pCategory, pResult,
                pExperience, pCookingTime, pGroup, "_from_campfire");
    }
    protected static void foodSmelting(RecipeOutput recipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult,
                                       float pExperience, int pCookingTime, String pGroup) {
        itemSmelting(recipeOutput, pIngredients, pCategory, pResult, pExperience, pCookingTime, pGroup);
        itemSmoking(recipeOutput, pIngredients, pCategory, pResult, pExperience, pCookingTime/2, pGroup);
        itemCampfire(recipeOutput, pIngredients, pCategory, pResult, pExperience, pCookingTime*3, pGroup);
    }

    protected static <T extends AbstractCookingRecipe> void itemCooking(RecipeOutput recipeOutput, RecipeSerializer<T> pCookingSerializer, AbstractCookingRecipe.Factory<T> factory,
                                                                        List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String pRecipeName) {
        for(ItemLike itemlike : pIngredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult, pExperience, pCookingTime, pCookingSerializer, factory).group(pGroup).unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(recipeOutput, BluBasicsMod.MODID + ":smelting/" + getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike));
        }
    }
}
