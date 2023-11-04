package h3o.ender.networking;

import h3o.ender.DwMod;
import h3o.ender.networking.packets.FrequencyDetectorC2Spacket;
import h3o.ender.networking.packets.TardisTerminalCharC2Spacket;
import h3o.ender.networking.packets.TardisTerminalOptC2Spacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class ModMessages {
    public static final Identifier FREQUENCY_DETECTOR_SETTING_ID = new Identifier(DwMod.MODID, "frequency_detector_setting");
    public static final Identifier TARDIS_TERMINAL_CHAR_ID = new Identifier(DwMod.MODID, "tardis_terminal_char");
    public static final Identifier TARDIS_TERMINAL_OPT_ID = new Identifier(DwMod.MODID, "tardis_terminal_opt");

    public static void registerC2Spackets() {
        ServerPlayNetworking.registerGlobalReceiver(FREQUENCY_DETECTOR_SETTING_ID, FrequencyDetectorC2Spacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(TARDIS_TERMINAL_CHAR_ID, TardisTerminalCharC2Spacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(TARDIS_TERMINAL_OPT_ID, TardisTerminalOptC2Spacket::receive);
    }

    public static void registerS2Cpackets() {
        
    }
}
