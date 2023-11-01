package h3o.ender.items;

import h3o.ender.blocks.GrowingTardis;
import h3o.ender.blocks.RegisterBlocks;
import h3o.ender.entities.RegisterEntities;
import h3o.ender.entities.Tardis;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ExitonicCircuit extends Item {

    public ExitonicCircuit(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (!context.getWorld().isClient() && context.getWorld().getBlockState(context.getBlockPos()).getBlock()
                .equals(RegisterBlocks.GROWING_TARDIS)) {
            BlockPos pos = context.getBlockPos();
            World world = context.getWorld();
            BlockState oldState = world.getBlockState(pos);
            if (world.getBlockState(pos.up()).getBlock().equals(oldState.getBlock())) {
                pos = pos.up();
                oldState = world.getBlockState(pos);
            }
            if ((oldState.get(GrowingTardis.AGE) + 1) % 5 == 0) {
                if (oldState.get(GrowingTardis.AGE) == 9) {
                    if (!oldState.get(GrowingTardis.UP)) {
                        world.setBlockState(pos.up(),
                                RegisterBlocks.GROWING_TARDIS.getDefaultState()
                                        .with(Properties.HORIZONTAL_FACING, oldState.get(Properties.HORIZONTAL_FACING))
                                        .with(GrowingTardis.UP, true),
                                Block.NOTIFY_ALL);
                    } else {
                        pos = pos.down();
                        Tardis tardis;
                        tardis = RegisterEntities.TARDIS.create(world);
                        tardis.setRotation(oldState.get(Properties.HORIZONTAL_FACING).asRotation());
                        tardis.refreshPositionAndAngles(pos.toCenterPos().getX(), pos.toCenterPos().getY() - 0.5, pos.toCenterPos().getZ(), oldState.get(Properties.HORIZONTAL_FACING).asRotation(), 0);
                        tardis.getWorld().spawnEntity(tardis);
                    }
                } else {
                    world.setBlockState(pos,
                            oldState.with(GrowingTardis.AGE, oldState.get(GrowingTardis.AGE) + 1),
                            Block.NOTIFY_ALL);
                }
                ItemStack stack = context.getPlayer().getStackInHand(context.getHand());
                stack.setCount(stack.getCount() - 1);
                context.getPlayer().setStackInHand(context.getHand(), stack);
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }

}
