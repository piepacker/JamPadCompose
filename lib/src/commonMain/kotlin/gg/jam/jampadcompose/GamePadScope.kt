package gg.jam.jampadcompose

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import gg.jam.jampadcompose.handlers.Handler
import gg.jam.jampadcompose.handlers.Pointer
import gg.jam.jampadcompose.inputstate.InputState

@Stable
class GamePadScope {
    internal val inputState = mutableStateOf(InputState())
    internal val handlers = mutableMapOf<String, Handler>()
    internal val gestureStart = mutableMapOf<String, Pointer?>()

    internal fun registerHandler(handler: Handler) {
        handlers[handler.handlerId()] = handler
    }
}
