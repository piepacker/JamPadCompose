/*
 * Copyright (c) Filippo Scognamiglio 2025.
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

package gg.jam.jampadcompose.inputevents

import androidx.compose.ui.geometry.Offset
import gg.jam.jampadcompose.inputstate.InputState
import kotlinx.collections.immutable.PersistentMap

class InputEventsGenerator {
    private var previousInputState: InputState = InputState()

    fun onInputStateChanged(current: InputState): List<InputEvent> {
        val previous = previousInputState

        if (current == previous) {
            return emptyList()
        }

        val result =
            buildList {
                addButtonPresses(previous, current)
                addButtonReleases(previous, current)
                addContinuousDirections(previous, current)
                addDiscreteDirections(previous, current)
            }

        previousInputState = current

        return result
    }

    private fun MutableList<InputEvent>.addButtonPresses(
        previous: InputState,
        current: InputState,
    ) {
        (current.digitalKeys - previous.digitalKeys)
            .forEach { add(InputEvent.Button(it, true)) }
    }

    private fun MutableList<InputEvent>.addButtonReleases(
        previous: InputState,
        current: InputState,
    ) {
        (previous.digitalKeys - current.digitalKeys)
            .forEach { add(InputEvent.Button(it, false)) }
    }

    private fun MutableList<InputEvent>.addContinuousDirections(
        previous: InputState,
        current: InputState,
    ) {
        val transformer = { key: Int, direction: Offset ->
            InputEvent.ContinuousDirection(key, direction)
        }
        buildDirectionList(previous.continuousDirections, current.continuousDirections, transformer)
    }

    private fun MutableList<InputEvent>.addDiscreteDirections(
        previous: InputState,
        current: InputState,
    ) {
        val transformer = { key: Int, direction: Offset ->
            InputEvent.DiscreteDirection(key, direction)
        }
        buildDirectionList(previous.discreteDirections, current.discreteDirections, transformer)
    }

    private fun MutableList<InputEvent>.buildDirectionList(
        previous: PersistentMap<Int, Offset>,
        current: PersistentMap<Int, Offset>,
        transformer: (Int, Offset) -> InputEvent,
    ) {
        val allKeys = previous.keys + current.keys
        for (key in allKeys) {
            val previousDirection = previous.getOrElse(key) { Offset.Unspecified }
            val currentDirection = current.getOrElse(key) { Offset.Unspecified }

            if (previousDirection == currentDirection) {
                continue
            }

            add(transformer(key, currentDirection))
        }
    }
}
