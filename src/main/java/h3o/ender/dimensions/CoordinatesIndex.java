package h3o.ender.dimensions;

import java.util.ArrayList;
import java.util.List;

import h3o.ender.items.RegisterItems;

public class CoordinatesIndex {
    private static final List<GalacticCoordinate> map = new ArrayList<>();

    public static List<GalacticCoordinate> getIndex() {
        return map;
    }


    static {
        //TODO add an item for the center of the universe
        map.add(new GalacticCoordinate(RegisterItems.ASTRALMAP_0, 0, 1));
        map.get(0).putChildren(new GalacticCoordinate(RegisterItems.ASTRALMAP_0_0, 90, 0.5));
        //temp
        map.get(0).putChildren(new GalacticCoordinate(RegisterItems.ASTRALMAP_0_0, 180, 0.5));
    }
}
