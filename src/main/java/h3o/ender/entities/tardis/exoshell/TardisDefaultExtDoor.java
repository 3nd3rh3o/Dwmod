package h3o.ender.entities.tardis.exoshell;

import h3o.ender.entities.Tardis;
import h3o.ender.entities.TardisInternalPortal;
import h3o.ender.entities.tardis.TardisExtDoor;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.util.GeckoLibUtil;

public class TardisDefaultExtDoor extends TardisExtDoor implements GeoEntity {
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    private Tardis tardis = initTardis();

    public static EntityType<TardisDefaultExtDoor> entityType = FabricEntityTypeBuilder
            .create(SpawnGroup.MISC, TardisDefaultExtDoor::new)
            .dimensions(EntityDimensions.changing(0F, 0F))
            .build();

    public TardisDefaultExtDoor(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void registerControllers(ControllerRegistrar controllers) {
        return;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isPushedByFluids() {
        return false;
    }
    
    
}
