package h3o.ender.items;

import java.util.function.Consumer;
import java.util.function.Supplier;

import h3o.ender.blocks.RegisterBlocks;
import h3o.ender.client.item.renderer.LLOEnergyDistributorRenderer;
import h3o.ender.components.Circuit;
import h3o.ender.components.Circuit.LOCATION;
import h3o.ender.entities.Tardis;
import h3o.ender.structures.tardis.DimensionalStorageHelper;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.RenderProvider;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.util.GeckoLibUtil;

public class LLOEnergyDistributor extends Item implements GeoItem {
    private AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);

    public LLOEnergyDistributor(Settings settings) {
        super(settings);
    }

    @Override
    public Supplier<Object> getRenderProvider() {
        return renderProvider;
    }

    @Override
    public void registerControllers(ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this,
                (animationState) -> animationState.setAndContinue(RawAnimation.begin().thenLoop("idle"))));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (!context.getWorld().isClient()) {
            BlockPos pos = context.getBlockPos();
            ServerWorld world = ((ServerWorld) context.getWorld());
            Tardis tardis = DimensionalStorageHelper.getTardis(world.getServer(), pos);
            Circuit circuit = new Circuit(Circuit.NAME.LLO_ENERGY_CONNECTOR, LOCATION.ROTOR_BASE);
            if (world.getBlockState(pos).getBlock().equals(RegisterBlocks.ROTOR_BASE)
                    && !Circuit.contains(tardis.getDataTracker().get(Tardis.CIRCUITS), circuit)) {
                tardis.addCircuit(circuit);
                ItemStack stack = context.getPlayer().getStackInHand(context.getHand());
                stack.setCount(stack.getCount() - 1);
                context.getPlayer().setStackInHand(context.getHand(), stack);
                world.getBlockEntity(pos).markDirty();
                world.getChunkManager().markForUpdate(pos);
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;

    }

    @Override
    public void createRenderer(Consumer<Object> consumer) {
        consumer.accept(new RenderProvider() {
            private final LLOEnergyDistributorRenderer renderer = new LLOEnergyDistributorRenderer();

            @Override
            public BuiltinModelItemRenderer getCustomRenderer() {
                return renderer;
            }
        });
    }
}
