package h3o.ender.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class FrequencyDetector extends Item {

    public FrequencyDetector(Settings settings) {
        super(settings.maxCount(1));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        //TODO if sneak, open screen, else scan (if settings selected(check if settings available for dimension, else show error))
        return super.use(world, user, hand);
    }
}