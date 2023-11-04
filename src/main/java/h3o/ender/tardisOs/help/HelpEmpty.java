package h3o.ender.tardisOs.help;

import h3o.ender.tardisOs.Command;
import h3o.ender.tardisOs.FormattedText;
import net.minecraft.text.MutableText;

public class HelpEmpty implements Command {

    @Override
    public MutableText execute(String[] args) {
        return FormattedText.empty().normal("LIST OF CATEGORY :").endLine().assemble();
    }

    @Override
    public void parse(String[] ars) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'parse'");
    }
    
}
