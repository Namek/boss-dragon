package net.bossdragon.util.operations

import com.artemis.annotations.Wire

import se.feomedia.orion.Executor
import se.feomedia.orion.OperationTree
import se.feomedia.orion.operation.SingleUseOperation

class CallOperation : SingleUseOperation() {
    lateinit var callback: Runnable


    override fun executorType(): Class<out Executor<*>> {
        return CallExecutor::class.java
    }

    @Wire
    class CallExecutor : SingleUseOperation.SingleUseExecutor<CallOperation>() {
        override fun act(op: CallOperation, node: OperationTree) {
            op.callback.run()
        }
    }
}
