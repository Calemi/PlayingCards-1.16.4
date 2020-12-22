package playingcards.tileentity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import playingcards.init.InitTileEntityTypes;
import playingcards.tileentity.base.TileEntityBase;

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
        this.ownerName = player.getDisplayName().getFormattedText();
    }

    public UUID getOwnerID() {
        return this.ownerID;
    }

    @Override
    public void read(CompoundNBT nbt) {

        ownerID = nbt.getUniqueId("OwnerID");
        ownerName = nbt.getString("OwnerName");

        super.read(nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {

        nbt.putUniqueId("OwnerID", ownerID);
        nbt.putString("OwnerName", ownerName);

        return super.write(nbt);
    }
}