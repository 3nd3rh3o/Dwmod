package h3o.ender.items;

import h3o.ender.screenHandler.FrequencyDetectorScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class FrequencyDetector extends Item implements ExtendedScreenHandlerFactory,Inventory {

    public FrequencyDetector(Settings settings) {
        super(settings.maxCount(1));
    }
    
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient() && hand == Hand.MAIN_HAND) {
            user.openHandledScreen(this);
            return TypedActionResult.success(user.getStackInHand(hand));
        }
        return TypedActionResult.pass(user.getStackInHand(hand));
    }


    @Override
    public ScreenHandler createMenu(int synId, PlayerInventory playerInventory, PlayerEntity player) {
        return new FrequencyDetectorScreenHandler(synId, playerInventory, this);
    }



    @Override
    public void clear() {
        return;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public ItemStack getStack(int var1) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStack(int var1, int var2) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStack(int var1) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setStack(int var1, ItemStack var2) {
        return;
    }

    @Override
    public void markDirty() {
        
    }

    @Override
    public boolean canPlayerUse(PlayerEntity var1) {
        return true;
    }

    @Override
    public Text getDisplayName() {
        return this.getName();
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeString(player.getWorld().getRegistryKey().toString());
    }

}