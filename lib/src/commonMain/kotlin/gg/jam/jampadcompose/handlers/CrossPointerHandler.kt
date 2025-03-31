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
import gg.jam.jampadcompose.anchors.Anchor
import gg.jam.jampadcompose.ids.DiscreteDirectionId
import gg.jam.jampadcompose.inputstate.InputState

internal class CrossPointerHandler(
    private val directionId: DiscreteDirectionId,
    private val directions: List<Anchor<Direction>>,
) : PointerHandler {
    enum class Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT,
    }

    override fun handle(
        pointers: List<Pointer>,
        inputState: InputState,
        startDragGesture: Pointer?,
        data: Any?,
    ): Result {
        val currentDragGesture = pointers.firstOrNull { it.pointerId == startDragGesture?.pointerId }

        return when {
            pointers.isEmpty() -> {
                Result(
                    inputState.setDiscreteDirection(directionId, Offset.Unspecified),
                    null,
                )
            }

            currentDragGesture != null -> {
                Result(
                    inputState.setDiscreteDirection(
                        directionId,
                        findCloserState(currentDragGesture),
                    ),
                    startDragGesture,
                )
            }

            else -> {
                val firstPointer = pointers.first()
                Result(
                    inputState.setDiscreteDirection(directionId, findCloserState(firstPointer)),
                    firstPointer,
                )
            }
        }
    }

    private fun findCloserState(pointer: Pointer): Offset {
        if (isInDeadZone(pointer)) {
            return Offset.Zero
        }

        return directions
            .minBy { it.distance(pointer.position) }
            .let { it.position.copy(y = -it.position.y) }
    }

    private fun isInDeadZone(pointer: Pointer): Boolean {
        return pointer.position.getDistanceSquared() < 0.025
    }
}
