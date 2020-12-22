package com.tm.playingcards.init;

import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import com.tm.playingcards.util.ItemHelper;

public class InitModelOverrides {

    public static void init() {
        ItemModelsProperties.registerProperty(InitItems.CARD.get(), new ResourceLocation("value"), (stack, world, player) -> stack.getDamage());
        ItemModelsProperties.registerProperty(InitItems.CARD_COVERED.get(), new ResourceLocation("skin"), (stack, world, player) -> ItemHelper.getNBT(stack).getByte("SkinID"));
        ItemModelsProperties.registerProperty(InitItems.CARD_DECK.get(), new ResourceLocation("skin"), (stack, world, player) -> ItemHelper.getNBT(stack).getByte("SkinID"));
    }
}
