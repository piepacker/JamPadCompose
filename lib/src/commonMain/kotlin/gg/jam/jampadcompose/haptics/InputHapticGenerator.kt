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

package gg.jam.jampadcompose.haptics

import androidx.compose.ui.geometry.Offset
import gg.jam.jampadcompose.config.HapticFeedbackType
import gg.jam.jampadcompose.inputstate.InputState

class InputHapticGenerator(
    private val generator: HapticGenerator,
    private val hapticFeedbackType: HapticFeedbackType,
    private var previousInputState: InputState
) {

    fun onInputStateChanged(current: InputState) {
        if (hapticFeedbackType == HapticFeedbackType.NONE) {
            return
        }

        val previous = previousInputState

        val requestedEffect =
            keyEffect(previous, current)
                ?: discreteDirectionEffect(previous, current)
                ?: continuousDirectionEffect(previous, current)

        if (requestedEffect != null && shouldPlayEffect(requestedEffect)) {
            generator.generate(requestedEffect)
        }

        previousInputState = current
    }

    private fun shouldPlayEffect(effect: HapticEffect): Boolean {
        return when {
            effect == HapticEffect.PRESS -> true
            effect == HapticEffect.RELEASE && hapticFeedbackType == HapticFeedbackType.PRESS_RELEASE -> true
            else -> false
        }
    }

    private fun keyEffect(
        previous: InputState,
        current: InputState,
    ): HapticEffect? {
        if (previous.digitalKeys == current.digitalKeys) {
            return null
        }

        val previouslyPressedInputs = previous.digitalKeys.size
        val currentlyPressedInputs = current.digitalKeys.size

        return if (currentlyPressedInputs >= previouslyPressedInputs) {
            HapticEffect.PRESS
        } else {
            HapticEffect.RELEASE
        }
    }

    private fun continuousDirectionEffect(
        previous: InputState,
        current: InputState,
    ): HapticEffect? {
        val previouslyActiveAnalogs =
            previous.continuousDirections
                .values.count { it != Offset.Unspecified }

        val currentlyActiveAnalogs =
            current.continuousDirections
                .values.count { it != Offset.Unspecified }

        return when {
            currentlyActiveAnalogs > previouslyActiveAnalogs -> HapticEffect.PRESS
            currentlyActiveAnalogs == previouslyActiveAnalogs -> null
            else -> HapticEffect.RELEASE
        }
    }

    private fun discreteDirectionEffect(
        previous: InputState,
        current: InputState,
    ): HapticEffect? {
        if (previous.discreteDirections == current.discreteDirections) {
            return null
        }

        val previouslyPressed =
            previous.discreteDirections.values
                .asSequence()
                .flatMap { sequenceOf(it.x > 0.5, it.x < -0.5, it.y > 0.5, it.y < -0.5) }
                .count { it }

        val currentlyPressed =
            current.discreteDirections.values
                .asSequence()
                .flatMap { sequenceOf(it.x > 0.5, it.x < -0.5, it.y > 0.5, it.y < -0.5) }
                .count { it }

        return if (currentlyPressed >= previouslyPressed) {
            HapticEffect.PRESS
        } else {
            HapticEffect.RELEASE
        }
    }
}
