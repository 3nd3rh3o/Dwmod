package h3o.ender.tardisOs;

import h3o.ender.blockEntity.tardis.TerminalBE;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;

public class MainConf implements Command {

    @Override
    public MutableText execute(String[] args, ServerPlayerEntity player, TerminalBE bEnt) {
        bEnt.switchMenu(2);
        player.openHandledScreen(bEnt);
        return FormattedText.empty().normal("Openning maintenance ARS...").endLine().assemble();
    }

    @Override
    public void parse(String[] ars) {
    }
    
}
