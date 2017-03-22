package net.bossdragon.util.operations;

import com.artemis.annotations.Wire;
import se.feomedia.orion.Executor;
import se.feomedia.orion.OperationTree;

/**
 *
 */
public class TemporalOp extends se.feomedia.orion.operation.TemporalOperation {
    public SimpleExecutor executor;

    interface SimpleExecutor {
        void act(float deltaTime, float percent, float alpha);
    }

    @Override
    public Class<? extends Executor> executorType() {
        return CustomTemporalExecutor.class;
    }


    @Wire
    public static class CustomTemporalExecutor extends se.feomedia.orion.operation.TemporalOperation.TemporalExecutor<TemporalOp> {
        @Override
        protected void act(float deltaTime, float alpha, TemporalOp op, OperationTree node) {
            op.executor.act(deltaTime, op.percent(), op.alpha());
        }
    }
}
