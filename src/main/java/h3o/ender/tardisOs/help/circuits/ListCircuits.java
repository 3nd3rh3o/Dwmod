package h3o.ender.tardisOs.help.circuits;

import h3o.ender.blockEntity.tardis.TerminalBE;
import h3o.ender.tardisOs.Command;
import h3o.ender.tardisOs.FormattedText;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;

public class ListCircuits implements Command {

    @Override
    public MutableText execute(String[] args, ServerPlayerEntity player, TerminalBE bEnt) {
        return FormattedText.empty().normal("Circuit list:").endLine()
                .normal("-MSTE").endLine()
                .normal("-2LO_E_D").endLine()
                .assemble();
    }

    @Override
    public void parse(String[] ars) {

    }
    
}
