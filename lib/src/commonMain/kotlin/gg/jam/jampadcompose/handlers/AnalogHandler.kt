package gg.jam.jampadcompose.handlers

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import gg.jam.jampadcompose.inputstate.InputState
import gg.jam.jampadcompose.utils.coerceIn

data class AnalogHandler(override val id: Int, override val rect: Rect) : Handler {
    override fun handle(
        pointers: List<Pointer>,
        inputState: InputState,
        currentGestureStart: Pointer?,
    ): HandleResult {
        val currentGesture = pointers.firstOrNull { it.pointerId == currentGestureStart?.pointerId }

        return when {
            pointers.isNotEmpty() && currentGestureStart == null -> {
                update(inputState, withOffset = Offset.Zero, pointers.first())
            }

            currentGestureStart != null && currentGesture != null -> {
                val deltaPosition = (currentGesture.position - currentGestureStart.position)
                val offsetValue = deltaPosition.coerceIn(Offset(-1f, -1f), Offset(1f, 1f))
                update(inputState, withOffset = offsetValue, withGestureStart = currentGestureStart)
            }

            else -> update(inputState, withOffset = Offset.Unspecified)
        }
    }

    private fun update(
        inputState: InputState,
        withOffset: Offset,
        withGestureStart: Pointer? = null,
    ): HandleResult {
        return HandleResult(inputState.setContinuousDirection(id, withOffset), withGestureStart)
    }
}
