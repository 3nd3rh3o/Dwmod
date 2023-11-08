package h3o.ender.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;

public class Screwdriver extends Item {

    public Screwdriver(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (!context.getWorld().isClient()) {
            // TODO find a way to identify a block as valid. 
            // HashMap like tardisOs?
            return ActionResult.PASS;
        }
        return super.useOnBlock(context);
    }

    
    
}
