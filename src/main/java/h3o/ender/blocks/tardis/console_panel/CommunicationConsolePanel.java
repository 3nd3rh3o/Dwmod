package h3o.ender.blocks.tardis.console_panel;

import h3o.ender.blockEntity.tardis.console_panel.CommunicationConsolePanelBE;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class CommunicationConsolePanel extends HorizontalFacingBlock implements BlockEntityProvider {

    public CommunicationConsolePanel(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
    }

    

    @Override
    protected void appendProperties(Builder<Block, BlockState> builder) {
        builder.add(Properties.HORIZONTAL_FACING);
    }



    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }



    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CommunicationConsolePanelBE(pos, state);
    }
    
}
