package gg.jam.jampadcompose.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun DefaultCompositeForeground(
    modifier: Modifier = Modifier,
    pressed: Boolean,
    color: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
    pressedColor: Color = MaterialTheme.colorScheme.inversePrimary.copy(alpha = 0.5f),
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Surface(
            modifier = modifier.aspectRatio(1f),
            shape = CircleShape,
            color = if (pressed) pressedColor else color,
        ) { }
    }
}
