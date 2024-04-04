package gg.jam.jampadcompose.handlers

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import gg.jam.jampadcompose.inputstate.InputState

data class CrossHandler(override val id: Int, override val rect: Rect) : Handler {
    enum class State(val position: Offset) {
        UP(Offset(0f, 1f)),
        DOWN(Offset(0f, -1f)),
        LEFT(Offset(-1f, 0f)),
        RIGHT(Offset(1f, 0f)),
        UP_LEFT(Offset(-1f, 1f)),
        UP_RIGHT(Offset(1f, 1f)),
        DOWN_LEFT(Offset(-1f, -1f)),
        DOWN_RIGHT(Offset(1f, -1f)),
    }

    override fun handle(
        pointers: List<Pointer>,
        inputState: InputState,
        currentGestureStart: Pointer?,
    ): HandleResult {
        val currentGesture = pointers.firstOrNull { it.pointerId == currentGestureStart?.pointerId }

        return when {
            pointers.isEmpty() -> update(inputState, withOffset = Offset.Unspecified)
            currentGesture != null -> {
                update(
                    inputState,
                    withOffset = findCloserState(currentGesture),
                    withGestureStart = currentGestureStart,
                )
            }

            else -> {
                val gestureStart = pointers.first()
                update(
                    inputState,
                    withOffset = findCloserState(gestureStart),
                    withGestureStart = gestureStart,
                )
            }
        }
    }

    private fun findCloserState(pointer: Pointer): Offset {
        return State.entries
            .minBy { (pointer.position - it.position).getDistanceSquared() }
            .position
            .let { it.copy(y = -it.y) }
    }

    private fun update(
        inputState: InputState,
        withOffset: Offset,
        withGestureStart: Pointer? = null,
    ): HandleResult {
        return HandleResult(inputState.setDiscreteDirection(id, withOffset), withGestureStart)
    }
}
