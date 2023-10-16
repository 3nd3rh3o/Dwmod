package h3o.ender.blocks.tardis;

import java.util.List;
import java.util.stream.Stream;

import h3o.ender.entities.Tardis;
import net.minecraft.block.AbstractGlassBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class TardisDefaultHitbox extends AbstractGlassBlock {
    public static final BooleanProperty OPENNED = BooleanProperty.of("openned");
    public static final BooleanProperty UPPER = BooleanProperty.of("upper");

    public TardisDefaultHitbox(Settings settings) {
        super(settings.nonOpaque().blockVision(Blocks::never).solidBlock(Blocks::never));
        setDefaultState(getDefaultState().with(OPENNED, false).with(UPPER, false));
    }

    @Override
    protected void appendProperties(Builder<Block, BlockState> builder) {
        builder.add(OPENNED).add(UPPER);
    }

    //FIXME VoxelShapeNotReplaced
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
            BlockHitResult hit) {
        if (!world.isClient) {
            if (!state.get(UPPER)) {
                List<Tardis> tardisList = world.getEntitiesByClass(Tardis.class,
                        new Box(pos.getX() - 0.5, pos.getY() - 0.5,
                                pos.getZ() - 0.5, pos.getX() + 1.5, pos.getY() + 1.5, pos.getZ() + 1.5),
                        entities -> true);
                if (tardisList.size() == 0) {
                    world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
                    return ActionResult.PASS;
                }
                return tardisList.get(0).interactAt(player, hit.getPos().subtract(pos.toCenterPos()), hand);
            } else {
                List<Tardis> tardisList = world.getEntitiesByClass(Tardis.class,
                        new Box(pos.getX() - 0.5, pos.getY() - 1.5,
                                pos.getZ() - 0.5, pos.getX() + 1.5, pos.getY() + 0.5, pos.getZ() + 1.5),
                        entities -> true);
                if (tardisList.size() == 0) {
                    world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
                    return ActionResult.PASS;
                }
                return tardisList.get(0).interactAt(player, hit.getPos().subtract(pos.toCenterPos()), hand);
            }
        }
        return ActionResult.PASS;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (state.get(UPPER)) {
            if (state.get(OPENNED)) {
                return Stream.of(
                    Block.createCuboidShape(0, -16, 0, 16, -15, 16),
                    Block.createCuboidShape(0, 15, 0, 16, 16, 16),
                    Block.createCuboidShape(0, -15, 0, 16, 15, 1),
                    Block.createCuboidShape(0, -15, 1, 1, 15, 16),
                    Block.createCuboidShape(15, -15, 1, 16, 15, 16)
                    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
            } else {
                return Block.createCuboidShape(0, -16, 0, 16, 16, 16);
            }
        } else {
            if (state.get(OPENNED)) {
                return Stream.of(
                    Block.createCuboidShape(0, 0, 0, 16, 1, 16),
                    Block.createCuboidShape(0, 31, 0, 16, 32, 16),
                    Block.createCuboidShape(0, 1, 0, 16, 31, 1),
                    Block.createCuboidShape(0, 1, 1, 1, 31, 16),
                    Block.createCuboidShape(15, 1, 1, 16, 31, 16)
                    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
            }
        }
        return Block.createCuboidShape(0, 0, 0, 16, 32, 16);

    }

}
