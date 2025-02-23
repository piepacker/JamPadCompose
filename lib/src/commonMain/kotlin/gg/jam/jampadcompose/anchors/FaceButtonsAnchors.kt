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

package gg.jam.jampadcompose.anchors

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import gg.jam.jampadcompose.ids.KeyId
import gg.jam.jampadcompose.utils.Constants
import gg.jam.jampadcompose.utils.GeometryUtils
import gg.jam.jampadcompose.utils.GeometryUtils.toRadians
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun rememberFaceButtonsAnchors(ids: List<KeyId>, rotationInDegrees: Float): List<Anchor> {
    return remember(ids, rotationInDegrees) {
        val baseRotation = rotationInDegrees.toRadians()
        val size = if (ids.size == 1) {
            0.5f
        } else {
            GeometryUtils.computeSizeOfItemsOnCircumference(ids.size)
        }

        val primaryAnchors =
            ids.mapIndexed { index, id ->
                val angle = (baseRotation + Constants.PI2 * index / ids.size)
                Anchor(
                    Offset(cos(angle), sin(angle)),
                    1f,
                    setOf(KeyId(id.value)),
                    size,
                )
            }

        primaryAnchors
    }
}
