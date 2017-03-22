package net.bossdragon.util.operations

import com.artemis.ComponentMapper
import com.artemis.annotations.Wire
import com.badlogic.gdx.math.Vector2
import se.feomedia.orion.Executor
import se.feomedia.orion.Operation
import se.feomedia.orion.OperationTree
import se.feomedia.orion.operation.TemporalOperation

/**
 * Translation over [Transform.displacement].
 */
class DisplaceByOperation : TemporalOperation() {
    val src = Vector2()
    val dest = Vector2()

    override fun executorType(): Class<out Executor<out Operation>> {
        return DisplaceByExecutor::class.java
    }

    override fun reset() {
        super.reset()
        src.setZero()
        dest.setZero()
    }

    @Wire
    class DisplaceByExecutor : TemporalOperation.TemporalExecutor<DisplaceByOperation>() {
        lateinit var positionMapper: ComponentMapper<Transform>

        override fun begin(op: DisplaceByOperation, node: OperationTree?) {
            super.begin(op, node)
            val transform = positionMapper[op.entityId]
            op.src.set(transform.displacement)
            op.dest.add(op.src)
        }

        override fun act(delta: Float, percent: Float, op: DisplaceByOperation, node: OperationTree) {
            val transform = positionMapper[op.entityId]
            transform.displacement
                .set(op.src)
                .interpolate(op.dest, percent, op.interpolation)
        }
    }
}
