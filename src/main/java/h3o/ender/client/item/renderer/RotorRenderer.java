package h3o.ender.client.item.renderer;

import h3o.ender.client.item.model.RotorModel;
import h3o.ender.items.Rotor;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class RotorRenderer extends GeoItemRenderer<Rotor> {

    public RotorRenderer() {
        super(new RotorModel());
    }
    
}
