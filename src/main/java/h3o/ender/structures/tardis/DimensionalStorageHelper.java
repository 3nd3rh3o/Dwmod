package h3o.ender.structures.tardis;

import net.minecraft.util.math.BlockPos;

public class DimensionalStorageHelper {
    // Tardis internal slots
    private static final int maxTx = 18;
    private static final int maxTz = 18;

    public static BlockPos getBasePosFromTardisIndex(int index) {
        return new BlockPos(
                (index % maxTx) * 2536,
                64,
                (Math.round(index / maxTx) % maxTz) * 2536);
    }
}
