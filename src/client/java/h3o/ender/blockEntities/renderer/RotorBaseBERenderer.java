package h3o.ender.blockEntities.renderer;

import h3o.ender.blockEntities.model.RotorBaseBEModel;
import h3o.ender.blockEntity.tardis.RotorBaseBE;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class RotorBaseBERenderer extends GeoBlockRenderer<RotorBaseBE> {

    public RotorBaseBERenderer(BlockEntityRendererFactory.Context ctx) {
        super(new RotorBaseBEModel());
    }

    //TODO learn how to use render layers and how to render items on block entity(or just switch the model if you can)
    
}
