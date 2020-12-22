package playingcards.item;

import com.mojang.realmsclient.gui.ChatFormatting;
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
import net.minecraft.world.World;
import playingcards.PlayingCards;
import playingcards.entity.EntityPokerChip;
import playingcards.init.InitItems;
import playingcards.item.base.ItemBase;
import playingcards.tileentity.TileEntityPokerTable;
import playingcards.util.ItemHelper;
import playingcards.util.Location;
import playingcards.util.StringHelper;
import playingcards.util.UnitChatMessage;

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
        return new UnitChatMessage("Poker Chip", players);
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
        UUID ownerID = nbt.getUniqueId("OwnerID");

        tooltip.add(new StringTextComponent(ChatFormatting.GRAY + "Owner: " + ChatFormatting.GOLD + (ownerID.getLeastSignificantBits() != 0 ? nbt.getString("OwnerName") : "Not set")));

        tooltip.add(new StringTextComponent(TextFormatting.GRAY + "Value (1): " + TextFormatting.GOLD + value));

        if (stack.getCount() > 1) {
            tooltip.add(new StringTextComponent(ChatFormatting.GRAY + "Value (" + stack.getCount() + "): " + ChatFormatting.GOLD + StringHelper.printCommas(value * stack.getCount())));
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {

        ItemStack heldItem = player.getHeldItem(hand);

        if (player.isCrouching()) {

            UnitChatMessage unitMessage = getUnitMessage(player);

            CompoundNBT nbt = ItemHelper.getNBT(heldItem);
            UUID ownerID = nbt.getUniqueId("OwnerID");

            if (ownerID.getLeastSignificantBits() == 0) {

                nbt.putUniqueId("OwnerID", player.getUniqueID());
                nbt.putString("OwnerName", player.getDisplayName().getFormattedText());

                if (world.isRemote) unitMessage.printMessage(TextFormatting.GREEN, "The owner has been set to you!");
            }

            else if (world.isRemote) unitMessage.printMessage(TextFormatting.RED, "An owner already exists!");

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
                UUID ownerID = nbt.getUniqueId("OwnerID");
                String ownerName = nbt.getString("OwnerName");

                if (ownerID.getLeastSignificantBits() != 0) {

                    if (location.getBlock().hasTileEntity(location.getBlockState())) {

                        TileEntity tileEntity = location.getTileEntity();

                        if (tileEntity instanceof TileEntityPokerTable) {

                            TileEntityPokerTable pokerTable = (TileEntityPokerTable) tileEntity;

                            if (!ownerID.equals(pokerTable.getOwnerID())) {
                                if (world.isRemote) unitMessage.printMessage(TextFormatting.RED, "The owner of your chip(s) do not match the owner of the table. Cannot place!");
                                return ActionResultType.PASS;
                            }
                        }
                    }

                    EntityPokerChip chip = new EntityPokerChip(world, context.getHitVec(), ownerID, ownerName, chipID);
                    world.addEntity(chip);
                    context.getItem().shrink(1);
                }

                else if (world.isRemote) unitMessage.printMessage(TextFormatting.RED, "No owner found! Cannot place!");

                return ActionResultType.SUCCESS;
            }
        }

        return ActionResultType.PASS;
    }
}
