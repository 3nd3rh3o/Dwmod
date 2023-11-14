package h3o.ender.tardisOs.help.consolePanel;

import h3o.ender.blockEntity.tardis.TerminalBE;
import h3o.ender.tardisOs.Command;
import h3o.ender.tardisOs.FormattedText;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;

public class ConsolePanelEmpty implements Command {

    @Override
    public MutableText execute(String[] args, ServerPlayerEntity player, TerminalBE bEnt) {
        return FormattedText.empty()
                .normal("List of all console panel:").endLine()
                .normal("-rotor_base").endLine()
                .assemble();
    }

    @Override
    public void parse(String[] ars) {

    }


}
