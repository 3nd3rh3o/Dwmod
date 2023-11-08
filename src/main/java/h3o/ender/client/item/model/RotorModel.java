package h3o.ender.client.item.model;

import static h3o.ender.components.Circuit.nameToStr;
import h3o.ender.DwMod;
import h3o.ender.items.Rotor;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class RotorModel extends GeoModel<Rotor> {

    @Override
    public Identifier getModelResource(Rotor animatable) {
        return switch (nameToStr(animatable.name)) {
            case "default_rotor" -> new Identifier(DwMod.MODID, "geo/item/default_rotor.geo.json");
            default -> null;
        };
    }

    @Override
    public Identifier getTextureResource(Rotor animatable) {
        return switch (nameToStr(animatable.name)) {
            case "default_rotor" -> new Identifier(DwMod.MODID, "textures/item/default_rotor.png");
            default -> null;
        };
    }

    @Override
    public Identifier getAnimationResource(Rotor animatable) {
        return null;
    }
    
}
