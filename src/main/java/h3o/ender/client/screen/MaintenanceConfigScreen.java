package h3o.ender.client.screen;

import java.util.ArrayList;
import java.util.List;

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
    private List<ButtonWidget> mapScroll = new ArrayList<>();
    private int roomLocCursor;
    private int selectedCategory = 0;
    private List<ButtonWidget> categoryScroll = new ArrayList<>();
    private int selectedInvPage = 0;
    private List<ButtonWidget> invScroll = new ArrayList<>();
    private int invCursorLoc = 0;
    private List<ButtonWidget> invGrid = new ArrayList<>();

    private enum CATEGORY {
        ALL,
        CORRIDORS;

        public List<Room.Name> getRooms() {
            List<Room.Name> rooms = new ArrayList<>();
            switch (this) {
                case ALL -> {
                    rooms.add(Room.Name.MAINTENANCE_ENTRANCE);
                }
                case CORRIDORS -> {
                    rooms.add(Room.Name.MAINTENANCE_ENTRANCE);
                }
            }
            return rooms;
        }
    }

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

    
    @SuppressWarnings("resource")
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.intSh = DimensionalStorageHelper
                .filterEngine(((TerminalBE) MinecraftClient.getInstance().world.getBlockEntity(handler.getPos()))
                        .getTardisInternalScheme());
        renderRoomIcons(context, mouseX, mouseY, delta);
        renderRoomInventory(context, mouseX, mouseY, delta);
        renderTextAndButtonOverlay(context, mouseX, mouseY, delta);
    }

    private void renderRoomIcons(DrawContext context, int mouseX, int mouseY, float delta) {
        int startX = (width - backgroundWidth) / 2;
        int startY = (height - backgroundHeight) / 2;

        for (int x = 0; x < 5; x++) {
            for (int z = 0; z < 5; z++) {
                int id = x + selectedFloor * 5 + z * 25;
                for (Room room : intSh) {
                    context.drawTexture(this.texture, (roomLocCursor % 5) * 15 + 8 + startX,
                            Math.round(roomLocCursor / 25) * 15 + 8 + startY, 176, 0, 15, 15);
                    if (room.getVId() == id) {
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
                        if (mouseX > x * 15 + startX + 7 && mouseX <= (x + 1) * 15 + startX + 7
                                && mouseY > z * 15 + startY + 7 && mouseY <= (z + 1) * 15 + startY + 7) {
                            context.drawTooltip(client.textRenderer,
                                    Text.of(room.getName().toString().replace("_", " ") + " - " + x + " "
                                            + selectedFloor + " " + z),
                                    x * 15 + startX + 16,
                                    z * 15 + startY + 16);
                        }
                    }
                }
            }
        }
    }

    private void renderRoomInventory(DrawContext context, int mouseX, int mouseY, float delta) {
        int startX = (width - backgroundWidth) / 2;
        int startY = (height - backgroundHeight) / 2;
        List<Room.Name> roomNames = CATEGORY.values()[selectedCategory % CATEGORY.values().length].getRooms();
        context.drawTexture(this.texture, (invCursorLoc % 5) * 15 + 91 + startX,
                Math.round(invCursorLoc / 5) * 15 + 25 + startY, 176, 15, 15, 15);
        for (int i = 0 + 25 * selectedInvPage; i < roomNames.size() && i < 25 * (selectedInvPage + 1); i++) {

            int x = i % 5;
            int z = Math.round(i / 5);
            MatrixStack stack = context.getMatrices();
            stack.push();
            stack.translate(x * 15 + startX + 101.5f, z * 15 + startY + 28.5f, 16);
            int scale = -15;
            stack.scale(scale, scale, scale);
            stack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(45f));
            stack.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(-45f));
            ItemStack itemStack = new ItemStack(roomNames.get(i).getIcon());
            BakedModel model = client.getItemRenderer().getModels().getModel(itemStack);
            client.getItemRenderer().renderItem(itemStack, ModelTransformationMode.GUI, false, stack,
                    ((VertexConsumerProvider) context.getVertexConsumers()), 0xF000F0,
                    OverlayTexture.DEFAULT_UV, model);
            stack.pop();
            if (mouseX > x * 15 + startX + 90 && mouseX <= (x + 1) * 15 + startX + 90
                    && mouseY > z * 15 + startY + 24 && mouseY <= (z + 1) * 15 + startY + 24) {
                context.drawTooltip(client.textRenderer,
                        Text.of(roomNames.get(i).toString().replace("_", " ")),
                        x * 15 + startX + 99,
                        z * 15 + startY + 33);
            }
        }

    }

    private void renderTextAndButtonOverlay(DrawContext context, int mouseX, int mouseY, float delta) {
        int startX = (width - backgroundWidth) / 2;
        int startY = (height - backgroundHeight) / 2;
        context.drawText(client.textRenderer, Text.of(String.valueOf(selectedFloor - 2) + "F"), startX + 37,
                startY + 86, Colors.WHITE, false);
        context.drawText(client.textRenderer,
                Text.of(CATEGORY.values()[selectedCategory % CATEGORY.values().length].toString()),
                startX + 101,
                startY + 8, Colors.WHITE, false);
        if (mouseX < startX + 45 && mouseX >= startX + 10 && mouseY < startY + 109 && mouseY >= startY + 95) {
            context.drawTexture(texture, startX + 10, startY + 95, 191, 14, 35, 14);
        }
        if (mouseX < startX + 81 && mouseX >= startX + 46 && mouseY < startY + 109 && mouseY >= startY + 95) {
            context.drawTexture(texture, startX + 46, startY + 95, 191, 0, 35, 14);
        }
        if (mouseX < startX + 99 && mouseX >= startX + 89 && mouseY < startY + 17 && mouseY >= startY + 6) {
            context.drawTexture(texture, startX + 89, startY + 6, 191, 28, 10, 11);
        }
        if (mouseX < startX + 168 && mouseX >= startX + 158 && mouseY < startY + 17 && mouseY >= startY + 6) {
            context.drawTexture(texture, startX + 158, startY + 6, 201, 28, 10, 11);
        }
        if (mouseX < startX + 157 && mouseX >= startX + 100 && mouseY < startY + 24 && mouseY >= startY + 16) {
            context.drawTexture(texture, startX + 100, startY + 16, 191, 39, 57, 8);
        }
        if (mouseX < startX + 157 && mouseX >= startX + 100 && mouseY < startY + 109 && mouseY >= startY + 101) {
            context.drawTexture(texture, startX + 100, startY + 101, 191, 47, 57, 8);
        }

    }

    @Override
    protected void init() {
        super.init();
        mapGrid = new ArrayList<>();
        mapScroll = new ArrayList<>();
        categoryScroll = new ArrayList<>();
        invScroll = new ArrayList<>();
        invGrid = new ArrayList<>();

        int startX = (width - backgroundWidth) / 2;
        int startY = (height - backgroundHeight) / 2;
        for (int x = 0; x < 5; x++) {
            for (int z = 0; z < 5; z++) {
                mapGrid.add(ButtonWidget.builder(Text.empty(), button -> {
                    this.roomLocCursor = ((button.getX() - startX - 7) / 15) + (selectedFloor * 5)
                            + ((button.getY() - startY - 7) / 15) * 25;
                }).dimensions(startX + 15 * x + 7, startY + 15 * z + 7, 16, 16).build());
            }
        }
        mapScroll.add(ButtonWidget.builder(Text.empty(), button -> {
            if (selectedFloor > 0) {
                selectedFloor--;
                roomLocCursor -= 5;
            }
        }).dimensions(startX + 10, startY + 95, 35, 14).build());

        mapScroll.add(ButtonWidget.builder(Text.empty(), button -> {
            if (selectedFloor < 4) {
                selectedFloor++;
                roomLocCursor += 5;
            }
        }).dimensions(startX + 46, startY + 95, 35, 14).build());

        categoryScroll.add(ButtonWidget.builder(Text.empty(), button -> {
            if (selectedCategory > 0) {
                selectedCategory--;
            } else {
                selectedCategory = CATEGORY.values().length - 1;
            }
            selectedInvPage = 0;
            invCursorLoc = 0;
        }).dimensions(startX + 89, startY + 6, 10, 11).build());

        categoryScroll.add(ButtonWidget.builder(Text.empty(), button -> {
            if (selectedCategory < CATEGORY.values().length - 1) {
                selectedCategory++;
            } else {
                selectedCategory = 0;
            }
            selectedInvPage = 0;
            invCursorLoc = 0;
        }).dimensions(startX + 158, startY + 6, 10, 11).build());

        invScroll.add(ButtonWidget.builder(Text.empty(), button -> {
            if (selectedInvPage > 0) {
                selectedInvPage--;
                invCursorLoc = 0;
            }
        }).dimensions(startX + 100, startY + 16, 57, 8).build());

        invScroll.add(ButtonWidget.builder(Text.empty(), button -> {
            if (selectedInvPage * 25 < Math
                    .round((CATEGORY.values()[selectedCategory % CATEGORY.values().length].getRooms().size()) / 25)) {
                selectedInvPage++;
                invCursorLoc = 0;
            }
        }).dimensions(startX + 100, startY + 101, 57, 8).build());

        for (int i = 0; i < CATEGORY.values()[selectedCategory % CATEGORY.values().length].getRooms().size()
                % 25; i++) {
            invGrid.add(ButtonWidget.builder(Text.empty(), button -> {
                invCursorLoc = ((button.getX() - startX - 91) / 15) + (selectedInvPage * 25)
                        + ((button.getY() - startY - 25) / 15) * 5;
            }).dimensions(i * 15 + startX + 91, i * 25 + startY + 25, 16, 16).build());
        }

        mapGrid.forEach(but -> addSelectableChild(but));
        mapScroll.forEach(but -> addSelectableChild(but));
        categoryScroll.forEach(but -> addSelectableChild(but));
        invScroll.forEach(but -> addSelectableChild(but));
        invGrid.forEach(but -> addSelectableChild(but));
    }

}
