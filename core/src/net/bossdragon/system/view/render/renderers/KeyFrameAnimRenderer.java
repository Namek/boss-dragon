package net.bossdragon.system.view.render.renderers;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.bossdragon.component.base.Transform;
import net.bossdragon.component.render.Colorized;
import net.bossdragon.component.render.Renderable;
import net.bossdragon.component.render.anim.KeyFrameAnimations;
import net.bossdragon.system.view.render.RenderBatchingSystem;


public class KeyFrameAnimRenderer implements RenderBatchingSystem.EntityProcessAgent {
    ComponentMapper<KeyFrameAnimations> mFrames;
    ComponentMapper<Transform> mTransform;
    ComponentMapper<Colorized> mColorized;


    private final World world;
    private final SpriteBatch batch;
    private final Color oldColor = Color.WHITE.cpy();


    public KeyFrameAnimRenderer(World world, SpriteBatch batch) {
        this.world = world;
        this.batch = batch;
        mFrames = world.getMapper(KeyFrameAnimations.class);
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
        KeyFrameAnimations frames = mFrames.get(e);
        Transform transform = mTransform.getSafe(e, null);
        Colorized colorized = mColorized.getSafe(e, null);

        float x = 0f;
        float y = 0f;;
        float w = frames.width;
        float h = frames.height;
        float scaleX = 1f;
        float scaleY = 1f;
        float originX = 0f;
        float originY = 0f;
        float rotation = 0f;

        if (transform != null) {
            x += transform.position.x + transform.displacement.x;
            y += transform.position.y + transform.displacement.y;
            scaleX = transform.flipX ? -1f : 1f;
            scaleY = transform.flipY ? -1f : 1f;
            rotation = transform.rotation;
            originX = transform.originX;
            originY = transform.originY;
        }

        originX *= w;
        originY *= h;
        x -= originX;
        y -= originY;

        frames.stateTime += world.getDelta();

        TextureRegion tex = (TextureRegion) frames.currentAnimation.getKeyFrame(frames.stateTime, true);

        if (colorized != null)
            batch.setColor(colorized.color);
        else
            batch.setColor(oldColor);


        batch.draw(
            tex,
            x, y, originX, originY,
            w, h,
            scaleX, scaleY, rotation
        );
    }

    @Override
    public int getType() {
        return Renderable.ANIM;
    }
}
