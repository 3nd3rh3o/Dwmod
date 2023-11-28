package h3o.ender.tardisOs;

import java.util.HashMap;

import java.util.List;

import h3o.ender.blockEntity.tardis.TerminalBE;
import static h3o.ender.components.Circuit.*;
import h3o.ender.components.Circuit.LOCATION;
import h3o.ender.components.Circuit.NAME;
import h3o.ender.tardisOs.help.Help;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import java.util.Arrays;

public class TardisOs {
    private static final HashMap<String, Command> commandMap = new HashMap<>();

    public static MutableText execute(String input, ServerPlayerEntity player, TerminalBE bEnt) {
        String[] parts = input.split(" ");
        String commandName = parts[0];
        String[] args = null;
        if (!(parts.length == 0)) {
            args = Arrays.copyOfRange(parts, 1, parts.length);
        }

        Command command = commandMap.get(commandName.toLowerCase());
        if (command == null) {
            return FormattedText.empty().error("COMMAND : ").info(commandName).error(" NOT FOUND! USE HELP").endLine()
                    .assemble();
        }
        return command.execute(args, player, bEnt);
    }

    public static List<MutableText> errorDisplayOrNormal(TerminalBE bEnt, NbtList circuits, TextRenderer textRenderer) {
        if (circuits != null) {
            HashMap<NAME, LOCATION> missingComp = new HashMap<>();
            missingComp.put(NAME.MAIN_SPACE_TIME_ELEMENT, LOCATION.ROTOR_BASE);
            missingComp.put(NAME.LLO_ENERGY_CONNECTOR, LOCATION.ROTOR_BASE);
            for (int y = 0; y < circuits.size(); y++) {
                if (missingComp.containsKey(strToName(circuits.getList(y).getString(0)))
                        && missingComp.get(strToName(circuits.getList(y).getString(0)))
                                .equals(strToLoc(circuits.getList(y).getString(1)))) {
                    missingComp.remove(strToName(circuits.getList(y).getString(0)),
                            strToLoc(circuits.getList(y).getString(1)));
                }
            }
            List<MutableText> list;
            if (missingComp.size() == 0) {
                list = FormattedText.wrapStyledText(bEnt.getText(), 160, textRenderer);
                list.addAll(FormattedText.wrapStyledText(FormattedText.empty().normal(bEnt.getPrompt()).assemble(), 160,
                        textRenderer));
            } else {
                int mNum = 0;
                FormattedText text = FormattedText.empty();
                for (NAME name : missingComp.keySet()) {
                    mNum++;
                    text = text.error("-MISSING KEY COMPONENT : ").info(name.toString())
                            .error(" IN CONSOLE PANEL : ").info(missingComp.get(name).toString()).endLine();
                    if (mNum == 2) {
                        break;
                    }
                }
                if (missingComp.size() > 2) {
                    text = text.error("+").info(String.valueOf(missingComp.size() - 2)).error(" MORE").endLine();
                }
                list = FormattedText.wrapStyledText(text.assemble(), 160, textRenderer);
            }
            return list;
        }
        return null;
    }

    static {
        commandMap.put("help", new Help());
        commandMap.put("astralmap", new AstralMap());
        commandMap.put("engineaccess", new EngineAccess());
        commandMap.put("mainconf", new MainConf());
    }
}
