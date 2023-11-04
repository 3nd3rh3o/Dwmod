package h3o.ender.tardisOs.help;

import java.util.Arrays;
import java.util.HashMap;

import h3o.ender.tardisOs.Command;
import h3o.ender.tardisOs.FormattedText;
import net.minecraft.text.MutableText;

public class Help implements Command {
    private static final HashMap<String, Command> commandMap = new HashMap<>();

    @Override
    public MutableText execute(String[] parts) {
        String commandName = parts.length == 0 ? "" : parts[0];
        String[] args = null;
        if (!(parts.length == 0)) {
            args = Arrays.copyOfRange(parts, 1, parts.length);
        }

        Command command = commandMap.get(commandName.toLowerCase());
        if (command == null) {
            return FormattedText.empty().error("CATEGORY NAME ").info(commandName).error(" NOT KNOWN! USE HELP").endLine()
                    .assemble();
        }
        return command.execute(args);
    }

    @Override
    public void parse(String[] ars) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'parse'");
    }

    static {
        commandMap.put("", new HelpEmpty());
    }

}
