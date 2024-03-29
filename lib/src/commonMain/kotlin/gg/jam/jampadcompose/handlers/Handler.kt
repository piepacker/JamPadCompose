package gg.jam.jampadcompose.handlers

import androidx.compose.ui.geometry.Rect
import gg.jam.jampadcompose.inputstate.InputState

interface Handler {
    val id: Int
    val rect: Rect

    data class Result(val inputState: InputState, val gestureStartPointer: Pointer? = null)

    fun handle(
        pointers: List<Pointer>,
        inputState: InputState,
        gestureStartPointer: Pointer?
    ): Result

    fun handlerId(): String {
        return "${this::class.simpleName}:$id"
    }
}
