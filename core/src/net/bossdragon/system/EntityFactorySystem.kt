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
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.physics.box2d.Shape
import net.bossdragon.component.Player
import net.bossdragon.component.base.Size
import net.bossdragon.component.base.Transform
import net.bossdragon.component.base.Velocity
import net.bossdragon.component.render.Renderable
import net.bossdragon.component.render.TextureComponent
import net.bossdragon.component.render.anim.KeyFrameAnimations
import net.bossdragon.enums.Assets
import net.bossdragon.enums.C
import net.bossdragon.enums.CollisionGroups
import net.bossdragon.enums.Tags
import net.bossdragon.system.base.collision.Collider
import net.bossdragon.system.base.physics.Physical
import net.bossdragon.system.base.physics.PhysicsSystem
import net.mostlyoriginal.api.system.core.PassiveSystem

@Wire
class EntityFactorySystem : PassiveSystem() {
    lateinit internal var assets: AssetSystem
    lateinit internal var tags: TagManager
    lateinit internal var physics: PhysicsSystem


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
            .texture = assets.ceilingTex

        e.create(Collider::class.java)
            .groups(CollisionGroups.WALL)

        return entity
    }

    fun createPlayer(): Entity {
        val entity = world.createEntity()
        val e = entity.edit()

        tags.register(Tags.Player, entity)

        e.create(Transform::class.java)

        val originX = 0.5f
        val originY = 0f

        e.create(Size::class.java)
            .set(Assets.StickMan.Width.toFloat(), Assets.StickMan.Height.toFloat())
            .origin(originX, originY)

        e.create(Renderable::class.java)
            .type(Renderable.ANIM)

        e.create(KeyFrameAnimations::class.java)
            .setup(assets.playerAnims)

        e.create(Player::class.java)
        e.create(Collider::class.java)
            .groups(CollisionGroups.CHARACTER)
            .spatialConstantSize(Assets.StickMan.ColliderWidth, Assets.StickMan.ColliderHeight)

        e.create(Velocity::class.java)
            .maxSpeed(C.Player.MaxSpeed)
            .friction(C.Player.Friction)


        return entity
    }

    fun createFireball_old(pos: Vector2, dir: Vector2): Entity {
        val entity = world.createEntity()
        val e = entity.edit()

        e.create(Transform::class.java)
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
            .groups(CollisionGroups.FIREBALL)

        return entity
    }

    fun createFireball(pos: Vector2, dir: Vector2): Entity {
        val entity = world.createEntity()
        val e = entity.edit()

        e.create(Transform::class.java)
            .xy(pos)

//        e.create(Velocity::class.java)
//            .maxSpeed(C.Fireball.MaxSpeed)
//            .setVelocityAtMax(dir)

        e.create(Renderable::class.java)
            .type(Renderable.TEXTURE)

        e.create(TextureComponent::class.java)
            .texture = assets.fireballTex

        val size = e.create(Size::class.java)
            .set(C.Fireball.Size, C.Fireball.Size)
            .origin(0.5f, 0.5f)

        val bodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.DynamicBody
        bodyDef.fixedRotation = true
        val body = physics.engine.createBody(bodyDef)
        val boxShape = PolygonShape()
        boxShape.setAsBox(size.width/2, size.height/2)
        body.createFixture(boxShape, 0.01f)

        e.create(Physical::class.java)
            .body = body

//        body.setLinearVelocity(dir.x*C.Fireball.MaxSpeed, dir.y*C.Fireball.MaxSpeed)

        return entity
    }
}
