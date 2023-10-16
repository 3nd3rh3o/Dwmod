package h3o.ender;

import h3o.ender.entities.RegisterEntities;
import h3o.ender.entities.renderer.TardisPortalRenderer;
import h3o.ender.entities.renderer.TardisRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class DwModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        EntityRendererRegistry.register(RegisterEntities.TARDIS, TardisRenderer::new);
        EntityRendererRegistry.register(RegisterEntities.TARDIS_PORTAL, TardisPortalRenderer::new);
    }
    
}
