package h3o.ender.commands;

import static net.minecraft.server.command.CommandManager.*;

import h3o.ender.entities.Tardis;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class RegisterCommands {
    


    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("debug_reload_tardis_interrior").requires(source -> source.hasPermissionLevel(4)).executes(context -> {
            for (ServerWorld world : context.getSource().getServer().getWorlds()) {
                world.getEntitiesByClass(Tardis.class,
                    Box.of(new Vec3d(0, 0, 0), World.HORIZONTAL_LIMIT * 2, World.MAX_Y - World.MIN_Y,
                            World.HORIZONTAL_LIMIT * 2),
                    (entities) -> true).forEach(ent -> ent.structureInit());
            }
            context.getSource().sendMessage(Text.literal("Successfully rebuilt all interriors!"));
            return 1;
        })));
    }
}
