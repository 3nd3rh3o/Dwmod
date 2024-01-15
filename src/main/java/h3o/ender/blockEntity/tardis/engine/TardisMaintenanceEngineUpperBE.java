package h3o.ender.blockEntity.tardis.engine;

import h3o.ender.blockEntity.RegisterBlockEntities;
import h3o.ender.blockEntity.tardis.TardisBentDependant;
import h3o.ender.entities.Tardis;
import h3o.ender.structures.tardis.DimensionalStorageHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.util.GeckoLibUtil;

public class TardisMaintenanceEngineUpperBE extends BlockEntity implements GeoBlockEntity,TardisBentDependant {
    private Tardis trds;
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public TardisMaintenanceEngineUpperBE(BlockPos pos, BlockState state) {
        super(RegisterBlockEntities.TARDIS_MAINTENANCE_ENGINE_UPPER_BE, pos, state);
    }

    @Override
    public void register() {
        this.trds = DimensionalStorageHelper.getTardis(world.getServer(), pos);
        if (trds != null) {
            trds.registerBlock(this);
        }
    }

    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public void unRegister() {
        if (trds != null) {
            trds.unRegisterBlock(this);
        }
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    @Override
    public void registerControllers(ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }
    
}
