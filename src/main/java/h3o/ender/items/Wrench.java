package h3o.ender.items;

import java.util.HashMap;

import h3o.ender.blocks.RegisterBlocks;
import h3o.ender.components.Circuit.LOCATION;
import h3o.ender.structures.tardis.DimensionalStorageHelper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;

public class Wrench extends Item {
    public static final HashMap<Block, LOCATION> BLOCKS = new HashMap<>();

    public Wrench(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (!context.getWorld().isClient()) {
            if (BLOCKS.containsKey(context.getWorld().getBlockState(context.getBlockPos()).getBlock())) {
                DimensionalStorageHelper.getTardis(context.getWorld().getServer(), context.getBlockPos()).popCircuit((ServerPlayerEntity)context.getPlayer(), BLOCKS.get(context.getWorld().getBlockState(context.getBlockPos()).getBlock()));
                return ActionResult.SUCCESS;
            }
            return ActionResult.PASS;
        }
        return super.useOnBlock(context);
    }

    static {
        BLOCKS.put(RegisterBlocks.ROTOR_BASE, LOCATION.ROTOR_BASE);
    }

}
