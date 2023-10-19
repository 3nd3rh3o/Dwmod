package h3o.ender.entities;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.World;
import qouteall.imm_ptl.core.portal.Portal;

public class TardisInternalPortal extends Portal {

    private Tardis tardis;

    public static EntityType<TardisInternalPortal> entityType = FabricEntityTypeBuilder.create(SpawnGroup.MISC, TardisInternalPortal::new)
            .dimensions(EntityDimensions.changing(0F, 0F))
            .build();

    public TardisInternalPortal(EntityType<?> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void tick() {
        // TODO Auto-generated method stub
        super.tick();
        if (!getWorld().isClient && this.tardis != null) {
            this.setDestinationDimension(tardis.getWorld().getRegistryKey());
            this.setDestination(tardis.getPos());
        }
    }

    public void setTardis(Tardis tardis2) {
        this.tardis=tardis2;
    }

    
}
