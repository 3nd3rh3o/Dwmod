package h3o.ender.tardisOs;

import java.util.HashMap;

import h3o.ender.tardisOs.help.Help;
import net.minecraft.text.MutableText;

import java.util.Arrays;

public class TardisOs {
    private static final HashMap<String, Command> commandMap = new HashMap<>();

    public static MutableText execute(String input) {
        String[] parts = input.split(" ");
        String commandName = parts[0];
        String[] args = Arrays.copyOfRange(parts, 1, parts.length);
        
        Command command = commandMap.get(commandName.toLowerCase());
        if (command == null) {
            return FormattedText.empty().error("COMMAND : ").info(commandName).error(" NOT FOUND! USE HELP").assemble();
        }
        return command.execute(args);
    }

    static {
        commandMap.put("help", new Help());
    }
}
