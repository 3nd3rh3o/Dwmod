package h3o.ender.entities;

import java.util.ArrayList;
import java.util.List;

import h3o.ender.blocks.RegisterBlocks;
import h3o.ender.blocks.tardis.TardisDefaultHitbox;
import h3o.ender.components.Circuit;
import h3o.ender.components.Circuit.LOCATION;
import h3o.ender.dimensions.RegisterDimensions;
import h3o.ender.structures.tardis.DimensionalStorageHelper;
import h3o.ender.structures.tardis.Room;
import h3o.ender.structures.tardis.Room.Features;
import h3o.ender.structures.tardis.Room.Name;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Arm;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import qouteall.q_misc_util.my_util.DQuaternion;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.util.GeckoLibUtil;

public class Tardis extends LivingEntity implements GeoEntity {
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    private final List<BlockEntity> depEntities = new ArrayList<>();

    private static final RawAnimation LEFT_OPEN = RawAnimation.begin()
            .thenPlayAndHold("animation.exoshell.default.left.open");
    private static final RawAnimation RIGHT_OPEN = RawAnimation.begin()
            .thenPlayAndHold("animation.exoshell.default.right.open");
    private static final RawAnimation LEFT_CLOSE = RawAnimation.begin()
            .thenPlayAndHold("animation.exoshell.default.left.close");
    private static final RawAnimation RIGHT_CLOSE = RawAnimation.begin()
            .thenPlayAndHold("animation.exoshell.default.right.close");

    private static final TrackedData<Byte> MOB_FLAGS = DataTracker.registerData(Tardis.class,
            TrackedDataHandlerRegistry.BYTE);
    private static final TrackedData<Boolean> DOORS = DataTracker.registerData(Tardis.class,
            TrackedDataHandlerRegistry.BOOLEAN);
    public static final TrackedData<Integer> EXOSHELL_ROT = DataTracker.registerData(Tardis.class,
            TrackedDataHandlerRegistry.INTEGER);
    public static final TrackedData<NbtCompound> CIRCUITS = DataTracker.registerData(Tardis.class,
            TrackedDataHandlerRegistry.NBT_COMPOUND);
    public static final TrackedData<NbtCompound> INTERNAL_SCHEME = DataTracker.registerData(Tardis.class,
            TrackedDataHandlerRegistry.NBT_COMPOUND);

    private boolean leftOpen = false;
    private boolean rightOpen = false;

    private int index = -1;
    private TardisPortal portal;
    private int activeConsId = 0;

    protected final float[] handDropChances;
    private final DefaultedList<ItemStack> armorItems;
    private final DefaultedList<ItemStack> handItems;
    private boolean engineAccess = false;

