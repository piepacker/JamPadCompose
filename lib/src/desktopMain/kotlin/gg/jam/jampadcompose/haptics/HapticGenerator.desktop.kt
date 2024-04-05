package gg.jam.jampadcompose.haptics

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

object DummyHapticGenerator : HapticGenerator {
    override fun generate(type: HapticEffect) { }
}

@Composable
actual fun rememberHapticGenerator(): HapticGenerator {
    return remember { DummyHapticGenerator }
}
