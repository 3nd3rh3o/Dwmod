package h3o.ender.client.blockEntities.renderer;

import h3o.ender.blockEntity.tardis.engine.TardisMaintenanceEngineUpperBE;
import h3o.ender.client.blockEntities.model.TardisMaintenaneEngineUpperModel;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class TardisMaintenanceEngineUpperRenderer extends GeoBlockRenderer<TardisMaintenanceEngineUpperBE> {

    public TardisMaintenanceEngineUpperRenderer(BlockEntityRendererFactory.Context ctx) {
        super(new TardisMaintenaneEngineUpperModel());

    }
}
