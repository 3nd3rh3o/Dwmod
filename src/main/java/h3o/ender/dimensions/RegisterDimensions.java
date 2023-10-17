package h3o.ender.dimensions;

import h3o.ender.DwMod;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

public class RegisterDimensions {
    public static final RegistryKey<World> VORTEX = RegistryKey.of(RegistryKeys.WORLD,
            new Identifier(DwMod.MODID, "vortex"));
    public static final RegistryKey<DimensionType> VORTEX_TYPE = RegistryKey.of(RegistryKeys.DIMENSION_TYPE,
            new Identifier(DwMod.MODID, "vortex"));
}
