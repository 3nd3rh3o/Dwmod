package h3o.ender.blockEntity;

import h3o.ender.DwMod;
import h3o.ender.blockEntity.tardis.RotorBaseBE;
import h3o.ender.blocks.RegisterBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class RegisterBlockEntities {
    public static final BlockEntityType<RotorBaseBE> ROTOR_BASE_BE = Registry.register(Registries.BLOCK_ENTITY_TYPE,
            new Identifier(DwMod.MODID, "tardis.console.rotor_base"),
            FabricBlockEntityTypeBuilder.create(RotorBaseBE::new, RegisterBlocks.ROTOR_BASE).build());
}
