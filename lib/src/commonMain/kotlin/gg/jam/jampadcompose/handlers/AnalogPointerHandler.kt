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
import gg.jam.jampadcompose.ids.ContinuousDirectionId
import gg.jam.jampadcompose.ids.KeyId
import gg.jam.jampadcompose.inputstate.InputState
import gg.jam.jampadcompose.inputstate.setDigitalKeyIfPressed
import gg.jam.jampadcompose.utils.Constants
import gg.jam.jampadcompose.utils.GeometryUtils
import gg.jam.jampadcompose.utils.coerceIn
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

internal class AnalogPointerHandler(
    private val directionId: ContinuousDirectionId,
    private val analogPressId: KeyId?,
) : PointerHandler {
    data class Data(
        var lastDownEvent: Instant = Instant.DISTANT_PAST,
        var pressed: Boolean = false,
    )

    override fun handle(
        pointers: List<Pointer>,
        inputState: InputState,
        startDragGesture: Pointer?,
        data: Any?,
    ): Result {
        val analogData = data as Data

        val currentDragGesture = pointers.firstOrNull { it.pointerId == startDragGesture?.pointerId }

        return when {
            pointers.isEmpty() -> {
                analogData.pressed = false
                Result(
                    updateInputState(inputState, Offset.Unspecified, analogData.pressed),
                    null,
                )
            }
            startDragGesture != null && currentDragGesture != null -> {
                val deltaPosition =
                    Offset(
                        currentDragGesture.position.x - startDragGesture.position.x,
                        startDragGesture.position.y - currentDragGesture.position.y,
                    )
                val offsetValue = deltaPosition.coerceIn(Offset(-1f, -1f), Offset(1f, 1f))
                Result(
                    updateInputState(inputState, GeometryUtils.mapCircleToSquare(offsetValue), analogData.pressed),
                    startDragGesture,
                )
            }
            else -> {
                val previousTime = analogData.lastDownEvent
                val currentTime = Clock.System.now()
                analogData.lastDownEvent = currentTime
                analogData.pressed = currentTime - previousTime < Constants.DOUBLE_TAP_INTERVAL

                val firstPointer = pointers.first()
                Result(updateInputState(inputState, Offset.Zero, analogData.pressed), firstPointer)
            }
        }
    }

    private fun updateInputState(
        inputState: InputState,
        direction: Offset,
        pressed: Boolean,
    ): InputState {
        var result = inputState.setContinuousDirection(directionId, direction)

        if (analogPressId != null) {
            result = result.setDigitalKeyIfPressed(analogPressId, pressed)
        }

        return result
    }
}
