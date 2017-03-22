package net.bossdragon.system


import com.artemis.BaseSystem
import com.artemis.annotations.Wire
import net.bossdragon.system.view.render.RenderSystem

@Wire
class WorldInitSystem : BaseSystem() {
    lateinit var factorySystem: EntityFactorySystem
    lateinit var renderSystem: RenderSystem

    internal var isInitialized = false


    internal fun _initialize() {
        factorySystem.createMap()
        factorySystem.createPlayer()
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
