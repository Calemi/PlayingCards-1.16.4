package com.tm.playingcards.entity;

import com.tm.playingcards.entity.base.EntityStacked;
import com.tm.playingcards.util.ChatHelper;
import com.tm.playingcards.util.ItemHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import com.tm.playingcards.init.InitEntityTypes;
import com.tm.playingcards.init.InitItems;
import com.tm.playingcards.item.ItemPokerChip;

import java.util.Optional;
import java.util.UUID;

public class EntityPokerChip extends EntityStacked {

    private static final DataParameter<Optional<UUID>> OWNER_UUID = EntityDataManager.createKey(EntityPokerChip.class, DataSerializers.OPTIONAL_UNIQUE_ID);
    private static final DataParameter<String> OWNER_NAME = EntityDataManager.createKey(EntityPokerChip.class, DataSerializers.STRING);

    public EntityPokerChip(EntityType<? extends EntityPokerChip> type, World world) {
        super(type, world);
    }

    public EntityPokerChip(World world, Vector3d position, UUID ownerID, String ownerName, byte firstChipID) {
        super(InitEntityTypes.POKER_CHIP.get(), world, position);

        createStack();
        addToTop(firstChipID);
        dataManager.set(OWNER_UUID, Optional.of(ownerID));
        dataManager.set(OWNER_NAME, ownerName);
    }

    public UUID getOwnerUUID() {
        return (dataManager.get(OWNER_UUID).isPresent()) ? dataManager.get(OWNER_UUID).get() : null;
    }

    private void takeChip(PlayerEntity player) {

        byte chipID = getTopStackID();

        if (!world.isRemote) spawnChip(player, ItemPokerChip.getPokerChip(chipID), 1);

        removeFromTop();

        if (getStackAmount() <= 0) {
            remove();
        }
    }

    @Override
    public ActionResultType processInitialInteract(PlayerEntity player, Hand hand) {

        ItemStack stack = player.getHeldItem(hand);

        if (stack.getItem() instanceof ItemPokerChip) {

            CompoundNBT nbt = ItemHelper.getNBT(stack);

            UUID ownerID = nbt.getUniqueId("OwnerID");

            if (ownerID.equals(getOwnerUUID())) {

                if (player.isCrouching()) {

                    while (true) {

                        if (getStackAmount() < MAX_STACK_SIZE && stack.getCount() > 0) {
                            ItemPokerChip chip = (ItemPokerChip) stack.getItem();
                            addToTop(chip.getChipID());
                            stack.shrink(1);
                        }

                        else break;
                    }
                }

                else {

                    if (getStackAmount() < MAX_STACK_SIZE) {
                        ItemPokerChip chip = (ItemPokerChip) stack.getItem();
                        addToTop(chip.getChipID());
                        stack.shrink(1);
                    }

                    else {
                        if (world.isRemote) ChatHelper.printModMessage(TextFormatting.RED, "The stack is full!", player);
                    }
                }
            }

            else if (world.isRemote) ChatHelper.printModMessage(TextFormatting.RED, "Owner does not match the one of the stack!", player);
        }

        else takeChip(player);

        return ActionResultType.SUCCESS;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {

        if (source.getImmediateSource() instanceof PlayerEntity) {

            PlayerEntity player = (PlayerEntity) source.getImmediateSource();

            int whiteAmount = 0;
            int redAmount = 0;
            int blueAmount = 0;
            int greenAmount = 0;
            int blackAmount = 0;

            for (int i = 0; i < dataManager.get(STACK).length; i++) {

                byte chipID = getIDAt(i);

                if (chipID == 0) whiteAmount++;
                if (chipID == 1) redAmount++;
                if (chipID == 2) blueAmount++;
                if (chipID == 3) greenAmount++;
                if (chipID == 4) blackAmount++;
            }

            if (whiteAmount > 0) spawnChip(player, InitItems.POKER_CHIP_WHITE.get(), whiteAmount);
            if (redAmount > 0) spawnChip(player, InitItems.POKER_CHIP_RED.get(), redAmount);
            if (blueAmount > 0) spawnChip(player, InitItems.POKER_CHIP_BLUE.get(), blueAmount);
            if (greenAmount > 0) spawnChip(player, InitItems.POKER_CHIP_GREEN.get(), greenAmount);
            if (blackAmount > 0) spawnChip(player, InitItems.POKER_CHIP_BLACK.get(), blackAmount);

            remove();

            return false;
        }

        return true;
    }

    private void spawnChip(PlayerEntity player, Item item, int amount) {

        if (!world.isRemote) {
            ItemStack chip = new ItemStack(item, amount);
            CompoundNBT nbt = ItemHelper.getNBT(chip);
            nbt.putUniqueId("OwnerID", getOwnerUUID());
            nbt.putString("OwnerName", dataManager.get(OWNER_NAME));
            ItemHelper.spawnStackAtEntity(world, player, chip);
        }
    }

    @Override
    public void tick() {
        super.tick();

        Vector3d pos = getPositionVec();
        double size = 0.1D;
        double addAmount = 0.01575D;

        setBoundingBox(new AxisAlignedBB(pos.x - size, pos.y, pos.z - size, pos.x + size, pos.y + 0.02D + (addAmount * getStackAmount()), pos.z + size));
    }

    @Override
    public void moreData() {
        dataManager.register(OWNER_UUID, Optional.empty());
        dataManager.register(OWNER_NAME, "");
    }

    @Override
    protected void readAdditional(CompoundNBT nbt) {
        super.readAdditional(nbt);
        dataManager.set(OWNER_UUID, Optional.of(nbt.getUniqueId("OwnerID")));
        dataManager.set(OWNER_NAME, nbt.getString("OwnerName"));
    }

    @Override
    protected void writeAdditional(CompoundNBT nbt) {
        super.writeAdditional(nbt);
        nbt.putUniqueId("OwnerID", getOwnerUUID());
        nbt.putString("OwnerName", dataManager.get(OWNER_NAME));
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
