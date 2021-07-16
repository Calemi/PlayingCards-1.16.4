package com.tm.playingcards.item;

import com.tm.playingcards.entity.EntityPokerChip;
import com.tm.playingcards.init.InitItems;
import com.tm.playingcards.item.base.ItemBase;
import com.tm.playingcards.main.PlayingCards;
import com.tm.playingcards.tileentity.TileEntityPokerTable;
import com.tm.playingcards.util.ItemHelper;
import com.tm.playingcards.util.Location;
import com.tm.playingcards.util.StringHelper;
import com.tm.playingcards.util.UnitChatMessage;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class ItemPokerChip extends ItemBase {

    private final byte chipID;
    private final int value;

    public ItemPokerChip(byte chipID, int value) {
        super(new Properties().group(PlayingCards.TAB));
        this.chipID = chipID;
        this.value = value;
    }

    private UnitChatMessage getUnitMessage(PlayerEntity... players) {
        return new UnitChatMessage("poker_chip", players);
    }

    public byte getChipID() {
        return this.chipID;
    }

    public static Item getPokerChip(byte pokerChipID) {

        switch (pokerChipID) {
            case 1:
                return InitItems.POKER_CHIP_RED.get();
            case 2:
                return InitItems.POKER_CHIP_BLUE.get();
            case 3:
                return InitItems.POKER_CHIP_GREEN.get();
            case 4:
                return InitItems.POKER_CHIP_BLACK.get();
        }

        return InitItems.POKER_CHIP_WHITE.get();
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {

        CompoundNBT nbt = ItemHelper.getNBT(stack);

        if (nbt.hasUniqueId("OwnerID")) {
            tooltip.add(new StringTextComponent(TextFormatting.GRAY + "Owner: " + TextFormatting.GOLD + nbt.getString("OwnerName")));
        }

        else tooltip.add(new StringTextComponent(TextFormatting.GRAY + "Owner: " + TextFormatting.GOLD + "Not set"));

        tooltip.add(new StringTextComponent(TextFormatting.GRAY + "Value (1): " + TextFormatting.GOLD + value));

        if (stack.getCount() > 1) {
            tooltip.add(new StringTextComponent(TextFormatting.GRAY + "Value (" + stack.getCount() + "): " + TextFormatting.GOLD + StringHelper.printCommas(value * stack.getCount())));
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {

        ItemStack heldItem = player.getHeldItem(hand);

        if (player.isCrouching()) {

            UnitChatMessage unitMessage = getUnitMessage(player);
            CompoundNBT nbt = ItemHelper.getNBT(heldItem);

            if (!nbt.hasUniqueId("OwnerID")) {

                nbt.putUniqueId("OwnerID", player.getUniqueID());
                nbt.putString("OwnerName", player.getDisplayName().getString());

                if (world.isRemote) unitMessage.printMessage(TextFormatting.GREEN, new TranslationTextComponent("message.poker_chip_owner_set"));
            }

            else if (world.isRemote) unitMessage.printMessage(TextFormatting.RED, new TranslationTextComponent("message.poker_chip_owner_error"));

            return new ActionResult<>(ActionResultType.SUCCESS, heldItem);
        }

        return new ActionResult<>(ActionResultType.FAIL, heldItem);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {

        PlayerEntity player = context.getPlayer();

        if (player != null) {

            if (!player.isCrouching()) {

                World world = context.getWorld();
                Location location = new Location(world, context.getPos());

                UnitChatMessage unitMessage = getUnitMessage(player);
                CompoundNBT nbt = ItemHelper.getNBT(context.getItem());

                if (nbt.hasUniqueId("OwnerID")) {

                    UUID ownerID = nbt.getUniqueId("OwnerID");
                    String ownerName = nbt.getString("OwnerName");

                    if (location.getBlock().hasTileEntity(location.getBlockState())) {

                        TileEntity tileEntity = location.getTileEntity();

                        if (tileEntity instanceof TileEntityPokerTable) {

                            TileEntityPokerTable pokerTable = (TileEntityPokerTable) tileEntity;

                            if (!ownerID.equals(pokerTable.getOwnerID())) {

                                if (world.isRemote) unitMessage.printMessage(TextFormatting.RED, new TranslationTextComponent("message.poker_chip_table_error"));
                                return ActionResultType.PASS;
                            }
                        }
                    }

                    EntityPokerChip chip = new EntityPokerChip(world, context.getHitVec(), ownerID, ownerName, chipID);
                    world.addEntity(chip);
                    context.getItem().shrink(1);
                }

                else if (world.isRemote) unitMessage.printMessage(TextFormatting.RED, new TranslationTextComponent("message.poker_chip_owner_missing"));

                return ActionResultType.SUCCESS;
            }
        }

        return ActionResultType.PASS;
    }
}
