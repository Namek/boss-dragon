package net.bossdragon.system


import net.bossdragon.system.base.collision.CollisionDetectionSystem
import net.bossdragon.enums.CollisionGroups as CG

import com.artemis.BaseSystem
import com.artemis.annotations.Wire
import com.badlogic.gdx.math.Vector3
import net.bossdragon.system.view.render.RenderSystem
import net.bossdragon.system.view.render.debug.TopDownEntityDebugSystem

@Wire
class WorldInitSystem : BaseSystem() {
    lateinit var factorySystem: EntityFactorySystem
    lateinit var renderSystem: RenderSystem
    lateinit var topViewDebugSystem: TopDownEntityDebugSystem

    internal var isInitialized = false


    internal fun _initialize() {
        setCollisionRelations()

        factorySystem.createTestBg()
        factorySystem.createPlayer()
    }

    internal fun setCollisionRelations() {
        val relations = world.getSystem(CollisionDetectionSystem::class.java).relations
        relations.connectGroups(CG.PLAYER, CG.ENEMY)
        relations.connectGroups(CG.BURNING_AREA, CG.PLAYER or CG.ENEMY)
        relations.connectGroups(CG.FIREBALL, CG.PLAYER or CG.ENEMY)
        relations.connectGroups(CG.BULLET, CG.DRAGON)
    }

    override fun processSystem() {
        if (!isInitialized) {
            _initialize()
            isInitialized = true

            // the system is neeed no more to process every frame
            isEnabled = false
        }
    }
}
