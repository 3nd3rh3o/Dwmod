package h3o.ender.tardisOs.help.circuits;

import h3o.ender.blockEntity.tardis.TerminalBE;
import h3o.ender.tardisOs.Command;
import h3o.ender.tardisOs.FormattedText;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;

public class LLOEnergyDistributor implements Command {

    @Override
    public MutableText execute(String[] args, ServerPlayerEntity player, TerminalBE bEnt) {
        return FormattedText.empty().normal("The ").info("2LO energy distributor").normal(
                " is a key component of nearly all console panel. It serves as an energy input/regulator between the panel and the power stations. Without it, consider the panel and it's circuitry as disabled.")
                .assemble();
    }

    @Override
    public void parse(String[] ars) {

    }

}
