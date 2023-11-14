package h3o.ender.client.screen;

import java.util.List;
import java.util.ArrayList;

import h3o.ender.DwMod;
import h3o.ender.components.Circuit;
import h3o.ender.components.Circuit.NAME;
import h3o.ender.screenHandler.AstralMapScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;

public class AstralMapScreen extends HandledScreen<AstralMapScreenHandler> {
    private final Identifier texture = new Identifier(DwMod.MODID, "textures/gui/astralmap.png");

    private static final List<Integer> planets = new ArrayList<>();
    private double zoom = 0.9;
    private double tilt = 45;

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
        renderOrbitsAndGUI(context);
        //TODO figure out why it don't work
        renderPlanets(context);
        
    }

    private void renderPlanets(DrawContext context) {
        MatrixStack stack = context.getMatrices();
        stack.push();
        stack.translate((width/2) + 8, (height/2)+8, (width/2) + 8);
        BakedModel model = client.getItemRenderer().getModel(Circuit.getItemForName(Circuit.nameToStr(NAME.DEFAULT_ROTOR)), null, null, 0);
        this.client.getItemRenderer().renderItem(Circuit.getItemForName(Circuit.nameToStr(NAME.DEFAULT_ROTOR)), ModelTransformationMode.NONE, false, stack, context.getVertexConsumers(), 0, 0, model);
        context.draw();
        stack.pop();
    }

    private void renderOrbitsAndGUI(DrawContext context) {
        context.getMatrices().push();
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        for (int s = 1; s <= planets.size(); s++) {
            int segments = 100;
            
            // deg
            int radius = ((int) Math.round((s * (width / 2) / planets.size()) * zoom));
            for (int i = 0; i < segments; i++) {
                double sig1 = (double) i / segments * 2.0 * Math.PI;
                double sig2 = (double) (i + 1) / segments * 2.0 * Math.PI;
                double tet = (double) Math.toRadians(tilt % 360);

                double x1 = Math.sin(sig1) * radius + (width / 2);
                double y1 = (height / 2) - Math.cos(sig1) * Math.sin(tet) * radius;
                double z1 = Math.cos(sig1) * Math.cos(tet) * radius + radius;

                double x2 = Math.sin(sig2) * radius + (width / 2);
                double y2 = (height / 2) - Math.cos(sig2) * Math.sin(tet) * radius;
                double z2 = Math.cos(sig2) * Math.cos(tet) * radius + radius;

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
        context.drawTexture(this.texture, 0, 0, 175, 1, 14, 14);
        context.drawTexture(this.texture, 0, 14, 175, 15, 14, 14);
        context.getMatrices().pop();
    }

    

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.client.options.forwardKey.matchesKey(keyCode, scanCode)) {
            tilt = (tilt+1) % 360;
            return true;
        }
        if (this.client.options.backKey.matchesKey(keyCode, scanCode)) {
            tilt = (tilt-1) % 360;
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }



    static {
        planets.add(Integer.valueOf(1));
        planets.add(Integer.valueOf(2));
    }
}
