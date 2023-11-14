package h3o.ender.tardisOs.help.consolePanel;

import java.util.Arrays;
import java.util.HashMap;

import h3o.ender.blockEntity.tardis.TerminalBE;
import h3o.ender.tardisOs.Command;
import h3o.ender.tardisOs.FormattedText;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;

public class ConsolePanel implements Command {
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
            return FormattedText.empty().error("No such console panel : ").info(commandName).error(". use HELP CONSOLE_PANEL").assemble();
        }
        return command.execute(args, player, bEnt);
    }

    @Override
    public void parse(String[] ars) {

    }
    
    static {
        commandMap.put("", new ConsolePanelEmpty());
        commandMap.put("rotor_base", new RotorBase());
    }
}
