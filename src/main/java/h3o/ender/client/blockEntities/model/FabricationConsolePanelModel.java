package h3o.ender.client.blockEntities.model;

import h3o.ender.DwMod;
import h3o.ender.blockEntity.tardis.console_panel.FabricationConsolePanelBE;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class FabricationConsolePanelModel extends GeoModel<FabricationConsolePanelBE> {

    @Override
    public Identifier getModelResource(FabricationConsolePanelBE animatable) {
        return new Identifier(DwMod.MODID, "geo/block_entities/tardis.console_panel.fabrication.geo.json");
    }

    @Override
    public Identifier getTextureResource(FabricationConsolePanelBE animatable) {
        return new Identifier(DwMod.MODID, "textures/block_entities/gauge.png");
    }

    @Override
    public Identifier getAnimationResource(FabricationConsolePanelBE animatable) {
       return null;
    }
    
}
