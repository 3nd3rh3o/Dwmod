package h3o.ender.tardisOs;

import net.minecraft.text.MutableText;

public interface Command {
    MutableText execute(String[] args);
    void parse(String[] ars);
}
