package net.bossdragon.component.render.anim;

import com.artemis.PooledComponent;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.bossdragon.system.view.render.anim.KeyFrameAnimationsMap;
import net.bossdragon.util.data.Tuple;

/**
 *
 */
public class KeyFrameAnimations extends PooledComponent {
    public Animation<?> currentAnimation = null;

    public KeyFrameAnimationsMap animations = null;
    public float width = 0;
    public float height = 0;
    public float stateTime = 0;

    @Override
    protected void reset() {
        currentAnimation = null;
        animations = null;
        width = height = 0;
        stateTime = 0;
    }

    public KeyFrameAnimations setup(Tuple<String, Animation<?>> animation, Tuple<String, Animation<?>>... otherAnimations) {
        this.animations = new KeyFrameAnimationsMap();
        this.currentAnimation = animation.Item2;

        animations.put(animation.Item1, animation.Item2);
        for (Tuple<String, Animation<?>> tuple : otherAnimations) {
            animations.put(tuple.Item1, tuple.Item2);
        }
        return this;
    }

    public KeyFrameAnimations setup(KeyFrameAnimationsMap animations) {
        this.animations = animations;
        TextureRegion firstFrame = (TextureRegion) animations.get(0).getKeyFrames()[0];
        this.width = firstFrame.getRegionWidth();
        this.height = firstFrame.getRegionHeight();
        currentAnimation = this.animations.get(0);

        return this;
    }

    public boolean setAnimation(int index) {
        final Animation<?> newAnim = animations.get(index);
        final boolean isChanging = newAnim != currentAnimation;

        if (isChanging)
            currentAnimation = animations.get(index);

        return isChanging;
    }

    public boolean setAnimation(String name) {
        final Animation<?> newAnim = animations.get(name);
        final boolean isChanging = newAnim != currentAnimation;

        if (isChanging)
            currentAnimation = animations.get(name);

        return isChanging;
    }
}
