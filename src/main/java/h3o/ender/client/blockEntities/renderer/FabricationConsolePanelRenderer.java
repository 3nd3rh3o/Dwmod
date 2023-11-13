package h3o.ender.client.blockEntities.renderer;

import h3o.ender.blockEntity.tardis.console_panel.FabricationConsolePanelBE;
import h3o.ender.client.blockEntities.model.FabricationConsolePanelModel;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class FabricationConsolePanelRenderer extends GeoBlockRenderer<FabricationConsolePanelBE> {

    public FabricationConsolePanelRenderer(BlockEntityRendererFactory.Context ctx) {
        super(new FabricationConsolePanelModel());
    }
    
}
