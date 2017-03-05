package net.bossdragon.util.operations

import com.artemis.ComponentMapper
import com.artemis.annotations.Wire
import com.badlogic.gdx.math.Vector2
import net.bossdragon.component.base.Transform
import se.feomedia.orion.Executor
import se.feomedia.orion.Operation
import se.feomedia.orion.OperationTree
import se.feomedia.orion.operation.TemporalOperation

/**
 * Translation over [Transform.currentPos].
 */
class MoveToOperation : TemporalOperation() {
    val src = Vector2()
    val dest = Vector2()

    override fun executorType(): Class<out Executor<out Operation>> {
        return MoveToExecutor::class.java
    }

    override fun reset() {
        super.reset()
        src.setZero()
        dest.setZero()
    }

    @Wire
    class MoveToExecutor : TemporalOperation.TemporalExecutor<MoveToOperation>() {
        lateinit var positionMapper: ComponentMapper<Transform>

        override fun begin(op: MoveToOperation, node: OperationTree?) {
            super.begin(op, node)
            op.src.set(positionMapper[op.entityId].currentPos)
        }

        override fun act(delta: Float, percent: Float, op: MoveToOperation, node: OperationTree) {
            val transform = positionMapper[op.entityId]
            transform.currentPos
                .set(op.src)
                .interpolate(op.dest, percent, op.interpolation)
        }
    }
}
