package playingcards.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import playingcards.entity.base.EntityStacked;
import playingcards.init.InitEntityTypes;
import playingcards.init.InitItems;
import playingcards.item.ItemCardCovered;
import playingcards.util.ChatHelper;
import playingcards.util.ItemHelper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class EntityCard extends EntityStacked {

    private static final DataParameter<Float> ROTATION = EntityDataManager.createKey(EntityCard.class, DataSerializers.FLOAT);
    private static final DataParameter<Byte> SKIN_ID = EntityDataManager.createKey(EntityCard.class, DataSerializers.BYTE);
    private static final DataParameter<Optional<UUID>> DECK_UUID = EntityDataManager.createKey(EntityCard.class, DataSerializers.OPTIONAL_UNIQUE_ID);
    private static final DataParameter<Boolean> COVERED = EntityDataManager.createKey(EntityCard.class, DataSerializers.BOOLEAN);

    public EntityCard(EntityType<? extends EntityCard> type, World world) {
        super(type, world);
    }

    public EntityCard(World world, Vec3d position, float rotation, byte skinID, UUID deckUUID, boolean covered, byte firstCardID) {
        super(InitEntityTypes.CARD.get(), world, position);

        createStack();
        addToTop(firstCardID);
        dataManager.set(ROTATION, rotation);
        dataManager.set(SKIN_ID, skinID);
        dataManager.set(DECK_UUID, Optional.of(deckUUID));
        dataManager.set(COVERED, covered);
    }

    public float getRotation() {
        return dataManager.get(ROTATION);
    }

    public byte getSkinID() {
        return dataManager.get(SKIN_ID);
    }

    public UUID getDeckUUID() {
        return (dataManager.get(DECK_UUID).isPresent()) ? dataManager.get(DECK_UUID).get() : null;
    }

    public boolean isCover() {
        return dataManager.get(COVERED);
    }

    private void takeCard(PlayerEntity player) {

        ItemStack card = new ItemStack(InitItems.CARD.get());
        if (dataManager.get(COVERED)) card = new ItemStack(InitItems.CARD_COVERED.get());

        card.setDamage(getTopStackID());
        ItemHelper.getNBT(card).putUniqueId("UUID", getDeckUUID());
        ItemHelper.getNBT(card).putByte("SkinID", dataManager.get(SKIN_ID));

        if (!world.isRemote) {
            ItemHelper.spawnStackAtEntity(world, player, card);
        }

        removeFromTop();

        if (getStackAmount() <= 0) {
            remove();
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (world.getGameTime() % 20 == 0) {

            BlockPos pos = getPosition();

            List<EntityCardDeck> closeDecks = world.getEntitiesWithinAABB(EntityCardDeck.class, new AxisAlignedBB(pos.getX() - 20, pos.getY() - 20, pos.getZ() - 20, pos.getX() + 20, pos.getY() + 20, pos.getZ() + 20));

            boolean foundParentDeck = false;

            for (EntityCardDeck closeDeck : closeDecks) {

                if (getDeckUUID().equals(closeDeck.getUniqueID())) {
                    foundParentDeck = true;
                }
            }

            if (!foundParentDeck) remove();

            super.onRemovedFromWorld();
        }
    }

    @Override
    public boolean processInitialInteract(PlayerEntity player, Hand hand) {

        ItemStack stack = player.getHeldItem(hand);

        if (stack.getItem() instanceof ItemCardCovered) {

            if (getStackAmount() < MAX_STACK_SIZE) {
                addToTop((byte) stack.getDamage());
                stack.shrink(1);
            }

            else {
                if (world.isRemote) ChatHelper.printModMessage(TextFormatting.RED, "The stack is full!", player);
            }
        }

        else takeCard(player);

        return true;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        dataManager.set(COVERED, !dataManager.get(COVERED));
        return true;
    }

    @Override
    public void moreData() {
        dataManager.register(ROTATION, 0F);
        dataManager.register(SKIN_ID, (byte) 0);
        dataManager.register(DECK_UUID, Optional.empty());
        dataManager.register(COVERED, false);
    }

    @Override
    protected void readAdditional(CompoundNBT nbt) {
        super.readAdditional(nbt);
        dataManager.set(ROTATION, nbt.getFloat("Rotation"));
        dataManager.set(SKIN_ID, nbt.getByte("SkinID"));
        dataManager.set(DECK_UUID, Optional.of(nbt.getUniqueId("DeckID")));
        dataManager.set(COVERED, nbt.getBoolean("Covered"));
    }

    @Override
    protected void writeAdditional(CompoundNBT nbt) {
        super.writeAdditional(nbt);
        nbt.putFloat("Rotation", dataManager.get(ROTATION));
        nbt.putByte("SkinID", dataManager.get(SKIN_ID));
        nbt.putUniqueId("DeckID", getDeckUUID());
        nbt.putBoolean("Covered", dataManager.get(COVERED));
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
