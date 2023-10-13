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
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.util.GeckoLibUtil;

public class Tardis extends LivingEntity implements GeoEntity {
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    private static final TrackedData<Byte> MOB_FLAGS;
    private int type;
    private ArrayList<Circuit> circuits;
    private HashMap<Integer, Room> internalScheme;

    protected final float[] handDropChances;
    private final DefaultedList<ItemStack> armorItems;
    private final DefaultedList<ItemStack> handItems;

    protected Tardis(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
        this.handDropChances = new float[2];
        this.armorItems = DefaultedList.ofSize(4, ItemStack.EMPTY);
        this.handItems = DefaultedList.ofSize(2, ItemStack.EMPTY);
    }

    //TODO nbt save/load
    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        /* this.setType(nbt.getInt("Type"));
        this.setCircuits(nbt.getCompound("Circuits"));
        this.setRoom(nbt.getCompound("InternalScheme")); */
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        /* nbt.putInt("Type", this.getType()); */
        
    }


    @Override
    public void equipStack(EquipmentSlot slot, ItemStack stack) {
        this.processEquippedStack(stack);
        switch (slot.getType().ordinal()) {
            case 1 -> this.handDropChances[slot.getEntitySlotId()] = 2.0F;
            case 2 -> this.handDropChances[slot.getEntitySlotId()] = 2.0F;
        }
    }

    @Override
    public Iterable<ItemStack> getArmorItems() {
        return this.armorItems;
    }

    @Override
    public ItemStack getEquippedStack(EquipmentSlot slot) {
        switch (slot.getType().ordinal()) {
            case 1:
                return (ItemStack)this.handItems.get(slot.getEntitySlotId());
            case 2:
                return (ItemStack)this.armorItems.get(slot.getEntitySlotId());
            default:
                return ItemStack.EMPTY;
        }
    }

    @Override
    public Arm getMainArm() {
        return this.isLeftHanded() ? Arm.LEFT : Arm.RIGHT;
    }

    private boolean isLeftHanded() {
        return ((Byte)this.dataTracker.get(MOB_FLAGS) & 2) != 0;
    }

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(MOB_FLAGS, (byte)0);
    }

    static {
        MOB_FLAGS = DataTracker.registerData(Tardis.class, TrackedDataHandlerRegistry.BYTE);
    }

    @Override
    public void registerControllers(ControllerRegistrar controllers) {
        //TODO animationController here
        return;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }
}
