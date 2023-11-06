package h3o.ender.tardisOs;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class FormattedText {
    private List<MutableText> content = new ArrayList<>();
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
        this.first = first;
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

    public static List<MutableText> wrapStyledText(MutableText text, int maxWidth, TextRenderer textRenderer) {
        List<MutableText> wrappedText = new ArrayList<>();
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

            if (currentLineWidth + wordWidth > maxWidth && !currentLine.getString().isEmpty()
                    || wordText.getStyle().equals(Style.EMPTY.withColor(Formatting.BLACK))) {
                if (wordText.getStyle().equals(Style.EMPTY.withColor(Formatting.BLACK))) {
                    wordText = Text.empty();
                }
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

    public FormattedText endLine() {
        return this.withColor("empty", Formatting.BLACK);
    }

    public static NbtList toNbt(MutableText text) {
        NbtList nbt = new NbtList();
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

        for (MutableText word : words) {
            NbtCompound subNbt = new NbtCompound();
            subNbt.putString("Text", word.getString());
            subNbt.putString("Style", colorToString(word.getStyle()));
            nbt.add(subNbt);
        }

        return nbt;
    }

    public static MutableText fromNbt(NbtList nbt) {
        MutableText text = Text.empty();
        for (int i = 0; i < nbt.size(); i++) {
            text.append(Text.empty().append(nbt.getCompound(i).getString("Text")).setStyle(stringToColor(nbt.getCompound(i).getString("Style"))));
        }
        return text;
    }

    private static Style stringToColor(String str) {
        if (str.equals("nL")) {
            return Style.EMPTY.withColor(Formatting.BLACK);
        }
        if (str.equals("i")) {
            return Style.EMPTY.withColor(Formatting.GOLD);
        }
        if (str.equals("e")) {
            return Style.EMPTY.withColor(Formatting.RED);
        }
        if (str.equals("n")) {
            return Style.EMPTY.withColor(Formatting.WHITE);
        }
        return Style.EMPTY;
    }

    private static String colorToString(Style style) {
        if (style.equals(Style.EMPTY.withColor(Formatting.BLACK))) {
            return "nL";
        }
        if (style.equals(Style.EMPTY.withColor(Formatting.GOLD))) {
            return "i";
        }
        if (style.equals(Style.EMPTY.withColor(Formatting.RED))) {
            return "e";
        }
        if (style.equals(Style.EMPTY.withColor(Formatting.WHITE))) {
            return "n";
        }
        return "";
    }

    public static MutableText trim(MutableText text, int i) {
        List<MutableText> words = new ArrayList<>();
        List<MutableText> line = new ArrayList<>();
        MutableText res = Text.empty();
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
        MutableText currLine = Text.empty();
        for (MutableText word : words) {
            currLine.append(word);
            if (word.getStyle().equals(Style.EMPTY.withColor(Formatting.BLACK))) {
                line.add(currLine);
                currLine = Text.empty();
            }
        }
        if (line.size() <= i) {
            return text;
        }
        for (int n = line.size()-i; n<line.size(); n++) {
            res.append(line.get(n));
        }
        return res;
    }
}
