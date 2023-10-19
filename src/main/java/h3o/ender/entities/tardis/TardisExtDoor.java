package h3o.ender.entities.tardis;

import java.util.List;

import h3o.ender.entities.Tardis;
import h3o.ender.structures.tardis.DimensionalStorageHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Arm;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class TardisExtDoor extends LivingEntity {

    private static final TrackedData<Byte> MOB_FLAGS = DataTracker.registerData(TardisExtDoor.class,
            TrackedDataHandlerRegistry.BYTE);
    protected final float[] handDropChances;
    private final DefaultedList<ItemStack> armorItems;
    private final DefaultedList<ItemStack> handItems;

    

    protected TardisExtDoor(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
        this.handDropChances = new float[2];
        this.armorItems = DefaultedList.ofSize(4, ItemStack.EMPTY);
        this.handItems = DefaultedList.ofSize(2, ItemStack.EMPTY);
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

    protected Tardis initTardis() {
        if (!getWorld().isClient) {
            for (ServerWorld world : getServer().getWorlds()) {
                List<Tardis> trds = world.getEntitiesByClass(Tardis.class,
                        Box.of(new Vec3d(0, 0, 0), World.HORIZONTAL_LIMIT * 2, World.MAX_Y - World.MIN_Y,
                                World.HORIZONTAL_LIMIT * 2),
                        (entities) -> ((Tardis) entities).getIndex() == DimensionalStorageHelper
                                .getIndex(getBlockPos()));
                if (trds.size() != 0) {
                    return trds.get(0);
                }
            }
        }
        return null;
    }
}
