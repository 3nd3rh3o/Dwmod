package h3o.ender.items;

import h3o.ender.DwMod;
import h3o.ender.blocks.RegisterBlocks;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class RegisterItems {
    public static final Item TARDIS_DEFAULT_WALL_LAMP = new BlockItem(RegisterBlocks.TARDIS_DEFAULT_WALL_LAMP, new FabricItemSettings());
    public static final Item TARDIS_DEFAULT_FLOOR = new BlockItem(RegisterBlocks.TARDIS_DEFAULT_FLOOR, new FabricItemSettings());
    public static final Item GRAY_PRING = new BlockItem(RegisterBlocks.GRAY_PRINT, new FabricItemSettings());
    public static final Item CRYSTAL_RESONATOR = new Item(new FabricItemSettings());
    public static final Item TOOL_BASE = new Item(new FabricItemSettings());

    public static void register() {
        Registry.register(Registries.ITEM, new Identifier(DwMod.MODID, "tardis.default.wall.lamp"), TARDIS_DEFAULT_WALL_LAMP);
        Registry.register(Registries.ITEM, new Identifier(DwMod.MODID, "tardis.default.floor"), TARDIS_DEFAULT_FLOOR);
        Registry.register(Registries.ITEM, new Identifier(DwMod.MODID, "gray_print"), GRAY_PRING);
        Registry.register(Registries.ITEM, new Identifier(DwMod.MODID, "crystal_resonator"), CRYSTAL_RESONATOR);
        Registry.register(Registries.ITEM, new Identifier(DwMod.MODID, "tool_base"), TOOL_BASE);
    }
}
