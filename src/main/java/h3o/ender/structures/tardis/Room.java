package h3o.ender.structures.tardis;

import java.util.HashMap;
import java.util.List;

import net.minecraft.util.math.BlockPos;

public class Room {
    private int id;
    private int size;
    private int orientation;
    private int vId;
    private Name name;

    

    public enum Name {
        DEFAULT_CONSOLE_ROOM;

        public int getSize() {
            return switch(this) {
                case DEFAULT_CONSOLE_ROOM -> 1;
            };
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



    public int getOrientation() {
        return orientation;
    }



    public int getVId() {
        return vId;
    }



    public static String getStructName(Name name) {
        return switch (name) {
            case DEFAULT_CONSOLE_ROOM -> "dwmod:tardis/default/console_room";
        };
    }

    public HashMap<String,BlockPos> getFeatures() {
        HashMap<String, BlockPos> features = new HashMap<>();
        switch (this.name) {
            case DEFAULT_CONSOLE_ROOM -> {
                features.put("RealWorldInterface", new BlockPos(9, 2, 13));
            }
        }
        return features;
    }



    public static Room getById(List<Room> internalScheme, Integer id) {
        for (Room room : internalScheme) {
            if (room.getId().equals(id)) {
                return room;
            }
        }
        return null;
    }
}
