package net.bossdragon.util.operations;

import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import net.bossdragon.component.base.Transform;
import se.feomedia.orion.Executor;
import se.feomedia.orion.OperationTree;
import se.feomedia.orion.operation.TemporalOperation;
import se.feomedia.orion.operation.*;

/**
 * Updats over {@link Transform#rotation}. Degrees.
 */
public class RotateToOp extends TemporalOperation {
    public float src = 0f;
    public float dst = 0f;

    @Override
    public Class<? extends Executor> executorType() {
        return RotateToExecutor.class;
    }

    @Override
    public void reset() {
        super.reset();
        src = 0f;
        dst = 0f;
    }


    @Wire
    public static class RotateToExecutor extends TemporalOperation.TemporalExecutor<RotateToOp> {
        ComponentMapper<Transform> mTransform;

        @Override
        protected void begin(RotateToOp op, OperationTree node) {
            super.begin(op, node);
            Transform transform = mTransform.get(op.entityId);
            op.src = transform.rotation;
            op.dst = op.dst;
        }

        @Override
        protected void act(float delta, float alpha, RotateToOp op, OperationTree node) {
            Transform transform = mTransform.get(op.entityId);
            transform.rotation = op.interpolation.apply(op.src, op.dst, alpha);
        }
    }
}
