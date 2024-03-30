package gg.jam.jampadcompose.handlers

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import gg.jam.jampadcompose.inputstate.InputState
import gg.jam.jampadcompose.utils.coerceIn

data class AnalogHandler(override val id: Int, override val rect: Rect) : Handler {
    override fun handle(
        pointers: List<Pointer>,
        inputState: InputState,
        gestureStartPointer: Pointer?,
    ): HandleResult {
        val updatedGesturePosition =
            pointers
                .firstOrNull { it.pointerId == gestureStartPointer?.pointerId }

        return when {
            pointers.isEmpty() -> HandleResult(inputState.setAnalogKey(id, Offset.Zero))
            gestureStartPointer == null -> {
                val gestureStart = pointers.first()
                HandleResult(inputState.setAnalogKey(id, Offset.Zero), gestureStart)
            }

            updatedGesturePosition != null -> {
                val dragPosition = (updatedGesturePosition.position - gestureStartPointer.position)
                val updatedPosition = dragPosition.coerceIn(Offset(-1f, -1f), Offset(1f, 1f))
                HandleResult(inputState.setAnalogKey(id, updatedPosition), gestureStartPointer)
            }

            else -> HandleResult(inputState.setAnalogKey(id, Offset.Zero))
        }
    }
}
