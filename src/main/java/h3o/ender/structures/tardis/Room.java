package h3o.ender.structures.tardis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import h3o.ender.items.RegisterItems;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;

public class Room {
    public static final List<Name> MAINTENANCE = new ArrayList<>();
    private int id;
    private int size;
    private int orientation;
    private int vId;
    private Name name;

    public enum Name {
        DEFAULT_CONSOLE_ROOM, MAINTENANCE_ENTRANCE;

        public int getSize() {
            return switch (this) {
                case DEFAULT_CONSOLE_ROOM -> 1;
                // TODO
                case MAINTENANCE_ENTRANCE -> 1;
            };
        }

        public String getStructName() {
            return switch (this) {
                case DEFAULT_CONSOLE_ROOM -> "dwmod:tardis/default/console_room";
                case MAINTENANCE_ENTRANCE -> "dwmod:tardis/maintenance/entrance";
            };
        }

        public Item getIcon() {
            return switch (this) {
                case DEFAULT_CONSOLE_ROOM -> null;
                case MAINTENANCE_ENTRANCE -> RegisterItems.MAINTENANCE_ACCESS;
            };
        }

        public HashMap<String, BlockPos> getFeatures() {
            HashMap<String, BlockPos> features = new HashMap<>();
            switch (this) {
                case DEFAULT_CONSOLE_ROOM -> {
                    features.put("RealWorldInterface", new BlockPos(9, 2, 13));
                    features.put("EngineAccess", new BlockPos(14, 2, 8));
                }
                case MAINTENANCE_ENTRANCE -> {
                    // TODO
                }
            }
            return features;
        }
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
