package com.tm.playingcards.main;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import com.tm.playingcards.init.InitItems;

class PCTab extends ItemGroup {

    PCTab () {
        super(PCReference.MOD_ID + ".tabMain");
    }

    @Override
    public ItemStack createIcon () {
        return new ItemStack(InitItems.CARD.get());
    }
}
