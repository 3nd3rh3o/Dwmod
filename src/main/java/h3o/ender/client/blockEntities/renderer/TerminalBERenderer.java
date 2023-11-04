package h3o.ender.client.blockEntities.renderer;

import java.util.HashMap;
import java.util.List;

import org.joml.Matrix4f;

import h3o.ender.blockEntity.tardis.TerminalBE;
import h3o.ender.client.blockEntities.model.TerminalBEModel;
import h3o.ender.components.Circuit;
import h3o.ender.components.Circuit.LOCATION;
import h3o.ender.components.Circuit.NAME;
import h3o.ender.tardisOs.FormattedText;
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
                        poseStack.push();
                        HashMap<Circuit.NAME, Circuit.LOCATION> missingComp = new HashMap<>();
                        missingComp.put(NAME.MAIN_SPACE_TIME_ELEMENT, LOCATION.ROTOR_BASE);
                        missingComp.put(NAME.LLO_ENERGY_CONNECTOR, LOCATION.ROTOR_BASE);
                        for (int y = 0; y < nbt.size(); y++) {
                                if (Circuit.strToName(nbt.getList(y).getString(0))
                                                .equals(Circuit.NAME.MAIN_SPACE_TIME_ELEMENT)) {
                                        missingComp.remove(NAME.MAIN_SPACE_TIME_ELEMENT, LOCATION.ROTOR_BASE);
                                }
                                if (Circuit.strToName(nbt.getList(y).getString(0))
                                                .equals(Circuit.NAME.LLO_ENERGY_CONNECTOR)
                                                && Circuit.strToLoc(nbt.getList(y).getString(1))
                                                                .equals(Circuit.LOCATION.ROTOR_BASE)) {
                                        missingComp.remove(NAME.LLO_ENERGY_CONNECTOR, LOCATION.ROTOR_BASE);
                                }
                        }
                        List<MutableText> list;
                        if (missingComp.size() == 0) {
                                list = FormattedText.wrapStyledText(animatable.getText(), 160, MinecraftClient.getInstance().textRenderer);
                                list.addAll(FormattedText.wrapStyledText(FormattedText.empty().normal(animatable.getPrompt()).assemble(), 160, MinecraftClient.getInstance().textRenderer));
                        } else {
                                FormattedText text = FormattedText.empty();
                                for (NAME name : missingComp.keySet()) {

                                        text = text.error("-MISSING KEY COMPONENT : ").info(name.toString())
                                                        .error(" IN CONSOLE PANEL : ")
                                                        .info(missingComp.get(name).toString()).endLine();
                                }
                                list = FormattedText.wrapStyledText(text.assemble(), 160,
                                                MinecraftClient.getInstance().textRenderer);
                        }
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
