package net.bossdragon.util.operations;

import com.artemis.annotations.Wire;
import se.feomedia.orion.Executor;
import se.feomedia.orion.OperationTree;
import se.feomedia.orion.operation.SingleUseOperation;

public class CallOp extends SingleUseOperation {
    public Runnable callback;

    @Override
    public Class<? extends Executor> executorType() {
        return null;
    }

    @Wire
    public static class CallExecutor extends SingleUseOperation.SingleUseExecutor<CallOp> {
        @Override
        protected void act(CallOp op, OperationTree node) {
            op.callback.run();
        }
    }
}
