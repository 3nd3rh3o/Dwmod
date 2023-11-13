package h3o.ender.blocks;

import h3o.ender.DwMod;
import h3o.ender.blocks.tardis.DefaultFloor;
import h3o.ender.blocks.tardis.DefaultWallLamp;
import h3o.ender.blocks.tardis.GrayPrint;
import h3o.ender.blocks.tardis.GrowingTardis;
import h3o.ender.blocks.tardis.RotorBase;
import h3o.ender.blocks.tardis.TardisDefaultHitbox;
import h3o.ender.blocks.tardis.Terminal;
import h3o.ender.blocks.tardis.console_panel.CommunicationConsolePanel;
import h3o.ender.blocks.tardis.console_panel.FabricationConsolePanel;
import h3o.ender.blocks.tardis.exoshellIntDoor.TardisDefaultExtDoorHitBox;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class RegisterBlocks {
    public static final Block TARDIS_DEFAULT_FLOOR = new DefaultFloor(FabricBlockSettings.create().strength(-1.0f));
    public static final Block TARDIS_DEFAULT_WALL_LAMP = new DefaultWallLamp(FabricBlockSettings.create().strength(-1.0f).nonOpaque().luminance(13));
    public static final Block TARDIS_DEFAULT_HITBOX = new TardisDefaultHitbox(FabricBlockSettings.create().strength(-1.0f).nonOpaque());
    public static final Block TARDIS_EXT_DOOR_DEFAULT_HITBOX = new TardisDefaultExtDoorHitBox(FabricBlockSettings.create().strength(-1.0f).nonOpaque());
    public static final Block GRAY_PRINT = new GrayPrint(FabricBlockSettings.create().strength(1f));
    public static final Block GROWING_TARDIS = new GrowingTardis(FabricBlockSettings.create().strength(-1f));
    public static final Block ROTOR_BASE = new RotorBase(FabricBlockSettings.create().strength(-1f));
    public static final Block TERMINAL = new Terminal(FabricBlockSettings.create().strength(-1f));
    public static final Block FABRICATION_CONSOLE_PANEL = new FabricationConsolePanel(FabricBlockSettings.create().strength(-1f).nonOpaque());
    public static final Block COMMUNICATION_CONSOLE_PANEL = new CommunicationConsolePanel(FabricBlockSettings.create().strength(-1f).nonOpaque());

    public static void register() {
        DwMod.LOGGER.info("Registering Blocks");
        Registry.register(Registries.BLOCK, new Identifier(DwMod.MODID, "tardis.default.floor"), TARDIS_DEFAULT_FLOOR);
        Registry.register(Registries.BLOCK, new Identifier(DwMod.MODID, "tardis.default.hitbox"), TARDIS_DEFAULT_HITBOX);
        Registry.register(Registries.BLOCK, new Identifier(DwMod.MODID, "tardis.default.extdoor.hitbox"), TARDIS_EXT_DOOR_DEFAULT_HITBOX);
        Registry.register(Registries.BLOCK, new Identifier(DwMod.MODID, "tardis.default.wall.lamp"), TARDIS_DEFAULT_WALL_LAMP);
        Registry.register(Registries.BLOCK, new Identifier(DwMod.MODID, "gray_print"), GRAY_PRINT);
        Registry.register(Registries.BLOCK, new Identifier(DwMod.MODID, "growing_tardis"), GROWING_TARDIS);
        Registry.register(Registries.BLOCK, new Identifier(DwMod.MODID, "tardis.console.rotor_base"), ROTOR_BASE);
        Registry.register(Registries.BLOCK, new Identifier(DwMod.MODID, "tardis.console.terminal"), TERMINAL);
        Registry.register(Registries.BLOCK, new Identifier(DwMod.MODID, "tardis.console.fabrication"), FABRICATION_CONSOLE_PANEL);
        Registry.register(Registries.BLOCK, new Identifier(DwMod.MODID, "tardis.console.communication"), COMMUNICATION_CONSOLE_PANEL);
    }
    
}
