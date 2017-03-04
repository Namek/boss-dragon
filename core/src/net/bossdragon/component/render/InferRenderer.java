package net.bossdragon.component.render;

import com.artemis.PooledComponent;
import net.bossdragon.system.view.render.DeferredRendererSetSystem;
import net.bossdragon.system.view.render.RenderBatchingSystem;

/**
 * Tags entities having {@link Renderable#type} set to {@link Renderable#NONE}
 * by the time of creation.
 *
 * @see DeferredRendererSetSystem
 * @see RenderBatchingSystem
 *
 * @author Namek
 */
public class InferRenderer extends PooledComponent {

    @Override
    protected void reset() {
    }

}
