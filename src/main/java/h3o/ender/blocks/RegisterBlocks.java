package h3o.ender.blocks;

import h3o.ender.DwMod;
import h3o.ender.blocks.tardis.DefaultFloor;
import h3o.ender.blocks.tardis.DefaultWallLamp;
import h3o.ender.blocks.tardis.TardisDefaultHitbox;
import h3o.ender.blocks.tardis.exoshellIntDoor.TardisDefaultExtDoorHitBox;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class RegisterBlocks {
    public static final Block TARDIS_DEFAULT_FLOOR = new DefaultFloor(FabricBlockSettings.create().strength(-1.0f));
    public static final Block TARDIS_DEFAULT_WALL_LAMP = new DefaultWallLamp(FabricBlockSettings.create().strength(-1.0f).nonOpaque().luminance(13));
    public static final Block TARDIS_DEFAULT_HITBOX = new TardisDefaultHitbox(FabricBlockSettings.create().strength(-1.0f).nonOpaque());
    public static final Block TARDIS_EXT_DOOR_DEFAULT_HITBOX = new TardisDefaultExtDoorHitBox(FabricBlockSettings.create().strength(-1.0f).nonOpaque());


    public static void register() {
        DwMod.LOGGER.info("Registering Blocks");
        Registry.register(Registries.BLOCK, new Identifier(DwMod.MODID, "tardis.default.floor"), TARDIS_DEFAULT_FLOOR);
        Registry.register(Registries.ITEM, new Identifier(DwMod.MODID, "tardis.default.floor"), new BlockItem(TARDIS_DEFAULT_FLOOR, new FabricItemSettings()));
        Registry.register(Registries.BLOCK, new Identifier(DwMod.MODID, "tardis.default.hitbox"), TARDIS_DEFAULT_HITBOX);
        Registry.register(Registries.BLOCK, new Identifier(DwMod.MODID, "tardis.default.extdoor.hitbox"), TARDIS_EXT_DOOR_DEFAULT_HITBOX);
        Registry.register(Registries.BLOCK, new Identifier(DwMod.MODID, "tardis.default.wall.lamp"), TARDIS_DEFAULT_WALL_LAMP);
        Registry.register(Registries.ITEM, new Identifier(DwMod.MODID, "tardis.default.wall.lamp"), new BlockItem(TARDIS_DEFAULT_WALL_LAMP, new FabricItemSettings()));
    }
    
}
