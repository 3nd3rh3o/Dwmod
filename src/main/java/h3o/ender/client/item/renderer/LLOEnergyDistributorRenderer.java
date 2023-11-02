package h3o.ender.client.item.renderer;


import h3o.ender.client.item.model.LLOEnergyDistributorModel;
import h3o.ender.items.LLOEnergyDistributor;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry.DynamicItemRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import software.bernie.geckolib.renderer.GeoItemRenderer;


public class LLOEnergyDistributorRenderer extends GeoItemRenderer<LLOEnergyDistributor> implements DynamicItemRenderer {

    public LLOEnergyDistributorRenderer() {
        super(new LLOEnergyDistributorModel());
    }

    @Override
    public void render(ItemStack stack, ModelTransformationMode transformType, MatrixStack poseStack,
            VertexConsumerProvider bufferSource, int packedLight, int packedOverlay) {
        super.render(stack, transformType, poseStack, bufferSource, packedLight, packedOverlay);
    }

    
}
