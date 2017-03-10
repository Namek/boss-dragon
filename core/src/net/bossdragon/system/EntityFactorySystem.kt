package net.bossdragon.system

import com.artemis.Archetype
import com.artemis.ArchetypeBuilder
import com.artemis.Entity
import com.artemis.EntityEdit
import com.artemis.annotations.Wire
import com.artemis.managers.TagManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import net.bossdragon.component.Player
import net.bossdragon.component.base.Position
import net.bossdragon.component.base.Size
import net.bossdragon.component.base.Transform
import net.bossdragon.component.base.Velocity
import net.bossdragon.component.render.Renderable
import net.bossdragon.component.render.TextureComponent
import net.bossdragon.component.render.anim.KeyFrameAnimations
import net.bossdragon.enums.Assets
import net.bossdragon.enums.C
import net.bossdragon.enums.Tags
import net.bossdragon.system.base.collision.Collider
import net.bossdragon.system.view.render.RenderSystem
import net.mostlyoriginal.api.system.core.PassiveSystem
import net.bossdragon.enums.CollisionGroups as CG

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

    fun createPlayer(): Entity {
        val entity = world.createEntity()
        val e = entity.edit()

        tags.register(Tags.Player, entity)

        e.create(Transform::class.java)
        e.create(Position::class.java)

        val originX = 0.5f
        val originY = 0f

        e.create(Size::class.java)
            .set(Assets.Character.Width.toFloat(), Assets.Character.Height.toFloat())
            .origin(originX, originY)

        e.create(Renderable::class.java)
            .type(Renderable.ANIM)

        e.create(KeyFrameAnimations::class.java)
            .setup(assets.playerAnims)

        e.create(Player::class.java)
        e.create(Collider::class.java)
            .groups(CG.CHARACTER)
            .setCircular(Assets.Character.ColliderRadius)

        e.create(Velocity::class.java)
            .maxSpeed(C.Player.MaxSpeed)
            .friction(C.Player.Friction)


        return entity
    }

    fun createFireball(pos: Vector2, dir: Vector2): Entity {
        val entity = world.createEntity()
        val e = entity.edit()

        e.create(Transform::class.java)
            .xy(pos)

        e.create(Position::class.java)
            .xy(pos)

        e.create(Velocity::class.java)
            .maxSpeed(C.Fireball.MaxSpeed)
            .setVelocityAtMax(dir)

        e.create(Renderable::class.java)
            .type(Renderable.TEXTURE)

        e.create(TextureComponent::class.java)
            .texture = assets.fireballTex

        e.create(Size::class.java)
            .set(C.Fireball.Size, C.Fireball.Size)
            .origin(0.5f, 0.5f)

        e.create(Collider::class.java)
            .groups(CG.FIREBALL)
            .setCircular(C.Fireball.Size/2)

        return entity
    }

    fun createMap(): Entity {
        val entity = world.createEntity()
        val e = entity.edit()

        tags.register(Tags.Map, e.entityId)

        e.create(Transform::class.java)
            .xy(0f, 0f)

        e.create(Size::class.java)
            .set(C.Map.Width, C.Map.Height)
            .origin(0.5f, 0.5f)

        e.create(Position::class.java)
            .xy(0f, 0f)

        e.create(Collider::class.java)
            .groups(CG.WALL)
            .setCircular(C.Map.LogicalRadius)

        e.create(Renderable::class.java)
            .type(Renderable.TEXTURE)

        e.create(TextureComponent::class.java)
            .texture = assets.playgroundTex


        return entity
    }
}
