package net.bossdragon.util.operations

import com.artemis.annotations.Wire

import se.feomedia.orion.Executor
import se.feomedia.orion.OperationTree
import se.feomedia.orion.operation.TemporalOperation as OrigTemporalOperation

class TemporalOperation : OrigTemporalOperation() {
    var executor: SimpleExecutor? = null

    interface SimpleExecutor {
        fun act(deltaTime: Float, percent: Float, alpha: Float)
    }


    override fun reset() {
        executor = null
        super.reset()
    }

    override fun executorType(): Class<out Executor<*>> {
        return CustomTemporalExecutor::class.java
    }

    @Wire
    class CustomTemporalExecutor : OrigTemporalOperation.TemporalExecutor<TemporalOperation>() {
        override fun act(deltaTime: Float, alpha: Float, op: TemporalOperation, node: OperationTree) {
            op.executor!!.act(deltaTime, op.percent(), op.alpha())
        }
    }
}
