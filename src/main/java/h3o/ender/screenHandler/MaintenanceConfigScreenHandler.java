package h3o.ender.screenHandler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.math.BlockPos;

public class MaintenanceConfigScreenHandler extends ScreenHandler {
    private Inventory inventory;
    private BlockPos pos;


    public MaintenanceConfigScreenHandler(int syncId, PlayerInventory pInventory, Inventory inventory, BlockPos pos) {
        super(RegisterScreenHandler.MAINTENANCE_CONF_SCREEN_HANDLER, syncId);
        this.inventory = inventory;
        this.pos = pos;
    }

    protected MaintenanceConfigScreenHandler(int syncId, PlayerInventory pInventory, PacketByteBuf buf) {
        this(syncId, pInventory, new SimpleInventory(0), buf.readBlockPos());
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return ItemStack.EMPTY;
    }

    public BlockPos getPos() {
        return pos;
    }

}
