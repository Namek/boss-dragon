package net.bossdragon.component.render;

import com.artemis.PooledComponent;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureComponent extends PooledComponent {
    public TextureRegion texture;

    public int blendSrcFunc = GL20.GL_SRC_ALPHA;
    public int blendDestFunc = GL20.GL_ONE_MINUS_SRC_ALPHA;

    @Override
    protected void reset() {
        texture = null;
        blendSrcFunc = GL20.GL_SRC_ALPHA;
        blendDestFunc = GL20.GL_ONE_MINUS_SRC_ALPHA;
    }
}
