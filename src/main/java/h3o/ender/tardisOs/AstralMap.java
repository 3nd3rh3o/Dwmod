package h3o.ender.tardisOs;

import h3o.ender.blockEntity.tardis.TerminalBE;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;

public class AstralMap implements Command {

    @Override
    public void parse(String[] ars) {
    }

    @Override
    public MutableText execute(String[] args, ServerPlayerEntity player, TerminalBE bEnt) {
        bEnt.switchMenu(1);
        player.openHandledScreen(bEnt);
        return FormattedText.empty().normal("Openning astral map...").endLine().assemble();
    }

}
