package h3o.ender.client.blockEntities.model;

import h3o.ender.DwMod;
import h3o.ender.blockEntity.tardis.TerminalBE;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class TerminalBEModel extends GeoModel<TerminalBE> {

    @Override
    public Identifier getModelResource(TerminalBE animatable) {
        return new Identifier(DwMod.MODID, "geo/block_entities/tardis.console.terminal.geo.json");
    }

    @Override
    public Identifier getTextureResource(TerminalBE animatable) {
        return new Identifier(DwMod.MODID, "textures/block_entities/tardis.console.terminal.png");
    }

    @Override
    public Identifier getAnimationResource(TerminalBE animatable) {
        return null;
    }
    
}
