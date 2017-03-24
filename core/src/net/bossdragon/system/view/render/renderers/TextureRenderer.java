package net.bossdragon.system.view.render.renderers;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.bossdragon.component.base.Transform;
import net.bossdragon.component.render.Colorized;
import net.bossdragon.component.render.Renderable;
import net.bossdragon.component.render.TextureComponent;
import net.bossdragon.system.view.render.RenderBatchingSystem;

/**
 *
 */
public class TextureRenderer implements RenderBatchingSystem.EntityProcessAgent {
    ComponentMapper<TextureComponent> mTexture;
    ComponentMapper<Transform> mTransform;
    ComponentMapper<Colorized> mColorized;

    private final World world;
    private final SpriteBatch batch;
    private final Color oldColor = Color.WHITE.cpy();


    public TextureRenderer(World world, SpriteBatch batch) {
        this.world = world;
        this.batch = batch;
        mTexture = world.getMapper(TextureComponent.class);
        mTransform = world.getMapper(Transform.class);
        mColorized = world.getMapper(Colorized.class);
    }

    @Override
    public void begin() {
        oldColor.set(batch.getColor());
        batch.begin();
    }

    @Override
    public void end() {
        batch.end();
        batch.setColor(oldColor);
    }

    @Override
    public void process(Entity e) {
        TextureComponent texture = mTexture.get(e);
        Transform transform = mTransform.get(e);
        Colorized colorized = mColorized.getSafe(e, null);

        float width = texture.texture.getRegionWidth();
        float height = texture.texture.getRegionHeight();

        float originX = transform.originX * width;
        float originY = transform.originY * height;
        float x = transform.position.x + transform.displacement.x - originX;
        float y = transform.position.y + transform.displacement.y - originY;

        // TODO size?.scale
        float scaleX = transform.flipX ? -1f : 1f;
        float scaleY = transform.flipY ? -1f : 1f;

        batch.setBlendFunction(texture.blendSrcFunc, texture.blendDestFunc);

        if (colorized != null)
            batch.setColor(colorized.color);
        else
            batch.setColor(oldColor);

        batch.draw(
            texture.texture,
            x, y, originX, originY,
            width, height,
            scaleX, scaleY, transform.rotation
        );
    }

    @Override
    public int getType() {
        return Renderable.TEXTURE;
    }
}
