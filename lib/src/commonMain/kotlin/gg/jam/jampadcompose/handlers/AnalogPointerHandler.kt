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
import gg.jam.jampadcompose.utils.coerceIn

data class AnalogPointerHandler(override val id: Int, override val rect: Rect) : PointerHandler {
    override fun handle(
        pointers: List<Pointer>,
        inputState: InputState,
        startDragGesture: Pointer?,
    ): Result {
        val currentDragGesture = pointers.firstOrNull { it.pointerId == startDragGesture?.pointerId }

        return when {
            pointers.isEmpty() -> {
                Result(
                    inputState.setContinuousDirection(id, Offset.Unspecified),
                    null
                )
            }
            startDragGesture != null && currentDragGesture != null -> {
                val deltaPosition = (currentDragGesture.position - startDragGesture.position)
                val offsetValue = deltaPosition.coerceIn(Offset(-1f, -1f), Offset(1f, 1f))
                Result(
                    inputState.setContinuousDirection(id, offsetValue),
                    startDragGesture
                )
            }
            else -> {
                val firstPointer = pointers.first()
                Result(
                    inputState.setContinuousDirection(id, Offset.Zero),
                    firstPointer
                )
            }
        }
    }
}
