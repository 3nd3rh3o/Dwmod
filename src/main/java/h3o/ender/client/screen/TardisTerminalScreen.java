package h3o.ender.client.screen;

import java.util.HashMap;
import java.util.List;


import h3o.ender.DwMod;
import h3o.ender.blockEntity.tardis.TerminalBE;
import h3o.ender.components.Circuit;
import h3o.ender.components.Circuit.LOCATION;
import h3o.ender.components.Circuit.NAME;
import h3o.ender.networking.ModMessages;
import h3o.ender.screenHandler.TardisTerminalScreenHandler;
import h3o.ender.tardisOs.FormattedText;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class TardisTerminalScreen extends HandledScreen<TardisTerminalScreenHandler> {
    private final Identifier texture = new Identifier(DwMod.MODID, "textures/gui/frequency_detector_screen.png");
    private String prompt;
    private MutableText text;
    private NbtList nbt;

    @SuppressWarnings("resource")
    public TardisTerminalScreen(TardisTerminalScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        prompt = ((TerminalBE) MinecraftClient.getInstance().world.getBlockEntity(handler.getPos())).getPrompt();
        text = ((TerminalBE) MinecraftClient.getInstance().world.getBlockEntity(handler.getPos())).getText();
        nbt = ((TerminalBE) MinecraftClient.getInstance().world.getBlockEntity(handler.getPos())).getTardisCircuits();
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
        prompt = ((TerminalBE) this.client.world.getBlockEntity(handler.getPos())).getPrompt();
        text = ((TerminalBE) this.client.world.getBlockEntity(handler.getPos())).getText();
        nbt = ((TerminalBE) this.client.world.getBlockEntity(handler.getPos())).getTardisCircuits();
        HashMap<Circuit.NAME, Circuit.LOCATION> missingComp = new HashMap<>();
        missingComp.put(NAME.MAIN_SPACE_TIME_ELEMENT, LOCATION.ROTOR_BASE);
        missingComp.put(NAME.LLO_ENERGY_CONNECTOR, LOCATION.ROTOR_BASE);
        for (int y = 0; y < nbt.size(); y++) {
            if (Circuit.strToName(nbt.getList(y).getString(0))
                    .equals(Circuit.NAME.MAIN_SPACE_TIME_ELEMENT)) {
                missingComp.remove(NAME.MAIN_SPACE_TIME_ELEMENT, LOCATION.ROTOR_BASE);
            }
            if (Circuit.strToName(nbt.getList(y).getString(0))
                    .equals(Circuit.NAME.LLO_ENERGY_CONNECTOR)
                    && Circuit.strToLoc(nbt.getList(y).getString(1))
                            .equals(Circuit.LOCATION.ROTOR_BASE)) {
                missingComp.remove(NAME.LLO_ENERGY_CONNECTOR, LOCATION.ROTOR_BASE);
            }
        }
        List<MutableText> list;
        if (missingComp.size() == 0) {
            list = FormattedText.wrapStyledText(text, 160, textRenderer);
            FormattedText pText = FormattedText.empty();
            for (String word : prompt.split(" ")) {
                pText = pText.normal(word);
            }
            list.addAll(FormattedText
                    .wrapStyledText(pText.assemble(), 160, textRenderer));
        } else {
            FormattedText text = FormattedText.empty();
            for (NAME name : missingComp.keySet()) {

                text = text.error("-MISSING KEY COMPONENT : ").info(name.toString())
                        .error(" IN CONSOLE PANEL : ")
                        .info(missingComp.get(name).toString()).endLine();
            }
            list = FormattedText.wrapStyledText(text.assemble(), 160,
                    textRenderer);
        }
        for (int i = 0; i < list.size(); i++) {
            context.drawText(textRenderer, list.get(i), (width - backgroundWidth) / 2 + 8,
                    (height - backgroundHeight) / 2 + 8 + i * 9, 0, false);
        }

    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.client.options.inventoryKey.matchesKey(keyCode, scanCode)) {
            return false;
        }
        if (keyCode == 259) {
            if (prompt.length() == 1) {
                return true;
            }
            ClientPlayNetworking.send(ModMessages.TARDIS_TERMINAL_OPT_ID,
                        PacketByteBufs.create().writeString("suppr"));
            prompt = prompt.substring(0, prompt.length()-1);
            return true;
        }
        if (keyCode == 257) {
            ClientPlayNetworking.send(ModMessages.TARDIS_TERMINAL_OPT_ID,
                        PacketByteBufs.create().writeString("exec"));
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    

    @Override
    public boolean charTyped(char chr, int modifiers) {
        ClientPlayNetworking.send(ModMessages.TARDIS_TERMINAL_CHAR_ID,
                        PacketByteBufs.create().writeString(String.valueOf(chr)));
        return true;
    }
}
