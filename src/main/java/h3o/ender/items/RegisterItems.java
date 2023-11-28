package h3o.ender.items;

import h3o.ender.DwMod;
import h3o.ender.blocks.RegisterBlocks;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import static h3o.ender.components.Circuit.strToName;

public class RegisterItems {
        public static final Item TARDIS_DEFAULT_WALL_LAMP = new BlockItem(RegisterBlocks.TARDIS_DEFAULT_WALL_LAMP,
                        new FabricItemSettings());
        public static final Item TARDIS_DEFAULT_FLOOR = new BlockItem(RegisterBlocks.TARDIS_DEFAULT_FLOOR,
                        new FabricItemSettings());
        public static final Item TARDIS_MAINTENANCE_WALL_LOWER = new BlockItem(RegisterBlocks.TARDIS_MAINTENANCE_WALL_LOWER,
                        new FabricItemSettings());
        public static final Item TARDIS_MAINTENANCE_WALL_FRAME_LOWER = new BlockItem(RegisterBlocks.TARDIS_MAINTENANCE_WALL_FRAME_LOWER,
                        new FabricItemSettings());
        public static final Item TARDIS_MAINTENANCE_WALL = new BlockItem(RegisterBlocks.TARDIS_MAINTENANCE_WALL,
                        new FabricItemSettings());
        public static final Item TARDIS_MAINTENANCE_WALL_FRAME = new BlockItem(RegisterBlocks.TARDIS_MAINTENANCE_WALL_FRAME,
                        new FabricItemSettings());
        public static final Item GRAY_PRING = new BlockItem(RegisterBlocks.GRAY_PRINT, new FabricItemSettings());
        public static final Item CRYSTAL_RESONATOR = new Item(new FabricItemSettings());
        public static final Item TOOL_BASE = new Item(new FabricItemSettings());
        public static final Item FREQUENCY_DETECTOR = new FrequencyDetector(new FabricItemSettings().maxCount(1));
        public static final Item SCREWDRIVER = new Screwdriver(new FabricItemSettings().maxCount(1));
        public static final Item WRENCH = new Wrench(new FabricItemSettings().maxCount(1));
        public static final Item PROTYON_UNIT = new ProtyonUnit(new FabricItemSettings());
        public static final Item RAW_EXITONIC_CIRCUIT = new Item(new FabricItemSettings());
        public static final Item EXITONIC_CIRCUIT = new ExitonicCircuit(new FabricItemSettings());
        public static final Item LLOENERGY_DISTRIBUTOR = new LLOEnergyDistributor(new FabricItemSettings());
        public static final Item MAIN_SPACE_TIME_ELEMENT = new MainSpaceTimeElement(new FabricItemSettings());
        public static final Item PCB = new Item(new FabricItemSettings());
        public static final Item SILICON_BALL = new Item(new FabricItemSettings());
        public static final Item CONTROL_UNIT = new Item(new FabricItemSettings());
        public static final Item RIFT_STIMULATOR = new Item(new FabricItemSettings());
        public static final Item ENERGY_CONDENSER = new Item(new FabricItemSettings());
        public static final Item DEFAULT_ROTOR = new Rotor(new FabricItemSettings(), strToName("default_rotor"));
        


        // astralMap
        public static final Item ASTRALMAP_0 = new Item(new FabricItemSettings());
        public static final Item ASTRALMAP_0_0 = new Item(new FabricItemSettings());
        // rooms
        public static final Item MAINTENANCE_ACCESS = new Item(new FabricItemSettings());


        public static void register() {
                Registry.register(Registries.ITEM, new Identifier(DwMod.MODID, "tardis.default.wall.lamp"),
                                TARDIS_DEFAULT_WALL_LAMP);
                Registry.register(Registries.ITEM, new Identifier(DwMod.MODID, "tardis.default.floor"),
                                TARDIS_DEFAULT_FLOOR);
                Registry.register(Registries.ITEM, new Identifier(DwMod.MODID, "tardis.maintenance.wall.lower"),
                                TARDIS_MAINTENANCE_WALL_LOWER);
                Registry.register(Registries.ITEM, new Identifier(DwMod.MODID, "tardis.maintenance.wall.frame.lower"),
                                TARDIS_MAINTENANCE_WALL_FRAME_LOWER);
                Registry.register(Registries.ITEM, new Identifier(DwMod.MODID, "tardis.maintenance.wall"),
                                TARDIS_MAINTENANCE_WALL);
                Registry.register(Registries.ITEM, new Identifier(DwMod.MODID, "tardis.maintenance.wall.frame"),
                                TARDIS_MAINTENANCE_WALL_FRAME);
                Registry.register(Registries.ITEM, new Identifier(DwMod.MODID, "gray_print"), GRAY_PRING);
                Registry.register(Registries.ITEM, new Identifier(DwMod.MODID, "crystal_resonator"), CRYSTAL_RESONATOR);
                Registry.register(Registries.ITEM, new Identifier(DwMod.MODID, "tool_base"), TOOL_BASE);
                Registry.register(Registries.ITEM, new Identifier(DwMod.MODID, "frequency_detector"),
                                FREQUENCY_DETECTOR);
                Registry.register(Registries.ITEM, new Identifier(DwMod.MODID, "screwdriver"), SCREWDRIVER);
                Registry.register(Registries.ITEM, new Identifier(DwMod.MODID, "wrench"), WRENCH);
                Registry.register(Registries.ITEM, new Identifier(DwMod.MODID, "protyon_unit"), PROTYON_UNIT);
                Registry.register(Registries.ITEM, new Identifier(DwMod.MODID, "raw_exitonic_circuit"),
                                RAW_EXITONIC_CIRCUIT);
                Registry.register(Registries.ITEM, new Identifier(DwMod.MODID, "exitonic_circuit"), EXITONIC_CIRCUIT);
                Registry.register(Registries.ITEM, new Identifier(DwMod.MODID, "2lo_energy_distributor"),
                                LLOENERGY_DISTRIBUTOR);
                Registry.register(Registries.ITEM, new Identifier(DwMod.MODID, "main_space_time_element"),
                                MAIN_SPACE_TIME_ELEMENT);
                Registry.register(Registries.ITEM, new Identifier(DwMod.MODID, "pcb"), PCB);
                Registry.register(Registries.ITEM, new Identifier(DwMod.MODID, "silicon_ball"), SILICON_BALL);
                Registry.register(Registries.ITEM, new Identifier(DwMod.MODID, "control_unit"), CONTROL_UNIT);
                Registry.register(Registries.ITEM, new Identifier(DwMod.MODID, "rift_stimulator"), RIFT_STIMULATOR);
                Registry.register(Registries.ITEM, new Identifier(DwMod.MODID, "energy_condenser"), ENERGY_CONDENSER);
                Registry.register(Registries.ITEM, new Identifier(DwMod.MODID, "default_rotor"), DEFAULT_ROTOR);

                // ASTRAL MAP
                Registry.register(Registries.ITEM, new Identifier(DwMod.MODID, "astralmap_0"), ASTRALMAP_0);
                Registry.register(Registries.ITEM, new Identifier(DwMod.MODID, "astralmap_0_0"), ASTRALMAP_0_0);

                // ROOMS
                Registry.register(Registries.ITEM, new Identifier(DwMod.MODID, "tardis.room.maintenance_access"), MAINTENANCE_ACCESS);
        }
}
