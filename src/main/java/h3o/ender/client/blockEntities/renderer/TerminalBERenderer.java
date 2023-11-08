package h3o.ender.client.blockEntities.renderer;

import java.util.List;

import org.joml.Matrix4f;

import h3o.ender.blockEntity.tardis.TerminalBE;
import h3o.ender.client.blockEntities.model.TerminalBEModel;
import h3o.ender.tardisOs.TardisOs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.MutableText;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class TerminalBERenderer extends GeoBlockRenderer<TerminalBE> {

        public TerminalBERenderer(BlockEntityRendererFactory.Context ctx) {
                super(new TerminalBEModel());
        }

        @Override
        @SuppressWarnings("resource")
        public void actuallyRender(MatrixStack poseStack, TerminalBE animatable, BakedGeoModel model,
                        RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer,
                        boolean isReRender,
                        float partialTick, int packedLight, int packedOverlay, float red, float green, float blue,
                        float alpha) {
                super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender,
                                partialTick,
                                packedLight, packedOverlay, red, green, blue, alpha);

                NbtList nbt = animatable.getTardisCircuits();
                if (nbt != null) {
                        List<MutableText> list = TardisOs.errorDisplayOrNormal(animatable, nbt, MinecraftClient.getInstance().textRenderer);
                        poseStack.push();
                        Matrix4f matrix = poseStack.peek().getPositionMatrix().translate(-0.22f, 1.2f, -0.045f)
                                        .rotate((float) (135 * Math.PI / 180f), 1, 0, 0)
                                        .scale(0.003f);
                        for (int i = 0; i < list.size(); i++) {
                                MinecraftClient.getInstance().textRenderer.draw(list.get(i), -7, -21 + 9 * i, 1, false,
                                                matrix,
                                                bufferSource, TextRenderer.TextLayerType.NORMAL, 0, packedLight);
                        }
                        poseStack.pop();
                }
        }

}
