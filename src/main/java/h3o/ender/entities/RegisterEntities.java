package h3o.ender.entities;

import h3o.ender.DwMod;
import h3o.ender.entities.tardis.exoshell.TardisDefaultExtDoor;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class RegisterEntities {
    public static final EntityType<Tardis> TARDIS = Registry.register(Registries.ENTITY_TYPE, 
            new Identifier(DwMod.MODID, "tardis"),
            FabricEntityTypeBuilder.create(null, Tardis::new).dimensions(EntityDimensions.fixed(0, 0)).build());
    

    public static final EntityType<TardisDefaultExtDoor> TARDIS_EXT_DOOR_DEFAULT = Registry.register(Registries.ENTITY_TYPE, new Identifier(DwMod.MODID, "tardis_default_ext_door"), TardisDefaultExtDoor.entityType);
    public static final EntityType<TardisPortal> TARDIS_PORTAL = Registry.register(Registries.ENTITY_TYPE, new Identifier(DwMod.MODID, "tardis_portal"), TardisPortal.entityType);
    public static final EntityType<TardisInternalPortal> TARDIS_INT_PORTAL = Registry.register(Registries.ENTITY_TYPE, new Identifier(DwMod.MODID, "tardis_internal_portal"), TardisInternalPortal.entityType);
}
