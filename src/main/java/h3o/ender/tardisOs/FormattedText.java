package h3o.ender.tardisOs;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class FormattedText {
    private List<MutableText> content;
    private FormattedText next;
    private FormattedText first;

    private FormattedText(MutableText empty, FormattedText first) {
        this.content.add(empty);
        this.first = first;
    }

    private FormattedText(MutableText empty) {
        this.content.add(empty);
        this.first = this;
    }

    private FormattedText(List<MutableText> text, FormattedText first) {
        this.content.addAll(text);
    }

    public static FormattedText empty() {
        return new FormattedText(Text.empty());
    }

    public FormattedText error(String string) {
        return this.withColor(string, Formatting.RED);
    }

    public FormattedText info(String string) {
        return this.withColor(string, Formatting.GOLD);
    }

    private FormattedText withColor(String string, Formatting color) {
        List<MutableText> text = new ArrayList<>();
        String[] words = string.split(" ");
        for (int i = 0; i < words.length - 1; i++) {
            text.add(Text.literal(words[i] + " ").setStyle(Style.EMPTY.withColor(color)));
        }
        text.add(Text.literal(words[words.length - 1]).setStyle(Style.EMPTY.withColor(color)));
        this.next = new FormattedText(text, this.first);
        return next;
    }

    public MutableText assemble() {
        return first.assembleN();
    }

    private MutableText assembleN() {
        MutableText res = Text.empty();
        for (MutableText t : content) {
            res.append(t);
        }
        return res.append(next != null ? next.assembleN() : Text.empty());

    }

    public FormattedText normal(String string) {
        return this.withColor(string, Formatting.WHITE);
    }

}
