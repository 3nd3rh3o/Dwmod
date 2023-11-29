package h3o.ender.networking.packets;

import h3o.ender.blockEntity.tardis.TerminalBE;
import h3o.ender.screenHandler.MaintenanceConfigScreenHandler;
import h3o.ender.structures.tardis.DimensionalStorageHelper;
import h3o.ender.structures.tardis.Room.Name;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;

public class TardisMainConfC2Spacket {
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler,
            PacketByteBuf buf, PacketSender sender) {
        BlockPos pos = ((TerminalBE) (player.getWorld()
                .getWorldChunk(((MaintenanceConfigScreenHandler) player.currentScreenHandler).getPos())
                .getBlockEntity(((MaintenanceConfigScreenHandler) player.currentScreenHandler).getPos())))
                .getPos();
        NbtCompound nbt = buf.readNbt();
        switch (nbt.getInt("Type")) {
            case 0 -> {DimensionalStorageHelper.getTardis(server, pos).addRoomE(Name.valueOf(nbt.getString("Name")), BlockRotation.values()[nbt.getInt("Rotation")], nbt.getInt("VID"));}
            case 1 -> {DimensionalStorageHelper.getTardis(server, pos).removeRoomE(nbt.getInt("VID"));}
        }
    }
}
