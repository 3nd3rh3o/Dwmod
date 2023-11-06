package h3o.ender;


import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerBlockEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.util.Identifier;
import qouteall.q_misc_util.LifecycleHack;
import software.bernie.geckolib.network.GeckoLibNetwork;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import h3o.ender.blockEntity.tardis.TardisBentDependant;
import h3o.ender.blocks.RegisterBlocks;
import h3o.ender.commands.RegisterCommands;
import h3o.ender.entities.RegisterEntities;
import h3o.ender.entities.Tardis;
import h3o.ender.itemGroup.RegisterItemGroups;
import h3o.ender.items.RegisterItems;
import h3o.ender.networking.ModMessages;
import h3o.ender.persistantState.StateSaverAndLoader;
import h3o.ender.screenHandler.RegisterScreenHandler;

public class DwMod implements ModInitializer {
	public static final String MODID = "dwmod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	public static final Identifier TARDIS = new Identifier(MODID, "tardis");

	@Override
	public void onInitialize() {

		LOGGER.info("DwMod init!");
		LifecycleHack.markNamespaceStable("dwmod");


		RegisterEntities.register();
		RegisterBlocks.register();
		RegisterItems.register();
		RegisterItemGroups.register();
		RegisterScreenHandler.register();
		ModMessages.registerC2Spackets();
		RegisterCommands.register();


		ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
			if (!world.isClient() && entity.getClass().equals(Tardis.class)) {
				GeckoLibNetwork.registerSyncedAnimatable((Tardis)entity);
				if (((Tardis) entity).getIndex() == -1) {
					StateSaverAndLoader serverState = StateSaverAndLoader.getServerState(world.getServer());
					if (serverState.tardis == null) {
						serverState.tardis = new ArrayList<>();
					}
					for (int i = 0; i < 305; i++) {
						if (!serverState.tardis.contains(i)) {
							((Tardis) entity).setIndex(i);
							serverState.tardis.add(i);
							((Tardis) entity).structureInit();
							break;
						}
					}
					if (((Tardis) entity).getIndex() == -1) {
						entity.kill();
					}
				}
			}
		});


		ServerEntityEvents.ENTITY_UNLOAD.register((entity, world) -> {
			if (entity instanceof Tardis && entity.isRemoved()) {
				Tardis ent = (Tardis) entity;
				ent.purgeIntPortals();
				if (!world.isClient()) {
					StateSaverAndLoader serverState = StateSaverAndLoader.getServerState(world.getServer());
					if (((Tardis)entity).getIndex() != -1) {
						serverState.tardis.removeIf(val -> val.equals(ent.getIndex()));
						DwMod.LOGGER.debug(serverState.tardis.toString());
					}
				}
			}
		});

		ServerBlockEntityEvents.BLOCK_ENTITY_LOAD.register((bEnt, world) -> {
			if (!world.isClient() && bEnt instanceof TardisBentDependant) {
				((TardisBentDependant)bEnt).register();
			}
		});

		ServerBlockEntityEvents.BLOCK_ENTITY_UNLOAD.register((bEnt, world) -> {
			if (!world.isClient() && bEnt instanceof TardisBentDependant) {
				((TardisBentDependant)bEnt).unRegister();
			}
		});
	}
}