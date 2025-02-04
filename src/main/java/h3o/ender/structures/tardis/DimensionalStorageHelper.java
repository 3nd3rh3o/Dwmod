package h3o.ender.structures.tardis;

import java.util.ArrayList;
import java.util.List;

import org.joml.Math;

import h3o.ender.entities.Tardis;
import h3o.ender.entities.TardisInternalPortal;
import h3o.ender.entities.tardis.TardisExtDoor;
import h3o.ender.structures.tardis.Room.Name;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import qouteall.q_misc_util.my_util.DQuaternion;

public class DimensionalStorageHelper {
    // Tardis internal slots
    private static final int maxTx = 18;
    private static final int maxTz = 18;
    // Tardis' room slots
    private static final int max_x = 96;
    private static final int max_y = 8;
    private static final int max_z = 96;

    public static BlockPos getBasePosFromTardisIndex(int index) {
        return new BlockPos(
                (index % maxTx) * 2536,
                64,
                (Math.round(index / maxTx) % maxTz) * 2536);
    }

    public static BlockPos getRoomPosFromRoomIndex(int index) {
        return new BlockPos(
                (index % max_x) * 16,
                Math.round(index / (max_x * max_z)) * 16,
                Math.round(index / max_x) % max_z * 16);
    }

    public static void loadStructure(ServerWorld world, BlockPos pos, String structName, BlockRotation rot) {
        loadStructure(world, pos, structName, new StructurePlacementData().setMirror(BlockMirror.NONE)
                .setRotation(rot)
                .setIgnoreEntities(false));
    }

    private static void loadStructure(ServerWorld level, BlockPos bPos, String structureName,
            StructurePlacementData settings) {
        StructureTemplateManager structureTemplateManager = level.getStructureTemplateManager();
        StructureTemplate struct = structureTemplateManager.getTemplate(Identifier.tryParse(structureName)).get();
        struct.place(level, bPos, bPos, settings, Random.create(Util.getEpochTimeMs()), 2);
    }

    public static int getValidPos(int size, List<Room> intSh) {
        for (int n = 0; n < 73727; n++) {
            List<Integer> mask = new ArrayList<>();
            boolean valid = true;
            if (roomIdToX(n) + size < max_x && roomIdToY(n) + size < max_y && roomIdToZ(n) + size < max_z) {
                for (int y = 0; y < size; y++) {
                    for (int z = 0; z < size; z++) {
                        for (int x = 0; x < size; x++) {
                            int computedId = x_p(y_p(z_p(n, z), y), x);
                            mask.add(computedId);
                            if (x == 0) {
                                if (noOverLap(0, computedId, intSh)) {
                                    break;
                                }
                            }
                            if (y == 0) {
                                if (noOverLap(1, computedId, intSh)) {
                                    break;
                                }
                            }
                            if (z == 0) {
                                if (noOverLap(2, computedId, intSh)) {
                                    break;
                                }
                            }
                            if (!valid) {
                                break;
                            }
                        }
                        if (!valid) {
                            break;
                        }
                    }
                    if (!valid) {
                        break;
                    }
                }
                if (valid) {
                    for (Integer val : mask) {
                        valid = !contain(intSh, val);
                        if (!valid) {
                            break;
                        }
                    }
                    if (valid) {
                        return n;
                    }
                }
            }
        }
        return -1;
    }

    public static boolean contain(List<Room> intSh, Integer id) {
        for (Room room : intSh) {
            if (room.getId() == id) {
                return true;
            }
        }
        return false;
    }

    private static int getSize(List<Room> intSh, Integer id) {
        for (Room room : intSh) {
            if (room.getId() == id) {
                return room.getSize();
            }
        }
        return -1;
    }

    private static boolean noOverLap(int axis, int index, List<Room> intSh) {
        switch (axis) {
            default:
            case 0:
                for (int x = 1; x < 8; x++) {
                    if (x_m(index, x) < 0) {
                        return true;
                    }
                    if (contain(intSh, x_m(index, x)) && getSize(intSh, x_m(index, x)) > x) {
                        return false;
                    }
                }
                return true;
            case 1:
                for (int y = 1; y < 8; y++) {
                    if (y_m(index, y) < 0) {
                        return true;
                    }
                    if (contain(intSh, y_m(index, y)) && getSize(intSh, y_m(index, y)) > y) {
                        return false;
                    }
                }
                return true;
            case 2:
                for (int z = 1; z < 8; z++) {
                    if (z_m(index, z) < 0) {
                        return true;
                    }
                    if (contain(intSh, z_m(index, z)) && getSize(intSh, z_m(index, z)) > z) {
                        return false;
                    }
                }
                return true;
        }

    }

    public static int roomIdToX(int id) {
        return id % max_x;
    }

    public static int roomIdToY(int id) {
        return Math.round(id / (max_x * max_z));
    }

    public static int roomIdToZ(int id) {
        return Math.round(id / max_x) * max_z;
    }

    // id move relative
    public static int x_p(int x, int time) {
        if (time == 0) {
            return x;
        } else {
            return x_p(x + 1, time - 1);
        }
    }

    public static int x_m(int x, int time) {
        if (time == 0) {
            return x;
        } else {
            return x_m(x - 1, time - 1);
        }
    }

