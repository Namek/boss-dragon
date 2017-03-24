package net.bossdragon.system.view.render.anim;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import net.bossdragon.util.collections.RandomAccessMap;
import net.bossdragon.util.collections.tuples.TripleList;
import net.bossdragon.util.data.Triple;

public class KeyFrameAnimationsMap {
    private final RandomAccessMap<String, Animation> _map;


    public KeyFrameAnimationsMap() {
        _map = new RandomAccessMap<>();
    }

    public KeyFrameAnimationsMap(int initialSize) {
        _map = new RandomAccessMap<>(initialSize);
    }

    public void put(String name, Animation anim) {
        _map.put(name, anim);
    }

    public Animation get(String name) {
        return _map.get(name);
    }

    public int size() {
        return _map.size();
    }

    public static KeyFrameAnimationsMap create(TripleList<String, Float, Integer> anims, Array<Array<TextureRegion>> frames) {
        final KeyFrameAnimationsMap map = new KeyFrameAnimationsMap(anims.size());

        for (Triple<String, Float, Integer> triple : anims) {
            final String name = triple.Item1;
            final float frameDuration = triple.Item2;
            final Array<TextureRegion> keyFrames = frames.get(triple.Item3);
            final Animation anim = new Animation(frameDuration, keyFrames);

            map.put(name, anim);
        }

        return map;
    }

    public static KeyFrameAnimationsMap create(float frameDuration, Array<TextureRegion> frames) {
        KeyFrameAnimationsMap map = new KeyFrameAnimationsMap(1);
        Animation anim = new Animation(frameDuration, frames);
        map.put("default", anim);
        return map;
    }
}
