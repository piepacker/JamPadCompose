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

package gg.jam.jampadcompose.arrangements

import androidx.compose.ui.geometry.Offset
import gg.jam.jampadcompose.ids.KeyId
import gg.jam.jampadcompose.utils.Constants
import gg.jam.jampadcompose.utils.GeometryUtils
import gg.jam.jampadcompose.utils.GeometryUtils.toRadians
import kotlinx.collections.immutable.persistentSetOf
import kotlin.math.cos
import kotlin.math.sin

class CircumferenceGravityArrangement(
    private val ids: List<KeyId>,
    private val sockets: Int,
    private val rotationInDegrees: Float,
) : GravityArrangement() {
    override fun computeGravityPoints(): List<GravityPoint> {
        if (sockets <= 1) {
            return emptyList()
        }

        val baseRotation = rotationInDegrees.toRadians()

        val primaryGravityPoints =
            ids.mapIndexed { index, id ->
                val angle = (baseRotation + Constants.PI2 * index / sockets)
                GravityPoint(
                    Offset(cos(angle), sin(angle)),
                    1f,
                    persistentSetOf(id),
                )
            }

        return primaryGravityPoints
    }

    override fun computeSize(): Float {
        if (sockets == 1) {
            return 0.5f
        }
        return GeometryUtils.computeSizeOfItemsOnCircumference(sockets)
    }
}
