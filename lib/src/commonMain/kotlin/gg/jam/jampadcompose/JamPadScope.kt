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
import gg.jam.jampadcompose.ids.ControlId
import gg.jam.jampadcompose.ids.DirectionId
import gg.jam.jampadcompose.ids.KeyId
import gg.jam.jampadcompose.inputstate.InputState
import gg.jam.jampadcompose.utils.relativeToCenter

@Stable
class JamPadScope {
    private data class HandlerState(
        val pointerHandler: PointerHandler,
        var startDragGesture: Pointer? = null,
        val data: Any? = null
    )

    internal val inputState = mutableStateOf(InputState())

    private val handlers = mutableMapOf<String, HandlerState>()

    internal fun registerHandler(pointerHandler: PointerHandler, data: Any? = null) {
        handlers[pointerHandler.handlerId()] = HandlerState(pointerHandler, null, data)
    }

    private fun getAllHandlers(): Collection<HandlerState> {
        return handlers.values
    }

    private fun getTrackedIds(): Set<Long> {
        return handlers.values
            .mapNotNull { it.startDragGesture?.pointerId }
            .toSet()
    }

    private fun getHandlerAtPosition(position: Offset): PointerHandler? {
        return handlers.values
            .firstOrNull { (handler, _) -> handler.rect.contains(position) }
            ?.pointerHandler
    }

    private fun getHandlerTracking(pointerId: Long): PointerHandler? {
        return handlers.values
            .firstOrNull { (_, dragGesture) -> dragGesture?.pointerId == pointerId }
            ?.pointerHandler
    }

    internal fun handleInputEvent(eventPointers: Sequence<Pointer>): InputState {
        val trackedPointers = getTrackedIds()

        val handlersAssociations: Map<PointerHandler?, List<Pointer>> = eventPointers
            .groupBy { pointer ->
                if (pointer.pointerId in trackedPointers) {
                    getHandlerTracking(pointer.pointerId)
                } else {
                    getHandlerAtPosition(pointer.position)
                }
            }

        return getAllHandlers()
            .fold(inputState.value) { state, handler ->
                val pointerHandler = handler.pointerHandler
                val pointers =
                    handlersAssociations.getOrElse(pointerHandler) { emptyList() }

                val relativePointers =
                    pointers
                        .map {
                            Pointer(
                                it.pointerId,
                                it.position.relativeToCenter(pointerHandler.rect),
                            )
                        }

                val (updatedState, startDragGesture) =
                    pointerHandler.handle(
                        relativePointers,
                        state,
                        handler.startDragGesture,
                        handler.data,
                    )

                handler.startDragGesture = startDragGesture
                updatedState
            }
    }

    internal fun handleSimulatedInputEvents(
        simulatedControlIds: Set<ControlId>,
        inputState: InputState,
        simulatedInputState: InputState,
    ): InputState {
        return simulatedControlIds.fold(inputState) { state, id ->
            when (id) {
                is KeyId -> state.setDigitalKey(id, simulatedInputState.getDigitalKey(id))
                is DirectionId -> state.setDirection(id, simulatedInputState.getDirection(id))
            }
        }
    }
}
