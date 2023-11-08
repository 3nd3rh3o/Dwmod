package h3o.ender.components;

import java.util.ArrayList;
import java.util.List;

import h3o.ender.items.RegisterItems;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;

public class Circuit {
    private final NAME name;
    private final LOCATION loc;

    public enum LOCATION {
        ROTOR_BASE;
    }

    public enum NAME {
        LLO_ENERGY_CONNECTOR, MAIN_SPACE_TIME_ELEMENT, DEFAULT_ROTOR
    }

    public static LOCATION strToLoc(String loc) {
        return switch (loc) {
            case "rotor_base" -> LOCATION.ROTOR_BASE;
            default -> null;
        };
    }

    public static NAME strToName(String name) {
        return switch (name) {
            case "2lo_energy_connector" -> NAME.LLO_ENERGY_CONNECTOR;
            case "main_space_time_element" -> NAME.MAIN_SPACE_TIME_ELEMENT;
            case "default_rotor" -> NAME.DEFAULT_ROTOR;
            default -> null;
        };
    }

    public Circuit(NAME name, LOCATION loc) {
        this.name = name;
        this.loc = loc;
    }

    public static String nameToStr(NAME name) {
        return switch (name) {
            case LLO_ENERGY_CONNECTOR -> "2lo_energy_connector";
            case MAIN_SPACE_TIME_ELEMENT -> "main_space_time_element";
            case DEFAULT_ROTOR -> "default_rotor";
        };
    }

    public static String locToStr(LOCATION loc) {
        return switch (loc) {
            case ROTOR_BASE -> "rotor_base";
        };
    }

    public String getName() {
        return nameToStr(name);
    }

    public String getLoc() {
        return locToStr(loc);
    }

    public static NbtCompound writeNbt(List<Circuit> circuitList) {
        NbtList circuits = new NbtList();
        for (Circuit circuit : circuitList) {
            NbtList strList = new NbtList();
            strList.add(NbtString.of(circuit.getName()));
            strList.add(NbtString.of(circuit.getLoc()));
            circuits.add(strList);
        }
        NbtCompound nbt = new NbtCompound();
        nbt.put("Circuits", circuits);
        return nbt;
    }

    public static List<Circuit> readFromNbt(NbtCompound nbt) {
        List<Circuit> circuits = new ArrayList<>();
        NbtList circuitsNbt = nbt.getList("Circuits", NbtElement.LIST_TYPE);
        for (int i = 0; i < circuitsNbt.size(); i++) {
            circuits.add(new Circuit(Circuit.strToName(circuitsNbt.getList(i).getString(0)),
                    Circuit.strToLoc(circuitsNbt.getList(i).getString(1))));
        }
        return circuits;
    }

    public static boolean contains(NbtCompound circuits, Circuit circuit) {
        List<Circuit> list = readFromNbt(circuits);
        for (Circuit c : list) {
            if (c.getName().equals(circuit.getName()) && c.getLoc().equals(circuit.getLoc())) {
                return true;
            }
        }
        return false;
    }

    public static ItemStack getItemForName(String name) {
        return switch (strToName(name)) {
            case LLO_ENERGY_CONNECTOR -> new ItemStack(RegisterItems.LLOENERGY_DISTRIBUTOR, 1);
            case MAIN_SPACE_TIME_ELEMENT -> new ItemStack(RegisterItems.MAIN_SPACE_TIME_ELEMENT, 1);
            case DEFAULT_ROTOR -> new ItemStack(RegisterItems.DEFAULT_ROTOR, 1);
        };
    }

    public static void renderPos(MatrixStack poseStack, String name, LOCATION location) {
        switch (location) {
            case ROTOR_BASE -> {
                switch (strToName(name)) {
                    case LLO_ENERGY_CONNECTOR -> poseStack.translate(0.25, 0.5625, 0.375);
                    case MAIN_SPACE_TIME_ELEMENT -> poseStack.translate(-0.0635, 0.5625, 0);
                    default -> {}
                }
            }
            default -> {}
        }
    }
}
