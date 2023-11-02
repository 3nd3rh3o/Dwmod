package h3o.ender.client.item.model;

import h3o.ender.DwMod;
import h3o.ender.items.LLOEnergyDistributor;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class LLOEnergyDistributorModel extends GeoModel<LLOEnergyDistributor> {

    @Override
    public Identifier getModelResource(LLOEnergyDistributor animatable) {
        return new Identifier(DwMod.MODID, "geo/item/2lo.energy.distributor.geo.json");
    }

    @Override
    public Identifier getTextureResource(LLOEnergyDistributor animatable) {
        return new Identifier(DwMod.MODID, "textures/item/2lo.energy.distributor.png");
    }

    @Override
    public Identifier getAnimationResource(LLOEnergyDistributor animatable) {
        return new Identifier(DwMod.MODID, "animations/items/2lo_energy_distributor.animation.json");
    }
}
