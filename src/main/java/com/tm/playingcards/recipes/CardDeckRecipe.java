package com.tm.playingcards.recipes;

import com.tm.playingcards.init.InitItems;
import com.tm.playingcards.init.InitRecipes;
import com.tm.playingcards.util.ItemHelper;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class CardDeckRecipe extends SpecialRecipe {

    public CardDeckRecipe(ResourceLocation id) {
        super(id);
    }

    @Override
    public boolean matches(CraftingInventory inv, World world) {

        boolean matches = true;

        for (int i = 0; i < inv.getSizeInventory(); ++i) {

            ItemStack stackInSlot = inv.getStackInSlot(i);

            if (i != 4) {

                if (stackInSlot.getItem() != Items.PAPER) {
                    matches = false;
                }
            }
        }

        ItemStack middleSlot = inv.getStackInSlot(4);

        if (middleSlot.getItem() != Items.BLUE_DYE && middleSlot.getItem() != Items.RED_DYE && middleSlot.getItem() != Items.BLACK_DYE && middleSlot.getItem() != Items.PINK_DYE) {
            matches = false;
        }

        return matches;
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory inv) {

        ItemStack result = new ItemStack(InitItems.CARD_DECK.get());
        CompoundNBT nbt = ItemHelper.getNBT(result);

        ItemStack middleSlot = inv.getStackInSlot(4);

        if (middleSlot.getItem() == Items.RED_DYE) {
            nbt.putByte("SkinID", (byte)1);
        }

        else if (middleSlot.getItem() == Items.BLACK_DYE) {
            nbt.putByte("SkinID", (byte)2);
        }

        else if (middleSlot.getItem() == Items.PINK_DYE) {
            nbt.putByte("SkinID", (byte)3);
        }

        return result;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height >= 9;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return InitRecipes.DECK.get();
    }
}
