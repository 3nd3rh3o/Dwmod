package h3o.ender.entities.tardis.exoshell;

import h3o.ender.blocks.RegisterBlocks;
import h3o.ender.blocks.tardis.exoshellIntDoor.TardisDefaultExtDoorHitBox;
import h3o.ender.entities.Tardis;
import h3o.ender.entities.tardis.TardisExtDoor;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.util.GeckoLibUtil;

public class TardisDefaultExtDoor extends TardisExtDoor implements GeoEntity {
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    private Tardis tardis;
    private static final TrackedData<Boolean> DOORS = DataTracker.registerData(TardisDefaultExtDoor.class, TrackedDataHandlerRegistry.BOOLEAN);
    private boolean oldDoors;
    private static final RawAnimation LEFT_OPEN = RawAnimation.begin()
            .thenPlayAndHold("animation.exoshell.default.int.left.open");
    private static final RawAnimation RIGHT_OPEN = RawAnimation.begin()
            .thenPlayAndHold("animation.exoshell.default.int.right.open");
    private static final RawAnimation LEFT_CLOSE = RawAnimation.begin()
            .thenPlayAndHold("animation.exoshell.default.int.left.close");
    private static final RawAnimation RIGHT_CLOSE = RawAnimation.begin()
            .thenPlayAndHold("animation.exoshell.default.int.right.close");

    public static EntityType<TardisDefaultExtDoor> entityType = FabricEntityTypeBuilder
            .create(SpawnGroup.MISC, TardisDefaultExtDoor::new)
            .dimensions(EntityDimensions.changing(0F, 0F))
            .build();

    public TardisDefaultExtDoor(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void kill() {
        super.kill();
        getWorld().setBlockState(getBlockPos(),
                Blocks.AIR.getDefaultState(),
                3);
        getWorld().setBlockState(getBlockPos().up(),
                Blocks.AIR.getDefaultState(),
                3);
    }

    @Override
    public void registerControllers(ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "left", 0, state -> {
            if (getDataTracker().get(DOORS)) {
                return state.setAndContinue(LEFT_OPEN);
            } else {
                return state.setAndContinue(LEFT_CLOSE);
            }
        }))
        .add(new AnimationController<>(this, "right", 0, state -> {
            if (getDataTracker().get(DOORS)) {
                return state.setAndContinue(RIGHT_OPEN);
            } else {
                return state.setAndContinue(RIGHT_CLOSE);
            }
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }

    

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(DOORS, false);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isPushedByFluids() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (!getWorld().isClient()) {
            if (this.tardis != null) {
                getWorld().setBlockState(getBlockPos(),
                        RegisterBlocks.TARDIS_EXT_DOOR_DEFAULT_HITBOX.getDefaultState()
                                .with(TardisDefaultExtDoorHitBox.UPPER, false)
                                .with(TardisDefaultExtDoorHitBox.OPENNED, tardis.getDoorsOpenned()),
                        3);
                getWorld().setBlockState(getBlockPos().up(),
                        RegisterBlocks.TARDIS_EXT_DOOR_DEFAULT_HITBOX.getDefaultState()
                                .with(TardisDefaultExtDoorHitBox.UPPER, true)
                                .with(TardisDefaultExtDoorHitBox.OPENNED, tardis.getDoorsOpenned()),
                        3);
                if (oldDoors != tardis.getDoorsOpenned()) {
                    getDataTracker().set(DOORS, tardis.getDoorsOpenned());
                    oldDoors = tardis.getDoorsOpenned();
                }
            } else {
                this.tardis = initTardis();
            }
        }
        if (this.getBodyYaw() / 90 != 0) {
            this.setBodyYaw(0);
            this.setHeadYaw(0);
        }
    }

}
