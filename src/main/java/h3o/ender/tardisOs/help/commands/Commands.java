package h3o.ender.tardisOs.help.commands;

import h3o.ender.blockEntity.tardis.TerminalBE;
import h3o.ender.tardisOs.Command;
import h3o.ender.tardisOs.FormattedText;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;

public class Commands implements Command {

    @Override
    public MutableText execute(String[] args, ServerPlayerEntity player, TerminalBE bEnt) {
        return FormattedText.empty().normal("LIST OF COMMANDS :").endLine()
                .normal("-HELP").endLine()
                .normal("-ASTRALMAP").endLine()
                .normal("-MAINCONF").endLine()
                .assemble();
    }

    @Override
    public void parse(String[] ars) {
    }
    
}
