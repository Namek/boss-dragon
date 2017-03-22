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
public class MoveByOp extends TemporalOperation {
    public final Vector2 src = new Vector2();
    public final Vector2 dst = new Vector2();

    @Override
    public Class<? extends Executor> executorType() {
        return MoveByExecutor.class;
    }

    @Override
    public void reset() {
        super.reset();
        src.setZero();
        dst.setZero();
    }

    @Wire
    public static class MoveByExecutor extends TemporalOperation.TemporalExecutor<MoveByOp> {
        ComponentMapper<Position> mPosition;

        @Override
        protected void begin(MoveByOp op, OperationTree node) {
            super.begin(op, node);
            op.src.set(mPosition.get(op.entityId).currentPos);
            op.dst.add(op.src);
        }

        @Override
        protected void act(float delta, float percent, MoveByOp op, OperationTree node) {
            final Position pos = mPosition.get(op.entityId);

            pos.currentPos
                .set(op.src)
                .interpolate(op.dst, percent, op.interpolation);

            pos.desiredPos
                .set(pos.currentPos);
        }
    }
}
