package com.tm.playingcards.block;

import com.tm.playingcards.entity.EntitySeat;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import com.tm.playingcards.block.base.BlockBase;

public class BlockBarStool extends BlockBase {

    public static final IntegerProperty FACING = IntegerProperty.create("facing", 0, 7);
    private static final VoxelShape AABB = Block.makeCuboidShape(3, 0, 3, 13, 16, 13);

    public BlockBarStool() {
        super(Block.Properties.create(Material.WOOD).sound(SoundType.WOOD).hardnessAndResistance(1).harvestLevel(0).notSolid().variableOpacity());
        setDefaultState(stateContainer.getBaseState().with(FACING, 0));
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        EntitySeat.createSeat(world, pos, player);
        return ActionResultType.SUCCESS;
    }

    /*
        Methods for Block properties.
     */

    @Override
    public BlockState getStateForPlacement (BlockItemUseContext context) {

        if (context.getPlayer() != null) {
            return stateContainer.getBaseState().with(FACING, MathHelper.floor((double) ((context.getPlayer().rotationYaw + 180) * 8.0F / 360.0F) + 0.5D) & 7);
        }

        return super.getStateForPlacement(context);
    }

    @Override
    protected void fillStateContainer (StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    /*
        Methods for Blocks that are not full and solid cubes.
     */

    @Override
    public VoxelShape getShape (BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        return AABB;
    }

    @Override
    public VoxelShape getCollisionShape (BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        return AABB;
    }

    @Override
    public boolean propagatesSkylightDown (BlockState state, IBlockReader world, BlockPos pos) {
        return true;
    }
}
