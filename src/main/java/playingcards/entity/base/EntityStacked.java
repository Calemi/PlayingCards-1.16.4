package playingcards.entity.base;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import playingcards.entity.data.PCDataSerializers;
import playingcards.util.ArrayHelper;

public abstract class EntityStacked extends Entity {

    public static final byte MAX_STACK_SIZE = 52;

    protected static final DataParameter<Byte[]> STACK = EntityDataManager.createKey(EntityStacked.class, PCDataSerializers.STACK);

    public EntityStacked(EntityType<? extends EntityStacked> type, World world) {
        super(type, world);
    }

    public EntityStacked(EntityType<? extends EntityStacked> type, World world, Vec3d position) {
        this(type, world);
        setLocationAndAngles(position.x, position.y, position.z, 0, 0);
    }

    public int getStackAmount() {
        return dataManager.get(STACK).length;
    }

    public byte getTopStackID() {
        return getIDAt(getStackAmount() - 1);
    }

    public byte getIDAt(int index) {

        if (index >= 0 && index < getStackAmount()) {
            return dataManager.get(STACK)[index];
        }

        return 0;
    }

    public void removeFromTop() {
        Byte[] newStack = new Byte[getStackAmount() - 1];

        for (int index = 0; index < newStack.length; index++) {
            newStack[index] = dataManager.get(STACK)[index];
        }

        dataManager.set(STACK, newStack);
    }

    public void addToTop(byte id) {

        Byte[] newStack = new Byte[getStackAmount() + 1];

        for (int index = 0; index < getStackAmount(); index++) {
            newStack[index] = dataManager.get(STACK)[index];
        }

        newStack[newStack.length - 1] = id;

        dataManager.set(STACK, newStack);
    }

    public void createStack() {
        Byte[] newStack = new Byte[0];
        dataManager.set(STACK, newStack);
    }

    public void shuffleStack() {

        Byte[] newStack = new Byte[getStackAmount()];

        for (int index = 0; index < getStackAmount(); index++) {
            newStack[index] = dataManager.get(STACK)[index];
        }

        ArrayHelper.shuffle(newStack);

        dataManager.set(STACK, newStack);
    }

    @Override
    public void tick() {
        super.tick();

        Vec3d pos = getPositionVec();
        double size = 0.2D;
        double addAmount = 0.0045D;

        setBoundingBox(new AxisAlignedBB(pos.x - size, pos.y, pos.z - size, pos.x + size, pos.y + 0.03D + (addAmount * getStackAmount()), pos.z + size));
    }

    public abstract void moreData();

    @Override
    protected void registerData() {
        dataManager.register(STACK, new Byte[0]);
        moreData();
    }

    @Override
    protected void readAdditional(CompoundNBT nbt) {
        dataManager.set(STACK, ArrayHelper.toObject(nbt.getByteArray("Stack")));
    }

    @Override
    protected void writeAdditional(CompoundNBT nbt) {
        nbt.putByteArray("Stack", ArrayHelper.toPrimitive(dataManager.get(STACK)));
    }

    public boolean canBeCollidedWith() {
        return true;
    }
}
