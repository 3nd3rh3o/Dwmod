package h3o.ender.entities;

import java.util.List;

import h3o.ender.entities.tardis.exoshell.TardisDefaultExtDoor;
import h3o.ender.structures.tardis.DimensionalStorageHelper;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import qouteall.imm_ptl.core.portal.Portal;
import qouteall.q_misc_util.my_util.DQuaternion;

public class TardisInternalPortal extends Portal {

    private Tardis tardis;

    public static EntityType<TardisInternalPortal> entityType = FabricEntityTypeBuilder
            .create(SpawnGroup.MISC, TardisInternalPortal::new)
            .dimensions(EntityDimensions.changing(0F, 0F))
            .build();

    public TardisInternalPortal(EntityType<?> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void tick() {
        super.tick();
        if (!getWorld().isClient()) {
            if (this.tardis != null) {
                if (this.getDestPos() != tardis.getPos().add(0, 1, 0.5)
                        || getDestDim() != tardis.getWorld().getRegistryKey()) {
                    this.setDestinationDimension(tardis.getWorld().getRegistryKey());
                    this.setDestination(tardis.getPos().add(new Vec3d(0, 1, 0.5).rotateY((float)(-tardis.getDataTracker().get(Tardis.EXOSHELL_ROT) * Math.PI / 180f))));
                    this.setRotationTransformation(DQuaternion
                            .fromEulerAngle(new Vec3d(0, tardis.getDataTracker().get(Tardis.EXOSHELL_ROT), 0)));
                    reloadAndSyncToClientNextTick();
                }
                if (getWorld()
                        .getEntitiesByClass(TardisDefaultExtDoor.class, this.getBoundingBox().expand(1), entity -> true)
                        .isEmpty()) {
                    TardisDefaultExtDoor extDoor;
                    extDoor = TardisDefaultExtDoor.entityType.create(getOriginWorld());
                    extDoor.refreshPositionAndAngles(getPos().x, getPos().y - 1, getPos().z, 0, 0);
                    extDoor.getWorld().spawnEntity(extDoor);
                }
            } else {
                this.tardis = initTardis();
            }
        }

    }

    public void setTardis(Tardis tardis2) {
        this.tardis = tardis2;
    }

    private Tardis initTardis() {
        if (!getWorld().isClient()) {
            for (ServerWorld world : getServer().getWorlds()) {
                List<Tardis> trds = world.getEntitiesByClass(Tardis.class,
                        Box.of(new Vec3d(0, 0, 0), World.HORIZONTAL_LIMIT * 2, World.MAX_Y - World.MIN_Y,
                                World.HORIZONTAL_LIMIT * 2),
                        (entities) -> ((Tardis) entities).getIndex() == DimensionalStorageHelper
                                .getIndex(getOriginPos()));
                if (trds.size() != 0) {
                    return trds.get(0);
                }
            }
        }
        return null;
    }
}
