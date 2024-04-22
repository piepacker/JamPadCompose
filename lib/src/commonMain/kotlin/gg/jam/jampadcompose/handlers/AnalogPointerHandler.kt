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
        currentGestureStart: Pointer?,
    ): Result {
        val currentGesture = pointers.firstOrNull { it.pointerId == currentGestureStart?.pointerId }

        return when {
            pointers.isNotEmpty() && currentGestureStart == null -> {
                update(inputState, withOffset = Offset.Zero, pointers.first())
            }

            currentGestureStart != null && currentGesture != null -> {
                val deltaPosition = (currentGesture.position - currentGestureStart.position)
                val offsetValue = deltaPosition.coerceIn(Offset(-1f, -1f), Offset(1f, 1f))
                update(inputState, withOffset = offsetValue, withGestureStart = currentGestureStart)
            }

            else -> update(inputState, withOffset = Offset.Unspecified)
        }
    }

    private fun update(
        inputState: InputState,
        withOffset: Offset,
        withGestureStart: Pointer? = null,
    ): Result {
        return Result(inputState.setContinuousDirection(id, withOffset), withGestureStart)
    }
}
