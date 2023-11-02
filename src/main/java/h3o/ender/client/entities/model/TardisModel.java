package h3o.ender.client.entities.model;

import h3o.ender.DwMod;
import h3o.ender.entities.Tardis;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class TardisModel extends GeoModel<Tardis> {

    @Override
    public Identifier getModelResource(Tardis animatable) {
        return new Identifier(DwMod.MODID, "geo/entities/tardis/exoshell.default.geo.json");
    }

    @Override
    public Identifier getTextureResource(Tardis animatable) {
        return new Identifier(DwMod.MODID, "textures/entities/tardis/exoshell.default.png");
    }

    @Override
    public Identifier getAnimationResource(Tardis animatable) {
        return new Identifier(DwMod.MODID, "animations/entities/tardis/exoshell.default.animation.json");
    }
    
}
