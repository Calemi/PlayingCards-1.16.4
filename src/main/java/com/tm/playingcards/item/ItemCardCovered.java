package com.tm.playingcards.item;

import com.tm.playingcards.entity.EntityCard;
import com.tm.playingcards.entity.EntityCardDeck;
import com.tm.playingcards.init.InitItems;
import com.tm.playingcards.item.base.ItemBase;
import com.tm.playingcards.util.CardHelper;
import com.tm.playingcards.util.ItemHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class ItemCardCovered extends ItemBase {

    protected boolean covered = true;

    public ItemCardCovered() {
        super(new Properties().maxStackSize(1));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        CompoundNBT nbt = ItemHelper.getNBT(stack);
        tooltip.add(new TranslationTextComponent("lore.cover").appendString(" ").mergeStyle(TextFormatting.GRAY).append(new TranslationTextComponent(CardHelper.CARD_SKIN_NAMES[nbt.getByte("SkinID")]).mergeStyle(TextFormatting.AQUA)));
    }

    public void flipCard(ItemStack heldItem, LivingEntity entity) {

        if (entity instanceof PlayerEntity) {

            PlayerEntity player = (PlayerEntity) entity;

            if (player.getHeldItemMainhand().getItem() instanceof ItemCardCovered) {

                Item nextCard = InitItems.CARD.get();
                if (!covered) nextCard = InitItems.CARD_COVERED.get();

                ItemStack newCard = new ItemStack(nextCard);
                newCard.setDamage(heldItem.getDamage());

                CompoundNBT heldNBT = ItemHelper.getNBT(heldItem);
                ItemHelper.getNBT(newCard).putUniqueId("UUID", heldNBT.getUniqueId("UUID"));
                ItemHelper.getNBT(newCard).putByte("SkinID", heldNBT.getByte("SkinID"));

                player.setHeldItem(Hand.MAIN_HAND, newCard);
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {

        if (world.getGameTime() % 60 == 0) {

            if (entity instanceof PlayerEntity) {

                PlayerEntity player = (PlayerEntity) entity;
                BlockPos pos = player.getPosition();

                CompoundNBT nbt = ItemHelper.getNBT(stack);

                if (nbt.hasUniqueId("UUID")) {
                    UUID id = ItemHelper.getNBT(stack).getUniqueId("UUID");

                    if (id.getLeastSignificantBits() == 0) {
                        return;
                    }

                    List<EntityCardDeck> closeDecks = world.getEntitiesWithinAABB(EntityCardDeck.class, new AxisAlignedBB(pos.getX() - 20, pos.getY() - 20, pos.getZ() - 20, pos.getX() + 20, pos.getY() + 20, pos.getZ() + 20));

                    boolean found = false;

                    for (EntityCardDeck closeDeck : closeDecks) {

                        if (closeDeck.getUniqueID().equals(id)) {
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        player.inventory.getStackInSlot(itemSlot).shrink(1);
                    }
                }
            }
        }
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {

        PlayerEntity player = context.getPlayer();

        if (player != null) {

            if (!player.isCrouching()) {

                BlockPos pos = context.getPos();
                List<EntityCardDeck> closeDecks = context.getWorld().getEntitiesWithinAABB(EntityCardDeck.class, new AxisAlignedBB(pos.getX() - 8, pos.getY() - 8, pos.getZ() - 8, pos.getX() + 8, pos.getY() + 8, pos.getZ() + 8));

                CompoundNBT nbt = ItemHelper.getNBT(context.getItem());

                UUID deckID = nbt.getUniqueId("UUID");

                for (EntityCardDeck closeDeck : closeDecks) {

                    if (closeDeck.getUniqueID().equals(deckID)) {

                        World world = context.getWorld();
                        EntityCard cardDeck = new EntityCard(world, context.getHitVec(), context.getPlacementYaw(), nbt.getByte("SkinID"), deckID, covered, (byte) context.getItem().getDamage());
                        world.addEntity(cardDeck);
                        context.getItem().shrink(1);

                        return ActionResultType.SUCCESS;
                    }
                }
            }
        }

        return ActionResultType.PASS;
    }
}
