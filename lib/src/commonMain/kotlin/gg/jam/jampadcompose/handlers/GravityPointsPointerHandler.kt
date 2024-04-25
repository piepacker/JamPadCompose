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

import androidx.compose.ui.geometry.Rect
import gg.jam.jampadcompose.arrangements.GravityArrangement
import gg.jam.jampadcompose.inputstate.InputState

class GravityPointsPointerHandler(
    override val id: Int,
    override val rect: Rect,
    primaryArrangement: GravityArrangement,
    compositeArrangement: GravityArrangement,
) : PointerHandler {
    private val gravityPoints = primaryArrangement.getGravityPoints()
    private val compositePoints = compositeArrangement.getGravityPoints()
    private val allPoints = gravityPoints + compositePoints
    private val allKeys =
        allPoints
            .flatMap { it.keys }
            .toSet()

    override fun handle(
        pointers: List<Pointer>,
        inputState: InputState,
        startDragGesture: Pointer?,
    ): Result {
        val pressedKeys =
            pointers
                .flatMap { pointer ->
                    allPoints
                        .minBy { it.distance(pointer.position) }
                        .keys
                }
                .toSet()

        val finalState =
            allKeys.fold(inputState) { updatedState, key ->
                updatedState.setDigitalKey(key, key in pressedKeys)
            }

        return Result(finalState)
    }
}
