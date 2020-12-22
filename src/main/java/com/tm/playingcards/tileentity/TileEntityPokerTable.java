package com.tm.playingcards.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import com.tm.playingcards.init.InitTileEntityTypes;
import com.tm.playingcards.tileentity.base.TileEntityBase;

import java.util.UUID;

public class TileEntityPokerTable extends TileEntityBase {

    private UUID ownerID;
    private String ownerName;

    public TileEntityPokerTable (TileEntityType type) {
        super(type);
    }

    public TileEntityPokerTable () {
        super(InitTileEntityTypes.POKER_TABLE.get());
    }

    public void setOwner(PlayerEntity player) {
        this.ownerID = player.getUniqueID();
        this.ownerName = player.getDisplayName().getString();
    }

    public UUID getOwnerID() {
        return this.ownerID;
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {

        ownerID = nbt.getUniqueId("OwnerID");
        ownerName = nbt.getString("OwnerName");

        super.read(state, nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {

        nbt.putUniqueId("OwnerID", ownerID);
        nbt.putString("OwnerName", ownerName);

        return super.write(nbt);
    }
}