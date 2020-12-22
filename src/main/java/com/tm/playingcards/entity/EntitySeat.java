package com.tm.playingcards.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import com.tm.playingcards.init.InitEntityTypes;

import java.util.List;

public class EntitySeat extends Entity {

    private BlockPos sourceBlock;

    public EntitySeat(EntityType<? extends EntitySeat> type, World world) {
        super(type, world);
    }

    public EntitySeat(World world, BlockPos sourceBlock) {
        this(InitEntityTypes.SEAT.get(), world);
        this.sourceBlock = sourceBlock;
        setPosition(sourceBlock.getX() + 0.5F, sourceBlock.getY() + 0.3F, sourceBlock.getZ() + 0.5F);
    }

    private BlockPos getSourceBlock() {
        return sourceBlock;
    }

    public static void createSeat(World world, BlockPos pos, PlayerEntity player) {

        if (!world.isRemote) {

            List<EntitySeat> seats = world.getEntitiesWithinAABB(EntitySeat.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1));

            if (seats.isEmpty()) {

                EntitySeat seat = new EntitySeat(world, pos);
                world.addEntity(seat);
                player.startRiding(seat);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (sourceBlock == null) {
            sourceBlock = this.getPosition();
        }

        if (!world.isRemote) {

            if (getPassengers().isEmpty() || this.world.isAirBlock(sourceBlock)) {
                remove();
            }
        }
    }

    @Override
    public double getMountedYOffset() {
        return 0.0D;
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {}

    @Override
    protected void writeAdditional(CompoundNBT compound) {}

    @Override
    protected void registerData() {}

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
