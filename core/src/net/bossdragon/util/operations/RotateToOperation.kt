package net.bossdragon.util.operations

import com.artemis.ComponentMapper
import com.artemis.annotations.Wire
import se.feomedia.orion.Executor
import se.feomedia.orion.Operation
import se.feomedia.orion.OperationTree
import se.feomedia.orion.operation.TemporalOperation

/**
 * Updates over [Transform.rotation]. Degrees.
 */
class RotateToOperation : TemporalOperation() {
    var src = 0f
    var dest = 0f

    override fun executorType(): Class<out Executor<out Operation>> {
        return RotateToExecutor::class.java
    }

    override fun reset() {
        super.reset()
        src = 0f
        dest = 0f
    }

    @Wire
    class RotateToExecutor : TemporalOperation.TemporalExecutor<RotateToOperation>() {
        lateinit var positionMapper: ComponentMapper<Transform>

        override fun begin(op: RotateToOperation, node: OperationTree?) {
            super.begin(op, node)
            val transform = positionMapper[op.entityId]
            op.src = transform.rotation
            op.dest = op.src
        }

        override fun act(delta: Float, percent: Float, op: RotateToOperation, node: OperationTree) {
            val transform = positionMapper[op.entityId]
            transform.rotation = op.interpolation.apply(op.src, op.dest, percent)
        }
    }
}
