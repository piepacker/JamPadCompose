package gg.jam.jampadcompose.layouts

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.round
import gg.jam.jampadcompose.geometry.GravityArrangement
import kotlin.math.roundToInt

@Composable
internal fun GravityPointsLayout(
    modifier: Modifier = Modifier,
    gravityArrangement: GravityArrangement,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        val placeableSize = (gravityArrangement.getSize() * minOf(
            constraints.maxHeight,
            constraints.maxWidth
        )).roundToInt()

        val childConstraints = constraints.copy(
            minWidth = placeableSize,
            maxWidth = placeableSize,
            minHeight = placeableSize,
            maxHeight = placeableSize,
        )

        val placeables = measurables.map { it.measure(childConstraints) }

        val radius = (minOf(constraints.maxWidth, constraints.maxHeight) - placeableSize) / 2f

        layout(constraints.maxWidth, constraints.maxHeight) {
            val center = Offset(constraints.maxWidth / 2f, constraints.maxHeight / 2f)
            val offsets = gravityArrangement.getGravityPoints().map { it.position }

            placeables
                .zip(offsets)
                .forEach { (placeable, offset) ->
                    placeable.place(
                        (center + offset * radius - Offset(
                            placeableSize / 2f,
                            placeableSize / 2f
                        )).round()
                    )
                }
        }
    }
}
