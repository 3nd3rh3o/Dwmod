package h3o.ender.entities.renderer;

import h3o.ender.entities.Tardis;
import h3o.ender.entities.model.TardisModel;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class TardisRenderer extends GeoEntityRenderer<Tardis> {

    public TardisRenderer(EntityRendererFactory.Context context) {
        super(context, new TardisModel());
    }


    //TODO change that to change the model!! and the animations
    @Override
    public GeoModel<Tardis> getGeoModel() {
        return super.getGeoModel();
    }

    
    

}
