package net.bossdragon.system.view.render

import net.bossdragon.component.render.InferRenderer
import net.bossdragon.component.render.Renderable
import net.bossdragon.system.view.render.RenderBatchingSystem.EntityProcessAgent

import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.Entity
import com.artemis.systems.EntityProcessingSystem
import net.mostlyoriginal.api.plugin.extendedcomponentmapper.M


class DeferredRendererSetSystem : EntityProcessingSystem(
    Aspect.all(InferRenderer::class.java, Renderable::class.java)
) {
    lateinit internal var mInferRenderer: M<InferRenderer>
    lateinit internal var mRenderable: M<Renderable>

    lateinit internal var renderSystem: RenderBatchingSystem


    override fun process(entity: Entity) {
        val renderable = mRenderable.getSafe(entity, null)

        val removeInferTag = renderable == null || renderable.type != Renderable.NONE
        val inferRenderer = renderable != null && renderable.type != Renderable.NONE

        if (inferRenderer) {
            val renderer = renderSystem.getRendererByType(renderable!!.type)
            renderSystem.registerAgent(entity, renderer)
        }

        if (removeInferTag) {
            mInferRenderer.remove(entity)
        }
    }

}
