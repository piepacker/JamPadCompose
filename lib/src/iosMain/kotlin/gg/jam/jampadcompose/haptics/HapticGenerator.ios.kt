package gg.jam.jampadcompose.haptics

import platform.UIKit.UIImpactFeedbackGenerator

actual object HapticGenerator {

    private val impactFeedbackGenerator = UIImpactFeedbackGenerator()

    actual fun generate(type: HapticEffect) {
        when (type) {
            HapticEffect.PRESS -> impactFeedbackGenerator.impactOccurredWithIntensity(0.5)
            HapticEffect.RELEASE -> impactFeedbackGenerator.impactOccurredWithIntensity(0.3)
        }
    }
}
