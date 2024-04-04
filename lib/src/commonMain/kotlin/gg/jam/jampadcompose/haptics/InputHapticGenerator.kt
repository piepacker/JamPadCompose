package gg.jam.jampadcompose.haptics

import androidx.compose.ui.geometry.Offset
import gg.jam.jampadcompose.inputstate.InputState

class InputHapticGenerator(private val generator: HapticGenerator) {
    private var previousInputState: InputState? = null

    fun onInputStateChanged(current: InputState) {
        val previous = previousInputState

        if (previous == null) {
            previousInputState = current
            return
        }

        val effect =
            keyEffect(previous, current)
                ?: analogEffect(previous, current)

        if (effect != null) {
            generator.generate(effect)
        }

        previousInputState = current
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

        return if (currentlyPressedInputs > previouslyPressedInputs) {
            HapticEffect.PRESS
        } else {
            HapticEffect.RELEASE
        }
    }

    private fun analogEffect(
        previous: InputState,
        current: InputState,
    ): HapticEffect? {
        val previouslyActiveAnalogs =
            previous.analogKeys
                .values
                .count { it != Offset.Zero }

        val currentlyActiveAnalogs =
            current.analogKeys
                .values
                .count { it != Offset.Zero }

        return when {
            currentlyActiveAnalogs > previouslyActiveAnalogs -> HapticEffect.PRESS
            currentlyActiveAnalogs == previouslyActiveAnalogs -> null
            else -> HapticEffect.RELEASE
        }
    }
}
