package net.bossdragon.util.operations;

import com.artemis.annotations.Wire;
import se.feomedia.orion.Executor;
import se.feomedia.orion.OperationTree;
import se.feomedia.orion.operation.TemporalOperation;

public class TemporalDeltaOp extends TemporalOperation {
    public DeltaExecutor executor;
    protected float previousPercentage = 0f;


    interface DeltaExecutor {
        void act(float deltaTime, float deltaPercent);
    }


    @Override
    public Class<? extends Executor> executorType() {
        return TemporalDeltaOperationExecutor.class;
    }


    @Wire
    public static class TemporalDeltaOperationExecutor extends TemporalOperation.TemporalExecutor<TemporalDeltaOp> {
        @Override
        protected void begin(TemporalDeltaOp op, OperationTree node) {
            super.begin(op, node);
            op.previousPercentage = 0f;
        }

        @Override
        protected void act(float deltaTime, float alpha, TemporalDeltaOp op, OperationTree node) {
            float percent = op.percent();
            float deltaPercentage = percent - op.previousPercentage;
            op.executor.act(deltaTime, deltaPercentage);
            op.previousPercentage = percent;
        }
    }
}
