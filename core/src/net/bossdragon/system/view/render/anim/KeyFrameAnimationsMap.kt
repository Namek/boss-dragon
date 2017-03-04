package net.bossdragon.system.view.render.anim

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import net.bossdragon.util.collections.RandomAccessMap
import net.bossdragon.util.collections.tuples.TripleList

class KeyFrameAnimationsMap {
    private val _map: RandomAccessMap<String, Animation<*>>


    constructor() {
        _map = RandomAccessMap<String, Animation<*>>()
    }

    constructor(initialSize: Int) {
        _map = RandomAccessMap<String, Animation<*>>(initialSize)
    }

    fun put(name: String, animation: Animation<*>) {
        _map.put(name, animation)
    }

    operator fun get(name: String): Animation<*>? {
        return _map.get(name)
    }

    operator fun get(index: Int): Animation<*> {
        return _map.get(index)
    }

    fun size(): Int {
        return _map.size
    }

    companion object {

        fun create(animations: TripleList<String, Float, Int>, frames: Array<Array<TextureRegion>>): KeyFrameAnimationsMap {
            val map = KeyFrameAnimationsMap(animations.size())

            for (triple in animations) {
                val name = triple.Item1
                val frameDuration = triple.Item2
                val keyFrames = frames[triple.Item3]
                val animation = Animation(frameDuration, *keyFrames)

                map.put(name, animation)
            }

            return map
        }

        fun create(frameDuration: Float, frames: Array<TextureRegion>): KeyFrameAnimationsMap {
            val map = KeyFrameAnimationsMap(1)

            val animation = Animation(frameDuration, *frames)
            map.put("default", animation)
            return map
        }
    }
}
