package h3o.ender.structures.tardis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import h3o.ender.items.RegisterItems;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Room {
    public static final List<Name> MAINTENANCE = new ArrayList<>();
    private int id;
    private int size;
    private int orientation;
    private int vId;
    private Name name;

    public enum Features {
        EngineAccess,
        RealWorldInterface,
        NorthCorrLink,
        EastCorrLink,
        SouthCorrLink,
        WestCorrLink;

        public Features rotated(BlockRotation rot){
            if (rot.ordinal() == 0) {
                return this;
            } else {
                return switch (this) {
                    case NorthCorrLink -> EastCorrLink.rotated(BlockRotation.values()[(rot.ordinal()-1)]);
                    case EastCorrLink -> SouthCorrLink.rotated(BlockRotation.values()[(rot.ordinal()-1)]);
                    case SouthCorrLink -> WestCorrLink.rotated(BlockRotation.values()[rot.ordinal()-1]);
                    case WestCorrLink -> NorthCorrLink.rotated(BlockRotation.values()[rot.ordinal()-1]);
                    default -> throw new IllegalArgumentException("Unexpected feature: " + this + ". Should be a CorrLink.");
                };
            }
        }
        public Features complementary() {
            return switch (this) {
                case NorthCorrLink -> SouthCorrLink;
                case EastCorrLink -> WestCorrLink;
                case SouthCorrLink -> NorthCorrLink;
                case WestCorrLink -> EastCorrLink;
                default -> throw new IllegalArgumentException("Unexpected feature: " + this + ". Should be a CorrLink.");
            };
        }

        public boolean isCoorLink() {
            switch (this) {
                case NorthCorrLink:
                case SouthCorrLink:
                case EastCorrLink:
                case WestCorrLink:
                    return true;
                default:
                    return false;
            }
        }
        public int getMatchingVIDFor(int vId) {
            return switch (this) {
                case NorthCorrLink -> getZmVID(vId);
                case SouthCorrLink -> getZpVID(vId);
                case EastCorrLink -> getXpVID(vId);
                case WestCorrLink -> getXmVID(vId);
                case EngineAccess -> throw new UnsupportedOperationException("Unimplemented case: " + this);
                case RealWorldInterface -> throw new UnsupportedOperationException("Unimplemented case: " + this);
                default -> throw new IllegalArgumentException("Unexpected feature: " + this + ". Should be a CorrLink.");
            };
        }
        public double getAngle() {
            return switch(this) {
                case NorthCorrLink -> 0;
                case EastCorrLink -> 90;
                case SouthCorrLink -> 180;
                case WestCorrLink -> 270;
                default -> throw new IllegalArgumentException("Unexpected feature: " + this + ". Should be a CorrLink.");
            };
        }
    }


    public enum Name {
        DEFAULT_CONSOLE_ROOM, MAINTENANCE_ENTRANCE;

        public int getSize() {
            return switch (this) {
                case DEFAULT_CONSOLE_ROOM -> 1;
                case MAINTENANCE_ENTRANCE -> 2;
            };
        }

        public HashMap<String, BlockPos> getStructName() {
            HashMap<String, BlockPos> structNames = new HashMap<>();
            switch (this) {
                case DEFAULT_CONSOLE_ROOM -> structNames.put("dwmod:tardis/default/console_room", new BlockPos(0, 0, 0));
                case MAINTENANCE_ENTRANCE -> {
                    structNames.put("dwmod:tardis/maintenance/entrance", new BlockPos(0, 0, 0));
                    structNames.put("dwmod:tardis/maintenance/entrance1", new BlockPos(0, 0, 16));
                }
            };
            return structNames;

        }

        public Item getIcon() {
            return switch (this) {
                case DEFAULT_CONSOLE_ROOM -> null;
                case MAINTENANCE_ENTRANCE -> RegisterItems.MAINTENANCE_ACCESS;
            };
        }

        public HashMap<Features, Vec3d> getFeatures() {
            HashMap<Features, Vec3d> features = new HashMap<>();
            switch (this) {
                case DEFAULT_CONSOLE_ROOM -> {
                    features.put(Features.RealWorldInterface, new Vec3d(9, 2, 13));
                    features.put(Features.EngineAccess, new Vec3d(13.5, 1.5, 7.5));
                }
                case MAINTENANCE_ENTRANCE -> {
                     features.put(Features.EngineAccess, new Vec3d(7.0,0.5,5.0));
                     features.put(Features.SouthCorrLink, new Vec3d(7.5, 1, 11.5));
                }
            }
            return features;
        }
    }

    private static int getZpVID(int vId) {
        return vId + 5 * 5;
    }

    private static int getZmVID(int vId) {
        return vId - 5 * 5;
    }

    private static int getXmVID(int vId) {
        return vId - 1;
    }

    private static int getXpVID(int vId) {
        return vId + 1;
    }

    public Room(int id, int size, int orientation, int vId, Name name) {
        this.id = id;
        this.size = size;
        this.orientation = orientation;
        this.vId = vId;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public Name getName() {
        return this.name;
    }

    public int getSize() {
        return size;
    }

    public BlockRotation getOrientation() {
        return switch (orientation) {
            case 0 -> BlockRotation.NONE;
            case 1 -> BlockRotation.CLOCKWISE_90;
            case 2 -> BlockRotation.CLOCKWISE_180;
            case 3 -> BlockRotation.COUNTERCLOCKWISE_90;
            default -> throw new IllegalArgumentException("Unexpected value: " + orientation);
        };
    }

    public int getVId() {
        return vId;
    }

    public static Room getById(List<Room> internalScheme, Integer id) {
        for (Room room : internalScheme) {
            if (room.getId().equals(id)) {
                return room;
            }
        }
        return null;
    }

    public static NbtCompound toNBT(List<Room> intSh) {
        NbtCompound rooms = new NbtCompound();
        for (Room room : intSh) {
            NbtCompound tupple = new NbtCompound();

            tupple.putString("Name", room.getName().toString());
            tupple.putInt("Id", room.getId());
            tupple.putInt("Size", room.getSize());
            tupple.putInt("Rot", room.getOrientation().ordinal());
            tupple.putInt("vId", room.getVId());
            rooms.put(String.valueOf(intSh.indexOf(room)), tupple);
        }
        return rooms;
    }

    public static List<Room> fromNBT(NbtCompound nbt) {
        List<Room> internalScheme = new ArrayList<>();
        nbt.getKeys().forEach((key) -> {
            NbtCompound tupple = nbt.getCompound(key);
            int id = tupple.getInt("Id");
            int size = tupple.getInt("Size");
            int rot = tupple.getInt("Rot");
            int vId = tupple.getInt("vId");
            Room.Name name = Room.Name.valueOf(tupple.getString("Name"));
            internalScheme.add(new Room(id, size, rot, vId, name));
        });
        return internalScheme;
    }

    public static NbtCompound addRoom(NbtCompound intSh, Room room) {
        List<Room> internalScheme = fromNBT(intSh);
        internalScheme.add(room);
        return toNBT(internalScheme);
    }
    static {
        MAINTENANCE.add(Name.MAINTENANCE_ENTRANCE);
    }
}
