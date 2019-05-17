package team.hollow.abnormalib.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPlacementEnvironment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Waterloggable;
import net.minecraft.entity.EntityContext;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import team.hollow.abnormalib.utils.AbnormaProperties;

public class PostBaseBlock extends Block implements Waterloggable {
	public static final EnumProperty<Direction.Axis> AXIS;
	public static final BooleanProperty WATERLOGGED;
	protected static final VoxelShape Y_SHAPE;
	protected static final VoxelShape X_SHAPE;
	protected static final VoxelShape Z_SHAPE;
	protected static final VoxelShape Y_COLLISION;

	public PostBaseBlock(Settings block$Settings_1) {
		super(block$Settings_1);
		this.setDefaultState(this.getDefaultState().with(AXIS, Direction.Axis.Y).with(WATERLOGGED, false));
	}

	public VoxelShape getOutlineShape(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1, EntityContext entityContext) {
		Direction.Axis axis = blockState_1.get(AXIS);
		switch(axis) {
			case X:
				return X_SHAPE;
			case Z:
				return Z_SHAPE;
			default:
				return Y_SHAPE;
		}
	}

	public VoxelShape getCollisionShape(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1, EntityContext entityContext) {
		Direction.Axis axis = blockState_1.get(AXIS);
		if(axis == Direction.Axis.Y) return Y_COLLISION;
		else return super.getCollisionShape(blockState_1, blockView_1, blockPos_1, entityContext);
	}

	public BlockState getPlacementState(ItemPlacementContext itemPlacementContext_1) {
		BlockPos blockPos_1 = itemPlacementContext_1.getBlockPos();
		FluidState fluidState_1 = itemPlacementContext_1.getWorld().getFluidState(blockPos_1);
		return this.getDefaultState().with(AXIS, Direction.Axis.Y).with(WATERLOGGED, fluidState_1.getFluid() == Fluids.WATER);
	}

	public BlockState getStateForNeighborUpdate(BlockState blockState_1, Direction direction_1, BlockState blockState_2, IWorld iWorld_1, BlockPos blockPos_1, BlockPos blockPos_2) {
		if (blockState_1.get(WATERLOGGED)) {
			iWorld_1.getFluidTickScheduler().schedule(blockPos_1, Fluids.WATER, Fluids.WATER.getTickRate(iWorld_1));
		}

		return super.getStateForNeighborUpdate(blockState_1, direction_1, blockState_2, iWorld_1, blockPos_1, blockPos_2);
	}

	protected void appendProperties(StateFactory.Builder<Block, BlockState> stateFactory$Builder_1) {
		stateFactory$Builder_1.add(AXIS, WATERLOGGED);
	}

	public FluidState getFluidState(BlockState blockState_1) {
		return blockState_1.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(blockState_1);
	}

	public boolean canPlaceAtSide(BlockState blockState_1, BlockView blockView_1, BlockPos blockPos_1, BlockPlacementEnvironment blockPlacementEnvironment_1) {
		return false;
	}

	static {
		AXIS = AbnormaProperties.AXIS;
		WATERLOGGED = Properties.WATERLOGGED;
		Y_SHAPE = Block.createCuboidShape(6f, 0f, 6f, 10f, 16f, 10f);
		Y_COLLISION = Block.createCuboidShape(6f, 0f, 6f, 10f, 24f, 10f);
		X_SHAPE = Block.createCuboidShape(0f, 6f, 6f, 16f, 10f, 10f);
		Z_SHAPE = Block.createCuboidShape(6f, 6f, 0f, 10f, 10f, 16f);
	}
}