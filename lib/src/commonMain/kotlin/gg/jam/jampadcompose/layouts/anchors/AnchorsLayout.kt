/*
 * Copyright (c) Jam.gg 2025.
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

package gg.jam.jampadcompose.layouts.anchors

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.round
import gg.jam.jampadcompose.anchors.Anchor
import kotlin.math.roundToInt

@Composable
internal fun AnchorsLayout(
    modifier: Modifier = Modifier,
    anchors: List<Anchor>,
    content: @Composable () -> Unit,
) {
    Layout(
        modifier = modifier,
        content = content,
    ) { measurables, constraints ->
        val baseSize = minOf(constraints.maxHeight, constraints.maxWidth)

        val placeables = measurables.zip(anchors)
            .map { (measurable, anchor) ->
                val measurableConstraints =
                    constraints.copy(
                        minWidth = (baseSize * anchor.size).roundToInt(),
                        maxWidth = (baseSize * anchor.size).roundToInt(),
                        minHeight = (baseSize * anchor.size).roundToInt(),
                        maxHeight = (baseSize * anchor.size).roundToInt(),
                    )

                measurable.measure(measurableConstraints)
            }

        layout(constraints.maxWidth, constraints.maxHeight) {
            val center = Offset(constraints.maxWidth / 2f, constraints.maxHeight / 2f)
            val offsets = anchors.map { it.position }

            placeables
                .zip(offsets)
                .forEach { (placeable, offset) ->
                    val radius = (minOf(constraints.maxWidth, constraints.maxHeight) - placeable.width) / 2f

                    placeable.place(
                        (
                            center + offset * radius -
                                Offset(
                                    placeable.width / 2f,
                                    placeable.width / 2f,
                                )
                        ).round(),
                    )
                }
        }
    }
}
