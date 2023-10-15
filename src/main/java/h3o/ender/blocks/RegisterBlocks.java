package h3o.ender.blocks;

import h3o.ender.DwMod;
import h3o.ender.blocks.tardis.DefaultFloor;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class RegisterBlocks {
    public static final Block TARDIS_DEFAULT_FLOOR = new DefaultFloor(FabricBlockSettings.create().strength(4.0f));


    public static void register() {
        DwMod.LOGGER.info("Registering Blocks");
        Registry.register(Registries.BLOCK, new Identifier(DwMod.MODID, "tardis.default.floor"), TARDIS_DEFAULT_FLOOR);
    }
    
}
