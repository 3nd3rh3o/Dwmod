package h3o.ender.client.blockEntities.renderer;


import java.util.ArrayList;
import java.util.List;

import h3o.ender.blockEntity.tardis.RotorBaseBE;
import h3o.ender.client.blockEntities.model.RotorBaseBEModel;
import h3o.ender.components.Circuit;
import h3o.ender.components.Circuit.LOCATION;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class RotorBaseBERenderer extends GeoBlockRenderer<RotorBaseBE> {

    public RotorBaseBERenderer(BlockEntityRendererFactory.Context ctx) {
        super(new RotorBaseBEModel());
    }

    @Override
    public void actuallyRender(MatrixStack poseStack, RotorBaseBE animatable, BakedGeoModel model,
            RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender,
            float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick,
                packedLight, packedOverlay, red, green, blue, alpha);
        NbtList nbt = animatable.getTardisCircuits();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < nbt.size(); i++) {
            if (Circuit.strToLoc(nbt.getList(i).getString(1)).equals(Circuit.LOCATION.ROTOR_BASE)) {
                list.add(nbt.getList(i).getString(0));
            }
        }
        list.forEach((name) -> {
            poseStack.push();
            Circuit.renderPos(poseStack, name, Circuit.LOCATION.ROTOR_BASE);
            poseStack.scale(0.125f, 0.125f, 0.125f);
            MinecraftClient.getInstance().getItemRenderer().renderItem(Circuit.getItemForName(name), ModelTransformationMode.NONE, packedLight, packedOverlay, poseStack, bufferSource, animatable.getWorld(), 0);
            poseStack.pop();
        });
        
    }

    

    //TODO learn how to use render layers and how to render items on block entity(or just switch the model if you can)
    
}
