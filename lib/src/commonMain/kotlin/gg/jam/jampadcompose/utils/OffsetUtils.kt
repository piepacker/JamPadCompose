package gg.jam.jampadcompose.utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect

fun Offset.relativeTo(rect: Rect): Offset {
    val relativePosition = this - rect.center
    return Offset(relativePosition.x / rect.width, relativePosition.y / rect.height) * 2f
}
