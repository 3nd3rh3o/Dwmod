package h3o.ender.tardisOs;

import h3o.ender.blockEntity.tardis.TerminalBE;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;

public interface Command {
    MutableText execute(String[] args, ServerPlayerEntity player, TerminalBE bEnt);
    void parse(String[] ars);
}
