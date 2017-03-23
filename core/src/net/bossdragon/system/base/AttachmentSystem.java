package net.bossdragon.system.base;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.Vector2;
import net.bossdragon.component.base.Attached;
import net.bossdragon.component.base.Position;
import net.mostlyoriginal.api.plugin.extendedcomponentmapper.M;

/**
 *
 */
public class AttachmentSystem extends EntityProcessingSystem {
    public AttachmentSystem() {
        super(Aspect.all(Attached.class, Position.class));
    }

    M<Attached> mAttached;
    M<Position> mPosition;

    private final Vector2 tmpPos = new Vector2();

    @Override
    protected void process(Entity e) {
        final Attached att = mAttached.get(e);
        final Position pos = mPosition.get(e);
        final Position attPos = mPosition.get(att.entityId);

        tmpPos.set(attPos.desiredPos).add(att.displace);
        pos.xy(tmpPos);
    }
}
