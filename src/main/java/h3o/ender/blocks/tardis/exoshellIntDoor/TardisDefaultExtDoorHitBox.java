package h3o.ender.blocks.tardis.exoshellIntDoor;

import java.util.List;

import h3o.ender.entities.Tardis;
import h3o.ender.structures.tardis.DimensionalStorageHelper;
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
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class TardisDefaultExtDoorHitBox extends AbstractGlassBlock {
    public static final BooleanProperty OPENNED = BooleanProperty.of("openned");
    public static final BooleanProperty UPPER = BooleanProperty.of("upper");

    protected TardisDefaultExtDoorHitBox(Settings settings) {
        super(settings.nonOpaque().blockVision(Blocks::never).solidBlock(Blocks::never));
        setDefaultState(getDefaultState().with(OPENNED, false).with(UPPER, false));
    }

    @Override
    protected void appendProperties(Builder<Block, BlockState> builder) {
        builder.add(OPENNED).add(UPPER);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        // TODO Auto-generated method stub
        return super.getOutlineShape(state, world, pos, context);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
            BlockHitResult hit) {
        if (!world.isClient) {

            List<Tardis> tardisList = world.getEntitiesByClass(Tardis.class,
                    Box.of(new Vec3d(0, 0, 0), World.HORIZONTAL_LIMIT * 2, World.MAX_Y - World.MIN_Y,
                            World.HORIZONTAL_LIMIT * 2),
                    (entities) -> ((Tardis) entities).getIndex() == this.getIndex(pos));
            if (tardisList.size() == 0) {
                world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
                return ActionResult.PASS;
            }
            // TODO finish that, trrigger anim on exoshell and on ext door(get ext door)
            tardisList.get(0).interactAt(player,
                    hit.getPos().subtract(pos.toCenterPos()).rotateY((float) Math.toRadians(180)), hand);
            // inside blocks, acquire door entity
            if (!state.get(UPPER)) {

            } else {

            }
            return ActionResult.SUCCESS;

        }
        return ActionResult.PASS;
    }

    private int getIndex(BlockPos pos) {
        return DimensionalStorageHelper.getIndex(pos);
    }

}
