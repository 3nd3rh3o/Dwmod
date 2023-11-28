package h3o.ender.blocks.tardis.maintenanceWall;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class MaintenanceWallLower extends HorizontalFacingBlock {

    public MaintenanceWallLower(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    protected void appendProperties(Builder<Block, BlockState> builder) {
        builder.add(Properties.HORIZONTAL_FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(Properties.HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        
        return switch (state.get(Properties.HORIZONTAL_FACING)) {
            case NORTH -> Block.createCuboidShape(0, 0, 8.2, 16, 16, 16);
            case EAST -> Block.createCuboidShape(0, 0, 0, 7.8, 16, 16);
            case SOUTH -> Block.createCuboidShape(0, 0, 0, 16, 16, 7.8);
            case WEST -> Block.createCuboidShape(8.2, 0, 0, 16, 16, 16);
            default -> null;
        };
    }
}
