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
        gestureStartPointer: Pointer?
    ): Handler.Result {
        val updatedGesture = pointers.firstOrNull { it.pointerId == gestureStartPointer?.pointerId }

        return when {
            pointers.isEmpty() -> Handler.Result(inputState.setAnalogKey(id, Offset.Zero))
            updatedGesture != null -> {
                Handler.Result(
                    inputState.setAnalogKey(id, findCloserState(updatedGesture)),
                    gestureStartPointer
                )
            }

            else -> {
                val gestureStart = pointers.first()
                Handler.Result(
                    inputState.setAnalogKey(id, findCloserState(gestureStart)),
                    gestureStart
                )
            }
        }
    }

    private fun findCloserState(pointer: Pointer): Offset {
        val position = State.entries
            .minBy { (pointer.position - it.position).getDistanceSquared() }
            .position

        return position.copy(y = -position.y)
    }
}
