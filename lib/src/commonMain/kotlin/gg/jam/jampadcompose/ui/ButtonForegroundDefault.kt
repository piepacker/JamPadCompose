package gg.jam.jampadcompose.ui

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import gg.jam.jampadcompose.utils.GeometryUtils.textUnit

@Composable
fun ButtonForegroundDefault(
    modifier: Modifier = Modifier,
    pressed: Boolean,
    scale: Float = 0.75f,
    iconScale: Float = 1f,
    iconPainter: Painter? = null,
    label: String? = null,
    color: Color = MaterialTheme.colorScheme.primary,
    pressedColor: Color = MaterialTheme.colorScheme.inversePrimary,
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
    textPressedColor: Color = MaterialTheme.colorScheme.onPrimary,
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Surface(
            modifier =
                modifier
                    .fillMaxSize(scale)
                    .aspectRatio(1f),
            shape = CircleShape,
            color = if (pressed) pressedColor else color,
        ) {
            if (iconPainter != null) {
                Icon(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .scale(iconScale),
                    painter = iconPainter,
                    contentDescription = label,
                )
            } else if (label != null) {
                Text(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(align = Alignment.CenterVertically),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    text = label,
                    color = if (pressed) textPressedColor else textColor,
                    fontSize = (minOf(maxHeight * 0.5f, maxWidth / label.length)).textUnit(),
                )
            }
        }
    }
}
