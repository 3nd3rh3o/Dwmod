package h3o.ender.client.screen;

import h3o.ender.DwMod;
import h3o.ender.screenHandler.AstralMapScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;

public class AstralMapScreen extends HandledScreen<AstralMapScreenHandler> {
    private final Identifier texture = new Identifier(DwMod.MODID, "textures/gui/frequency_detector_screen.png");

    public AstralMapScreen(AstralMapScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        // context.drawTexture(this.texture, this.x, this.y, 0, 0, this.backgroundWidth,
        // this.backgroundHeight);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.getMatrices().push();
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        // ??
        bufferBuilder.begin(DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        int segments = 100;
        int radius = 10;
        for (int i = 0; i < segments; i++) {
            // gen the two points of the line
            double sig1 = (double) i / segments * 2.0 * Math.PI;
            double sig2 = (double) (i + 1) / segments * 2.0 * Math.PI;
            double tet = (double) 0.0; 

            double x1 = Math.sin(sig1) * radius + (width / 2);
            double y1 = (height / 2) - Math.cos(sig1) * Math.sin(tet) * radius;
            double z1 = Math.cos(sig1) * Math.cos(tet) * radius;

            double x2 = Math.sin(sig2) * radius + (width / 2);
            double y2 = (height / 2) - Math.cos(sig2) * Math.sin(tet) * radius;
            double z2 = Math.cos(sig2) * Math.cos(tet) * radius;
            int color = ColorHelper.Argb.getArgb(1, 245, 66, 66);

            bufferBuilder.vertex(context.getMatrices().peek().getPositionMatrix(), (float) x1, (float) y1, (float) z1)
                    .color(color).next();
            bufferBuilder.vertex(context.getMatrices().peek().getPositionMatrix(), (float) x2, (float) y2, (float) z2)
                    .color(color).next();

        }
        Tessellator.getInstance().draw();
        context.getMatrices().pop();
    }

}
