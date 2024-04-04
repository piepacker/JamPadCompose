package gg.jam.jampadcompose.handlers

import androidx.compose.ui.geometry.Rect
import gg.jam.jampadcompose.inputstate.InputState

interface Handler {
    val id: Int
    val rect: Rect

    fun handle(
        pointers: List<Pointer>,
        inputState: InputState,
        currentGestureStart: Pointer?,
    ): HandleResult

    fun handlerId(): String {
        return "${this::class.simpleName}:$id"
    }
}
