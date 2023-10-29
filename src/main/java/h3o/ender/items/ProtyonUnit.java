package h3o.ender.items;

import h3o.ender.blocks.RegisterBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ProtyonUnit extends Item {

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (!context.getWorld().isClient() && context.getWorld().getBlockState(context.getBlockPos()).getBlock()
                .equals(RegisterBlocks.GRAY_PRINT)) {
            BlockPos pos = context.getBlockPos();
            World world = context.getWorld();
            //TODO replace air by a growing tardis
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
            context.getPlayer().setStackInHand(context.getHand(), ItemStack.EMPTY);
            return ActionResult.CONSUME;
        }
        return ActionResult.PASS;
    }

    public ProtyonUnit(Settings settings) {
        super(settings);
    }

}
