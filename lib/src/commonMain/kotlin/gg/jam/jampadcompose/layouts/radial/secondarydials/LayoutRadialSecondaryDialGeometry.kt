/*
 * Copyright (c) Filippo Scognamiglio 2025.
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

package gg.jam.jampadcompose.layouts.radial.secondarydials

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import gg.jam.jampadcompose.utils.GeometryUtils
import gg.jam.jampadcompose.utils.GeometryUtils.toRadians
import kotlin.math.cos
import kotlin.math.sin

object LayoutRadialSecondaryDialGeometry {
    internal fun findSecondaryDialCenterPosition(
        parentData: LayoutRadialSecondaryDialProperties,
        primaryDialSize: Float,
        baseSecondaryDialSize: Float,
        primaryDialCenter: Offset,
    ): Offset {
        val angle = -(parentData.positionInDegrees).toRadians()
        val scale = parentData.scale
        val distance = 0.5f * (primaryDialSize + baseSecondaryDialSize * scale)
        return primaryDialCenter + Offset(cos(angle), sin(angle)) * distance
    }

    /** Returns the Rect representing the whole layout with the primary dial centered in 0,0. */
    internal fun findLayoutRectRelativeToPrimaryDial(
        secondaryDialMeasurablesParentData: List<LayoutRadialSecondaryDialProperties>,
        secondaryDialBaseScale: Float,
    ): Rect {
        val secondaryDialsRectangles =
            secondaryDialMeasurablesParentData
                .map { parentData ->
                    val position =
                        findSecondaryDialCenterPosition(
                            parentData,
                            1f,
                            secondaryDialBaseScale,
                            Offset(0f, 0f),
                        )

                    Rect(center = position, radius = secondaryDialBaseScale * parentData.scale / 2f)
                }

        val primaryRect = Rect(-0.5f, -0.5f, +0.5f, +0.5f)
        return GeometryUtils.mergeRectangles(listOf(primaryRect) + secondaryDialsRectangles)
    }
}
