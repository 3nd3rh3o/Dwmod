package h3o.ender.tardisOs.help;

import h3o.ender.blockEntity.tardis.TerminalBE;
import h3o.ender.tardisOs.Command;
import h3o.ender.tardisOs.FormattedText;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;

public class HelpEmpty implements Command {

    @Override
    public MutableText execute(String[] args, ServerPlayerEntity player, TerminalBE bEnt) {
        return FormattedText.empty().normal("LIST OF CATEGORY :").endLine()
                .normal("-CIRCUITS").endLine()
                .normal("-CONSOLE_PANEL").endLine()
                .assemble();
    }

    @Override
    public void parse(String[] ars) {
        throw new UnsupportedOperationException("Unimplemented method 'parse'");
    }
    
}
