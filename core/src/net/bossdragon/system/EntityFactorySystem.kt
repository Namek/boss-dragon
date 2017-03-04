package net.bossdragon.system

import com.artemis.Archetype
import com.artemis.Entity
import com.artemis.annotations.Wire
import com.artemis.managers.TagManager
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import net.bossdragon.component.Player
import net.bossdragon.component.base.Size
import net.bossdragon.component.base.Transform
import net.bossdragon.component.render.Renderable
import net.bossdragon.component.render.TextureComponent

import net.bossdragon.system.view.render.RenderSystem
import net.mostlyoriginal.api.system.core.PassiveSystem

@Wire
class EntityFactorySystem : PassiveSystem() {
    internal var renderSystem: RenderSystem? = null
    internal var tags: TagManager? = null


    internal var fireballArchetype: Archetype? = null


    override fun initialize() {
        //		energyBallArchetype = new ArchetypeBuilder()
        //			.add(EnergyBall.class)
        //			.add(DecalComponent.class)
        //			.add(Transform.class)
        //			.add(Renderable.class)
        //			.add(Damage.class)
        //			.add(Growable.class)
        //			.build(world);
    }

    fun createPlayer(): Entity {
        val entity = world.createEntity()
        val e = entity.edit()
        e.add(Player())
        val size = e.create(Size::class.java).set(100f, 100f)
        e.create(Transform::class.java).xy(-size.width/2, -size.height/2)

        e.create(Renderable::class.java).type(Renderable.TEXTURE)
        e.create(TextureComponent::class.java)
            .texture = TextureRegion(Texture("graphics/ceiling.jpg"))

        return entity
    }
}
