package net.bossdragon.util.operations;

import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.math.Vector2;
import net.bossdragon.component.base.Position;
import se.feomedia.orion.Executor;
import se.feomedia.orion.OperationTree;
import se.feomedia.orion.operation.*;
import se.feomedia.orion.operation.TemporalOperation;

/**
 * Translation over {@link Position#currentPos}.
 */
public class MoveToOp extends TemporalOperation {
    final Vector2 src = new Vector2();
    final Vector2 dst = new Vector2();

    @Override
    public Class<? extends Executor> executorType() {
        return MoveToExecutor.class;
    }

    @Override
    public void reset() {
        super.reset();
        src.setZero();
        dst.setZero();
    }

    @Wire
    public static class MoveToExecutor extends TemporalOperation.TemporalExecutor<MoveToOp> {
        ComponentMapper<Position> mPosition;

        @Override
        protected void begin(MoveToOp op, OperationTree node) {
            super.begin(op, node);
            op.src.set(mPosition.get(op.entityId).currentPos);
        }

        @Override
        protected void act(float delta, float percent, MoveToOp op, OperationTree node) {
            final Position pos = mPosition.get(op.entityId);

            pos.currentPos
                .set(op.src)
                .interpolate(op.dst, percent, op.interpolation);

            pos.desiredPos
                .set(pos.currentPos);
        }
    }
}
