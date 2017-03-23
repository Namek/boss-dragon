package net.bossdragon.system.base;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import net.bossdragon.component.Player;
import net.bossdragon.component.base.Position;
import net.mostlyoriginal.api.plugin.extendedcomponentmapper.M;

/**
 *
 */
public class MovementSystem extends EntityProcessingSystem {
    public MovementSystem() {
        super(Aspect.all(Position.class, Player.class));
    }

    M<Position> mPosition;

    @Override
    protected void process(Entity e) {
        final Position pos = mPosition.get(e);
        pos.currentPos.set(pos.desiredPos);
    }
}
