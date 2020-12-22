package com.tm.playingcards.entity;

import com.tm.playingcards.init.InitEntityTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityDice extends Entity {

    public EntityDice(EntityType<? extends EntityDice> type, World world) {
        super(type, world);
    }

    public EntityDice(World world, Vector3d position, float rotation) {
        this(InitEntityTypes.DICE.get(), world);
        setLocationAndAngles(position.x, position.y, position.z, rotation, 0);

        float sin = MathHelper.sin(this.rotationYaw * 0.017453292F - 11);
        float cos = MathHelper.cos(this.rotationYaw * 0.017453292F - 11);

        this.setMotion(0.5D * cos, 0.2D, 0.5D * sin);
    }

    public void tick() {

        super.tick();

        this.prevPosX = this.getPosition().getX();
        this.prevPosY = this.getPosition().getY();
        this.prevPosZ = this.getPosition().getZ();

        Vector3d motion = this.getMotion();

        if (!this.hasNoGravity()) {
            this.setMotion(this.getMotion().add(0.0D, -0.04D, 0.0D));
        }

        if (this.world.isRemote) {
            this.noClip = false;
        }

        if (!this.onGround || (this.ticksExisted + this.getEntityId()) % 4 == 0) {

            this.move(MoverType.SELF, this.getMotion());
            float f = 0.98F;

            if (this.onGround) {
                BlockPos pos = new BlockPos(this.getPosition().getX(), this.getPosition().getY() - 1.0D, this.getPosition().getZ());
                f = this.world.getBlockState(pos).getSlipperiness(this.world, pos, this) * 0.98F;
            }

            this.setMotion(this.getMotion().mul(f, 0.98D, f));
            if (this.onGround) {
                this.setMotion(this.getMotion().mul(1.0D, -0.5D, 1.0D));
            }
        }

        if (!this.world.isRemote) {
            double d0 = this.getMotion().subtract(motion).lengthSquared();
            if (d0 > 0.01D) {
                this.isAirBorne = true;
            }
        }
    }

    @Override
    public ITextComponent getCustomName() {
        return new StringTextComponent("6");
    }

    @Override
    public boolean getAlwaysRenderNameTagForRender() {
        return true;
    }

    @Override
    public boolean isCustomNameVisible() {
        return true;
    }

    @Override
    protected void registerData() {

    }

    @Override
    protected void readAdditional(CompoundNBT compound) {

    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {

    }

    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
