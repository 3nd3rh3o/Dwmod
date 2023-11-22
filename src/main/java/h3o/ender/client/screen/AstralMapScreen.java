package h3o.ender.client.screen;

import java.util.List;

import java.util.ArrayList;

import h3o.ender.DwMod;
import h3o.ender.dimensions.CoordinatesIndex;
import h3o.ender.dimensions.GalacticCoordinate;
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
import net.minecraft.util.math.Vec3d;

public class AstralMapScreen extends HandledScreen<AstralMapScreenHandler> {
    private final Identifier texture = new Identifier(DwMod.MODID, "textures/gui/astralmap.png");

    private double zoom = 0.9;
    private double tilt = -35;
    private double yaw = 0;
    private List<Integer> coords = new ArrayList<>();
    private int segments = 100;
    private int[] trajectory = { 1, 2 };

    public AstralMapScreen(AstralMapScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        // TODO remove its temp
        coords.add(0);
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

    private void genTrajectory(BufferBuilder bufferbuilder, int planet1, int planet2, List<GalacticCoordinate> map,
            DrawContext ctx) {
        double sig1 = Math.toRadians(yaw) + Math.toRadians(map.get(planet1).getInitAngle());
        double sig2 = Math.toRadians(yaw) + Math.toRadians(map.get(planet2).getInitAngle());
        
        double r1 = ((int) Math.round((planet1 * (width / 2) / (map.size() - 1)) * zoom));
        double r2 = ((int) Math.round((planet2 * (width / 2) / (map.size() - 1)) * zoom));
        double tet = Math.toRadians(tilt % 360);
        
        

        //params (-0.5 : 0.5)
        float rOffset = 0.5f;
        float tOffset = 0.5f;
        
        //TODO redo P1 gen, it come from a sphere, then you offset
        Vec3d P0 = new Vec3d(
                Math.sin(sig1) * r1,
                - Math.cos(sig1) * Math.sin(tet) * r1,
                Math.cos(sig1) * Math.cos(tet) * r1);
        Vec3d P2 = new Vec3d(
                Math.sin(sig2) * r2,
                - Math.cos(sig2) * Math.sin(tet) * r2,
                Math.cos(sig2) * Math.cos(tet) * r2);
        double rm = P0.distanceTo(P2) / 2.0;
        Vec3d PM = P0.add(P2).multiply(0.5);
        double r = Math.sqrt(Math.pow(rm * rOffset, 2) + Math.pow(rm * tOffset, 2));
        Vec3d P1 = new Vec3d(
                Math.sin(Math.PI/2.0 + Math.toRadians(yaw)) * r,
                - Math.cos(Math.PI/2.0 + Math.toRadians(yaw)) * Math.sin(tet) * r,
                Math.cos(Math.PI/2.0 + Math.toRadians(yaw)) * Math.cos(tet) * r
        ).subtract(PM);


        Vec3d translate = new Vec3d((width / 2), (height / 2), ((int) Math.round((width / 2) * zoom)));


        P0 = P0.add(translate);
        P1 = P1.add(translate);
        P2 = P2.add(translate);
        
        for (double t = 0; t < 1; t += 0.01) {
            
            double x1 = (1 - t) * (1 - t) * P0.getX() + 2 * (1 - t) * t * P1.getX() + t * t * P2.getX();
            double y1 = (1 - t) * (1 - t) * P0.getY() + 2 * (1 - t) * t * P1.getY() + t * t * P2.getY();
            double z1 = (1 - t) * (1 - t) * P0.getZ() + 2 * (1 - t) * t * P1.getZ() + t * t * P2.getZ();

            double x2 = (1 - (t + 0.01)) * (1 - (t + 0.01)) * P0.getX() + 2 * (1 - (t + 0.01)) * (t + 0.01) * P1.getX() + (t + 0.01) * (t + 0.01) * P2.getX();
            double y2 = (1 - (t + 0.01)) * (1 - (t + 0.01)) * P0.getY() + 2 * (1 - (t + 0.01)) * (t + 0.01) * P1.getY() + (t + 0.01) * (t + 0.01) * P2.getY();
            double z2 = (1 - (t + 0.01)) * (1 - (t + 0.01)) * P0.getZ() + 2 * (1 - (t + 0.01)) * (t + 0.01) * P1.getZ() + (t + 0.01) * (t + 0.01) * P2.getZ();

            int color = ColorHelper.Argb.getArgb(1, 223, 120, 120);
            bufferbuilder
                    .vertex(ctx.getMatrices().peek().getPositionMatrix(), (float) x1, (float) y1, (float) z1)
                    .color(color).next();
            bufferbuilder
                    .vertex(ctx.getMatrices().peek().getPositionMatrix(), (float) x2, (float) y2, (float) z2)
                    .color(color).next();
        }

    }

    private void renderPlanets(DrawContext context) {
        List<GalacticCoordinate> map = CoordinatesIndex.getIndex();
        List<Integer> coordsR = List.copyOf(coords);
        for (Integer n : coordsR) {
            map = map.get(n).getChildrens();
        }
        MatrixStack stack = context.getMatrices();
        stack.push();
        stack.translate((width / 2), (height / 2), ((int) Math.round(((width / 2)) * zoom)));
        stack.scale((float) (-20 * zoom), (float) (-20 * zoom), (float) (-20 * zoom));
        stack.multiply(RotationAxis.POSITIVE_X.rotationDegrees((float) tilt));
        stack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float) yaw));
        ItemStack itemStack = new ItemStack(map.get(0).getItem(), 1);
        BakedModel model = client.getItemRenderer().getModels().getModel(itemStack);
        client.getItemRenderer().renderItem(itemStack, ModelTransformationMode.NONE, false, stack,
                ((VertexConsumerProvider) context.getVertexConsumers()), 0xF000F0, OverlayTexture.DEFAULT_UV, model);
        context.draw();
        stack.pop();

        for (int i = 1; i < map.size(); i++) {
            // replace 0 by 1 * orbit time
            double sig = (double) 0 / segments * 2.0 * Math.PI + Math.toRadians(yaw)
                    + Math.toRadians(map.get(i).getInitAngle());
            double tet = (double) Math.toRadians(tilt % 360);
            int radius = ((int) Math.round((i * (width / 2) / (map.size() - 1)) * zoom));
            double x = Math.sin(sig) * radius + (width / 2);
            double y = (height / 2) - Math.cos(sig) * Math.sin(tet) * radius;
            double z = Math.cos(sig) * Math.cos(tet) * radius + ((int) Math.round(((width / 2)) * zoom));
            stack = context.getMatrices();
            stack.push();
            stack.translate(x, y, z);
            stack.scale((float) (-15 * zoom), (float) (-15 * zoom), (float) (-15 * zoom));
            stack.multiply(RotationAxis.POSITIVE_X.rotationDegrees((float) tilt));
            // replace 0 by 1 * orbit time
            stack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float) Math.toDegrees(sig)));
            itemStack = new ItemStack(map.get(i).getItem(), 1);
            model = client.getItemRenderer().getModels().getModel(itemStack);
            client.getItemRenderer().renderItem(itemStack, ModelTransformationMode.NONE, false, stack,
                    ((VertexConsumerProvider) context.getVertexConsumers()), 0xF000F0, OverlayTexture.DEFAULT_UV,
                    model);
            context.draw();
            stack.pop();
        }
    }

    private void renderOrbitsAndButton(DrawContext context, int mouseX, int mouseY) {
        List<GalacticCoordinate> map = CoordinatesIndex.getIndex();
        List<Integer> coordsR = List.copyOf(coords);
        for (Integer n : coordsR) {
            map = map.get(n).getChildrens();
        }

        MatrixStack stack = context.getMatrices();
        stack.push();
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        for (int s = 1; s < map.size(); s++) {
            // deg
            int radius = ((int) Math.round((s * (width / 2) / (map.size() - 1)) * zoom));
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
                int color = ColorHelper.Argb.getArgb(1, 223, 233, 243);
                bufferBuilder
                        .vertex(context.getMatrices().peek().getPositionMatrix(), (float) x1, (float) y1, (float) z1)
                        .color(color).next();
                bufferBuilder
                        .vertex(context.getMatrices().peek().getPositionMatrix(), (float) x2, (float) y2, (float) z2)
                        .color(color).next();
            }

        }
        if (trajectory != null) {
            genTrajectory(bufferBuilder, trajectory[0], trajectory[1], map, context);
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
        if (button == 0 && mouseX <= 13 && mouseX >= 0 && mouseY <= 13 && mouseY >= 0 && zoom < 5) {
            zoom += 0.1;
            return true;
        }
        if (button == 0 && mouseX <= 13 && mouseX >= 0 && mouseY <= 26 && mouseY >= 14 && zoom > 0.9) {
            zoom -= 0.1;
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
}
