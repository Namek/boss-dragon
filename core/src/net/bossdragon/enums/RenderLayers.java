package net.bossdragon.enums;

import net.bossdragon.component.render.Renderable;

/**
 * Higher value is in front, lower is behind.

 * @see Renderable#layer
 */
public interface RenderLayers {
    int WORLD = 0;
    int ENTITIES = 1;
    int HUD = 2;
}
