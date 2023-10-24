package h3o.ender.screen;

import h3o.ender.DwMod;
import h3o.ender.screenHandler.FrequencyDetectorScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class FrequencyDetectorScreen extends HandledScreen<FrequencyDetectorScreenHandler> {

    private final Identifier texture = new Identifier(DwMod.MODID, "textures/gui/tool/frequency_detector.png");

    public FrequencyDetectorScreen(FrequencyDetectorScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        context.drawTexture(this.texture, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }
}
