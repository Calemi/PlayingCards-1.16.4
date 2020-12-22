package com.tm.playingcards.init;

import com.tm.playingcards.main.PCReference;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import com.tm.playingcards.recipes.CardDeckRecipe;

public class InitRecipes {

    public static final DeferredRegister<IRecipeSerializer<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, PCReference.MOD_ID);

    public static final RegistryObject<IRecipeSerializer<CardDeckRecipe>> DECK = RECIPES.register("deck", () -> new SpecialRecipeSerializer<>(CardDeckRecipe::new));
}
