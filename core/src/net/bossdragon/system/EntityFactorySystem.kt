package net.bossdragon.system

import com.artemis.Archetype
import com.artemis.ArchetypeBuilder
import com.artemis.Entity
import com.artemis.EntityEdit
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
import net.bossdragon.component.base.Progressing
import net.bossdragon.component.base.Velocity
import net.bossdragon.component.render.anim.KeyFrameAnimations
import net.bossdragon.enums.Assets
import net.bossdragon.enums.C
import net.bossdragon.enums.CollisionGroups
import net.bossdragon.enums.Tags
import net.bossdragon.system.base.collision.Collider

import net.bossdragon.system.view.render.RenderSystem
import net.bossdragon.system.view.render.anim.KeyFrameAnimationsMap
import net.bossdragon.util.collections.tuples.TripleList
import net.mostlyoriginal.api.system.core.PassiveSystem

@Wire
class EntityFactorySystem : PassiveSystem() {
    lateinit internal var assets: AssetSystem
    lateinit internal var renderSystem: RenderSystem
    lateinit internal var tags: TagManager


    internal var fireballArchetype: Archetype? = null
    lateinit var renderableArchetype: Archetype


    override fun initialize() {
        renderableArchetype = ArchetypeBuilder()
            .add(Transform::class.java)
            .add(Size::class.java)
            .add(Renderable::class.java)
            .build(world)

        //		energyBallArchetype = new ArchetypeBuilder()
        //			.add(EnergyBall.class)
        //			.add(DecalComponent.class)
        //			.add(Transform.class)
        //			.add(Renderable.class)
        //			.add(Damage.class)
        //			.add(Growable.class)
        //			.build(world);
    }

    fun createRenderable(texturePath: String? = null, width: Float? = null, height: Float? = null): EntityEdit {
        val entity = world.createEntity()
        val e = entity.edit()

        val texture = if (texturePath != null) TextureRegion(Texture(texturePath)) else null
        val size = e.create(Size::class.java)

        if (width != null && height != null)
            size.set(width, height)
        else if (texture != null)
            size.set(texture.regionWidth.toFloat(), texture.regionHeight.toFloat())

        val renderable = e.create(Renderable::class.java)

        if (texture != null) {
            e.create(TextureComponent::class.java)
                .texture = texture

            renderable.type = Renderable.TEXTURE
        }

        return e
    }

    fun createTestBg(): Entity {
        val entity = world.createEntity()
        val e = entity.edit()
        e.add(Player())
        val size = e.create(Size::class.java)
            .set(100f, 100f)

        e.create(Transform::class.java)
            .xy(-size.width/2, -size.height/2)

        e.create(Renderable::class.java)
            .type(Renderable.TEXTURE)

        e.create(TextureComponent::class.java)
            .texture = TextureRegion(Texture("graphics/ceiling.jpg"))

        e.create(Collider::class.java)
            .groups(CollisionGroups.WALL)

        return entity
    }

    fun createPlayer(): Entity {
        val entity = world.createEntity()
        val e = entity.edit()

        tags.register(Tags.Player, entity)

        e.create(Transform::class.java)

        val size = e.create(Size::class.java)
            .set(Assets.StickMan.Width.toFloat(), Assets.StickMan.Height.toFloat())
            .origin(0f, 0f)

        e.create(Renderable::class.java)
            .type(Renderable.ANIM)

        e.create(Progressing::class.java)
        e.create(KeyFrameAnimations::class.java)
            .setup(assets.playerAnims)

        e.create(Player::class.java)
        e.create(Collider::class.java)
            .groups(CollisionGroups.PLAYER)
            .spatialConstantSize(Assets.StickMan.ColliderWidth, Assets.StickMan.ColliderHeight)

        e.create(Velocity::class.java)
            .maxSpeed(C.Player.MaxSpeed)
            .friction(C.Player.Friction)


        return entity
    }
}
