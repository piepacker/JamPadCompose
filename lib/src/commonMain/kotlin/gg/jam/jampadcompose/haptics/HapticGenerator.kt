package gg.jam.jampadcompose.haptics

import androidx.compose.runtime.Composable

interface HapticGenerator {
    fun generate(type: HapticEffect)
}

@Composable
expect fun rememberHapticGenerator(): HapticGenerator
