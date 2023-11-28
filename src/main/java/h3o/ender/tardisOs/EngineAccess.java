package h3o.ender.tardisOs;

import h3o.ender.blockEntity.tardis.TerminalBE;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;

public class EngineAccess implements Command {

    @Override
    public MutableText execute(String[] args, ServerPlayerEntity player, TerminalBE bEnt) {
        bEnt.switchEngineAccess();
        return FormattedText.empty().normal("toggling engine access...").endLine().assemble();
    }

    @Override
    public void parse(String[] ars) {

    }

}
