package com.tm.playingcards.block;

import com.tm.playingcards.tileentity.TileEntityPokerTable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import com.tm.playingcards.block.base.BlockContainerBase;
import com.tm.playingcards.init.InitTileEntityTypes;
import com.tm.playingcards.util.Location;

import javax.annotation.Nullable;

public class BlockPokerTable extends BlockContainerBase {

    private static final BooleanProperty NORTH = BooleanProperty.create("north");
    private static final BooleanProperty EAST = BooleanProperty.create("east");
    private static final BooleanProperty SOUTH = BooleanProperty.create("south");
    private static final BooleanProperty WEST = BooleanProperty.create("west");

    private static final BooleanProperty NORTHWEST = BooleanProperty.create("northwest");
    private static final BooleanProperty NORTHEAST = BooleanProperty.create("northeast");
    private static final BooleanProperty SOUTHWEST = BooleanProperty.create("southwest");
    private static final BooleanProperty SOUTHEAST = BooleanProperty.create("southeast");

    private static final VoxelShape AABB = Block.makeCuboidShape(0, 0, 0, 16, 15, 16);

    public BlockPokerTable() {
        super(Block.Properties.create(Material.WOOD).sound(SoundType.WOOD).hardnessAndResistance(1).harvestLevel(0).notSolid().variableOpacity());
        setDefaultState(stateContainer.getBaseState().with(NORTH, false).with(EAST, false).with(SOUTH, false).with(WEST, false).with(NORTHWEST, false).with(NORTHEAST, false).with(SOUTHWEST, false).with(SOUTHEAST, false));
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {

        if (placer instanceof PlayerEntity) {

            PlayerEntity player = (PlayerEntity) placer;

            Location location = new Location(world, pos);
            TileEntity tileEntity = location.getTileEntity();

            if (tileEntity instanceof TileEntityPokerTable) {

                TileEntityPokerTable pokerTable = (TileEntityPokerTable) tileEntity;
                pokerTable.setOwner(player);
            }
        }
    }

    /**
     * Checks if the Block at the given pos can connect to the Block given by the Direction.
     */
    private boolean canConnectTo (IBlockReader world, BlockPos pos, int offX, int offZ) {
        BlockPos otherPos = pos.add(offX, 0, offZ);
        Block otherBlock = world.getBlockState(otherPos).getBlock();
        return otherBlock instanceof BlockPokerTable;
    }

    /*
        Methods for Block properties.
     */

    @Override
    public BlockState getStateForPlacement (BlockItemUseContext context) {
        return getState(context.getWorld(), context.getPos());
    }

    private BlockState getState (IWorld world, BlockPos pos) {

        boolean north = canConnectTo(world, pos, 0, -1);
        boolean east = canConnectTo(world, pos, 1, 0);
        boolean south = canConnectTo(world, pos, 0, 1);
        boolean west = canConnectTo(world, pos, -1, 0);

        boolean northwest = canConnectTo(world, pos, -1, -1);
        boolean northeast = canConnectTo(world, pos, 1, -1);
        boolean southwest = canConnectTo(world, pos, -1, 1);
        boolean southeast = canConnectTo(world, pos, 1, 1);

        return getDefaultState().with(NORTH, north).with(EAST, east).with(SOUTH, south).with(WEST, west).with(NORTHWEST, northwest).with(NORTHEAST, northeast).with(SOUTHWEST, southwest).with(SOUTHEAST, southeast);
    }

    @Override
    public BlockState updatePostPlacement (BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos pos, BlockPos facingPos) {
        return getState(world, pos);
    }

    @Override
    protected void fillStateContainer (StateContainer.Builder<Block, BlockState> builder) {
        builder.add(NORTH, SOUTH, EAST, WEST, NORTHWEST, NORTHEAST, SOUTHWEST, SOUTHEAST);
    }

    @Override
    public TileEntity createNewTileEntity(IBlockReader world) {
        return InitTileEntityTypes.POKER_TABLE.get().create();
    }

    /*
        Methods for Blocks that are not full and solid cubes.
     */

    @Override
    public VoxelShape getShape (BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return AABB;
    }

    @Override
    public VoxelShape getCollisionShape (BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return AABB;
    }

    @Override
    public boolean propagatesSkylightDown (BlockState state, IBlockReader world, BlockPos pos) {
        return true;
    }
}
