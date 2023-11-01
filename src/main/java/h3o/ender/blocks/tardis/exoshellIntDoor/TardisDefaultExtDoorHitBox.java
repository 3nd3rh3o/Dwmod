package h3o.ender.blocks.tardis.exoshellIntDoor;

import java.util.List;
import java.util.stream.Stream;

import h3o.ender.entities.Tardis;
import h3o.ender.structures.tardis.DimensionalStorageHelper;
import net.minecraft.block.AbstractGlassBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class TardisDefaultExtDoorHitBox extends AbstractGlassBlock {
    public static final BooleanProperty OPENNED = BooleanProperty.of("openned");
    public static final BooleanProperty UPPER = BooleanProperty.of("upper");

    public TardisDefaultExtDoorHitBox(Settings settings) {
        super(settings.nonOpaque().blockVision(Blocks::never).solidBlock(Blocks::never));
        setDefaultState(getDefaultState().with(OPENNED, false).with(UPPER, false));
    }

    @Override
    protected void appendProperties(Builder<Block, BlockState> builder) {
        builder.add(OPENNED).add(UPPER);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (state.get(UPPER)) {
            if (state.get(OPENNED)) {
                return Stream.of(
                    Block.createCuboidShape(1, -16, 0, 15, -15, 15),
                    Block.createCuboidShape(15, -16, 0, 16, 16, 15),
                    Block.createCuboidShape(0, -16, 0, 1, 16, 15),
                    Block.createCuboidShape(1, 15, 0, 15, 16, 15),
                    Block.createCuboidShape(0, -16, 15, 16, 16, 16)
                    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
            } else {
                return Block.createCuboidShape(0, -16, 0, 16, 16, 16);
            }
        } else {
            if (state.get(OPENNED)) {
                return Stream.of(
                    Block.createCuboidShape(1, 0, 0, 15, 1, 15),
                    Block.createCuboidShape(15, 0, 0, 16, 32, 15),
                    Block.createCuboidShape(0, 0, 0, 1, 32, 15),
                    Block.createCuboidShape(1, 31, 0, 15, 32, 15),
                    Block.createCuboidShape(0, 0, 15, 16, 32, 16)
                    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
            } else {
                return Block.createCuboidShape(0, 0, 0, 16, 32, 16);
            }
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
            BlockHitResult hit) {
        if (!world.isClient) {

            Tardis trds = getTardis(world, pos);
            if (trds == null) {
                world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
                return ActionResult.PASS;
            } else {
                return trds.interactAt(player,
                        hit.getPos().subtract(pos.toCenterPos()).rotateY((float)Math.toRadians(180)), hand);
            }
        }
        return ActionResult.PASS;
    }

    private Tardis getTardis(World worlds, BlockPos pos) {
        for (ServerWorld world : worlds.getServer().getWorlds()) {
            List<Tardis> trds = world.getEntitiesByClass(Tardis.class,
                    Box.of(new Vec3d(0, 0, 0), World.HORIZONTAL_LIMIT * 2, World.MAX_Y - World.MIN_Y,
                            World.HORIZONTAL_LIMIT * 2),
                    (entities) -> ((Tardis) entities).getIndex() == DimensionalStorageHelper
                            .getIndex(pos));
            if (trds.size() != 0) {
                return trds.get(0);
            }
        }
        return null;
    }
}
