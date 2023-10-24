package h3o.ender.screenHandler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;

public class FrequencyDetectorScreenHandler extends ScreenHandler {
    private String dimKey;
    private Inventory inventory;

    protected FrequencyDetectorScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, new SimpleInventory(0));
        this.dimKey = buf.readString();
    }

    public FrequencyDetectorScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(RegisterScreenHandler.FREQUENCY_DETECTOR_SCREEN_HANDLER, syncId);
        this.inventory = inventory;
        this.dimKey = "";
    }

    @Override
    public ItemStack quickMove(PlayerEntity var1, int var2) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }    
}
