package h3o.ender.entities;

import java.util.ArrayList;
import java.util.HashMap;

import h3o.ender.components.Circuit;
import h3o.ender.structures.tardis.Room;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Arm;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.core.animation.AnimationController.State;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class Tardis extends LivingEntity implements GeoEntity {
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    private static final RawAnimation LEFT_OPEN = RawAnimation.begin()
            .thenPlayAndHold("animation.exoshell.default.left.open");
    private static final RawAnimation RIGHT_OPEN = RawAnimation.begin()
            .thenPlayAndHold("animation.exoshell.default.right.open");
    private static final RawAnimation LEFT_CLOSE = RawAnimation.begin()
            .thenPlayAndHold("animation.exoshell.default.left.close");
    private static final RawAnimation RIGHT_CLOSE = RawAnimation.begin()
            .thenPlayAndHold("animation.exoshell.default.right.close");

    private static final TrackedData<Byte> MOB_FLAGS;

    private boolean leftOpen = true;
    private boolean rightOpen = true;

    private int type;
    private ArrayList<Circuit> circuits;
    private HashMap<Integer, Room> internalScheme;

    protected final float[] handDropChances;
    private final DefaultedList<ItemStack> armorItems;
    private final DefaultedList<ItemStack> handItems;

    protected Tardis(EntityType<? extends LivingEntity> entityType, World world) {
        super(RegisterEntities.TARDIS, world);
        this.handDropChances = new float[2];
        this.armorItems = DefaultedList.ofSize(4, ItemStack.EMPTY);
        this.handItems = DefaultedList.ofSize(2, ItemStack.EMPTY);
    }

    // TODO nbt save/load
    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        /*
         * this.setType(nbt.getInt("Type"));
         * this.setCircuits(nbt.getCompound("Circuits"));
         * this.setRoom(nbt.getCompound("InternalScheme"));
         */
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        /* nbt.putInt("Type", this.getType()); */

    }

    @Override
    public void equipStack(EquipmentSlot slot, ItemStack stack) {
        this.processEquippedStack(stack);
        switch (slot.getType()) {
            case HAND: {
                this.onEquipStack(slot, this.handItems.set(slot.getEntitySlotId(), stack), stack);
                break;
            }
            case ARMOR: {
                this.onEquipStack(slot, this.armorItems.set(slot.getEntitySlotId(), stack), stack);
            }
        }
    }

    @Override
    public Iterable<ItemStack> getArmorItems() {
        return this.armorItems;
    }

    @Override
    public ItemStack getEquippedStack(EquipmentSlot slot) {
        switch (slot.getType()) {
            case HAND: {
                return this.handItems.get(slot.getEntitySlotId());
            }
            case ARMOR: {
                return this.armorItems.get(slot.getEntitySlotId());
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public Arm getMainArm() {
        return this.isLeftHanded() ? Arm.LEFT : Arm.RIGHT;
    }

    private boolean isLeftHanded() {
        return ((Byte) this.dataTracker.get(MOB_FLAGS) & 2) != 0;
    }

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(MOB_FLAGS, (byte) 0);
    }

    static {
        MOB_FLAGS = DataTracker.registerData(Tardis.class, TrackedDataHandlerRegistry.BYTE);
    }

    @Override
    public void registerControllers(ControllerRegistrar controllers) {
        // TODO controller goes here
        controllers
                .add(new AnimationController<>(this, "left", this::commonAnimController)
                        .triggerableAnim("left_open", LEFT_OPEN)
                        .triggerableAnim("left_close", LEFT_CLOSE))
                .add(new AnimationController<>(this, "right", this::commonAnimController)
                        .triggerableAnim("right_open", RIGHT_OPEN)
                        .triggerableAnim("right_close", RIGHT_CLOSE));
        
        
    }

    //TODO test with normal interraction. If too fast, find another way to check for timed out animation
    //FIXME MAKE IT WORK!
    protected <E extends Tardis> PlayState commonAnimController(final AnimationState<E> event) {
        if (event.getController().getAnimationState().equals(State.TRANSITIONING)) {
            event.setControllerSpeed(10f);
        }
        if (leftOpen) {
            this.triggerAnim("left", "left_open");
        }
        if (rightOpen) {
            this.triggerAnim("right", "right_open");
        }
        if (event.getController().getAnimationState().equals(State.TRANSITIONING)) {
            event.setControllerSpeed(1f);
        }
        if (event.getController().getAnimationState().equals(State.PAUSED)) {
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }
}
