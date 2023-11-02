package h3o.ender.client.entities.model;

import h3o.ender.DwMod;
import h3o.ender.entities.tardis.exoshell.TardisDefaultExtDoor;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class TardisDefaultExtDoorModel extends GeoModel<TardisDefaultExtDoor>{

    @Override
    public Identifier getModelResource(TardisDefaultExtDoor animatable) {
        return new Identifier(DwMod.MODID, "geo/entities/tardis/exoshell.default.internal.door.geo.json");
    }

    @Override
    public Identifier getTextureResource(TardisDefaultExtDoor animatable) {
        return new Identifier(DwMod.MODID, "textures/entities/tardis/default_console_room/default_exoshell/internaldoor.png");
    }

    @Override
    public Identifier getAnimationResource(TardisDefaultExtDoor animatable) {
        return new Identifier(DwMod.MODID, "animations/entities/tardis/exoshell.default.internaldoor.animation.json");
    }
    
}
