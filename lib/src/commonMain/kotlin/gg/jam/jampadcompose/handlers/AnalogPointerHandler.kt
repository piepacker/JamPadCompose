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
import gg.jam.jampadcompose.ids.DirectionId
import gg.jam.jampadcompose.ids.KeyId
import gg.jam.jampadcompose.inputstate.InputState
import gg.jam.jampadcompose.utils.Constants
import gg.jam.jampadcompose.utils.coerceIn
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

data class AnalogPointerHandler(
    override val id: DirectionId,
    override val rect: Rect,
    private val analogPressId: KeyId?,
) : PointerHandler {

    data class Data(var lastDownEvent: Instant = Instant.DISTANT_PAST)

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
                    updateInputState(inputState, Offset.Unspecified, false),
                    null,
                )
            }
            startDragGesture != null && currentDragGesture != null -> {
                val deltaPosition = (currentDragGesture.position - startDragGesture.position)
                val offsetValue = deltaPosition.coerceIn(Offset(-1f, -1f), Offset(1f, 1f))
                Result(
                    inputState.setDirection(id, offsetValue),
                    startDragGesture,
                )
            }
            else -> {
                val analogData = data as Data

                val previousTime = analogData.lastDownEvent
                val currentTime = Clock.System.now()
                analogData.lastDownEvent = currentTime

                val isDoubleTap = currentTime - previousTime < Constants.DOUBLE_TAP_INTERVAL

                val firstPointer = pointers.first()
                Result(updateInputState(inputState, Offset.Zero, isDoubleTap), firstPointer)
            }
        }
    }

    private fun updateInputState(
        inputState: InputState,
        direction: Offset,
        pressed: Boolean,
    ): InputState {
        var result = inputState.setDirection(id, direction)

        if (analogPressId != null) {
            result = result.setDigitalKey(analogPressId, pressed)
        }

        return result
    }
}
