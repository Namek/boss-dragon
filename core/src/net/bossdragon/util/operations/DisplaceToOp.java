package net.bossdragon.util.operations;

import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.math.Vector2;
import net.bossdragon.component.base.Transform;
import se.feomedia.orion.Executor;
import se.feomedia.orion.OperationTree;
import se.feomedia.orion.operation.TemporalOperation;

/**
 * Translation over {@link Transform#displacement}
 */
public class DisplaceToOp extends TemporalOperation {
    public Vector2 src = new Vector2();
    public Vector2 dst = new Vector2();

    @Override
    public Class<? extends Executor> executorType() {
        return DisplaceToExecutor.class;
    }

    @Override
    public void reset() {
        super.reset();
        src.setZero();
        dst.setZero();
    }

    @Wire
    class DisplaceToExecutor extends TemporalOperation.TemporalExecutor<DisplaceToOp> {
        ComponentMapper<Transform> mTransform;

        @Override
        protected void begin(DisplaceToOp op, OperationTree node) {
            super.begin(op, node);
            Transform transform = mTransform.get(op.entityId);
            op.src.set(transform.displacement);
        }

        @Override
        protected void act(float delta, float alpha, DisplaceToOp op, OperationTree node) {
            Transform transform = mTransform.get(op.entityId);
            transform.displacement
                .set(op.src)
                .interpolate(op.dst, alpha, op.interpolation);
        }
    }
}
