/*
 * Copyright (c) Jam.gg 2024.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package gg.jam.jampadcompose

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import gg.jam.jampadcompose.handlers.Pointer
import gg.jam.jampadcompose.handlers.PointerHandler
import gg.jam.jampadcompose.inputstate.InputState

@Stable
class JamPadScope {
    private data class HandlerState(
        val pointerHandler: PointerHandler,
        var startDragGesture: Pointer? = null,
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
            .mapNotNull { it.startDragGesture?.pointerId }
            .toSet()
    }

    internal fun getHandlerAtPosition(position: Offset): PointerHandler? {
        return handlers.values
            .firstOrNull { (handler, _) -> handler.rect.contains(position) }
            ?.pointerHandler
    }

    internal fun getHandlerTracking(pointerId: Long): PointerHandler? {
        return handlers.values
            .firstOrNull { (_, dragGesture) -> dragGesture?.pointerId == pointerId }
            ?.pointerHandler
    }

    internal fun getStartDragGestureForHandler(pointerHandler: PointerHandler): Pointer? {
        return handlers[pointerHandler.handlerId()]?.startDragGesture
    }

    internal fun setStartDragGestureForHandler(
        pointerHandler: PointerHandler,
        newGestureStart: Pointer?,
    ) {
        handlers[pointerHandler.handlerId()]?.startDragGesture = newGestureStart
    }
}
