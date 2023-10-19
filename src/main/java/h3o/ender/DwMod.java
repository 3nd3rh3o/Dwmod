package h3o.ender;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.util.Identifier;
import qouteall.q_misc_util.LifecycleHack;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import h3o.ender.blocks.RegisterBlocks;
import h3o.ender.entities.RegisterEntities;
import h3o.ender.entities.Tardis;
import h3o.ender.persistantState.StateSaverAndLoader;

public class DwMod implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MODID = "dwmod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	public static final Identifier TARDIS = new Identifier(MODID, "tardis");

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("DwMod init!");
		LifecycleHack.markNamespaceStable("dwmod");

		FabricDefaultAttributeRegistry.register(RegisterEntities.TARDIS, Tardis.createLivingAttributes());
		RegisterBlocks.register();

		ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
			if (!world.isClient && entity.getClass().equals(Tardis.class)) {
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

					// not usefull in this case, no need to tell the client about server logic
					/*
					 * MinecraftServer server = world.getServer();
					 * PacketByteBuf data = PacketByteBufs.create();
					 * 
					 * int[] arr = new int[serverState.tardis.size()];
					 * for (Integer val : serverState.tardis) {
					 * arr[serverState.tardis.indexOf(val)] = val.intValue();
					 * }
					 * data.writeIntArray(arr);
					 * 
					 * List<ServerPlayerEntity> serverPlayerEntities =
					 * server.getPlayerManager().getPlayerList();
					 * for (ServerPlayerEntity serverPlayerEntity : serverPlayerEntities) {
					 * server.execute(() -> {
					 * ServerPlayNetworking.send(serverPlayerEntity, TARDIS, data);
					 * });
					 * }
					 */
				}
			}
		});

		ServerEntityEvents.ENTITY_UNLOAD.register((entity, world) -> {
			if (entity instanceof Tardis && entity.isRemoved()) {
				Tardis ent = (Tardis) entity;
				ent.purgeIntPortals();
				if (!world.isClient) {
					//TODO kill each portal inside each rooms of the TARDIS removed (foreachroom -> purge room)
					StateSaverAndLoader serverState = StateSaverAndLoader.getServerState(world.getServer());
					if (serverState.tardis == null) {
						serverState.tardis = new ArrayList<>();
					}
					serverState.tardis.remove(ent.getIndex());
				}
			}

		});
	}
}