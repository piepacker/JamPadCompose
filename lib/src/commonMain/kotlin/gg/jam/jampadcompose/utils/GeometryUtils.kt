/*
 * Copyright (c) Jam.gg 2024.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package gg.jam.jampadcompose.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.sin
import kotlin.math.sqrt

object GeometryUtils {
    fun computeSizeOfItemsOnCircumference(itemsCount: Int): Float {
        val angle = sin(Constants.PI / maxOf(itemsCount, 2))
        return (angle / (1 + angle))
    }

    fun computeSizeOfItemsAroundCircumference(itemsCount: Int): Float {
        val sinValue = sin(Constants.PI / itemsCount)
        return sinValue / (1f - sinValue)
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

    fun mergeRectangles(rectangles: List<Rect>): Rect {
        return rectangles.reduce { acc, rect ->
            Rect(
                left = minOf(acc.left, rect.left),
                top = minOf(acc.top, rect.top),
                right = maxOf(acc.right, rect.right),
                bottom = maxOf(acc.bottom, rect.bottom),
            )
        }
    }

    fun mapCircleToSquare(input: Offset): Offset {
        return convertPolarCoordinatesToSquare(pointToPolarCoordinates(input))
    }

    fun mapSquareToCircle(input: Offset): Offset {
        val (x, y) = input
        return Offset(
            x * sqrt(1f - (y * y) / 2f),
            y * sqrt(1f - (x * x) / 2f),
        )
    }

    private fun pointToPolarCoordinates(input: Offset): Offset {
        val result =
            Offset(
                -((-atan2(input.y, input.x) + Constants.PI2) % (Constants.PI2)),
                hypot(input.x, input.y).coerceIn(0f, 1f),
            )
        return result
    }

    private fun convertPolarCoordinatesToSquare(input: Offset): Offset {
        val (angle, strength) = input
        val offset =
            Offset(
                strength * cos(angle),
                strength * sin(angle),
            )
        return mapEllipticalDiskCoordinatesToSquare(offset)
    }

    private fun mapEllipticalDiskCoordinatesToSquare(input: Offset): Offset {
        val (u, v) = input
        val u2 = u * u
        val v2 = v * v
        val twoSqrt2 = 2.0f * sqrt(2.0f)
        val subTermX = 2.0f + u2 - v2
        val subTermY = 2.0f - u2 + v2

        val termX1 = max(0f, subTermX + u * twoSqrt2)
        val termX2 = max(0f, subTermX - u * twoSqrt2)
        val termY1 = max(0f, subTermY + v * twoSqrt2)
        val termY2 = max(0f, subTermY - v * twoSqrt2)

        val x = (0.5f * sqrt(termX1) - 0.5f * sqrt(termX2))
        val y = (0.5f * sqrt(termY1) - 0.5f * sqrt(termY2))

        return Offset(x, y)
    }
}