    protected Tardis(EntityType<? extends LivingEntity> entityType, World world) {
        super(RegisterEntities.TARDIS, world);
        this.handDropChances = new float[2];
        this.index = -1;
        this.armorItems = DefaultedList.ofSize(4, ItemStack.EMPTY);
        this.handItems = DefaultedList.ofSize(2, ItemStack.EMPTY);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        getDataTracker().set(EXOSHELL_ROT, Math.round(nbt.getList("Rotation", NbtElement.FLOAT_TYPE).getFloat(0)));
        byte[] temp = nbt.getByteArray("Doors");
        if (temp.length == 2) {
            leftOpen = temp[0] == 1 ? true : false;
            rightOpen = temp[1] == 1 ? true : false;
        }
        if (nbt.contains("Index")) {
            this.index = nbt.getInt("Index");
        }
        getDataTracker().set(INTERNAL_SCHEME, nbt.getCompound("InternalScheme"));
        getDataTracker().set(DOORS, getDoorsOpenned());
        getDataTracker().set(CIRCUITS, nbt.getCompound("Circuits"));
        this.engineAccess = nbt.getBoolean("EngineAccess");
        super.readCustomDataFromNbt(nbt);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.put("Rotation", this.toNbtList((float) getDataTracker().get(EXOSHELL_ROT), this.getPitch()));
        nbt.putByteArray("Doors", new byte[] { leftOpen ? (byte) 1 : (byte) 0, rightOpen ? (byte) 1 : (byte) 0 });
        if (this.index != -1) {
            nbt.putInt("Index", this.index);
        }
        nbt.put("InternalScheme", getDataTracker().get(INTERNAL_SCHEME));
        nbt.put("Circuits", getDataTracker().get(CIRCUITS));
        nbt.putBoolean("EngineAccess", engineAccess);
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
        if (!getWorld().isClient()) {
            if (getWorld().getEntitiesByClass(TardisPortal.class, this.getBoundingBox().expand(1), entity -> true)
                    .isEmpty()) {
                List<Room> internalScheme = Room.fromNBT(getDataTracker().get(INTERNAL_SCHEME));
                if (DimensionalStorageHelper.contain(internalScheme, activeConsId)) {
                    BlockPos dest = DimensionalStorageHelper.getRoomPosFromRoomIndex(activeConsId);
                    dest = dest.add(DimensionalStorageHelper.getBasePosFromTardisIndex(this.index));
                    Vec3d t = Room.getById(internalScheme, activeConsId).getName().getFeatures().get(Features.RealWorldInterface);
                    dest = dest.add(
                            new BlockPos(Math.round((float)t.getX()), Math.round((float)t.getY()), Math.round((float)t.getZ()))
                                    .rotate(Room.getById(internalScheme, activeConsId).getOrientation()));
                    this.portal = TardisPortal.entityType.create(this.getWorld());
                    this.portal.setOriginPos(this.getBlockPos().toCenterPos().add(0, 0.5, 0));
                    this.portal.setDestinationDimension(RegisterDimensions.VORTEX);
                    this.portal.setDestination(dest.toCenterPos().add(0, 0.5, -0.5));
                    this.portal.setRotationTransformation(
                            DQuaternion.fromEulerAngle(new Vec3d(0, -getDataTracker().get(EXOSHELL_ROT), 0)));
                    this.portal.setOrientationAndSize(
                            new Vec3d(1, 0, 0).rotateY((float) (-getDataTracker().get(EXOSHELL_ROT) * Math.PI / 180f)),
                            new Vec3d(0, 1, 0), 1, 2);
                    this.portal.getWorld().spawnEntity(portal);
                }
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
                                .with(TardisDefaultHitbox.UPPER, false)
                                .with(Properties.HORIZONTAL_FACING,
                                        Direction.fromRotation(getDataTracker().get(EXOSHELL_ROT))),
                        3);
                getWorld().setBlockState(getBlockPos().up(),
                        RegisterBlocks.TARDIS_DEFAULT_HITBOX.getDefaultState().with(TardisDefaultHitbox.UPPER, true)
                                .with(TardisDefaultHitbox.OPENNED, (leftOpen || rightOpen))
                                .with(Properties.HORIZONTAL_FACING,
                                        Direction.fromRotation(getDataTracker().get(EXOSHELL_ROT))),
                        3);
            }
        }
        if (this.getBodyYaw() / 90 != 0 || this.getBodyYaw() != getDataTracker().get(EXOSHELL_ROT)) {
            this.setBodyYaw(getDataTracker().get(EXOSHELL_ROT));
            this.setHeadYaw(getDataTracker().get(EXOSHELL_ROT));
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
                getDataTracker().set(DOORS, getDoorsOpenned());
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
        this.dataTracker.startTracking(DOORS, (boolean) false);
        this.dataTracker.startTracking(EXOSHELL_ROT, (int) 0);
        this.dataTracker.startTracking(CIRCUITS, new NbtCompound());
        this.dataTracker.startTracking(INTERNAL_SCHEME, new NbtCompound());
    }

    @Override
    public void registerControllers(ControllerRegistrar controllers) {

        controllers
                .add(new AnimationController<>(this, "left", state -> {
                    if (getDataTracker().get(DOORS)) {
                        return state.setAndContinue(LEFT_OPEN);
                    } else {
                        return state.setAndContinue(LEFT_CLOSE);
                    }
                }))
                .add(new AnimationController<>(this, "right", state -> {
                    if (getDataTracker().get(DOORS)) {
                        return state.setAndContinue(RIGHT_OPEN);
                    } else {
                        return state.setAndContinue(RIGHT_CLOSE);
                    }
                }));
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
        if (!getWorld().isClient()) {
            List<Room> internalScheme = new ArrayList<>();
            Room.Name name = Room.Name.DEFAULT_CONSOLE_ROOM;
            ServerWorld vortex = getWorld().getServer().getWorld(RegisterDimensions.VORTEX);
            List<Room> intSh = DimensionalStorageHelper.filterNormal(internalScheme);
            int id = DimensionalStorageHelper.getValidPos(name.getSize(), intSh);
            Room room = new Room(id, name.getSize(), 0, 0, name);
            DimensionalStorageHelper.addN(name, BlockRotation.NONE, this.index, vortex, internalScheme, this, room);
            internalScheme.add(room);
            name = Name.MAINTENANCE_ENTRANCE;
            intSh = DimensionalStorageHelper.filterEngine(internalScheme);
            id = DimensionalStorageHelper.getValidPos(name.getSize(), intSh);
            room = new Room(id * -1, name.getSize(), 0, 0, name);
            DimensionalStorageHelper.addE(name, BlockRotation.NONE, this.index, vortex, internalScheme, this, room);
            internalScheme.add(room);
            getDataTracker().set(INTERNAL_SCHEME, Room.toNBT(internalScheme));
            updateDependantBlocks();
        }
    }

