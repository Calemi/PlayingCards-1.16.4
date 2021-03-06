package com.tm.playingcards.event;

import com.tm.playingcards.util.ItemHelper;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import com.tm.playingcards.item.ItemCardDeck;

public class CraftingEvent {

    @SubscribeEvent
    public void onCrafted(PlayerEvent.ItemCraftedEvent event) {

        if (event.getCrafting().getItem() instanceof ItemCardDeck) {

            if (event.getInventory().getStackInSlot(4).getItem() == Items.RED_DYE) {
                CompoundNBT nbt = ItemHelper.getNBT(event.getCrafting());
                nbt.putByte("SkinID", (byte)1);
            }
        }
    }
}
