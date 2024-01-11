package h3o.ender.blockEntity.tardis.engine;

import h3o.ender.blockEntity.RegisterBlockEntities;
import h3o.ender.blockEntity.tardis.TardisBentDependant;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class TardisMaintenanceEngineUpperBE extends BlockEntity implements TardisBentDependant {
    //TODO renderer, renderer registration!!!!
    public TardisMaintenanceEngineUpperBE(BlockPos pos, BlockState state) {
        super(RegisterBlockEntities.TARDIS_MAINTENANCE_ENGINE_UPPER_BE, pos, state);
    }

    @Override
    public void register() {
        //TODO
    }

    @Override
    public void unRegister() {
        //TODO 
    }
    
}
