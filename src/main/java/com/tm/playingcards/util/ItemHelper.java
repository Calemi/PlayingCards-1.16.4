package com.tm.playingcards.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

public class ItemHelper {

    public static CompoundNBT getNBT (ItemStack is) {

        if (is.getTag() == null) {
            is.setTag(new CompoundNBT());
        }

        return is.getTag();
    }

    public static void spawnStackAtEntity(World world, Entity entity, ItemStack stack) {
        spawnStack(world, entity.getPositionVec().x, entity.getPositionVec().y, entity.getPositionVec().z, stack);
    }

    private static void spawnStack(World world, double x, double y, double z, ItemStack stack) {
        ItemEntity item = new ItemEntity(world, x, y, z, stack);
        item.setNoPickupDelay();
        item.setMotion(0, 0, 0);
        world.addEntity(item);
    }
}
