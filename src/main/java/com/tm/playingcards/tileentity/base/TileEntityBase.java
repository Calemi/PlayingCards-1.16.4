package com.tm.playingcards.tileentity.base;

import com.tm.playingcards.util.Location;
import com.tm.playingcards.util.UnitChatMessage;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

import javax.annotation.Nullable;

public abstract class TileEntityBase extends TileEntity implements ITickableTileEntity {

    public TileEntityBase (final TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    protected UnitChatMessage getUnitName (PlayerEntity player) {
        return new UnitChatMessage(getLocation().getBlock().getTranslatedName().getString(), player);
    }

    public Location getLocation () {
        return new Location(world, pos);
    }

    @Override
    public void tick() {

    }

    @Override
    public void onDataPacket (NetworkManager net, SUpdateTileEntityPacket pkt) {
        read(getBlockState(), pkt.getNbtCompound());
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        read(state, tag);
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket () {
        CompoundNBT nbtTagCompound = new CompoundNBT();
        write(nbtTagCompound);
        int tileEntityType = 64;
        return new SUpdateTileEntityPacket(getPos(), tileEntityType, nbtTagCompound);
    }

    @Override
    public CompoundNBT getUpdateTag () {
        CompoundNBT nbt = new CompoundNBT();
        write(nbt);
        return nbt;
    }

    @Override
    public CompoundNBT getTileData () {
        CompoundNBT nbt = new CompoundNBT();
        write(nbt);
        return nbt;
    }
}
