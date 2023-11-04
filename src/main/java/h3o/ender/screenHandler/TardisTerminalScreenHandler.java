package h3o.ender.screenHandler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.math.BlockPos;

public class TardisTerminalScreenHandler extends ScreenHandler {
    private Inventory inventory;
    private BlockPos pos;

    protected TardisTerminalScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, new SimpleInventory(0), buf.readBlockPos());
    }

    public TardisTerminalScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, BlockPos pos) {
        super(RegisterScreenHandler.TARDIS_TERMINAL_SCREEN_HANDLER, syncId);
        this.inventory = inventory;
        this.pos = pos;
    }

    @Override
    public ItemStack quickMove(PlayerEntity var1, int var2) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUse(PlayerEntity var1) {
        return this.inventory.canPlayerUse(var1);
    }

    public BlockPos getPos() {
        return this.pos;
    }

}
