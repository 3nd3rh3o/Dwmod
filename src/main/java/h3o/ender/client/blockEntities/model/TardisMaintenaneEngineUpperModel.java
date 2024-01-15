package h3o.ender.client.blockEntities.model;

import h3o.ender.DwMod;
import h3o.ender.blockEntity.tardis.engine.TardisMaintenanceEngineUpperBE;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class TardisMaintenaneEngineUpperModel extends GeoModel<TardisMaintenanceEngineUpperBE> {

    @Override
    public Identifier getModelResource(TardisMaintenanceEngineUpperBE animatable) {
        return new Identifier(DwMod.MODID, "geo/block_entities/tardis.engine.upper.geo.json");
    }

    @Override
    public Identifier getTextureResource(TardisMaintenanceEngineUpperBE animatable) {
        return new Identifier(DwMod.MODID, "textures/block_entities/tardis.maintenance.engine.upper.png");
    }

    @Override
    public Identifier getAnimationResource(TardisMaintenanceEngineUpperBE animatable) {
        return null;
    }
    
}
