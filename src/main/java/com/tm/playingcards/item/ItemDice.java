package com.tm.playingcards.item;

import com.tm.playingcards.item.base.ItemBase;
import com.tm.playingcards.main.PlayingCards;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import com.tm.playingcards.entity.EntityDice;

public class ItemDice extends ItemBase {

    public ItemDice() {
        super(new Item.Properties().group(PlayingCards.TAB).maxStackSize(5));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {

        ItemStack stack = player.getHeldItem(hand);

        EntityDice cardDeck = new EntityDice(world, player.getPositionVec(), player.getRotationYawHead());
        world.addEntity(cardDeck);
        stack.shrink(1);

        return new ActionResult<>(ActionResultType.SUCCESS, stack);
    }
}
