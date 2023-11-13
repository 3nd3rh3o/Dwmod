package h3o.ender.items;

import java.util.HashMap;

import h3o.ender.blocks.RegisterBlocks;
import h3o.ender.blocks.tardis.console_panel.ConsolePanel;
import h3o.ender.components.Circuit.LOCATION;
import h3o.ender.entities.Tardis;
import h3o.ender.structures.tardis.DimensionalStorageHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

public class Wrench extends Item {
    public static final HashMap<Block, LOCATION> BLOCKS = new HashMap<>();

    public Wrench(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (!context.getWorld().isClient()) {
            Block block = context.getWorld().getBlockState(context.getBlockPos()).getBlock();
            if (BLOCKS.containsKey(context.getWorld().getBlockState(context.getBlockPos()).getBlock())) {
                Tardis tardis = DimensionalStorageHelper.getTardis(context.getWorld().getServer(), context.getBlockPos());
                if (block.equals(RegisterBlocks.ROTOR_BASE)) {
                    tardis.removeRotor((ServerPlayerEntity) context.getPlayer(),
                                    BLOCKS.get(context.getWorld().getBlockState(context.getBlockPos()).getBlock()));
                    return ActionResult.SUCCESS;
                }
                if (block.equals(RegisterBlocks.COMMUNICATION_CONSOLE_PANEL)) {
                    BlockPos pos = context.getBlockPos();
                    BlockState oldState = context.getWorld().getBlockState(pos);
                    context.getWorld().setBlockState(pos, oldState.with(ConsolePanel.OPENNED, !oldState.get(ConsolePanel.OPENNED)), Block.NOTIFY_ALL);
                    return ActionResult.SUCCESS;
                }
            }
            return ActionResult.PASS;
        }
        return super.useOnBlock(context);
    }

    static {
        BLOCKS.put(RegisterBlocks.ROTOR_BASE, LOCATION.ROTOR_BASE);
        BLOCKS.put(RegisterBlocks.COMMUNICATION_CONSOLE_PANEL, LOCATION.COMMUNICATION_CONSOLE_PANEL);
    }

}
