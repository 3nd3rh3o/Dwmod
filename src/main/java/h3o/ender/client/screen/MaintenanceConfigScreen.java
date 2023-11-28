package h3o.ender.client.screen;

import java.util.List;

import h3o.ender.DwMod;
import h3o.ender.blockEntity.tardis.TerminalBE;
import h3o.ender.screenHandler.MaintenanceConfigScreenHandler;
import h3o.ender.structures.tardis.DimensionalStorageHelper;
import h3o.ender.structures.tardis.Room;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class MaintenanceConfigScreen extends HandledScreen<MaintenanceConfigScreenHandler> {
    private final Identifier texture = new Identifier(DwMod.MODID, "textures/gui/astralmap.png");
    private List<Room> intSh;

    @SuppressWarnings("resource")
    public MaintenanceConfigScreen(MaintenanceConfigScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.intSh = DimensionalStorageHelper.filterEngine(((TerminalBE) MinecraftClient.getInstance().world.getBlockEntity(handler.getPos())).getTardisInternalScheme());
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        context.drawTexture(this.texture, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
    }
}
