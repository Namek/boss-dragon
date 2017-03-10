package net.bossdragon.util.operations

import com.artemis.ComponentMapper
import com.artemis.annotations.Wire
import com.badlogic.gdx.math.Vector2
import net.bossdragon.component.base.Position
import se.feomedia.orion.Executor
import se.feomedia.orion.Operation
import se.feomedia.orion.OperationTree
import se.feomedia.orion.operation.TemporalOperation

/**
 * Translation over [Position.currentPos].
 */
class MoveByOperation : TemporalOperation() {
    val src = Vector2()
    val dest = Vector2()

    override fun executorType(): Class<out Executor<out Operation>> {
        return MoveByExecutor::class.java
    }

    override fun reset() {
        super.reset()
        src.setZero()
        dest.setZero()
    }

    @Wire
    class MoveByExecutor : TemporalOperation.TemporalExecutor<MoveByOperation>() {
        lateinit var positionMapper: ComponentMapper<Position>

        override fun begin(op: MoveByOperation, node: OperationTree?) {
            super.begin(op, node)
            op.src.set(positionMapper[op.entityId].currentPos)
            op.dest.add(op.src)
        }

        override fun act(delta: Float, percent: Float, op: MoveByOperation, node: OperationTree) {
            val transform = positionMapper[op.entityId]
            transform.currentPos
                .set(op.src)
                .interpolate(op.dest, percent, op.interpolation)
        }
    }
}
