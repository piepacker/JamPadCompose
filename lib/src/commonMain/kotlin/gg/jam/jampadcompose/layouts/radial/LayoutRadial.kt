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
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.round
import androidx.compose.ui.unit.toOffset
import gg.jam.jampadcompose.JamPadScope
import gg.jam.jampadcompose.layouts.radial.secondarydials.LayoutRadialSecondaryDialGeometry
import gg.jam.jampadcompose.layouts.radial.secondarydials.LayoutRadialSecondaryDialProperties
import gg.jam.jampadcompose.layouts.radial.secondarydials.LayoutRadialSecondaryDialsScope
import gg.jam.jampadcompose.utils.GeometryUtils
import kotlin.math.roundToInt

@Composable
fun JamPadScope.LayoutRadial(
    modifier: Modifier = Modifier,
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

        val radialRelativeSize =
            LayoutRadialSecondaryDialGeometry.findLayoutSizeRelativeToPrimaryDial(
                secondaryDialsProperties,
                secondaryDialsBaseScale,
            )

        val minRelativeSize = minOf(
            constraints.maxWidth / radialRelativeSize.width,
            constraints.maxHeight / radialRelativeSize.height,
        )

        val primaryDialSize = minRelativeSize.roundToInt()

        val placePrimaryDial = placePrimaryDial(
            primaryDialMeasurable,
            primaryDialSize,
            constraints,
        )

        val secondaryDialSize = primaryDialSize * secondaryDialsBaseScale

        val placeSecondaryDials = placeSecondaryDials(
            secondaryDialsMeasurables,
            secondaryDialsProperties,
            primaryDialSize,
            secondaryDialSize,
            constraints,
        )

        layout(constraints.maxWidth, constraints.maxHeight) {
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
    layoutConstraints: Constraints,
): Placeable.PlacementScope.() -> Unit {
    val placeable =
        primaryDialMeasurable.measure(Constraints.fixed(primaryDialSize, primaryDialSize))

    return {
        val center = Offset(layoutConstraints.maxWidth / 2f, layoutConstraints.maxHeight / 2f)
        val position = center - Offset(placeable.width / 2f, placeable.height / 2f)
        placeable.place(position.round())
    }
}

private fun placeSecondaryDials(
    secondaryMeasurables: List<Measurable>,
    secondaryParentData: List<LayoutRadialSecondaryDialProperties>,
    primaryDialSize: Int,
    secondaryDialSize: Float,
    layoutConstraints: Constraints,
): Placeable.PlacementScope.() -> Unit {
    val placeables = secondaryMeasurables.mapIndexed { index, measurable ->
        val parentData = secondaryParentData[index]
        val size = (secondaryDialSize * parentData.scale).roundToInt()
        val childConstraints = Constraints.fixed(size, size)
        measurable.measure(childConstraints)
    }

    return {
        val center =
            IntOffset(layoutConstraints.maxWidth, layoutConstraints.maxHeight).toOffset() / 2f

        placeables.forEachIndexed { index, placeable ->
            val position = LayoutRadialSecondaryDialGeometry.findSecondaryDialCenterPosition(
                secondaryParentData[index],
                primaryDialSize.toFloat(),
                secondaryDialSize,
                center,
            )
            val sizeOffset = IntOffset(placeable.width, placeable.height) / 2f
            placeable.place(position.round() - sizeOffset)
        }
    }
}
