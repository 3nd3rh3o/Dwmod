package h3o.ender.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class GrowingTardis extends HorizontalFacingBlock {
    public static final IntProperty AGE = IntProperty.of("age", 0, 100);
    protected GrowingTardis(Settings settings) {
        super(settings.nonOpaque().ticksRandomly());
        setDefaultState(getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH).with(AGE, 0));
    }
    
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(Properties.HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected void appendProperties(Builder<Block, BlockState> builder) {
        builder.add(Properties.HORIZONTAL_FACING);
        builder.add(AGE);
    }

    
    //FIXME dont tick?
    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if(world.isClient() && !(state.get(AGE) != 0 && state.get(AGE) % 10 == 0) && random.nextBetween(0, 10) == 3) {
            world.setBlockState(pos, state.with(AGE, state.get(AGE)+1), Block.NOTIFY_ALL);
        }
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        
        return switch (state.get(Properties.HORIZONTAL_FACING)) {
            case NORTH -> Block.createCuboidShape(2, 0, 2, 14, 16, 11);
            case EAST -> Block.createCuboidShape(5, 0, 2, 14, 16, 14);
            case SOUTH -> Block.createCuboidShape(2, 0, 5, 14, 16, 14);
            case WEST -> Block.createCuboidShape(2, 0, 2, 11, 16, 14);
            default -> null;
        };
    }
}