    public void addRoomE(Name name, BlockRotation rot, int vId) {
        List<Room> internalScheme = Room.fromNBT(getDataTracker().get(INTERNAL_SCHEME));
        List<Room> intSh = DimensionalStorageHelper.filterEngine(internalScheme);
        int id = DimensionalStorageHelper.getValidPos(name.getSize(), intSh);
        ServerWorld vortex = getWorld().getServer().getWorld(RegisterDimensions.VORTEX);
        Room room = new Room(id * -1, name.getSize(), rot.ordinal(), vId, name);
        DimensionalStorageHelper.addE(name, rot, this.index, vortex, internalScheme, this, room);
        internalScheme.add(room);
        getDataTracker().set(INTERNAL_SCHEME, Room.toNBT(internalScheme));
        updateDependantBlocks();
    }

    public void removeRoomE(int vid) {
        List<Room> internalScheme = Room.fromNBT(getDataTracker().get(INTERNAL_SCHEME));
        for (Room room : internalScheme) {
            if (room.getVId() == vid) {
                DimensionalStorageHelper.unlinkPortalE(getServer().getWorld(RegisterDimensions.VORTEX), room.getName(), this, room);
                internalScheme.remove(room);
                DimensionalStorageHelper.removeRoomE(index, room.getId() * -1, room.getSize(),
                        getServer().getWorld(RegisterDimensions.VORTEX));
                break;
            }
        }
        getDataTracker().set(INTERNAL_SCHEME, Room.toNBT(internalScheme));
        updateDependantBlocks();
    }

    public void purgeIntPortals() {
        List<Room> internalScheme = Room.fromNBT(getDataTracker().get(INTERNAL_SCHEME));
        internalScheme.forEach(room -> {
            DimensionalStorageHelper.removeRoom(index, room.getId(), room.getSize(),
                    getServer().getWorld(RegisterDimensions.VORTEX));
        });
    }

    public void setRotation(float asRotation) {
        this.getDataTracker().set(EXOSHELL_ROT, Math.round(asRotation));
    }

    public void registerBlock(BlockEntity ent) {
        depEntities.add(ent);
    }

    public void unRegisterBlock(BlockEntity ent) {
        depEntities.remove(ent);
    }

    public void addCircuit(Circuit circuit) {
        NbtCompound nbt = getDataTracker().get(CIRCUITS);
        if (nbt == null) {
            nbt = new NbtCompound();
        }
        List<Circuit> circuits = Circuit.readFromNbt(nbt);
        circuits.add(circuit);
        nbt = Circuit.writeNbt(circuits);
        getDataTracker().set(CIRCUITS, nbt);
        updateDependantBlocks();
    }

    private void updateDependantBlocks() {
        depEntities.forEach((bEnt) -> {
            bEnt.markDirty();
            ((ServerWorld) bEnt.getWorld()).getChunkManager().markForUpdate(bEnt.getPos());
            ;
        });
    }

    public void removeRotor(ServerPlayerEntity player, LOCATION loc) {
        NbtCompound nbt = getDataTracker().get(CIRCUITS);
        if (nbt == null) {
            nbt = new NbtCompound();
        }
        List<Circuit> circuits = Circuit.readFromNbt(nbt);
        for (Circuit circuit : circuits) {
            if (Circuit.isRotor(circuit.getName())) {
                ItemStack item = Circuit.getItemForName(circuit.getName());
                if (item != null && !player.giveItemStack(item)) {
                    player.getWorld().spawnEntity(
                            new ItemEntity(player.getWorld(), player.getX(), player.getY(), player.getZ(), item));
                }
                circuits.remove(circuit);
                break;
            }
        }
        nbt = Circuit.writeNbt(circuits);
        getDataTracker().set(CIRCUITS, nbt);
        updateDependantBlocks();
    }

