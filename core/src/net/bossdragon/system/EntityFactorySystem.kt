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
import net.bossdragon.enums.Tags
import net.bossdragon.system.base.collision.Collider
import net.bossdragon.system.view.render.RenderSystem
import net.bossdragon.system.view.render.anim.KeyFrameAnimationsMap
import net.mostlyoriginal.api.plugin.extendedcomponentmapper.M
import net.mostlyoriginal.api.system.core.PassiveSystem

@Wire
class EntityFactorySystem : PassiveSystem() {
    lateinit internal var assets: AssetSystem
    lateinit internal var renderSystem: RenderSystem
    lateinit internal var tags: TagManager

    lateinit internal var mPosition: M<Position>
    lateinit internal var mVelocity: M<Velocity>
    lateinit internal var mCollider: M<Collider>


    internal var fireballArchetype: Archetype? = null
    lateinit var renderableArchetype: Archetype


    override fun initialize() {
        renderableArchetype = ArchetypeBuilder()
            .add(Transform::class.java)
            .add(Renderable::class.java)
            .build(world)
    }

    fun setKeyFramesAnim(e: EntityEdit, anim: KeyFrameAnimationsMap): KeyFrameAnimations {
        e.create(Renderable::class.java)
            .type(Renderable.ANIM)

        return e.create(KeyFrameAnimations::class.java)
            .setup(anim)
    }

    fun setTexture(e: EntityEdit, tex: TextureRegion) {
        e.create(Renderable::class.java)
            .type(Renderable.TEXTURE)

        e.create(TextureComponent::class.java)
            .texture = tex
    }

    fun createPlayer(): Entity {
        val entity = world.createEntity()
        val e = entity.edit()

        tags.register(Tags.Player, entity)

        // logic
        e.create(Player::class.java)

        e.create(Position::class.java)

        e.create(Collider::class.java)
            .groups(CG.CHARACTER)
            .setCircular(Assets.Character.ColliderRadius)

        e.create(Velocity::class.java)
            .maxSpeed(C.Player.MaxSpeed)
            .friction(C.Player.Friction)

        // visuals
        e.create(Transform::class.java)
            .origin(0.5f, 0f)

        setKeyFramesAnim(e, assets.playerCharacterAnims)

        return entity
    }

    /**
     * Does NOT set up [FightAI] component.
     */
    fun createSoldierEnemy(startPos: Vector2): Entity {
        val entity = world.createEntity()
        val e = entity.edit()

        tags.register(Tags.Soldier, e.entityId)

        // logic
        e.create(Soldier::class.java)

        e.create(Position::class.java)
            .xy(startPos)

        e.create(Collider::class.java)
            .groups(CG.SOLDIER)
            .setCircular(Assets.Soldier.ColliderRadius)

        e.create(Velocity::class.java)
            .maxSpeed(C.Soldier.MaxSpeed)
            .friction(C.Soldier.Friction)


        // visuals
        e.create(Transform::class.java)
            .origin(0.5f, 0f)

        setKeyFramesAnim(e, assets.soldierAnims)
            .setAnimation(Animations.StickMan.IDLE)

        return entity
    }

    fun createFireball(pos: Vector2, dir: Vector2): Entity {
        val entity = world.createEntity()
        val e = entity.edit()

        e.create(Transform::class.java)
            .xy(pos)
            .origin(0.5f, 0.5f)

        e.create(Position::class.java)
            .xy(pos)

        e.create(Velocity::class.java)
            .maxSpeed(C.Fireball.MaxSpeed)
            .setVelocityAtMax(dir)

        setTexture(e, assets.fireballTex)

        e.create(Collider::class.java)
            .groups(CG.FIREBALL)
            .setCircular(C.Fireball.Size/2)

        return entity
    }

    fun createMap(): Entity {
        val entity = world.createEntity()
        val e = entity.edit()

        tags.register(Tags.Map, e.entityId)


        e.create(Position::class.java)
            .xy(0f, 0f)

        e.create(Collider::class.java)
            .groups(CG.WALL)
            .setCircular(C.Map.LogicalRadius)

        e.create(Transform::class.java)
            .xy(0f, 0f)
            .origin(0.5f, 0.5f)
            .flipX = true

        setTexture(e, assets.playgroundTex)

        return entity
    }
}
