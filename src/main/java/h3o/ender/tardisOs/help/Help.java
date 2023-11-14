package h3o.ender.tardisOs.help;

import java.util.Arrays;
import java.util.HashMap;

import h3o.ender.blockEntity.tardis.TerminalBE;
import h3o.ender.tardisOs.Command;
import h3o.ender.tardisOs.FormattedText;
import h3o.ender.tardisOs.help.circuits.Circuits;
import h3o.ender.tardisOs.help.consolePanel.ConsolePanel;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;

public class Help implements Command {
    private static final HashMap<String, Command> commandMap = new HashMap<>();

    @Override
    public MutableText execute(String[] parts, ServerPlayerEntity player, TerminalBE bEnt) {
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
        return command.execute(args, player, bEnt);
    }

    @Override
    public void parse(String[] ars) {
        throw new UnsupportedOperationException("Unimplemented method 'parse'");
    }

    static {
        commandMap.put("", new HelpEmpty());
        commandMap.put("circuits", new Circuits());
        commandMap.put("console_panel", new ConsolePanel());
    }

}