    public void popCircuit(ServerPlayerEntity player, LOCATION loc) {
        NbtCompound nbt = getDataTracker().get(CIRCUITS);
        if (nbt == null) {
            nbt = new NbtCompound();
        }
        List<Circuit> circuits = Circuit.readFromNbt(nbt);
        if (Circuit.containsRotor(nbt, loc)) {
            return;
        }
        for (Circuit circuit : circuits) {
            if (!Circuit.isRotor(circuit.getName()) && circuit.getLoc().equals(Circuit.locToStr(loc))) {
                ItemStack item = Circuit.getItemForName(circuit.getName());
                if (item != null && !player.giveItemStack(item)) {
                    player.getWorld().spawnEntity(
                            new ItemEntity(player.getWorld(), player.getX(), player.getY(), player.getZ(), item));
                }
                circuits.remove(circuit);
                break;
            }
        }
        nbt = Circuit.writeNbt(circuits);
        getDataTracker().set(CIRCUITS, nbt);
        updateDependantBlocks();
    }

    public void switchEngineAccess() {
        engineAccess = !engineAccess;
        List<Room> internalScheme = Room.fromNBT(getDataTracker().get(INTERNAL_SCHEME));
        List<Room> maintenanceAccess = new ArrayList<>();
        maintenanceAccess.add(internalScheme.get(activeConsId));
        internalScheme.forEach(room -> {
            if (room.getName().equals(Name.MAINTENANCE_ENTRANCE)) {
                maintenanceAccess.add(room);
            }
        });
        maintenanceAccess.forEach(room -> {
            Vec3d vpos = (DimensionalStorageHelper.getFeaturePos(Features.EngineAccess, index, room));
            BlockPos pos = new BlockPos((int)vpos.getX(), (int)vpos.getY(), (int)vpos.getZ());
            if (engineAccess) {
                getServer().getWorld(RegisterDimensions.VORTEX).setBlockState(pos, Blocks.AIR.getDefaultState(),
                        Block.NOTIFY_ALL);
                getServer().getWorld(RegisterDimensions.VORTEX).setBlockState(pos.up(), Blocks.AIR.getDefaultState(),
                        Block.NOTIFY_ALL);
                if (Room.MAINTENANCE.contains(room.getName())) {
                    DimensionalStorageHelper.summonPortal(getServer().getWorld(RegisterDimensions.VORTEX), vpos.add(0, 0.5, 0), DimensionalStorageHelper.getFeaturePos(Features.EngineAccess, index, internalScheme.get(activeConsId)).add(0.5,0.5,0.5), room.getOrientation().ordinal()*-90 - 90, internalScheme.get(activeConsId).getOrientation().ordinal() * -90 + room.getOrientation().ordinal()*-90 + 180, 1, 2);
                } else {
                    if (room.getName() == Name.DEFAULT_CONSOLE_ROOM) {
                        DimensionalStorageHelper.summonPortal(getServer().getWorld(RegisterDimensions.VORTEX), vpos.add(0.5,0.5,0.5), DimensionalStorageHelper.getFeaturePos(Features.EngineAccess, index, Room.getById(DimensionalStorageHelper.filterEngine(internalScheme), 0)).add(0, 0.5, 0), internalScheme.get(activeConsId).getOrientation().ordinal() * -90 - 90, room.getOrientation().ordinal()*90 + 180, 1, 2);
                    }
                }
            } else {
                getServer().getWorld(RegisterDimensions.VORTEX).setBlockState(pos,
                        RegisterBlocks.TARDIS_DEFAULT_WALL_LAMP.getDefaultState(),
                        Block.NOTIFY_ALL);
                getServer().getWorld(RegisterDimensions.VORTEX).setBlockState(pos.up(),
                        RegisterBlocks.TARDIS_DEFAULT_WALL_LAMP.getDefaultState(),
                        Block.NOTIFY_ALL);
                DimensionalStorageHelper.removePortal(getServer().getWorld(RegisterDimensions.VORTEX), vpos);
            }
        });
    }
}
