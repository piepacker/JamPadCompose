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
