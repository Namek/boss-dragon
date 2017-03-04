package net.bossdragon.component.render

import com.artemis.PooledComponent
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.TextureRegion

class TextureComponent : PooledComponent() {
    var texture: TextureRegion? = null

    var blendSrcFunc = GL20.GL_SRC_ALPHA
    var blendDestFunc = GL20.GL_ONE_MINUS_SRC_ALPHA


    override fun reset() {
        texture = null
    }
}
