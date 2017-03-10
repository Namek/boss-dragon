package net.bossdragon.util.operations

import com.artemis.ComponentMapper
import com.artemis.annotations.Wire
import net.bossdragon.component.render.Colorized
import net.bossdragon.component.render.Renderable
import se.feomedia.orion.Executor
import se.feomedia.orion.Operation
import se.feomedia.orion.OperationTree
import se.feomedia.orion.operation.TemporalOperation

/**
 * Interpolates [Renderable.opacity] from current value to given.
 */
class OpacityToOperation : TemporalOperation() {
    var src = 0f
    var dst = 0f
    var hadColorizedComponent = false

    override fun executorType(): Class<out Executor<out Operation>> {
        return OpacityToExecutor::class.java
    }

    override fun reset() {
        super.reset()
        src = 0f
        dst = 0f
        hadColorizedComponent = false
    }

    @Wire
    class OpacityToExecutor : TemporalOperation.TemporalExecutor<OpacityToOperation>() {
        lateinit var mColorized: ComponentMapper<Colorized>

        override fun begin(op: OpacityToOperation, node: OperationTree) {
            super.begin(op, node)

            var colorized = mColorized.getSafe(op.entityId, null)
            op.hadColorizedComponent = colorized != null

            if (colorized != null) {
                colorized = mColorized.create(op.entityId)
            }

            op.src = colorized.color.a
            op.dst = op.dst
        }

        override fun act(delta: Float, alpha: Float, op: OpacityToOperation, node: OperationTree?) {
            val opacity = op.interpolation.apply(op.src, op.dst, alpha)
            mColorized[op.entityId].color.a = opacity
        }

        override fun end(op: OpacityToOperation, node: OperationTree?) {
            if (!op.hadColorizedComponent && op.dst == 1f) {
                // remove the component cause it's useless
                mColorized.remove(op.entityId)
            }
        }
    }
}
