package h3o.ender.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public class Tardis extends AnimalEntity {

    //TODO add fields here

    protected Tardis(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public PassiveEntity createChild(ServerWorld arg0, PassiveEntity arg1) {
        return null;
    }
    
}
