package h3o.ender;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import qouteall.q_misc_util.LifecycleHack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import h3o.ender.blocks.RegisterBlocks;
import h3o.ender.entities.RegisterEntities;
import h3o.ender.entities.Tardis;

public class DwMod implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final String MODID = "dwmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		
		LOGGER.info("DwMod init!");
		LifecycleHack.markNamespaceStable("dwmod");


		FabricDefaultAttributeRegistry.register(RegisterEntities.TARDIS, Tardis.createLivingAttributes());
		RegisterBlocks.register();
	}
}