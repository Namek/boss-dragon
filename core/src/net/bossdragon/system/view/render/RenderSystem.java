package net.bossdragon.system.view.render;

import com.artemis.Aspect;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.bossdragon.component.Player;
import net.bossdragon.component.base.Transform;
import net.bossdragon.component.render.Renderable;
import net.bossdragon.system.view.render.renderers.KeyFrameAnimRenderer;
import net.bossdragon.system.view.render.renderers.TextureRenderer;
import net.mostlyoriginal.api.plugin.extendedcomponentmapper.M;

/**
 * Basic renderer supporting layers and different renderers.
 *
 * @author Namek
 * @see Renderable
 */
@Wire(injectInherited = true)
public class RenderSystem extends RenderBatchingSystem {
    M<Transform> mTransform;
    M<Player> mPlayer;

    TagManager tagManager;

    OrthographicCamera camera;
    SpriteBatch spriteBatch;
    TextureRenderer textureRenderer;
    KeyFrameAnimRenderer keyFrameAnimRenderer;


    @Override
    public void initialize() {
        super.initialize();

        world.getAspectSubscriptionManager().get(Aspect.all(Renderable.class))
            .addSubscriptionListener(this);

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(w, h);

        spriteBatch = new SpriteBatch();
        textureRenderer = new TextureRenderer(world, spriteBatch);
        keyFrameAnimRenderer = new KeyFrameAnimRenderer(world, spriteBatch);
    }

    @Override
    public EntityProcessAgent getRendererByType(int type) {
        switch (type) {
            case Renderable.TEXTURE: return textureRenderer;
            case Renderable.ANIM: return keyFrameAnimRenderer;
            default: throw new UnsupportedOperationException("Getting unknown renderer type: " + type);
        }
    }

    @Override
    public void processSystem() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);
        super.processSystem();

//        val entity = tagManager.getEntity(Tags.Player)
//        val transform = mPosition.get(entity)
//        val player = mPlayer.get(entity)

        /*
        camera.position
                .set(transform.currentPos)
                .add(transform.displacement)
                .add(0, player.eyeAltitude, 0)

        transform.toDirection(camera.direction)
        transform.toUpDir(camera.up)

        camera.update()

        super.processSystem()
        shaderProvider.updateAll(world.getDelta())*/
    }
}
