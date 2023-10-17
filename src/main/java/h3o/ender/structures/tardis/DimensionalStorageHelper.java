package h3o.ender.structures.tardis;

import java.util.ArrayList;
import java.util.List;

import h3o.ender.structures.tardis.Room.Name;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import qouteall.imm_ptl.core.portal.Mirror;

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
            List<Room> intSh) {
        BlockPos basePos = getBasePosFromTardisIndex(index);
        int id = getValidPos(name.getSize(), intSh);
        loadStructure(vortex, basePos.add(getRoomPosFromRoomIndex(id)), Room.getStructName(name), rot);
    }
}
