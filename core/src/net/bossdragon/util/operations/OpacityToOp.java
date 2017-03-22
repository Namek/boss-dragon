package net.bossdragon.util.operations;

import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import net.bossdragon.component.render.Colorized;
import se.feomedia.orion.Executor;
import se.feomedia.orion.OperationTree;
import se.feomedia.orion.operation.TemporalOperation;

/**
 * Interpolates {@Renderable#opacity} from current value to given.
 */
public class OpacityToOp extends TemporalOperation {
    public float src = 0f;
    public float dst = 0f;

    private boolean hadColorizedComponent = false;


    @Override
    public Class<? extends Executor> executorType() {
        return OpacityToExecutor.class;
    }

    @Override
    public void reset() {
        super.reset();
        src = 0f;
        dst = 0f;
        hadColorizedComponent = false;
    }


    @Wire
    public static class OpacityToExecutor extends TemporalOperation.TemporalExecutor<OpacityToOp> {
        ComponentMapper<Colorized> mColorized;

        @Override
        public void begin(OpacityToOp op, OperationTree node) {
            super.begin(op, node);

            Colorized colorized = mColorized.getSafe(op.entityId, null);
            op.hadColorizedComponent = colorized != null;

            if (colorized != null) {
                colorized = mColorized.create(op.entityId);
            }

            op.src = colorized.color.a;
            op.dst = op.dst;
        }

        @Override
        public void act(float delta, float alpha, OpacityToOp op, OperationTree node) {
            float opacity = op.interpolation.apply(op.src, op.dst, alpha);
            mColorized.get(op.entityId).color.a = opacity;
        }

        @Override
        public void end(OpacityToOp op, OperationTree node) {
            if (!op.hadColorizedComponent && op.dst == 1f) {
                // remove the component cause it's useless
                mColorized.remove(op.entityId);
            }
        }
    }
}
