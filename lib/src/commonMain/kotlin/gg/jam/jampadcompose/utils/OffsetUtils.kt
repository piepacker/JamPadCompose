package gg.jam.jampadcompose.utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.isUnspecified

fun Offset.relativeTo(rect: Rect): Offset {
    val relativePosition = this - rect.center
    return Offset(relativePosition.x / rect.width, relativePosition.y / rect.height) * 2f
}

fun Offset.coerceIn(
    min: Offset,
    max: Offset,
): Offset {
    return Offset(x.coerceIn(min.x, max.x), y.coerceIn(min.y, max.y))
}

fun Offset.ifUnspecified(producer: () -> Offset): Offset {
    return if (this.isUnspecified) producer() else this
}
