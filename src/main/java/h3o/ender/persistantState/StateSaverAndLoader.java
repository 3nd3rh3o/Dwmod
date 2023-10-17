package h3o.ender.persistantState;

import java.util.ArrayList;
import java.util.List;

import h3o.ender.DwMod;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

public class StateSaverAndLoader extends PersistentState {

    public List<Integer> tardis;

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putIntArray("tardis", tardis);
        return nbt;
    }

    public static StateSaverAndLoader createFromNbt(NbtCompound tag) {
        StateSaverAndLoader state = new StateSaverAndLoader();
        state.tardis = new ArrayList<Integer>();
        for (int val : tag.getIntArray("tardis")) {
            state.tardis.add(val);
        }
        return state;
    }

    private static Type<StateSaverAndLoader> type = new Type<>(
            StateSaverAndLoader::new,
            StateSaverAndLoader::createFromNbt,
            null
    );

    public static StateSaverAndLoader getServerState(MinecraftServer server) {
        PersistentStateManager persistentStateManager = server.getWorld(World.OVERWORLD).getPersistentStateManager();
        StateSaverAndLoader state = persistentStateManager.getOrCreate(type, DwMod.MODID);
        state.markDirty();
        return state;
    }
    
}
