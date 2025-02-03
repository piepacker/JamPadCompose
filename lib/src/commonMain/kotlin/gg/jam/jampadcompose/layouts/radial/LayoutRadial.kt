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

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import gg.jam.jampadcompose.JamPadScope
import gg.jam.jampadcompose.layouts.radial.secondarydials.LayoutRadialSecondaryDialGeometry
import gg.jam.jampadcompose.layouts.radial.secondarydials.LayoutRadialSecondaryDialProperties
import gg.jam.jampadcompose.layouts.radial.secondarydials.LayoutRadialSecondaryDialsScope
import gg.jam.jampadcompose.utils.GeometryUtils
import gg.jam.jampadcompose.utils.min
import gg.jam.jampadcompose.utils.relativeToTopLeft
import kotlin.math.roundToInt

@Composable
fun JamPadScope.LayoutRadial(
    modifier: Modifier = Modifier,
    primaryDialMaxSize: Dp = 160.dp,
    secondaryDialsBaseRotationInDegrees: Float = 0f,
    secondaryDialsBaseScale: Float = GeometryUtils.computeSizeOfItemsAroundCircumference(12),
    primaryDial: @Composable () -> Unit,
    secondaryDials: @Composable LayoutRadialSecondaryDialsScope.() -> Unit,
) {
    val scope = remember { LayoutRadialSecondaryDialsScope() }

    Layout(
        modifier = modifier,
        content = {
            scope.secondaryDials()
            Box(modifier = Modifier.layoutId("primary")) {
                primaryDial()
            }
        },
    ) { measurables, constraints ->
        val primaryDialMeasurable = measurables.first { it.layoutId == "primary" }
        val secondaryDialsMeasurables = measurables.filter { it.layoutId != "primary" }

        val secondaryDialsProperties = findSecondaryDialProperties(
            secondaryDialsMeasurables,
            secondaryDialsBaseRotationInDegrees,
        )

        val relativeLayoutRect =
            LayoutRadialSecondaryDialGeometry.findLayoutRectRelativeToPrimaryDial(
                secondaryDialsProperties,
                secondaryDialsBaseScale,
            )

        val relativeLayoutSizes = Offset(
            constraints.maxWidth / relativeLayoutRect.width,
            constraints.maxHeight / relativeLayoutRect.height,
        )

        val primaryDialSize = minOf(
            relativeLayoutSizes.min().roundToInt(),
            primaryDialMaxSize.roundToPx()
        )

        val layoutSize = Size(
            primaryDialSize * relativeLayoutRect.width,
            primaryDialSize * relativeLayoutRect.height
        )

        val primaryDialCenter = Offset(0f, 0f)
            .relativeToTopLeft(relativeLayoutRect)
            .let { Offset(it.x * layoutSize.width, it.y * layoutSize.height) }

        val placePrimaryDial = placePrimaryDial(
            primaryDialMeasurable,
            primaryDialSize,
            primaryDialCenter,
        )

        val secondaryDialSize = primaryDialSize * secondaryDialsBaseScale

        val placeSecondaryDials = placeSecondaryDials(
            secondaryDialsMeasurables,
            secondaryDialsProperties,
            primaryDialSize,
            secondaryDialSize,
            primaryDialCenter,
        )

        layout(layoutSize.width.roundToInt(), layoutSize.height.roundToInt()) {
            placePrimaryDial()
            placeSecondaryDials()
        }
    }
}

private fun findSecondaryDialProperties(
    secondaryDialsMeasurables: List<Measurable>,
    secondaryDialsBaseRotationInDegrees: Float,
): List<LayoutRadialSecondaryDialProperties> {
    return secondaryDialsMeasurables.asSequence()
        .map { (it.parentData as? LayoutRadialSecondaryDialProperties) }
        .map { it ?: LayoutRadialSecondaryDialProperties() }
        .map { it.copy(positionInDegrees = it.positionInDegrees + secondaryDialsBaseRotationInDegrees) }
        .toList()
}

private fun placePrimaryDial(
    primaryDialMeasurable: Measurable,
    primaryDialSize: Int,
    primaryLayoutCenter: Offset,
): Placeable.PlacementScope.() -> Unit {
    val placeable =
        primaryDialMeasurable.measure(Constraints.fixed(primaryDialSize, primaryDialSize))

    return {
        val position = primaryLayoutCenter - Offset(placeable.width / 2f, placeable.height / 2f)
        placeable.place(position.round())
    }
}

private fun placeSecondaryDials(
    secondaryMeasurables: List<Measurable>,
    secondaryParentData: List<LayoutRadialSecondaryDialProperties>,
    primaryDialSize: Int,
    secondaryDialSize: Float,
    primaryLayoutCenter: Offset,
): Placeable.PlacementScope.() -> Unit {
    val placeables = secondaryMeasurables.mapIndexed { index, measurable ->
        val parentData = secondaryParentData[index]
        val size = (secondaryDialSize * parentData.scale).roundToInt()
        val childConstraints = Constraints.fixed(size, size)
        measurable.measure(childConstraints)
    }

    return {
        placeables.forEachIndexed { index, placeable ->
            val position = LayoutRadialSecondaryDialGeometry.findSecondaryDialCenterPosition(
                secondaryParentData[index],
                primaryDialSize.toFloat(),
                secondaryDialSize,
                primaryLayoutCenter,
            )
            val sizeOffset = IntOffset(placeable.width, placeable.height) / 2f
            placeable.place(position.round() - sizeOffset)
        }
    }
}
