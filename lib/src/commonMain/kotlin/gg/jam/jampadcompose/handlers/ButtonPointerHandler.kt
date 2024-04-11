package gg.jam.jampadcompose.handlers

import androidx.compose.ui.geometry.Rect
import gg.jam.jampadcompose.inputstate.InputState

data class ButtonPointerHandler(override val id: Int, override val rect: Rect) : PointerHandler {
    override fun handle(
        pointers: List<Pointer>,
        inputState: InputState,
        currentGestureStart: Pointer?,
    ): Result {
        return Result(inputState.setDigitalKey(id, pointers.isNotEmpty()))
    }
}
