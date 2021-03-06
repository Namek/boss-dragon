package net.bossdragon.util.operations

import com.artemis.annotations.Wire

import se.feomedia.orion.Executor
import se.feomedia.orion.OperationTree
import se.feomedia.orion.operation.TemporalOperation

class TemporalDeltaOperation : TemporalOperation() {
    var executor: DeltaExecutor? = null
    protected var previousPercentage: Float = 0.toFloat()

    interface DeltaExecutor {
        fun act(deltaTime: Float, deltaPercent: Float)
    }


    override fun reset() {
        executor = null
        super.reset()
    }

    override fun executorType(): Class<out Executor<*>> {
        return TemporalDeltaOperationExecutor::class.java
    }

    @Wire
    class TemporalDeltaOperationExecutor : TemporalOperation.TemporalExecutor<TemporalDeltaOperation>() {
        override fun begin(op: TemporalDeltaOperation, node: OperationTree?) {
            super.begin(op, node)
            op.previousPercentage = 0f
        }

        override fun act(deltaTime: Float, alpha: Float, op: TemporalDeltaOperation, node: OperationTree) {
            val percent = op.percent()
            val deltaPercentage = percent - op.previousPercentage
            op.executor!!.act(deltaTime, deltaPercentage)
            op.previousPercentage = percent
        }
    }
}
