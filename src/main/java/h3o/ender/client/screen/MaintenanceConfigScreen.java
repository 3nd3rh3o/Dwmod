package h3o.ender.client.screen;

import java.util.List;
import java.util.ArrayList;

import h3o.ender.DwMod;
import h3o.ender.blockEntity.tardis.TerminalBE;
import h3o.ender.screenHandler.MaintenanceConfigScreenHandler;
import h3o.ender.structures.tardis.DimensionalStorageHelper;
import h3o.ender.structures.tardis.Room;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class MaintenanceConfigScreen extends HandledScreen<MaintenanceConfigScreenHandler> {
    private final Identifier texture = new Identifier(DwMod.MODID, "textures/gui/ars.png");
    private List<Room> intSh;
    private int selectedFloor = 0;
    private List<ButtonWidget> mapGrid = new ArrayList<>();
    private int roomLocCursor;

    @SuppressWarnings("resource")
    public MaintenanceConfigScreen(MaintenanceConfigScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.intSh = DimensionalStorageHelper
                .filterEngine(((TerminalBE) MinecraftClient.getInstance().world.getBlockEntity(handler.getPos()))
                        .getTardisInternalScheme());
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        context.drawTexture(this.texture, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        renderRoomIcons(context, mouseX, mouseY, delta);
        renderFloorNumber(context, mouseX, mouseY, delta);
    }

    private void renderRoomIcons(DrawContext context, int mouseX, int mouseY, float delta) {
        int startX = (width - backgroundWidth) / 2;
        int startY = (height - backgroundHeight) / 2;
        for (int x = 0; x < 5; x++) {
            for (int z = 0; z < 5; z++) {
                int id = x + selectedFloor * 5 + z * 25;
                for (Room room : intSh) {
                    if (room.getVId() == id) {
                        if (mouseX > x * 15 + startX + 7 && mouseX <= (x + 1) * 15 + startX + 7
                                && mouseY > z * 15 + startY + 7 && mouseY <= (z + 1) * 15 + startY + 7) {
                            context.drawTooltip(client.textRenderer,
                                    Text.of(room.getName().toString().replace("_", " ") + " - " + x + " "
                                            + selectedFloor + " " + z),
                                    x * 15 + startX + 16,
                                    z * 15 + startY + 16);
                        }
                        MatrixStack stack = context.getMatrices();
                        stack.push();
                        stack.translate(x * 15 + startX + 15.5f, z * 15 + startY + 15.5f, 16);
                        int scale = -15;
                        stack.scale(scale, scale, scale);
                        stack.multiply(RotationAxis.POSITIVE_Z
                                .rotationDegrees((float) 90 * room.getOrientation().ordinal()));
                        ItemStack itemStack = new ItemStack(room.getName().getIcon());
                        BakedModel model = client.getItemRenderer().getModels().getModel(itemStack);
                        client.getItemRenderer().renderItem(itemStack, ModelTransformationMode.GUI, false, stack,
                                ((VertexConsumerProvider) context.getVertexConsumers()), 0xF000F0,
                                OverlayTexture.DEFAULT_UV, model);
                        stack.pop();
                    }
                }
            }
        }
        context.drawTexture(this.texture, (roomLocCursor % 5) * 15 + 8 + startX,
                Math.round(roomLocCursor / 25f) * 15 + 8 + startY, 176, 0, 15, 15);
    }

    private void renderFloorNumber(DrawContext context, int mouseX, int mouseY, float delta) {
        int startX = (width - backgroundWidth) / 2;
        int startY = (height - backgroundHeight) / 2;
        context.drawText(client.textRenderer, Text.of(String.valueOf(selectedFloor - 2) + "F"), startX + 37,
                startY + 86, Colors.WHITE, false);
    }

    @Override
    protected void init() {
        
        for (int x = 0; x < 5; x++) {
            for (int z = 0; z < 5; z++) {
                int startX = (width - backgroundWidth) / 2;
                int startY = (height - backgroundHeight) / 2;
                mapGrid.add(ButtonWidget.builder(Text.literal(""), button -> {
                    int startX1 = (width - backgroundWidth) / 2;
                    int startY1 = (height - backgroundHeight) / 2;
                    this.roomLocCursor = ((button.getX() - startX1 - 7) / 15) + (selectedFloor * 5)
                            + ((button.getY() - startY1 - 7) / 15) * 25;
                }).dimensions(startX + 15 * x + 7, startY + 15 * z + 7, 16, 16).build());
            }
        }
        mapGrid.forEach(but -> {
            addSelectableChild(but);
        });
        super.init();
    }

}
