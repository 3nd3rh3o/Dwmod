package h3o.ender.itemGroup;

import h3o.ender.DwMod;
import h3o.ender.items.RegisterItems;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class RegisterItemGroups {
    private static final ItemGroup TARDIS_BUILDING_BLOCK_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(RegisterItems.TARDIS_DEFAULT_WALL_LAMP))
            .displayName(Text.of("Tardis building block")).entries((context, entries) -> {
                entries.add(RegisterItems.TARDIS_DEFAULT_WALL_LAMP);
                entries.add(RegisterItems.TARDIS_DEFAULT_FLOOR);
            }).build();

    public static void register() {
        Registry.register(Registries.ITEM_GROUP, new Identifier(DwMod.MODID, "tardis.building.block"),
                TARDIS_BUILDING_BLOCK_GROUP);
    }
}
