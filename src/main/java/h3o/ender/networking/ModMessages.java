package h3o.ender.networking;

import h3o.ender.DwMod;
import h3o.ender.networking.packets.FrequencyDetectorC2Spacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class ModMessages {
    public static final Identifier FREQUENCY_DETECTOR_SETTING_ID = new Identifier(DwMod.MODID, "frequency_detector_setting");

    public static void registerC2Spackets() {
        ServerPlayNetworking.registerGlobalReceiver(FREQUENCY_DETECTOR_SETTING_ID, FrequencyDetectorC2Spacket::receive);
    }

    public static void registerS2Cpackets() {
        
    }
}
