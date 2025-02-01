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

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.isUnspecified

/** Return the relative position in the given rectangle with respect to the top left corner.
 *  Result in range [0, 1] on both axis. */
fun Offset.relativeToTopLeft(rect: Rect): Offset {
    val relativePosition = this - rect.topLeft
    return Offset(relativePosition.x / rect.width, relativePosition.y / rect.height)
}

/** Return the relative position in the given rectangle with respect to the top left corner.
 *  Result in range [-1, 1] on both axis. */
fun Offset.relativeToCenter(rect: Rect): Offset {
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

fun Offset.min() = minOf(this.x, this.y)
