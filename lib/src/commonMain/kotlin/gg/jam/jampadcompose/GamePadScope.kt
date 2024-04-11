package gg.jam.jampadcompose

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import gg.jam.jampadcompose.handlers.Pointer
import gg.jam.jampadcompose.handlers.PointerHandler
import gg.jam.jampadcompose.inputstate.InputState

@Stable
class GamePadScope {
    private data class HandlerState(
        val pointerHandler: PointerHandler,
        var gestureStart: Pointer? = null,
    )

    internal val inputState = mutableStateOf(InputState())

    private val handlers = mutableMapOf<String, HandlerState>()

    internal fun registerHandler(pointerHandler: PointerHandler) {
        handlers[pointerHandler.handlerId()] = HandlerState(pointerHandler, null)
    }

    internal fun getAllHandlers(): Collection<PointerHandler> {
        return handlers.values
            .map { it.pointerHandler }
    }

    internal fun getTrackedIds(): Set<Long> {
        return handlers.values
            .mapNotNull { it.gestureStart?.pointerId }
            .toSet()
    }

    internal fun getHandlerAtPosition(position: Offset): PointerHandler? {
        return handlers.values
            .firstOrNull { (handler, _) -> handler.rect.contains(position) }
            ?.pointerHandler
    }

    internal fun getHandlerTracking(pointerId: Long): PointerHandler? {
        return handlers.values
            .firstOrNull { (_, startGesture) -> startGesture?.pointerId == pointerId }
            ?.pointerHandler
    }

    internal fun getStartGestureForHandler(pointerHandler: PointerHandler): Pointer? {
        return handlers[pointerHandler.handlerId()]?.gestureStart
    }

    internal fun setStartGestureForHandler(
        pointerHandler: PointerHandler,
        newGestureStart: Pointer?,
    ) {
        handlers[pointerHandler.handlerId()]?.gestureStart = newGestureStart
    }
}
