package h3o.ender.entities.renderer;

import h3o.ender.DwMod;
import h3o.ender.entities.TardisPortal;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

public class TardisPortalRenderer extends EntityRenderer<TardisPortal> {

    public TardisPortalRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public Identifier getTexture(TardisPortal entity) {
        return entity.isVisible() ? new Identifier(DwMod.MODID, "") : new Identifier(DwMod.MODID, "");
    }
    
}