    public static int y_p(int y, int time) {
        if (time == 0) {
            return y;
        } else {
            return y_p(y + max_x * max_z, time - 1);
        }
    }

    public static int y_m(int y, int time) {
        if (time == 0) {
            return y;
        } else {
            return y_m(y - max_x * max_z, time - 1);
        }
    }

    public static int z_p(int z, int time) {
        if (time == 0) {
            return z;
        } else {
            return z_p(z + max_x, time - 1);
        }
    }

    public static int z_m(int z, int time) {
        if (time == 0) {
            return z;
        } else {
            return z_m(z - max_x, time - 1);
        }
    }

    public static void add(Name name, BlockRotation rot, int index, ServerWorld vortex,
            List<Room> intSh, Tardis tardis) {
        BlockPos basePos = getBasePosFromTardisIndex(index);
        int id = getValidPos(name.getSize(), intSh);
        summonPortals(vortex, name, basePos.add(getRoomPosFromRoomIndex(id)), tardis);
        loadStructure(vortex, basePos.add(getRoomPosFromRoomIndex(id)), Room.getStructName(name), rot);
    }

    private static void summonPortals(ServerWorld vortex, Name name, BlockPos origin, Tardis tardis) {
        switch (name) {
            case DEFAULT_CONSOLE_ROOM -> {
                TardisInternalPortal portal;
                portal = TardisInternalPortal.entityType.create(vortex);
                portal.setTardis(tardis);
                portal.setOriginPos(origin.toCenterPos().add(9, 2.5, 13));
                portal.setDestinationDimension(tardis.getWorld().getRegistryKey());
                portal.setDestination(tardis.getPos().add(new Vec3d(0, 1, 0.5)
                        .rotateY((float) (-tardis.getDataTracker().get(Tardis.EXOSHELL_ROT) * Math.PI / 180f))));
                portal.setRotationTransformation(
                        DQuaternion.fromEulerAngle(new Vec3d(0, -tardis.getDataTracker().get(Tardis.EXOSHELL_ROT), 0)));
                portal.setOrientationAndSize(new Vec3d(-1, 0, 0), new Vec3d(0, 1, 0), 1, 2);
                portal.getWorld().spawnEntity(portal);
            }
        }
    }

    public static int getIndex(BlockPos pos) {
        int x = pos.getX();
        int z = pos.getZ();
        x = Math.round(x / 2536);
        z = Math.round(z / 2536);
        return x + (z * maxTx);
    }

    public static void removeRoom(int tardisIndex, int roomIndex, int roomSize, ServerWorld world) {
        BlockPos pos = getBasePosFromTardisIndex(tardisIndex).add(getRoomPosFromRoomIndex(roomIndex));
        for (int x = 0; x < roomSize; x++) {
            for (int y = 0; y < roomSize; y++) {
                for (int z = 0; z < roomSize; z++) {
                    world.getEntitiesByClass(TardisInternalPortal.class,
                            new Box(pos.getX() + 16 * x, pos.getY() + 16 * y, pos.getZ() + 16 * z,
                                    pos.getX() + 16 * x + 16, pos.getY() + 16 * y + 16, pos.getZ() + 16 * z + 16),
                            entity -> true).forEach(ent -> {
                                ent.kill();
                                ent.reloadAndSyncToClient();
                            });
                    world.getEntitiesByClass(TardisExtDoor.class,
                            new Box(pos.getX() + 16 * x, pos.getY() + 16 * y, pos.getZ() + 16 * z,
                                    pos.getX() + 16 * x + 16, pos.getY() + 16 * y + 16, pos.getZ() + 16 * z + 16),
                            entity -> true).forEach(ent -> {
                                ent.kill();
                            });
                }
            }
        }
    }

    public static int getIndex(Vec3d originPos) {
        int x = (int) Math.round(originPos.getX());
        int y = (int) Math.round(originPos.getY());
        int z = (int) Math.round(originPos.getZ());
        return getIndex(new BlockPos(x, y, z));
    }

    public static Tardis getTardis(MinecraftServer server, BlockPos pos) {
        for (ServerWorld world : server.getWorlds()) {
            List<Tardis> trds = world.getEntitiesByClass(Tardis.class,
                    Box.of(new Vec3d(0, 0, 0), World.HORIZONTAL_LIMIT * 2, World.MAX_Y - World.MIN_Y,
                            World.HORIZONTAL_LIMIT * 2),
                    (entities) -> ((Tardis) entities).getIndex() == DimensionalStorageHelper
                            .getIndex(pos));
            if (trds.size() != 0) {
                return trds.get(0);
            }
        }
        return null;
    }

    public static Tardis getTardis(MinecraftClient client, BlockPos pos) {
        
            List<Tardis> trds = client.world.getEntitiesByClass(Tardis.class,
                    Box.of(new Vec3d(0, 0, 0), World.HORIZONTAL_LIMIT * 2, World.MAX_Y - World.MIN_Y,
                            World.HORIZONTAL_LIMIT * 2),
                    (entities) -> ((Tardis) entities).getIndex() == DimensionalStorageHelper
                            .getIndex(pos));
            if (trds.size() != 0) {
                return trds.get(0);
            }
        
        return null;
    }
}
