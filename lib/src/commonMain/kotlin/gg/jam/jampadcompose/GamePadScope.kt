package gg.jam.jampadcompose

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import gg.jam.jampadcompose.handlers.Handler
import gg.jam.jampadcompose.handlers.Pointer
import gg.jam.jampadcompose.inputstate.InputState

@Stable
class GamePadScope {
    private data class HandlerState(
        val handler: Handler,
        var gestureStart: Pointer? = null
    )

    internal val inputState = mutableStateOf(InputState())

    private val handlers = mutableMapOf<String, HandlerState>()

    internal fun registerHandler(handler: Handler) {
        handlers[handler.handlerId()] = HandlerState(handler, null)
    }

    internal fun getAllHandlers(): Collection<Handler> {
        return handlers.values
            .map { it.handler }
    }

    internal fun getTrackedIds(): Set<Long> {
        return handlers.values
            .mapNotNull { it.gestureStart?.pointerId }
            .toSet()
    }

    internal fun getHandlerAtPosition(position: Offset): Handler? {
        return handlers.values
            .firstOrNull { (handler, _) -> handler.rect.contains(position) }
            ?.handler
    }

    internal fun getHandlerTracking(pointerId: Long): Handler? {
        return handlers.values
            .firstOrNull { (_, startGesture) -> startGesture?.pointerId == pointerId }
            ?.handler
    }

    internal fun getStartGestureForHandler(handler: Handler): Pointer? {
        return handlers[handler.handlerId()]?.gestureStart
    }

    internal fun setStartGestureForHandler(handler: Handler, newGestureStart: Pointer?) {
        handlers[handler.handlerId()]?.gestureStart = newGestureStart
    }
}
