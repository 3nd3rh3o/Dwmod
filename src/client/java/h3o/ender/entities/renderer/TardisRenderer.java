package h3o.ender.entities.renderer;

import h3o.ender.entities.Tardis;
import h3o.ender.entities.model.TardisModel;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class TardisRenderer extends GeoEntityRenderer<Tardis> {

    public TardisRenderer(EntityRendererFactory.Context context) {
        super(context, new TardisModel());
    }

    
    //TODO change that to change the model!! and don't forget to handle the animation's change.
    // Maybe check if you need to notify of model change
    
    @Override
    public GeoModel<Tardis> getGeoModel() {
        return super.getGeoModel();
    }


    @Override
    public boolean shouldRender(Tardis entity, Frustum frustum, double x, double y, double z) {
        return true;
    }

    

    
    

}
