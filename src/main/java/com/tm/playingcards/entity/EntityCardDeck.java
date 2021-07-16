package com.tm.playingcards.entity;

import com.tm.playingcards.entity.base.EntityStacked;
import com.tm.playingcards.init.InitEntityTypes;
import com.tm.playingcards.init.InitItems;
import com.tm.playingcards.util.ChatHelper;
import com.tm.playingcards.util.ItemHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityCardDeck extends EntityStacked {

    private static final DataParameter<Float> ROTATION = EntityDataManager.createKey(EntityCardDeck.class, DataSerializers.FLOAT);
    private static final DataParameter<Byte> SKIN_ID = EntityDataManager.createKey(EntityCardDeck.class, DataSerializers.BYTE);

    public EntityCardDeck(EntityType<? extends EntityCardDeck> type, World world) {
        super(type, world);
    }

    public EntityCardDeck(World world, Vector3d position, float rotation, byte skinID) {
        super(InitEntityTypes.CARD_DECK.get(), world, position);

        createAndFillDeck();
        shuffleStack();

        dataManager.set(ROTATION, rotation);
        dataManager.set(SKIN_ID, skinID);
    }

    public float getRotation() {
        return dataManager.get(ROTATION);
    }

    public byte getSkinID() {
        return dataManager.get(SKIN_ID);
    }

    private void createAndFillDeck() {

        Byte[] newStack = new Byte[52];

        for (byte index = 0; index < 52; index++) {
            newStack[index] = index;
        }

        dataManager.set(STACK, newStack);
    }

    @Override
    public ActionResultType processInitialInteract(PlayerEntity player, Hand hand) {

        if (hand == Hand.MAIN_HAND) {

            if (getStackAmount() > 0) {

                int cardID = getTopStackID();

                ItemStack card = new ItemStack(InitItems.CARD_COVERED.get());

                card.setDamage(cardID);
                ItemHelper.getNBT(card).putUniqueId("UUID", getUniqueID());
                ItemHelper.getNBT(card).putByte("SkinID", dataManager.get(SKIN_ID));

                if (!world.isRemote) {
                    ItemHelper.spawnStackAtEntity(world, player, card);
                }

                removeFromTop();

                return player.getHeldItemMainhand().isEmpty() ? ActionResultType.SUCCESS : ActionResultType.FAIL;
            }

            else if (world.isRemote) ChatHelper.printModMessage(TextFormatting.RED, new TranslationTextComponent("message.stack_empty"), player);
        }

        return ActionResultType.FAIL;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {

        if (source.getImmediateSource() instanceof PlayerEntity) {

            PlayerEntity player = (PlayerEntity) source.getImmediateSource();

            if (player.isCrouching()) {
                ItemStack deck = new ItemStack(InitItems.CARD_DECK.get());
                ItemHelper.getNBT(deck).putByte("SkinID", dataManager.get(SKIN_ID));

                ItemHelper.spawnStackAtEntity(world, player, deck);
                remove();
            } else {
                shuffleStack();
                if (world.isRemote) ChatHelper.printModMessage(TextFormatting.GREEN, new TranslationTextComponent("message.stack_shuffled"), player);
            }

            return true;
        }

        return false;
    }

    @Override
    public void moreData() {
        dataManager.register(ROTATION, 0F);
        dataManager.register(SKIN_ID, (byte) 0);
    }

    @Override
    protected void readAdditional(CompoundNBT nbt) {
        super.readAdditional(nbt);
        dataManager.set(ROTATION, nbt.getFloat("Rotation"));
        dataManager.set(SKIN_ID, nbt.getByte("SkinID"));
    }

    @Override
    protected void writeAdditional(CompoundNBT nbt) {
        super.writeAdditional(nbt);
        nbt.putFloat("Rotation", dataManager.get(ROTATION));
        nbt.putByte("SkinID", dataManager.get(SKIN_ID));
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
