package net.bossdragon.screen;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.managers.TagManager;
import net.bossdragon.system.*;
import net.bossdragon.system.base.AttachmentSystem;
import net.bossdragon.system.base.MovementSystem;
import net.bossdragon.system.base.PositionSystem;
import net.bossdragon.system.base.events.EventSystem;
import net.bossdragon.system.view.PlayerCharacterAnimSystem;
import net.bossdragon.system.view.SoldierCharacterAnimSystem;
import net.bossdragon.system.view.render.DeferredRendererSetSystem;
import net.bossdragon.system.view.render.RenderSystem;
import net.bossdragon.system.view.render.debug.CollisionScaledDebugSystem;
import net.mostlyoriginal.api.plugin.extendedcomponentmapper.ExtendedComponentMapperPlugin;
import net.mostlyoriginal.api.screen.core.WorldScreen;
import net.namekdev.entity_tracker.EntityTracker;
import net.namekdev.entity_tracker.network.EntityTrackerServer;
import se.feomedia.orion.system.OperationSystem;

/**
 *
 */
public class GameScreen extends WorldScreen {
    @Override
    protected World createWorld() {
        return new World(new WorldConfigurationBuilder()
            .with(
                new AssetSystem(),
                new EntityFactorySystem(),
                new WorldInitSystem()
            )
            .with(
                new OperationSystem(),
                new InputSystem(),
                new EventSystem(),
                new EnemySoldierSpawnSystem(),
                new EnemySoldierFightAISystem(),
                new EnemySoldierStateSystem(),
                new CharacterStateSystem(),
                new AttachmentSystem(),
                new PositionSystem(),
                new CollisionSystem(),
                new CharacterMapCollisionSystem(),
                new MovementSystem(),// for everything but Player
                new LogicalToGraphicalPositionmentSystem(),
                new PlayerCharacterAnimSystem(),
                new SoldierCharacterAnimSystem(),
                new RenderSystem(),
                new CollisionScaledDebugSystem()
            )
            .with(
                new DeferredRendererSetSystem()
            )
            .with(
                new TagManager()
//              ,	createEntityTracker()
            )
            .with(
                new ExtendedComponentMapperPlugin()
            )
            .build());
    }

    EntityTracker createEntityTracker() {
        EntityTrackerServer tracker = new EntityTrackerServer();
        tracker.start();
        return new EntityTracker(tracker);
    }
}
