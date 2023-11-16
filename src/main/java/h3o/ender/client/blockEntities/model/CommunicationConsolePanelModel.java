package h3o.ender.client.blockEntities.model;

import h3o.ender.DwMod;
import h3o.ender.blockEntity.tardis.console_panel.CommunicationConsolePanelBE;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class CommunicationConsolePanelModel extends GeoModel<CommunicationConsolePanelBE> {

    @Override
    public Identifier getModelResource(CommunicationConsolePanelBE animatable) {
        return new Identifier(DwMod.MODID, "geo/block_entities/tardis.console_panel.communication.geo.json");
    }

    @Override
    public Identifier getTextureResource(CommunicationConsolePanelBE animatable) {
        return new Identifier(DwMod.MODID, "textures/block_entities/tardis.console.communication.png");
    }

    @Override
    public Identifier getAnimationResource(CommunicationConsolePanelBE animatable) {
        return new Identifier(DwMod.MODID, "animations/block_entities/tardis.console_panel.communication.animation.json");
    }
    
}
