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
import kotlinx.collections.immutable.persistentSetOf

class CircleArrangement(
    private val ids: List<Int>,
    private val sockets: Int,
    rotationInDegrees: Float,
) : GravityArrangement() {
    private val circumferenceGravityArrangement =
        CircumferenceGravityArrangement(
            ids.drop(1),
            sockets - 1,
            rotationInDegrees,
        )

    override fun computeGravityPoints(): List<GravityPoint> {
        val centralGravityPoint =
            GravityPoint(
                Offset.Zero,
                1f,
                persistentSetOf(ids.first()),
            )

        val circumferenceGravityPoint = circumferenceGravityArrangement.getGravityPoints()
        return listOf(centralGravityPoint) + circumferenceGravityPoint
    }

    override fun computeSize(): Float {
        if (sockets == 1) {
            return 0.5f
        }
        return minOf(circumferenceGravityArrangement.getSize(), 0.33f)
    }
}
