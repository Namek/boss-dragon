package net.bossdragon.screen

import com.artemis.World
import com.artemis.WorldConfigurationBuilder
import com.artemis.managers.TagManager
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import net.bossdragon.system.*
import net.bossdragon.system.base.MovementSystem
import net.bossdragon.system.base.PositionSystem
import net.bossdragon.system.base.collision.CollisionDebugSystem
import net.bossdragon.system.base.events.EventSystem
import net.bossdragon.system.base.physics.PhysicsDebugRenderSystem
import net.bossdragon.system.base.physics.PhysicsSystem
import net.bossdragon.system.view.PlayerCharacterAnimSystem
import net.bossdragon.system.view.render.DeferredRendererSetSystem
import net.bossdragon.system.view.render.RenderSystem
import net.mostlyoriginal.api.plugin.extendedcomponentmapper.ExtendedComponentMapperPlugin
import net.mostlyoriginal.api.screen.core.WorldScreen
import net.namekdev.entity_tracker.EntityTracker
import net.namekdev.entity_tracker.network.EntityTrackerServer
import se.feomedia.orion.system.OperationSystem

class GameScreen : WorldScreen() {
    override fun createWorld(): World {
        val cfg = WorldConfigurationBuilder()
            .with(
                AssetSystem(),
                EntityFactorySystem(),
                WorldInitSystem()
            )
            .with(
                OperationSystem(),
                InputSystem(),
                PlayerStateSystem(),
                EventSystem(),
                PositionSystem(),
//                CollisionSystem(),
                PhysicsSystem(Vector2(0f, 0f)),
                MovementSystem(),
                PlayerCharacterAnimSystem(),
                RenderSystem(),
//                CollisionDebugSystem(),
                PhysicsDebugRenderSystem()
            )
            .with(
                DeferredRendererSetSystem()
            )
            .with(
                TagManager()
//              ,	createEntityTracker()
            )
            .with(
                ExtendedComponentMapperPlugin()
            )
            .build()

        cfg.register(OrthographicCamera(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat()))

        return return World(cfg)
    }

    fun createEntityTracker(): EntityTracker {
        val tracker = EntityTrackerServer()
        tracker.start();
        return EntityTracker(tracker)
    }
}
