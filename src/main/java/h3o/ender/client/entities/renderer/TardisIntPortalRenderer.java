package h3o.ender.client.entities.renderer;

import h3o.ender.DwMod;
import h3o.ender.entities.TardisInternalPortal;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

public class TardisIntPortalRenderer extends EntityRenderer<TardisInternalPortal> {

    public TardisIntPortalRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public Identifier getTexture(TardisInternalPortal var1) {
        return var1.isVisible() ? new Identifier(DwMod.MODID, "") : new Identifier(DwMod.MODID, "");
    }
    
}
