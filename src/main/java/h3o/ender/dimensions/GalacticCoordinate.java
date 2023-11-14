package h3o.ender.dimensions;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;

public class GalacticCoordinate {
    private final List<GalacticCoordinate> children = new ArrayList<>();
    private Item item;
    private int initAngle;
    private double orbitTime;


    public GalacticCoordinate(Item item, int initAngle, double orbitTime) {
        this.item = item;
        this.initAngle = initAngle;
        this.orbitTime = orbitTime;
    }

    public void putChildren(GalacticCoordinate object) {
        this.children.add(object);
    }

    public List<GalacticCoordinate> getChildrens() {
        return children;
    }

    public Item getItem() {
        return item;
    }

	public int getInitAngle() {
		return initAngle;
	}

    public double getOrbitTime() {
        return orbitTime;
    }
}
