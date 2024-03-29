package gg.jam.jampadcompose.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun ButtonForegroundDefault(
    modifier: Modifier = Modifier,
    pressed: Boolean,
    scale: Float = 0.75f,
    icon: ImageVector? = null,
    color: Color = MaterialTheme.colorScheme.primary,
    pressedColor: Color = MaterialTheme.colorScheme.inversePrimary
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = modifier
                .fillMaxSize(scale)
                .aspectRatio(1f),
            shape = CircleShape,
            color = if (pressed) pressedColor else color
        ) {
            if (icon != null) {
                Icon(imageVector = icon, contentDescription = null)
            }
        }
    }
}
