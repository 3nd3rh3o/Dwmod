package h3o.ender.client.screen;

import java.util.List;

import java.util.ArrayList;

import h3o.ender.DwMod;
import h3o.ender.items.RegisterItems;
import h3o.ender.screenHandler.AstralMapScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.RotationAxis;

public class AstralMapScreen extends HandledScreen<AstralMapScreenHandler> {
    private final Identifier texture = new Identifier(DwMod.MODID, "textures/gui/astralmap.png");

    private static final List<Integer> planets = new ArrayList<>();
    private double zoom = 0.9;
    private double tilt = -215;
    private double yaw = 0;

    public AstralMapScreen(AstralMapScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {

    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        renderOrbitsAndButton(context, mouseX, mouseY);
        renderPlanets(context);
    }

    private void renderPlanets(DrawContext context) {
        MatrixStack stack = context.getMatrices();
        stack.push();
        stack.translate((width / 2), (height / 2), ((int) Math.round(((width / 2)) * zoom)));
        stack.scale((float)(-20 * zoom), (float)(-20 * zoom), (float)(-20 * zoom));
        stack.multiply(RotationAxis.POSITIVE_X.rotationDegrees((float) tilt));
        stack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float) yaw));
        ItemStack itemStack = new ItemStack(RegisterItems.TARDIS_DEFAULT_WALL_LAMP, 1);
        // context.drawItem(itemStack, titleX, playerInventoryTitleX);
        BakedModel model = client.getItemRenderer().getModels().getModel(itemStack);
        client.getItemRenderer().renderItem(itemStack, ModelTransformationMode.NONE, false, stack,
                ((VertexConsumerProvider) context.getVertexConsumers()), 0xF000F0, OverlayTexture.DEFAULT_UV, model);
        context.draw();
        stack.pop();
    }

    private void renderOrbitsAndButton(DrawContext context, int mouseX, int mouseY) {
        MatrixStack stack = context.getMatrices();
        stack.push();
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        for (int s = 1; s <= planets.size(); s++) {
            int segments = 100;

            // deg
            int radius = ((int) Math.round((s * (width / 2) / planets.size()) * zoom));
            for (int i = 0; i < segments; i++) {
                double sig1 = (double) i / segments * 2.0 * Math.PI + Math.toRadians(yaw);
                double sig2 = (double) (i + 1) / segments * 2.0 * Math.PI + Math.toRadians(yaw);
                double tet = (double) Math.toRadians(tilt % 360);

                double x1 = Math.sin(sig1) * radius + (width / 2);
                double y1 = (height / 2) - Math.cos(sig1) * Math.sin(tet) * radius;
                double z1 = Math.cos(sig1) * Math.cos(tet) * radius + ((int) Math.round(((width / 2)) * zoom));

                double x2 = Math.sin(sig2) * radius + (width / 2);
                double y2 = (height / 2) - Math.cos(sig2) * Math.sin(tet) * radius;
                double z2 = Math.cos(sig2) * Math.cos(tet) * radius + ((int) Math.round(((width / 2)) * zoom));

                // color
                int color = ColorHelper.Argb.getArgb(1, 245, 66, 66);
                bufferBuilder
                        .vertex(context.getMatrices().peek().getPositionMatrix(), (float) x1, (float) y1, (float) z1)
                        .color(color).next();
                bufferBuilder
                        .vertex(context.getMatrices().peek().getPositionMatrix(), (float) x2, (float) y2, (float) z2)
                        .color(color).next();
            }

        }
        Tessellator.getInstance().draw();
        if (mouseX <= 13 && mouseX >= 0 && mouseY <= 13 && mouseY >= 0) {
            context.drawTexture(this.texture, 0, 0, 189, 1, 14, 14);
        } else {
            context.drawTexture(this.texture, 0, 0, 175, 1, 14, 14);
        }
        if (mouseX <= 13 && mouseX >= 0 && mouseY <= 26 && mouseY >= 14) {
            context.drawTexture(this.texture, 0, 14, 189, 15, 14, 14);
        } else {
            context.drawTexture(this.texture, 0, 14, 175, 15, 14, 14);
        }
        stack.pop();
    }

    

    

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 1 && mouseX <= 13 && mouseX >= 0 && mouseY <= 13 && mouseY >= 0 && zoom < 5) {
            zoom+=0.1;
            return true;
        }
        if (button == 1 && mouseX <= 13 && mouseX >= 0 && mouseY <= 26 && mouseY >= 14 && zoom > 0.9) {
            zoom-=0.1;
            return true;
        }
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.client.options.forwardKey.matchesKey(keyCode, scanCode)) {
            tilt = (tilt + 1) % 360;
            return true;
        }
        if (this.client.options.backKey.matchesKey(keyCode, scanCode)) {
            tilt = (tilt - 1) % 360;
            return true;
        }
        if (this.client.options.leftKey.matchesKey(keyCode, scanCode)) {
            yaw = (yaw + 1) % 360;
            return true;
        }
        if (this.client.options.rightKey.matchesKey(keyCode, scanCode)) {
            yaw = (yaw - 1) % 360;
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }



    static {
        planets.add(Integer.valueOf(1));
        planets.add(Integer.valueOf(2));
    }
}
