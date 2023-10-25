package h3o.ender.networking.packets;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;

public class FrequencyDetectorC2Spacket {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        //on ServerSide
        int settings = buf.readInt();
        NbtCompound nbt = player.getStackInHand(Hand.MAIN_HAND).getNbt();
        nbt.putInt("settings", settings);
        player.getStackInHand(Hand.MAIN_HAND).setNbt(nbt);
        player.closeHandledScreen();
    }
}
