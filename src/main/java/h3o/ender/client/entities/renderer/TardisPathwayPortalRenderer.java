package h3o.ender.client.entities.renderer;

import h3o.ender.DwMod;
import h3o.ender.entities.TardisPathwayPortal;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

public class TardisPathwayPortalRenderer extends EntityRenderer<TardisPathwayPortal> {
    
    public TardisPathwayPortalRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public Identifier getTexture(TardisPathwayPortal var1) {
        return var1.isVisible() ? new Identifier(DwMod.MODID, "") : new Identifier(DwMod.MODID, "");
    }
}
