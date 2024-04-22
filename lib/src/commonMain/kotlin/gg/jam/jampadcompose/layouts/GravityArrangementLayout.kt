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

package gg.jam.jampadcompose.layouts

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.round
import gg.jam.jampadcompose.arrangements.GravityArrangement
import kotlin.math.roundToInt

@Composable
internal fun GravityArrangementLayout(
    modifier: Modifier = Modifier,
    gravityArrangement: GravityArrangement,
    content: @Composable () -> Unit,
) {
    Layout(
        modifier = modifier,
        content = content,
    ) { measurables, constraints ->
        val placeableSize =
            (
                gravityArrangement.getSize() *
                    minOf(
                        constraints.maxHeight,
                        constraints.maxWidth,
                    )
            ).roundToInt()

        val childConstraints =
            constraints.copy(
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
                        (
                            center + offset * radius -
                                Offset(
                                    placeableSize / 2f,
                                    placeableSize / 2f,
                                )
                        ).round(),
                    )
                }
        }
    }
}
