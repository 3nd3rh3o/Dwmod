package h3o.ender.blockEntities.model;

import h3o.ender.DwMod;
import h3o.ender.blockEntity.tardis.RotorBaseBE;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class RotorBaseBEModel extends GeoModel<RotorBaseBE> {

    @Override
    public Identifier getModelResource(RotorBaseBE animatable) {
        return new Identifier(DwMod.MODID, "geo/block_entities/tardis.console.rotor_base.geo.json");
    }

    @Override
    public Identifier getTextureResource(RotorBaseBE animatable) {
        return new Identifier(DwMod.MODID, "textures/block_entities/tardis.console.rotor_base.png");
    }

    @Override
    public Identifier getAnimationResource(RotorBaseBE animatable) {
        return null;
    }
    
}
