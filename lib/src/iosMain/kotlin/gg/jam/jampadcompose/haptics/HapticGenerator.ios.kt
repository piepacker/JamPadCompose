package gg.jam.jampadcompose.haptics

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.UIKit.UIImpactFeedbackGenerator

object IosHapticGenerator : HapticGenerator {
    private val impactFeedbackGenerator = UIImpactFeedbackGenerator()

    override fun generate(type: HapticEffect) {
        when (type) {
            HapticEffect.PRESS -> impactFeedbackGenerator.impactOccurredWithIntensity(0.5)
            HapticEffect.RELEASE -> impactFeedbackGenerator.impactOccurredWithIntensity(0.3)
        }
    }
}

@Composable
actual fun rememberHapticGenerator(): HapticGenerator {
    return remember { IosHapticGenerator }
}
