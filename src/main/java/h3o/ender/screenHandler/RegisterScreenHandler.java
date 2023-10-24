package h3o.ender.screenHandler;

import h3o.ender.DwMod;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class RegisterScreenHandler {
    public static final ScreenHandlerType<FrequencyDetectorScreenHandler> FREQUENCY_DETECTOR_SCREEN_HANDLER = new ExtendedScreenHandlerType<>(FrequencyDetectorScreenHandler::new);

    public static void register() {
        Registry.register(Registries.SCREEN_HANDLER, new Identifier(DwMod.MODID, "frequency_detector"), FREQUENCY_DETECTOR_SCREEN_HANDLER);
    }
}
