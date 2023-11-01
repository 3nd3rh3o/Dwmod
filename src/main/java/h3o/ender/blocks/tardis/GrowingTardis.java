package h3o.ender.blocks.tardis;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class GrowingTardis extends HorizontalFacingBlock {
    public static final IntProperty AGE = IntProperty.of("age", 0, 9);
    public static final BooleanProperty UP = BooleanProperty.of("up");

    public GrowingTardis(Settings settings) {
        super(settings.nonOpaque().ticksRandomly());
        setDefaultState(
                getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH).with(AGE, 0).with(UP, false));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(Properties.HORIZONTAL_FACING,
                ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected void appendProperties(Builder<Block, BlockState> builder) {
        builder.add(Properties.HORIZONTAL_FACING);
        builder.add(AGE);
        builder.add(UP);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!world.isClient()) {
            if (!state.get(UP) && !((state.get(AGE) + 1 ) % 5 == 0) && random.nextInt(10) <= 2) {
                world.setBlockState(pos, state.with(AGE, state.get(AGE) + 1), Block.NOTIFY_ALL);
                return;
            }
            if (state.get(UP) && !((state.get(AGE) + 1)% 5 == 0) && random.nextInt(10) <= 2) {
                world.setBlockState(pos, state.with(AGE, state.get(AGE) + 1), Block.NOTIFY_ALL);
            }
        }
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (!((state.get(AGE) + 1) % 5 == 0)) {
            double x = (double) pos.getX() + 1 - (double) (random.nextFloat());
            double y = (double) pos.getY() + 1 - (double) (random.nextFloat());
            double z = (double) pos.getZ() + 1 - (double) (random.nextFloat());
            if (random.nextInt(5) >= 2) {
                world.addParticle(ParticleTypes.END_ROD, x,
                        y, z,
                        random.nextGaussian() * 0.005, random.nextGaussian() * 0.005, random.nextGaussian() * 0.005);
            }
        }
    }

}
