package com.tm.playingcards.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeBlockState;

public class Location {

    public final World world;
    public final int x, y, z;
    private final BlockPos blockPos;

    public Location (World world, BlockPos pos) {
        this(world, pos.getX(), pos.getY(), pos.getZ());
    }

    public Location (World world, int x, int y, int z) {

        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;

        blockPos = new BlockPos(x, y, z);
    }

    public Location (TileEntity tileEntity) {
        this(tileEntity.getWorld(), tileEntity.getPos().getX(), tileEntity.getPos().getY(), tileEntity.getPos().getZ());
    }

    public Location (Entity entity) {
        this(entity.world, entity.getPosition().getX(), entity.getPosition().getY(), entity.getPosition().getZ());
    }

    public BlockPos getBlockPos () {
        return blockPos;
    }

    public IForgeBlockState getForgeBlockState () {

        if (getBlockPos() == null) {
            return null;
        }

        return world.getBlockState(getBlockPos());
    }

    public BlockState getBlockState () {

        if (getForgeBlockState() == null) {
            return null;
        }

        return getForgeBlockState().getBlockState();
    }

    public Block getBlock () {

        if (getBlockState() == null) {
            return null;
        }

        return getBlockState().getBlock();
    }

    public TileEntity getTileEntity () {
        return world.getTileEntity(getBlockPos());
    }

    @Override
    public boolean equals (Object obj) {

        if (obj instanceof Location) {
            Location newLoc = (Location) obj;
            return world == newLoc.world && x == newLoc.x && y == newLoc.y && z == newLoc.z;
        }

        return super.equals(obj);
    }

    @Override
    public String toString () {
        return "[" + x + ", " + y + ", " + z + "]";
    }
}
