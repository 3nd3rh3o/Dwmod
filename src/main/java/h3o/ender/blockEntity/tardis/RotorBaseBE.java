package h3o.ender.blockEntity.tardis;

import h3o.ender.blockEntity.RegisterBlockEntities;
import h3o.ender.entities.Tardis;
import h3o.ender.structures.tardis.DimensionalStorageHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.util.GeckoLibUtil;

public class RotorBaseBE extends BlockEntity implements GeoBlockEntity {
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    private NbtCompound tardisCircuits;

    public RotorBaseBE(BlockPos pos, BlockState state) {
        super(RegisterBlockEntities.ROTOR_BASE_BE, pos, state);
    }
    
    

    @Override
    public void registerControllers(ControllerRegistrar controllers) {
        return;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }

    

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    

    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }



    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.tardisCircuits = nbt.getCompound("Circuits");
    }



    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.put("Circuits", DimensionalStorageHelper.getTardis(world.getServer(), pos).getDataTracker().get(Tardis.CIRCUITS));
        super.writeNbt(nbt);
    }



    public NbtList getTardisCircuits() {
        return tardisCircuits.getList("Circuits", NbtElement.LIST_TYPE);
    }

    
    

}
