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
import gg.jam.jampadcompose.anchors.BaseAnchor
import gg.jam.jampadcompose.ids.DiscreteDirectionId
import gg.jam.jampadcompose.inputstate.InputState
import kotlin.math.sqrt

internal class CrossPointerHandler(
    private val directionId: DiscreteDirectionId,
    allowDiagonals: Boolean,
) : PointerHandler {
    enum class State(val position: Offset, val anchor: BaseAnchor, val isDiagonal: Boolean) {
        UP(
            Offset(+0f, +1f),
            BaseAnchor(Offset(+0f, +1f), ORTHOGONAL_DIRECTION_STRENGTH),
            false,
        ),
        DOWN(
            Offset(+0f, -1f),
            BaseAnchor(Offset(+0f, -1f), ORTHOGONAL_DIRECTION_STRENGTH),
            false,
        ),
        LEFT(
            Offset(-1f, +0f),
            BaseAnchor(Offset(-1f, +0f), ORTHOGONAL_DIRECTION_STRENGTH),
            false,
        ),
        RIGHT(
            Offset(+1f, +0f),
            BaseAnchor(Offset(+1f, +0f), ORTHOGONAL_DIRECTION_STRENGTH),
            false,
        ),
        UP_LEFT(
            Offset(-1f, +1f),
            BaseAnchor(Offset(-DIAGONAL_DISTANCE, +DIAGONAL_DISTANCE), DIAGONAL_DIRECTION_STRENGTH),
            true,
        ),
        UP_RIGHT(
            Offset(+1f, +1f),
            BaseAnchor(Offset(+DIAGONAL_DISTANCE, +DIAGONAL_DISTANCE), DIAGONAL_DIRECTION_STRENGTH),
            true,
        ),
        DOWN_LEFT(
            Offset(-1f, -1f),
            BaseAnchor(Offset(-DIAGONAL_DISTANCE, -DIAGONAL_DISTANCE), DIAGONAL_DIRECTION_STRENGTH),
            true,
        ),
        DOWN_RIGHT(
            Offset(+1f, -1f),
            BaseAnchor(Offset(+DIAGONAL_DISTANCE, -DIAGONAL_DISTANCE), DIAGONAL_DIRECTION_STRENGTH),
            true,
        ),
    }

    private val allStates =
        buildList {
            val orthogonalDirections = State.entries.filter { !it.isDiagonal }
            addAll(orthogonalDirections)

            if (allowDiagonals) {
                val diagonalDirections = State.entries.filter { it.isDiagonal }
                addAll(diagonalDirections)
            }
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

        return allStates
            .minBy { it.anchor.distance(pointer.position) }
            .let { it.position.copy(y = -it.position.y) }
    }

    private fun isInDeadZone(pointer: Pointer): Boolean {
        return pointer.position.getDistanceSquared() < 0.025
    }

    companion object {
        private const val ORTHOGONAL_DIRECTION_STRENGTH = 1f
        private const val DIAGONAL_DIRECTION_STRENGTH = 0.5f
        private val DIAGONAL_DISTANCE = 0.9f * sqrt(2f) / 2f
    }
}
