package h3o.ender.entities;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.World;
import qouteall.imm_ptl.core.portal.Portal;

public class TardisPathwayPortal extends Portal {
    public static EntityType<TardisPathwayPortal> entityType = FabricEntityTypeBuilder
            .create(SpawnGroup.MISC, TardisPathwayPortal::new)
            .dimensions(EntityDimensions.changing(0f, 0f))
            .build();
    
    public TardisPathwayPortal(EntityType<?> entityType, World world) {
        super(entityType, world);
    }
}
