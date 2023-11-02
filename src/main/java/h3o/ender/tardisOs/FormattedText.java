package h3o.ender.tardisOs;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import net.minecraft.client.font.TextRenderer;
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

    // TODO test later
    public static List<Text> wrapStyledText(MutableText text, int maxWidth, TextRenderer textRenderer) {
        List<Text> wrappedText = new ArrayList<>();
        List<MutableText> words = new ArrayList<>();

        text.visit((style, asString) -> {

            String[] split = asString.split("\\s+");
            for (String word : split) {
                if (!word.isEmpty()) {
                    MutableText wordText = Text.literal(word).setStyle(style);
                    words.add(wordText);
                }
            }
            return Optional.empty();
        }, Style.EMPTY);

        MutableText currentLine = Text.empty();
        int currentLineWidth = 0;

        for (MutableText wordText : words) {
            int wordWidth = textRenderer.getWidth(wordText);
            if (!currentLine.getString().isEmpty()) {
                wordWidth += textRenderer.getWidth(" ");
            }

            if (currentLineWidth + wordWidth > maxWidth && !currentLine.getString().isEmpty()) {
                wrappedText.add(currentLine);
                currentLine = Text.empty().append(wordText);
                currentLineWidth = wordWidth;
            } else {
                if (!currentLine.getString().isEmpty()) {
                    currentLine.append(" ");
                }
                currentLine.append(wordText);
                currentLineWidth += wordWidth;
            }
        }

        if (!currentLine.getString().isEmpty()) {
            wrappedText.add(currentLine);
        }

        return wrappedText;
    }
}
