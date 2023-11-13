package h3o.ender.blocks.tardis.console_panel;

import h3o.ender.blockEntity.tardis.console_panel.CommunicationConsolePanelBE;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class CommunicationConsolePanel extends ConsolePanel implements BlockEntityProvider {

    public CommunicationConsolePanel(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CommunicationConsolePanelBE(pos, state);
    }
    
}
