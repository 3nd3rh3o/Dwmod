package h3o.ender.client.item.model;

import h3o.ender.DwMod;
import h3o.ender.items.MainSpaceTimeElement;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class MainSpaceTimeElementModel extends GeoModel<MainSpaceTimeElement> {

    @Override
    public Identifier getModelResource(MainSpaceTimeElement animatable) {
        return new Identifier(DwMod.MODID, "geo/item/main_space_time_element.geo.json");        
    }

    @Override
    public Identifier getTextureResource(MainSpaceTimeElement animatable) {
        return new Identifier(DwMod.MODID, "textures/item/main_space_time_element.png");
    }

    @Override
    public Identifier getAnimationResource(MainSpaceTimeElement animatable) {
        return new Identifier(DwMod.MODID, "animations/items/main_space_time_element.animation.json");       
    }
    
}
