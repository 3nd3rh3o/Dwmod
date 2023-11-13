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
            .displayName(Text.of("DwMod - Tardis building block")).entries((context, entries) -> {
                entries.add(RegisterItems.TARDIS_DEFAULT_WALL_LAMP);
                entries.add(RegisterItems.TARDIS_DEFAULT_FLOOR);
            }).build();
    private static final ItemGroup COMPONENT = FabricItemGroup.builder()
            .icon(() -> new ItemStack(RegisterItems.CRYSTAL_RESONATOR))
            .displayName(Text.of("DwMod - Components")).entries((context, entries) -> {
                entries.add(RegisterItems.PROTYON_UNIT);
                entries.add(RegisterItems.GRAY_PRING);
                entries.add(RegisterItems.TOOL_BASE);
                entries.add(RegisterItems.CRYSTAL_RESONATOR);
                entries.add(RegisterItems.RAW_EXITONIC_CIRCUIT);
                entries.add(RegisterItems.EXITONIC_CIRCUIT);
                entries.add(RegisterItems.PCB);
                entries.add(RegisterItems.SILICON_BALL);
                entries.add(RegisterItems.CONTROL_UNIT);
                entries.add(RegisterItems.ENERGY_CONDENSER);
                entries.add(RegisterItems.RIFT_STIMULATOR);
                entries.add(RegisterItems.LLOENERGY_DISTRIBUTOR);
                entries.add(RegisterItems.MAIN_SPACE_TIME_ELEMENT);
                entries.add(RegisterItems.DEFAULT_ROTOR);
            }).build();
    private static final ItemGroup TOOLS = FabricItemGroup.builder()
            .icon(() -> new ItemStack(RegisterItems.FREQUENCY_DETECTOR))
            .displayName(Text.of("DwMod - Tools")).entries((context, entries) -> {
                entries.add(RegisterItems.FREQUENCY_DETECTOR);
                entries.add(RegisterItems.SCREWDRIVER);
                entries.add(RegisterItems.WRENCH);
            }).build();

    public static void register() {
        Registry.register(Registries.ITEM_GROUP, new Identifier(DwMod.MODID, "tardis.building.block"),
                TARDIS_BUILDING_BLOCK_GROUP);
        Registry.register(Registries.ITEM_GROUP, new Identifier(DwMod.MODID, "components"), COMPONENT);
        Registry.register(Registries.ITEM_GROUP, new Identifier(DwMod.MODID, "tools"), TOOLS);
    }
}
