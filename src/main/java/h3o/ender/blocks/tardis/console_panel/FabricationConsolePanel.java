package h3o.ender.blocks.tardis.console_panel;

import h3o.ender.blockEntity.tardis.console_panel.FabricationConsolePanelBE;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class FabricationConsolePanel extends ConsolePanel implements BlockEntityProvider {

    public FabricationConsolePanel(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FabricationConsolePanelBE(pos, state);
    }

}
