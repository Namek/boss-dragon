package net.bossdragon.screen

import com.artemis.World
import com.artemis.WorldConfigurationBuilder
import com.artemis.managers.TagManager
import net.bossdragon.system.*
import net.bossdragon.system.base.MovementSystem

import net.bossdragon.system.base.PositionSystem
import net.bossdragon.system.base.TimeProgressingSystem
import net.bossdragon.system.base.collision.CollisionDebugSystem
import net.bossdragon.system.base.collision.CollisionDetectionSystem
import net.bossdragon.system.base.events.EventSystem
import net.bossdragon.system.view.render.DeferredRendererSetSystem
import net.bossdragon.system.view.render.RenderSystem
import net.mostlyoriginal.api.plugin.extendedcomponentmapper.ExtendedComponentMapperPlugin
import net.mostlyoriginal.api.screen.core.WorldScreen
import net.namekdev.entity_tracker.EntityTracker
import net.namekdev.entity_tracker.network.EntityTrackerServer

class GameScreen : WorldScreen() {
    override fun createWorld(): World {

        return World(WorldConfigurationBuilder()
            .with(
                AssetSystem(),
                EntityFactorySystem(),
                WorldInitSystem()
            )
            .with(
                InputSystem(),
                PlayerStateSystem(),
                EventSystem(),
                TimeProgressingSystem(),
                PositionSystem(),
                CollisionSystem(),
                MovementSystem(),
                RenderSystem(),
                CollisionDebugSystem(),
                DeferredRendererSetSystem()
            )
            .with(
                TagManager()
//              ,	createEntityTracker()
            )
            .with(
                ExtendedComponentMapperPlugin()
            )
            .build())
    }

    fun createEntityTracker(): EntityTracker {
        val tracker = EntityTrackerServer()
        tracker.start();
        return EntityTracker(tracker)
    }
}
