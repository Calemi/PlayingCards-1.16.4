package playingcards.init;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import playingcards.PCReference;
import playingcards.recipes.CardDeckRecipe;

public class InitRecipes {

    public static final DeferredRegister<IRecipeSerializer<?>> RECIPES = new DeferredRegister<>(ForgeRegistries.RECIPE_SERIALIZERS, PCReference.MOD_ID);

    public static final RegistryObject<IRecipeSerializer<CardDeckRecipe>> DECK = RECIPES.register("deck", () -> new SpecialRecipeSerializer<>(CardDeckRecipe::new));
}
