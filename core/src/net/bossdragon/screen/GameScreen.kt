package net.bossdragon.screen

import com.artemis.World
import com.artemis.WorldConfigurationBuilder
import com.artemis.managers.TagManager
import net.bossdragon.system.EntityFactorySystem
import net.bossdragon.system.InputSystem

import net.bossdragon.system.WorldInitSystem
import net.bossdragon.system.base.PositionSystem
import net.bossdragon.system.base.collision.CollisionDetectionSystem
import net.bossdragon.system.base.events.EventSystem
import net.bossdragon.system.view.render.DeferredRendererSetSystem
import net.bossdragon.system.view.render.RenderSystem
import net.bossdragon.system.view.render.debug.TopDownEntityDebugSystem
import net.mostlyoriginal.api.plugin.extendedcomponentmapper.ExtendedComponentMapperPlugin
import net.mostlyoriginal.api.screen.core.WorldScreen
import net.namekdev.entity_tracker.EntityTracker
import net.namekdev.entity_tracker.network.EntityTrackerServer

class GameScreen : WorldScreen() {
    override fun createWorld(): World {

        return World(WorldConfigurationBuilder()
            .with(
                EntityFactorySystem(),
                WorldInitSystem()
            )
            .with(
                EventSystem(),
                PositionSystem(),
                CollisionDetectionSystem(),
                RenderSystem(),
                DeferredRendererSetSystem(),
                TopDownEntityDebugSystem(),
                InputSystem()
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
