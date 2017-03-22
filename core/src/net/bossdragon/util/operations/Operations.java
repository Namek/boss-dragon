package net.bossdragon.util.operations;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import static se.feomedia.orion.OperationFactory.*;

/**
 *
 */
public class Operations {
    public static MoveByOp moveBy(Vector2 vec, float duration, Interpolation interpolation) {
        MoveByOp op = operation(MoveByOp.class);
        configure(op, duration, interpolation);
        op.dst.set(vec);

        return op;
    }

    public static MoveByOp moveBy(Vector2 vec, float duration) {
        return moveBy(vec, duration, Interpolation.linear);
    }

    public static MoveByOp moveBy(Vector2 vec) {
        return moveBy(vec, 0f);
    }

    public static MoveToOp moveTo(Vector2 vec, float duration, Interpolation interpolation) {
        MoveToOp op = operation(MoveToOp.class);
        configure(op, duration, interpolation);
        op.dst.set(vec);

        return op;
    }

    public static MoveToOp moveTo(Vector2 vec, float duration) {
        return moveTo(vec, duration, Interpolation.linear);
    }

    public static MoveToOp moveTo(Vector2 vec) {
        return moveTo(vec, 0f, Interpolation.linear);
    }

    public static RotateByOp rotateBy(float angle, float duration, Interpolation interpolation) {
        RotateByOp op = operation(RotateByOp.class);
        configure(op, duration, interpolation);
        op.dst = angle;

        return op;
    }

    public static RotateByOp rotateBy(float angle, float duration) {
        return rotateBy(angle, duration, Interpolation.linear);
    }

    public static RotateByOp rotateBy(float angle) {
        return rotateBy(angle, 0f, Interpolation.linear);
    }

    public static RotateToOp rotateTo(float angle, float duration, Interpolation interpolation) {
        RotateToOp op = operation(RotateToOp.class);
        configure(op, duration, interpolation);
        op.dst = angle;

        return op;
    }

    public static RotateToOp rotateTo(float angle, float duration) {
        return rotateTo(angle, duration, Interpolation.linear);
    }

    public static RotateToOp rotateTo(float angle) {
        return rotateTo(angle, 0f, Interpolation.linear);
    }

    public static DisplaceByOp displaceBy(float x, float y, float duration, Interpolation interpolation) {
        DisplaceByOp op = operation(DisplaceByOp.class);
        configure(op, duration, interpolation);
        op.dst.set(x, y);

        return op;
    }

    public static DisplaceByOp displaceBy(float x, float y) {
        return displaceBy(x, y, 0f, Interpolation.linear);
    }

    public static DisplaceByOp displaceBy(Vector2 translation) {
        return displaceBy(translation.x, translation.y);
    }

    public static DisplaceByOp displaceBy(Vector2 translation, float duration) {
        return displaceBy(translation.x, translation.y, duration, Interpolation.linear);
    }

    public static DisplaceByOp displaceBy(Vector2 translation, float duration, Interpolation interpolation) {
        return displaceBy(translation.x, translation.y, duration, interpolation);
    }


    public static DisplaceToOp displaceTo(Vector2 dest, float duration, Interpolation interpolation) {
        DisplaceToOp op = operation(DisplaceToOp.class);
        configure(op, duration, interpolation);
        op.dst.set(dest);

        return op;
    }

    public static DisplaceToOp displaceTo(Vector2 dest, float duration) {
        return displaceTo(dest, duration, Interpolation.linear);
    }

    public static OpacityToOp opacityTo(float targetOpacity, float duration, Interpolation interpolation) {
        OpacityToOp op = operation(OpacityToOp.class);
        configure(op, duration, interpolation);
        op.dst = targetOpacity;

        return op;
    }

    public static OpacityToOp opacityTo(float targetOpacity, float duration) {
        return opacityTo(targetOpacity, duration, Interpolation.linear);
    }

    public static TemporalOp temporal(float duration, TemporalOp.SimpleExecutor executor) {
        TemporalOp op = operation(TemporalOp.class);
        op.duration = duration;
        op.executor = executor;

        return op;
    }

    public static TemporalOp temporal(float duration, Interpolation interpolation, TemporalOp.SimpleExecutor executor) {
        final TemporalOp op = temporal(duration, executor);
        op.interpolation = interpolation;

        return op;
    }

    public static TemporalDeltaOp deltaTemporal(float duration, TemporalDeltaOp.DeltaExecutor executor) {
        TemporalDeltaOp op = operation(TemporalDeltaOp.class);
        op.duration = duration;
        op.executor = executor;

        return op;
    }

    public static TemporalDeltaOp deltaOperation(float duration, Interpolation interpolation, TemporalDeltaOp.DeltaExecutor executor) {
        TemporalDeltaOp op = deltaTemporal(duration, executor);
        op.interpolation = interpolation;

        return op;
    }

    public static CallOp call(Runnable callback) {
        CallOp op = operation(CallOp.class);
        op.callback = callback;

        return op;
    }
}
