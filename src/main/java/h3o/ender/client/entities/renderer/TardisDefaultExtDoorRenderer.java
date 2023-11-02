package h3o.ender.client.entities.renderer;

import h3o.ender.client.entities.model.TardisDefaultExtDoorModel;
import h3o.ender.entities.tardis.exoshell.TardisDefaultExtDoor;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class TardisDefaultExtDoorRenderer extends GeoEntityRenderer<TardisDefaultExtDoor> {

    public TardisDefaultExtDoorRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new TardisDefaultExtDoorModel());
    }

    @Override
    public boolean shouldRender(TardisDefaultExtDoor entity, Frustum frustum, double x, double y, double z) {
        return true;
    }

    
}
