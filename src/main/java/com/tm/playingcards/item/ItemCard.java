package com.tm.playingcards.item;

import com.tm.playingcards.util.CardHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemCard extends ItemCardCovered {

    public ItemCard() {
        covered = false;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new StringTextComponent(TextFormatting.GOLD + CardHelper.getCardName(stack.getDamage())));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
