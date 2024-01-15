package h3o.ender.blocks.tardis.engine;

import h3o.ender.blockEntity.tardis.engine.TardisMaintenanceEngineUpperBE;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class TardisMaintenanceEngineUpper extends HorizontalFacingBlock implements BlockEntityProvider {

    public TardisMaintenanceEngineUpper(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TardisMaintenanceEngineUpperBE(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    
    
}
