package h3o.ender.networking.packets;

import h3o.ender.blockEntity.tardis.TerminalBE;
import h3o.ender.screenHandler.TardisTerminalScreenHandler;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class TardisTerminalCharC2Spacket {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
            PacketByteBuf buf, PacketSender sender) {
        ((TerminalBE) (player.getWorld()
                .getWorldChunk(((TardisTerminalScreenHandler) player.currentScreenHandler).getPos())
                .getBlockEntity(((TardisTerminalScreenHandler) player.currentScreenHandler).getPos())))
                .addChar(buf.readString());
    }
}
