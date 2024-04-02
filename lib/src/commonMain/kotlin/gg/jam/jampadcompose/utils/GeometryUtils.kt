package gg.jam.jampadcompose.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import kotlin.math.sin

object GeometryUtils {
    fun computeSizeOfItemsOnCircumference(itemsCount: Int): Float {
        val angle = sin(Constants.PI / maxOf(itemsCount, 2))
        return (angle / (1 + angle))
    }

    fun Float.toRadians(): Float {
        return this * Constants.PI / 180f
    }

    fun Float.toDegrees(): Float {
        return this * 180f / Constants.PI
    }

    @Composable
    fun Dp.textUnit(): TextUnit {
        val sizeInDp = this
        return with(LocalDensity.current) { sizeInDp.toSp() }
    }
}
