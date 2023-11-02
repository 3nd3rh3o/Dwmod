package h3o.ender.client.item.renderer;

import h3o.ender.client.item.model.MainSpaceTimeElementModel;
import h3o.ender.items.MainSpaceTimeElement;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class MainSpaceTimeElementRenderer extends GeoItemRenderer<MainSpaceTimeElement> {

    public MainSpaceTimeElementRenderer() {
        super(new MainSpaceTimeElementModel());
    }
    
}
