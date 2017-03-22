package net.bossdragon.system.view.render

/**
 * @author Daan van Yperen
 * *
 * @author Namek
 */

import net.bossdragon.component.render.InferRenderer
import net.mostlyoriginal.api.utils.BagUtils

import com.artemis.BaseSystem
import com.artemis.ComponentMapper
import com.artemis.Entity
import com.artemis.EntitySubscription.SubscriptionListener
import com.artemis.utils.Bag
import com.artemis.utils.IntBag

/**
 * Extensible rendering system.
 *
 *
 * Create specialized rendering systems with DeferredEntityProcessingSystems.
 * RenderBatchingSystem will take care of overarching concerns like grouping
 * and sorting, while the specialist systems take care of the actual rendering.
 *
 *
 * Currently only supports one specialist handling system per entity.

 * @author Daan van Yperen
 * *
 * @see net.mostlyoriginal.api.component.graphics.Anim
 */
open class RenderBatchingSystem : BaseSystem(), SubscriptionListener {
    lateinit protected var mRenderable: ComponentMapper<Renderable>
    lateinit private var mInferRenderer: ComponentMapper<InferRenderer>

    lateinit protected var sortedJobs: Bag<Job?>
    var sortedDirty = false

    // this is included here to make sure that this system was installed
    lateinit private var inferDeferredSystem: DeferredRendererSetSystem


    override fun initialize() {
        sortedJobs = Bag(Job::class.java)
    }

    /**
     * Expected to be overriden.
     */
    open fun getRendererByType(type: Int): EntityProcessAgent {
        throw RuntimeException("you should implement this method")
    }

    override fun inserted(entities: IntBag) {
        var i = 0
        val n = entities.size()
        while (i < n) {
            val entityId = entities.get(i)
            val renderable = mRenderable.get(entityId)

            if (renderable.type == Renderable.NONE) {
                mInferRenderer.create(entityId)
            }
            else {
                registerAgent(entityId, getRendererByType(renderable.type))
            }
            ++i
        }
    }

    override fun removed(entities: IntBag) {
        var i = 0
        val n = entities.size()
        while (i < n) {
            val entityId = entities.get(i)
            val renderable = mRenderable.get(entityId)

            unregisterAgent(entityId, getRendererByType(renderable.type))
            ++i
        }
    }

    /**
     * Declare entity relevant for agent.

     * After this is called, the principal can use the agent
     * interface to begin/endLeft and process the given entity.

     * @param e entity to process
     * *
     * @param agent interface to dispatch with.
     */
    fun registerAgent(entityId: Int, agent: EntityProcessAgent) {
        if (!mRenderable.has(entityId)) {
            throw RuntimeException("RenderBatchingSystem requires agents entities to have component Renderable.")
        }

        // register new job. this will influence sorting order.
        sortedJobs.add(Job(entityId, agent))
        sortedDirty = true
    }

    fun registerAgent(entity: Entity, agent: EntityProcessAgent) {
        registerAgent(entity.id, agent)
    }

    /**
     * Revoke relevancy of entity for agent.

     * After this is called, the principal should no longer
     * attempt to process the entity with the agent.

     * @param entity entity to process
     * *
     * @param agent interface to dispatch with.
     */
    fun unregisterAgent(entity: Entity, agent: EntityProcessAgent) {
        unregisterAgent(entity.id, agent)
    }

    fun unregisterAgent(entityId: Int, agent: EntityProcessAgent) {
        // forget about the job.
        val data = sortedJobs.data
        var i = 0
        val s = sortedJobs.size()
        while (i < s) {
            if (data[i]!!.entityId == entityId && data[i]!!.agent === agent) {
                sortedJobs.remove(i)
                sortedDirty = true
                break
            }
            i++
        }
    }

    override fun processSystem() {
        if (sortedDirty) {
            // sort our jobs (by layer).
            sortedDirty = false
            BagUtils.sort(sortedJobs)
        }

        // iterate through all the jobs.
        // @todo add support for entities being deleted.
        var activeAgent: EntityProcessAgent? = null
        var activeAgentType = Integer.MIN_VALUE
        var i = 0
        val s = sortedJobs.size()
        while (i < s) {
            val job = sortedJobs.data[i]
            val agent = job!!.agent as EntityProcessAgent
            val agentType = agent.type

            // agent changed? endLeft() the last agent, and begin() the next agent.
            // @todo extend this with eventual texture/viewport/etc demarcation.
            if (agentType != activeAgentType) {
                if (activeAgent != null) {
                    activeAgent.end()
                }
                activeAgent = agent
                activeAgentType = agentType
                activeAgent.begin()
            }

            // process the entity!
            val entity = world.getEntity(job.entityId)
            processByAgent(agent, entity)
            i++
        }

        // finished, terminate final agent.
        if (activeAgent != null) {
            activeAgent.end()
        }
    }

    /**
     * Can be overriden for debug purposes.
     */
    protected fun processByAgent(agent: EntityProcessAgent, entity: Entity) {
        agent.process(entity)
    }

    override fun checkProcessing(): Boolean {
        return true
    }

    /** Rendering job wrapper.  */
    inner class Job
    /**
     * @param entity entity we will process
     * *
     * @param agent agent responsible for processing.
     */
    (val entityId: Int, val agent: EntityProcessAgent) : Comparable<Job> {

        override fun compareTo(o: Job): Int {
            return mRenderable.get(this.entityId).layer - mRenderable.get(o.entityId).layer
        }
    }

    /**
     * Delegated processing is achieved by implementing
     * the EntityProcessAgent interface.

     * @author Daan van Yperen
     * *
     * @author Namek
     */
    interface EntityProcessAgent {

        /** Prepare to receive a set of entities.  */
        fun begin()

        /** Done receiving entities.  */
        fun end()

        /** Process the entity.  */
        fun process(entity: Entity)

        /** Gets agent type to differ from other agents.  */
        val type: Int

    }
}
