package net.bossdragon.system.view.render;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import net.bossdragon.component.render.InferRenderer;
import net.bossdragon.component.render.Renderable;
import net.mostlyoriginal.api.plugin.extendedcomponentmapper.M;

/**
 *
 */
public class DeferredRendererSetSystem extends EntityProcessingSystem {
    public DeferredRendererSetSystem() {
        super(Aspect.all(InferRenderer.class, Renderable.class));
    }

    M<InferRenderer> mInferRenderer;
    M<Renderable> mRenderable;

    RenderBatchingSystem renderSystem;

    @Override
    protected void process(Entity e) {
        Renderable renderable = mRenderable.getSafe(e, null);

        boolean removeInferTag = renderable == null || renderable.type != Renderable.NONE;
        boolean inferRenderer = renderable != null && renderable.type != Renderable.NONE;

        if (inferRenderer) {
            RenderBatchingSystem.EntityProcessAgent renderer = renderSystem.getRendererByType(renderable.type);
            renderSystem.registerAgent(e, renderer);
        }

        if (removeInferTag) {
            mInferRenderer.remove(e);
        }
    }
}
