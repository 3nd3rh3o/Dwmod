package h3o.ender.tardisOs.help.circuits;

import h3o.ender.tardisOs.Command;
import h3o.ender.tardisOs.FormattedText;
import net.minecraft.text.MutableText;

public class MainSpaceTimeElement implements Command {

    @Override
    public MutableText execute(String[] args) {
        return FormattedText.empty()
                .normal("The ")
                .info("Main space time element")
                .normal(" is the second heart of the TARDIS. It serve as a link between the system and the vortex.")
                .error(" DO NOT REMOVE ON FLIGHT!")
                .assemble();
    }

    @Override
    public void parse(String[] ars) {

    }

}
