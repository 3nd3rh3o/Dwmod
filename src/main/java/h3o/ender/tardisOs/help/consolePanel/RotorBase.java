package h3o.ender.tardisOs.help.consolePanel;

import h3o.ender.tardisOs.Command;
import h3o.ender.tardisOs.FormattedText;
import net.minecraft.text.MutableText;

public class RotorBase implements Command {
    
    @Override
    public MutableText execute(String[] args) {
        return FormattedText.empty()
                .normal("The ").info("rotor base").normal(" is located at the center of the console, under the rotor. It contains the following circuits:").endLine()
                .normal("-2LO energy distributor").endLine()
                .normal("main space time element(mste)").endLine()
                .assemble();
    }

    @Override
    public void parse(String[] ars) {

    }

}
