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
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
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

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        int y = 0;

        for (int x = 0 ; x < 5; x++) {
            for (int z = 0; z < 5; z++) {
                int id = x + y * 5 + z * 25;
                for (Room room : intSh) {
                    if (room.getVId() == id) {
                        MatrixStack stack = context.getMatrices();
                        stack.push();
                        stack.translate(x*16 + width / 2, z*16 + height/2, 16);
                        stack.scale(-20, -20, -20);
                        ItemStack itemStack = new ItemStack(room.getName().getIcon());
                        BakedModel model = client.getItemRenderer().getModels().getModel(itemStack);
                        client.getItemRenderer().renderItem(itemStack, ModelTransformationMode.GUI, false, stack, ((VertexConsumerProvider) context.getVertexConsumers()), 0xF000F0, OverlayTexture.DEFAULT_UV, model);
                        stack.pop();
                    }
                }
            }
        }
    }

    
}
