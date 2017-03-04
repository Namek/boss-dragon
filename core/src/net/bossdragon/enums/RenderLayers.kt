package net.bossdragon.enums

import net.bossdragon.component.render.Renderable

/**
 * Higher value is in front, lower is behind.

 * @see Renderable.layer
 */
object RenderLayers {
    val WORLD = 0
    val ENTITIES = 1
    val HUD = 2
}
