package net.bossdragon.system.view.render;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySubscription;
import com.artemis.utils.Bag;
import com.artemis.utils.IntBag;
import net.bossdragon.component.render.InferRenderer;
import net.bossdragon.component.render.Renderable;
import net.mostlyoriginal.api.utils.BagUtils;

/**
 * Extensible rendering system.
 *
 * Create specialized rendering systems with DeferredEntityProcessingSystems.
 * RenderBatchingSystem will take care of overarching concerns like grouping
 * and sorting, while the specialist systems take care of the actual rendering.
 *
 * Currently only supports one specialist handling system per entity.
 *
 * @author Daan van Yperen
 * @author Namek
 * @see net.mostlyoriginal.api.component.graphics.Anim
 */
public class RenderBatchingSystem extends BaseSystem implements EntitySubscription.SubscriptionListener {
    ComponentMapper<Renderable> mRenderable;
    ComponentMapper<InferRenderer> mInferRenderer;

    protected Bag<Job> sortedJobs;
    private boolean sortedDirty = false;

    // this is included here to make sure that this system was installed
    private DeferredRendererSetSystem inferDeferredSystem;


    @Override
    public void initialize() {
        sortedJobs = new Bag(Job.class);
    }

    public EntityProcessAgent getRendererByType(int type) {
        throw new RuntimeException("you should implement this method");
    }


    @Override
    public void inserted(IntBag entities) {
        int i = 0;
        final int n = entities.size();
        while (i < n) {
            int entityId = entities.get(i);
            Renderable renderable = mRenderable.get(entityId);

            if (renderable.type == Renderable.NONE) {
                mInferRenderer.create(entityId);
            }
            else {
                registerAgent(entityId, getRendererByType(renderable.type));
            }
            ++i;
        }
    }

    @Override
    public void removed(IntBag entities) {
        int i = 0;
        final int n = entities.size();
        while (i < n) {
            int entityId = entities.get(i);
            Renderable renderable = mRenderable.get(entityId);

            unregisterAgent(entityId, getRendererByType(renderable.type));
            ++i;
        }
    }

    /**
     * Declare entity relevant for agent.
     *
     * After this is called, the principal can use the agent
     * interface to begin/endLeft and process the given entity.
     *
     * @param entityId entity to process
     * @param agent interface to dispatch with.
     */
    public void registerAgent(int entityId, EntityProcessAgent agent) {
        if (!mRenderable.has(entityId)) {
            throw new RuntimeException("RenderBatchingSystem requires agents entities to have component Renderable.");
        }

        // register new job. this will influence sorting order.
        sortedJobs.add(new Job(entityId, agent));
        sortedDirty = true;
    }

    public void registerAgent(Entity entity, EntityProcessAgent agent) {
        registerAgent(entity.getId(), agent);
    }

    /**
     * Revoke relevancy of entity for agent.
     *
     * After this is called, the principal should no longer
     * attempt to process the entity with the agent.
     *
     * @param entity entity to process
     * @param agent interface to dispatch with.
     */
    public void unregisterAgent(Entity entity, EntityProcessAgent agent) {
        unregisterAgent(entity.getId(), agent);
    }

    public void unregisterAgent(int entityId, EntityProcessAgent agent) {
        // forget about the job.
        final Job[] data = sortedJobs.getData();
        int i = 0;
        final int s = sortedJobs.size();
        while (i < s) {
            if (data[i].entityId == entityId && data[i].agent == agent) {
                sortedJobs.remove(i);
                sortedDirty = true;
                break;
            }
            i++;
        }
    }

    @Override
    protected void processSystem() {
        if (sortedDirty) {
            // sort our jobs (by layer).
            sortedDirty = false;
            BagUtils.sort(sortedJobs);
        }

        // iterate through all the jobs.
        // @todo add support for entities being deleted.
        EntityProcessAgent activeAgent = null;
        int activeAgentType = Integer.MIN_VALUE;
        int i = 0;
        final int s = sortedJobs.size();
        final Job[] jobs = sortedJobs.getData();
        while (i < s) {
            Job job = jobs[i];
            EntityProcessAgent agent = (EntityProcessAgent) job.agent;
            int agentType = agent.getType();

            // agent changed? endLeft() the last agent, and begin() the next agent.
            // @todo extend this with eventual texture/viewport/etc demarcation.
            if (agentType != activeAgentType) {
                if (activeAgent != null) {
                    activeAgent.end();
                }
                activeAgent = agent;
                activeAgentType = agentType;
                activeAgent.begin();
            }

            // process the entity!
            Entity entity = world.getEntity(job.entityId);
            processByAgent(agent, entity);
            i++;
        }

        // finished, terminate final agent.
        if (activeAgent != null) {
            activeAgent.end();
        }
    }


    protected void processByAgent(EntityProcessAgent agent, Entity entity) {
        agent.process(entity);
    }

    @Override
    protected boolean checkProcessing() {
        return true;
    }

    /** Rendering job wrapper. */
    class Job implements Comparable<Job> {
        final int entityId;
        final EntityProcessAgent agent;

        /**
         *
         * @param entityId entity we will process
         * @param  agent agent responsible for processing
         */
        public Job(int entityId, EntityProcessAgent agent) {
            this.entityId = entityId;
            this.agent = agent;
        }

        @Override
        public int compareTo(Job o) {
            return mRenderable.get(this.entityId).layer - mRenderable.get(o.entityId).layer;
        }
    }


    /**
     * Delegated processing is achieved by implementing
     * the EntityProcessAgent interface.
     *
     * @author Daan van Yperen
     * @author Namek
     */
    interface EntityProcessAgent {

        /** Prepare to receive a set of entities.  */
        void begin();

        /** Done receiving entities.  */
        void end();

        /** Process the entity.  */
        void process(Entity entity);

        /** Gets agent type to differ from other agents.  */
        int getType();

    }
}
