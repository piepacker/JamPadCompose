package gg.jam.jampadcompose.handlers

import androidx.compose.ui.geometry.Rect
import gg.jam.jampadcompose.inputstate.InputState

data class ButtonHandler(override val id: Int, override val rect: Rect) : Handler {

    override fun handle(
        pointers: List<Pointer>,
        inputState: InputState,
        gestureStartPointer: Pointer?
    ): Handler.Result {
        return Handler.Result(inputState.setDigitalKey(id, pointers.isNotEmpty()))
    }
}
