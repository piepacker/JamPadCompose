package gg.jam.jampadcompose.handlers

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import gg.jam.jampadcompose.inputstate.InputState

data class AnalogHandler(override val id: Int, override val rect: Rect) : Handler {

    override fun handle(
        pointers: List<Pointer>,
        inputState: InputState,
        gestureStartPointer: Pointer?
    ): Handler.Result {
        val updatedGesturePosition = pointers
            .firstOrNull { it.pointerId == gestureStartPointer?.pointerId }

        return when {
            pointers.isEmpty() -> Handler.Result(inputState.setAnalogKey(id, Offset.Zero))
            gestureStartPointer == null -> {
                val gestureStart = pointers.first()
                Handler.Result(inputState.setAnalogKey(id, Offset.Zero), gestureStart)
            }

            updatedGesturePosition != null -> {
                val updatedPosition =
                    (updatedGesturePosition.position - gestureStartPointer.position)
                        .let { (x, y) -> Offset(x.coerceIn(-1f, 1f), y.coerceIn(-1f, 1f)) }

                Handler.Result(inputState.setAnalogKey(id, updatedPosition), gestureStartPointer)
            }

            else -> Handler.Result(inputState.setAnalogKey(id, Offset.Zero))
        }
    }
}
