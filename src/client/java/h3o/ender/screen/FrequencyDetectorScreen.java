package h3o.ender.screen;

import java.util.List;
import java.util.ArrayList;

import h3o.ender.DwMod;
import h3o.ender.items.FrequencyDetector;
import h3o.ender.networking.ModMessages;
import h3o.ender.screenHandler.FrequencyDetectorScreenHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class FrequencyDetectorScreen extends HandledScreen<FrequencyDetectorScreenHandler> {
    private String dimKey;

    private List<ButtonWidget> button = new ArrayList<>();

    private final Identifier texture = new Identifier(DwMod.MODID, "textures/gui/frequency_detector_screen.png");

    public FrequencyDetectorScreen(FrequencyDetectorScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.dimKey = handler.getDimKey();
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        context.drawTexture(this.texture, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }

    @Override
    protected void init() {
        super.init();
        int imageStartx = (width - backgroundWidth) / 2;
        int imageStarty = (height - backgroundHeight) / 2;
        List<String> struct = FrequencyDetector.getStructForW(dimKey);
        for (String struc : struct) {
            button.add(ButtonWidget.builder(Text.literal(struc), button -> {
                ClientPlayNetworking.send(ModMessages.FREQUENCY_DETECTOR_SETTING_ID,
                        PacketByteBufs.create().writeInt(struct.indexOf(struc)));
            }).dimensions(imageStartx + 7, imageStarty + 7 + 17 * struct.indexOf(struc), backgroundWidth - 16,
                    17 * struct.indexOf(struc))
                    .build());

        }
        button.forEach(but -> addDrawableChild(but));
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {

    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        button.forEach(but -> but.render(context, mouseX, mouseY, delta));
        super.render(context, mouseX, mouseY, delta);

    }
}