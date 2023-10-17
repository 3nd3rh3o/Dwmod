package h3o.ender.entities;

import java.util.ArrayList;
import java.util.HashMap;

import h3o.ender.blocks.RegisterBlocks;
import h3o.ender.blocks.tardis.TardisDefaultHitbox;
import h3o.ender.components.Circuit;
import h3o.ender.dimensions.RegisterDimensions;
import h3o.ender.structures.tardis.DimensionalStorageHelper;
import h3o.ender.structures.tardis.Room;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Vec3d;
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

//TODO MAKE A BLOCK FOR THE HIT BOX!!!! hook the entity interract and remove it's AABB!!!
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

    private static final TrackedData<Byte> MOB_FLAGS = DataTracker.registerData(Tardis.class,
            TrackedDataHandlerRegistry.BYTE);;

    private boolean leftOpen = false;
    private boolean rightOpen = false;

    private int index = -1;
    private ArrayList<Circuit> circuits;
    private HashMap<Integer, Room> internalScheme;
    private TardisPortal portal;

    protected final float[] handDropChances;
    private final DefaultedList<ItemStack> armorItems;
    private final DefaultedList<ItemStack> handItems;

    protected Tardis(EntityType<? extends LivingEntity> entityType, World world) {
        super(RegisterEntities.TARDIS, world);
        this.handDropChances = new float[2];
        this.index = -1;
        this.armorItems = DefaultedList.ofSize(4, ItemStack.EMPTY);
        this.handItems = DefaultedList.ofSize(2, ItemStack.EMPTY);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        byte[] temp = nbt.getByteArray("Doors");
        if (temp.length == 2) {
            leftOpen = temp[0] == 1 ? true : false;
            rightOpen = temp[1] == 1 ? true : false;
        }
        if (nbt.contains("Index")) {
            this.index = nbt.getInt("Index");
        }
        super.readCustomDataFromNbt(nbt);

    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putByteArray("Doors", new byte[] { leftOpen ? (byte) 1 : (byte) 0, rightOpen ? (byte) 1 : (byte) 0 });
        if (this.index != -1) {
            nbt.putInt("Index", this.index);
        }
        super.writeCustomDataToNbt(nbt);
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
    public void tick() {
        super.tick();
        if (!getWorld().isClient) {
            if (getWorld().getEntitiesByClass(TardisPortal.class, this.getBoundingBox().expand(1), entity -> true)
                    .isEmpty()) {
                this.portal = TardisPortal.entityType.create(this.getWorld());
                this.portal.setOriginPos(this.getBlockPos().toCenterPos().add(0, 0.5, 0));
                this.portal.setDestinationDimension(World.NETHER);
                this.portal.setDestination(new Vec3d(0, 64, 0));
                this.portal.setOrientationAndSize(new Vec3d(1, 0, 0), new Vec3d(0, 1, 0), 1, 2);
                this.portal.getWorld().spawnEntity(portal);
            }
            if (!this.isOnGround() && this.getWorld().getBlockState(getBlockPos()).getBlock()
                    .equals(RegisterBlocks.TARDIS_DEFAULT_HITBOX.getDefaultState().getBlock())) {
                this.getWorld().setBlockState(getBlockPos().up(), Blocks.AIR.getDefaultState(), 3);
            }
            if (!this.isOnGround() && this.getWorld().getBlockState(getBlockPos().up()).getBlock()
                    .equals(RegisterBlocks.TARDIS_DEFAULT_HITBOX.getDefaultState().getBlock())) {
                this.getWorld().setBlockState(getBlockPos().up().up(), Blocks.AIR.getDefaultState(), 3);
            }
            if (this.isOnGround()) {
                getWorld().setBlockState(getBlockPos(),
                        RegisterBlocks.TARDIS_DEFAULT_HITBOX.getDefaultState()
                                .with(TardisDefaultHitbox.OPENNED, (leftOpen || rightOpen))
                                .with(TardisDefaultHitbox.UPPER, false),
                        3);
                getWorld().setBlockState(getBlockPos().up(),
                        RegisterBlocks.TARDIS_DEFAULT_HITBOX.getDefaultState().with(TardisDefaultHitbox.UPPER, true)
                                .with(TardisDefaultHitbox.OPENNED, (leftOpen || rightOpen)),
                        3);
            }
        }
    }

    @Override
    public Iterable<ItemStack> getArmorItems() {
        return this.armorItems;
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
    public ActionResult interactAt(PlayerEntity player, Vec3d hitPos, Hand hand) {
        if (hand == Hand.MAIN_HAND && !player.getWorld().isClient) {
            Vec3d relPos = hitPos.rotateY((float) (this.bodyYaw * Math.PI / 180f));
            if (relPos.getX() <= 0.48d && relPos.getX() >= -0.48d && relPos.getZ() >= -0.499d) {
                leftOpen = !leftOpen;
                rightOpen = leftOpen;
                this.updateAnim("right", rightOpen);
                this.updateAnim("left", leftOpen);
                return ActionResult.SUCCESS;
            }
        }

        return ActionResult.PASS;
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

    private void updateAnim(String controllerName, boolean value) {
        if (value) {
            this.triggerAnim(controllerName, controllerName + "_open");
        } else {
            this.triggerAnim(controllerName, controllerName + "_close");
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

    // FIXME anim not launched on world load
    @Override
    public void registerControllers(ControllerRegistrar controllers) {
        controllers
                .add(new AnimationController<>(this, "left", this::commonAnimController)
                        .triggerableAnim("left_open", LEFT_OPEN)
                        .triggerableAnim("left_close", LEFT_CLOSE))
                .add(new AnimationController<>(this, "right", this::commonAnimController)
                        .triggerableAnim("right_open", RIGHT_OPEN)
                        .triggerableAnim("right_close", RIGHT_CLOSE));

    }

    protected <E extends Tardis> PlayState commonAnimController(final AnimationState<E> event) {
        if (event.getController().getAnimationState().equals(State.TRANSITIONING)) {
            event.setControllerSpeed(10f);
        }
        if (event.getController().getName() == "left" && leftOpen) {
            this.triggerAnim("left", "left_open");
        }
        if (event.getController().getName() == "right" && rightOpen) {
            this.triggerAnim("right", "right_open");
        }
        if (event.getController().getAnimationState().equals(State.TRANSITIONING)) {
            event.setControllerSpeed(1f);
        }
        if (event.getController().getAnimationState().equals(State.TRANSITIONING)) {
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    @Override
    public boolean isCollidable() {
        return false;
    }

    @Override
    public boolean collidesWith(Entity other) {
        return false;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return this.isRemoved() || !damageSource.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }

    public boolean getDoorsOpenned() {
        return leftOpen || rightOpen;
    }

	public int getIndex() {
		return index;
	}

    public void setIndex(int i) {
        this.index = i;
    }

    public void structureInit() {
        //TODO gen struct here
        if (!getWorld().isClient) {
            ServerWorld vortex = getWorld().getServer().getWorld(RegisterDimensions.VORTEX);
            vortex.setBlockState(DimensionalStorageHelper.getBasePosFromTardisIndex(this.index), Blocks.STONE.getDefaultState(), 3);
        }
    }

}
