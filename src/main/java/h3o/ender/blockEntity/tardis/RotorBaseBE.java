package h3o.ender.blockEntity.tardis;

import h3o.ender.blockEntity.RegisterBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.util.GeckoLibUtil;

public class RotorBaseBE extends BlockEntity implements GeoBlockEntity {
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public RotorBaseBE(BlockPos pos, BlockState state) {
        super(RegisterBlockEntities.ROTOR_BASE_BE, pos, state);
    }

    @Override
    public void registerControllers(ControllerRegistrar controllers) {
        // TODO
        return;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }

    

}
