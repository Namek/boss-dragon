package net.bossdragon.system

import com.artemis.BaseSystem
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import net.bossdragon.enums.Animations
import net.bossdragon.enums.Assets
import net.bossdragon.enums.C
import net.bossdragon.system.view.render.anim.KeyFrameAnimationsMap
import net.bossdragon.util.collections.tuples.TripleList

class AssetSystem : BaseSystem() {
    override fun processSystem() {
        load()
        isEnabled = false
    }


    lateinit var playerCharacterAnims: KeyFrameAnimationsMap
    lateinit var soldierAnims: KeyFrameAnimationsMap
    lateinit var explosionAnim: KeyFrameAnimationsMap
    lateinit var ceilingTex: TextureRegion
    lateinit var fireballTex: TextureRegion
    lateinit var playgroundTex: TextureRegion

    private fun load() {
        // player animations
        val stickManAnimationsTexture = TextureRegion(Texture("graphics/stickman_anim.png"))
        var frames = stickManAnimationsTexture.split(Assets.Character.Width, Assets.Character.Height)
        val framesCountPerAnimation = frames[0].size

        val animations = TripleList.create<String, Float, Int>()
            .add(Animations.StickMan.WALK_RIGHT, 0.12f, 0)
            .add(Animations.StickMan.WALK_LEFT, 0.12f, 1)
            .add(Animations.StickMan.IDLE, 0.5f, 2)
            .add(Animations.StickMan.JUMP, 0.07f, 3)
            .add(Animations.StickMan.LYING, 0.5f, 4)

        playerCharacterAnims = KeyFrameAnimationsMap.create(animations, frames)
        soldierAnims = playerCharacterAnims


        // explosion
        val explosionTexture = TextureRegion(Texture("graphics/explosion.png"))
        frames = explosionTexture.split(explosionTexture.regionWidth, explosionTexture.regionHeight)
        explosionAnim = KeyFrameAnimationsMap.create(0.1f, frames[0])

        // textures
        ceilingTex = TextureRegion(Texture("graphics/ceiling.jpg"))
        fireballTex = TextureRegion(Texture("graphics/fireball.png"))
        playgroundTex = TextureRegion(Texture("graphics/playground.png"))
    }
}
