package h3o.ender.entities;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.World;
import qouteall.imm_ptl.core.portal.Portal;

public class TardisPortal extends Portal {

    public static EntityType<TardisPortal> entityType = FabricEntityTypeBuilder.create(SpawnGroup.MISC, TardisPortal::new)
            .dimensions(EntityDimensions.changing(0F, 0F))
            .build();


    public TardisPortal(EntityType<?> entityType, World world) {
        super(entityType, world);
    }


    @Override
    public void tick() {
        super.tick();
        
        if (!getWorld().isClient) {
            updateState();
        }
    }


    private void updateState() {
        if (getWorld().getEntitiesByClass(Tardis.class, this.getBoundingBox().expand(1), null).isEmpty()) {
            kill();
        }
    }
}
