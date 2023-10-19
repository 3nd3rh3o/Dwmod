package h3o.ender;

import h3o.ender.blocks.RegisterBlocks;
import h3o.ender.entities.RegisterEntities;
import h3o.ender.entities.renderer.TardisDefaultExtDoorRenderer;
import h3o.ender.entities.renderer.TardisIntPortalRenderer;
import h3o.ender.entities.renderer.TardisPortalRenderer;
import h3o.ender.entities.renderer.TardisRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;

public class DwModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        EntityRendererRegistry.register(RegisterEntities.TARDIS, TardisRenderer::new);
        EntityRendererRegistry.register(RegisterEntities.TARDIS_EXT_DOOR_DEFAULT, TardisDefaultExtDoorRenderer::new);
        EntityRendererRegistry.register(RegisterEntities.TARDIS_PORTAL, TardisPortalRenderer::new);
        EntityRendererRegistry.register(RegisterEntities.TARDIS_INT_PORTAL, TardisIntPortalRenderer::new);
        BlockRenderLayerMap.INSTANCE.putBlock(RegisterBlocks.TARDIS_DEFAULT_HITBOX, RenderLayer.getTranslucent());
    }
}
