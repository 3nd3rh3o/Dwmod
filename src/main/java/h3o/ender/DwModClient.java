package h3o.ender;


import h3o.ender.blockEntity.RegisterBlockEntities;
import h3o.ender.blocks.RegisterBlocks;
import h3o.ender.client.blockEntities.renderer.FabricationConsolePanelRenderer;
import h3o.ender.client.blockEntities.renderer.RotorBaseBERenderer;
import h3o.ender.client.blockEntities.renderer.TerminalBERenderer;
import h3o.ender.client.entities.renderer.TardisDefaultExtDoorRenderer;
import h3o.ender.client.entities.renderer.TardisIntPortalRenderer;
import h3o.ender.client.entities.renderer.TardisPortalRenderer;
import h3o.ender.client.entities.renderer.TardisRenderer;
import h3o.ender.client.screen.FrequencyDetectorScreen;
import h3o.ender.client.screen.TardisTerminalScreen;
import h3o.ender.entities.RegisterEntities;
import h3o.ender.screenHandler.RegisterScreenHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;


public class DwModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        BlockEntityRendererFactories.register(RegisterBlockEntities.ROTOR_BASE_BE, RotorBaseBERenderer::new);
        BlockEntityRendererFactories.register(RegisterBlockEntities.TERMINAL_BE, TerminalBERenderer::new);
        BlockEntityRendererFactories.register(RegisterBlockEntities.FABRICATION_CONSOLE_PANEL_BE, FabricationConsolePanelRenderer::new);
        EntityRendererRegistry.register(RegisterEntities.TARDIS, TardisRenderer::new);
        EntityRendererRegistry.register(RegisterEntities.TARDIS_EXT_DOOR_DEFAULT, TardisDefaultExtDoorRenderer::new);
        EntityRendererRegistry.register(RegisterEntities.TARDIS_PORTAL, TardisPortalRenderer::new);
        EntityRendererRegistry.register(RegisterEntities.TARDIS_INT_PORTAL, TardisIntPortalRenderer::new);
        BlockRenderLayerMap.INSTANCE.putBlock(RegisterBlocks.TARDIS_DEFAULT_HITBOX, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(RegisterBlocks.TARDIS_EXT_DOOR_DEFAULT_HITBOX, RenderLayer.getTranslucent());
        HandledScreens.register(RegisterScreenHandler.FREQUENCY_DETECTOR_SCREEN_HANDLER, FrequencyDetectorScreen::new);
        HandledScreens.register(RegisterScreenHandler.TARDIS_TERMINAL_SCREEN_HANDLER, TardisTerminalScreen::new);
    }
}
