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

package gg.jam.jampadcompose.handlers

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import gg.jam.jampadcompose.inputstate.InputState

data class CrossPointerHandler(override val id: Int, override val rect: Rect) : PointerHandler {
    enum class State(val position: Offset) {
        UP(Offset(0f, 1f)),
        DOWN(Offset(0f, -1f)),
        LEFT(Offset(-1f, 0f)),
        RIGHT(Offset(1f, 0f)),
        UP_LEFT(Offset(-1f, 1f)),
        UP_RIGHT(Offset(1f, 1f)),
        DOWN_LEFT(Offset(-1f, -1f)),
        DOWN_RIGHT(Offset(1f, -1f)),
    }

    override fun handle(
        pointers: List<Pointer>,
        inputState: InputState,
        startDragGesture: Pointer?,
    ): Result {
        val currentDragGesture = pointers.firstOrNull { it.pointerId == startDragGesture?.pointerId }

        return when {
            pointers.isEmpty() -> {
                Result(
                    inputState.setDiscreteDirection(id, Offset.Unspecified),
                    null
                )
            }

            currentDragGesture != null -> {
                Result(
                    inputState.setDiscreteDirection(id, findCloserState(currentDragGesture)),
                    startDragGesture
                )
            }

            else -> {
                val firstPointer = pointers.first()
                Result(
                    inputState.setDiscreteDirection(id, findCloserState(firstPointer)),
                    firstPointer
                )
            }
        }
    }

    private fun findCloserState(pointer: Pointer): Offset {
        return State.entries
            .minBy { (pointer.position - it.position).getDistanceSquared() }
            .position
            .let { it.copy(y = -it.y) }
    }
}
