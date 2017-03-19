/**
 * Project-specific operations to be used with artemis-odb-orion
 */
package net.bossdragon.util.operations.funcs

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2
import net.bossdragon.util.operations.*
import se.feomedia.orion.OperationFactory.configure
import se.feomedia.orion.OperationFactory.operation


fun moveBy(vec: Vector2, duration: Float, interpolation: Interpolation = Interpolation.linear): MoveByOperation {
    val op = operation(MoveByOperation::class.java)
    configure(op, duration, interpolation)
    op.dest.set(vec)

    return op
}

fun moveBy(vec: Vector2): MoveByOperation {
    return moveBy(vec, 0f, Interpolation.linear)
}

fun moveTo(destination: Vector2, duration: Float, interpolation: Interpolation = Interpolation.linear): MoveToOperation {
    val op = operation(MoveToOperation::class.java)
    configure(op, duration, interpolation)
    op.dest.set(destination)

    return op
}

fun moveTo(destination: Vector2): MoveToOperation {
    return moveTo(destination, 0f, Interpolation.linear)
}

fun rotateBy(angle: Float, duration: Float, interpolation: Interpolation = Interpolation.linear): RotateByOperation {
    val op = operation(RotateByOperation::class.java)
    configure(op, duration, interpolation)
    op.dest = angle

    return op
}

fun rotateBy(angle: Float): RotateByOperation {
    return rotateBy(angle, 0f, Interpolation.linear)
}

fun rotateTo(angle: Float, duration: Float, interpolation: Interpolation = Interpolation.linear): RotateToOperation {
    val op = operation(RotateToOperation::class.java)
    configure(op, duration, interpolation)
    op.dest = angle

    return op
}

fun rotateTo(angle: Float): RotateToOperation {
    return rotateTo(angle, 0f, Interpolation.linear)
}

fun displaceBy(translation: Vector2, duration: Float, interpolation: Interpolation = Interpolation.linear): DisplaceByOperation {
    val op = operation(DisplaceByOperation::class.java)
    configure(op, duration, interpolation)
    op.dest.set(translation)

    return op
}

fun displaceBy(x: Float, y: Float, duration: Float, interpolation: Interpolation = Interpolation.linear): DisplaceByOperation {
    val op = operation(DisplaceByOperation::class.java)
    configure(op, duration, interpolation)
    op.dest.set(x, y)

    return op
}

fun displaceBy(translation: Vector2): DisplaceByOperation {
    return displaceBy(translation, 0f, Interpolation.linear)
}

fun displaceBy(x: Float, y: Float): DisplaceByOperation {
    return displaceBy(x, y, 0f, Interpolation.linear)
}

fun displaceTo(destination: Vector2, duration: Float, interpolation: Interpolation = Interpolation.linear): DisplaceToOperation {
    val op = operation(DisplaceToOperation::class.java)
    configure(op, duration, interpolation)
    op.dest.set(destination)

    return op
}

fun opacityTo(targetOpacity: Float, duration: Float, interpolation: Interpolation = Interpolation.linear): OpacityToOperation {
    val op = operation(OpacityToOperation::class.java)
    configure(op, duration, interpolation)
    op.dst = targetOpacity

    return op
}

fun displaceTo(translation: Vector2): DisplaceToOperation {
    return displaceTo(translation, 0f, Interpolation.linear)
}

fun temporal(duration: Float, executor: TemporalOperation.SimpleExecutor): TemporalOperation {
    val action = operation(TemporalOperation::class.java)
    action.duration = duration
    action.executor = executor

    return action
}

fun temporal(duration: Float, interpolation: Interpolation, executor: TemporalOperation.SimpleExecutor): TemporalOperation {
    val action = temporal(duration, executor)
    action.interpolation = interpolation

    return action
}

fun deltaTemporal(duration: Float, executor: TemporalDeltaOperation.DeltaExecutor): TemporalDeltaOperation {
    val action = operation(TemporalDeltaOperation::class.java)
    action.duration = duration
    action.executor = executor

    return action
}

fun deltaOperation(duration: Float, interpolation: Interpolation, executor: TemporalDeltaOperation.DeltaExecutor): TemporalDeltaOperation {
    val action = deltaTemporal(duration, executor)
    action.interpolation = interpolation

    return action
}

fun call(callback: Runnable): CallOperation {
    val action = operation(CallOperation::class.java)
    action.callback = callback

    return action
}
