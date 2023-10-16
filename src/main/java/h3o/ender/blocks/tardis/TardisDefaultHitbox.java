package h3o.ender.blocks.tardis;

import net.minecraft.block.AbstractGlassBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;

public class TardisDefaultHitbox extends AbstractGlassBlock {
    public static final BooleanProperty OPENNED = BooleanProperty.of("openned");
    public static final BooleanProperty UPPER = BooleanProperty.of("upper");

    public TardisDefaultHitbox(Settings settings) {
        super(settings.nonOpaque().blockVision(Blocks::never).solidBlock(Blocks::never));
        setDefaultState(getDefaultState().with(OPENNED, false).with(UPPER, false));
    }

    @Override
    protected void appendProperties(Builder<Block, BlockState> builder) {
        builder.add(OPENNED).add(UPPER);
    }    
}
