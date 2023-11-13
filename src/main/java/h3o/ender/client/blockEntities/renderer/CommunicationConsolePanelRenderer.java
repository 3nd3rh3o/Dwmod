package h3o.ender.client.blockEntities.renderer;

import h3o.ender.blockEntity.tardis.console_panel.CommunicationConsolePanelBE;
import h3o.ender.client.blockEntities.model.CommunicationConsolePanelModel;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class CommunicationConsolePanelRenderer extends GeoBlockRenderer<CommunicationConsolePanelBE> {

    public CommunicationConsolePanelRenderer(BlockEntityRendererFactory.Context ctx) {
        super(new CommunicationConsolePanelModel());
    }
    
}
