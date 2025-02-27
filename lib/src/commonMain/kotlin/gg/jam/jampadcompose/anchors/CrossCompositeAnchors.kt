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
import gg.jam.jampadcompose.utils.Constants
import kotlin.math.cos
import kotlin.math.sin

@Composable
internal fun rememberCrossCompositeAnchors(): List<ButtonAnchor> {
    return remember {
        val compositeButtonAnchors =
            (0..4)
                .map { index ->
                    val radius = 0.9f
                    val angle = (Constants.PI2 * (index + 0.5f) / 4)
                    ButtonAnchor(
                        Offset(cos(angle), sin(angle)) * radius,
                        0.5f,
                        setOf(),
                        0.1f,
                    )
                }

        compositeButtonAnchors
    }
}
