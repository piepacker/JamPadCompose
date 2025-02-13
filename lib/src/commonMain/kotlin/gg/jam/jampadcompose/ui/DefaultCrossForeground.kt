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

package gg.jam.jampadcompose.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import gg.jam.jampadcompose.arrangements.CrossCompositeArrangement
import gg.jam.jampadcompose.layouts.circular.CircularLayout
import gg.jam.jampadcompose.layouts.gravity.GravityArrangementLayout
import gg.jam.jampadcompose.utils.ifUnspecified

@Composable
fun DefaultCrossForeground(
    modifier: Modifier = Modifier,
    direction: Offset,
    rightDial: @Composable (Boolean) -> Unit = {
        DefaultButtonForeground(
            pressed = it,
            iconPainter = rememberVectorPainter(Icons.Default.KeyboardArrowRight),
        )
    },
    bottomDial: @Composable (Boolean) -> Unit = {
        DefaultButtonForeground(
            pressed = it,
            iconPainter = rememberVectorPainter(Icons.Default.KeyboardArrowDown),
        )
    },
    leftDial: @Composable (Boolean) -> Unit = {
        DefaultButtonForeground(
            pressed = it,
            iconPainter = rememberVectorPainter(Icons.Default.KeyboardArrowLeft),
        )
    },
    topDial: @Composable (Boolean) -> Unit = {
        DefaultButtonForeground(
            pressed = it,
            iconPainter = rememberVectorPainter(Icons.Default.KeyboardArrowUp),
        )
    },
    foregroundComposite: @Composable (Boolean) -> Unit = { pressed ->
        DefaultCompositeForeground(pressed = pressed)
    },
) {
    val adjustedDirection = direction.ifUnspecified { Offset.Zero }
    val isTop = adjustedDirection.y > 0.5f
    val isLeft = adjustedDirection.x < -0.5f
    val isRight = adjustedDirection.x > 0.5f
    val isBottom = adjustedDirection.y < -0.5f

    CircularLayout(modifier = modifier.fillMaxSize()) {
        rightDial(isRight)
        bottomDial(isBottom)
        leftDial(isLeft)
        topDial(isTop)
    }

    val compositeArrangement = remember { CrossCompositeArrangement(0f) }

    GravityArrangementLayout(
        modifier = Modifier.fillMaxSize(),
        gravityArrangement = compositeArrangement,
    ) {
        foregroundComposite(isBottom && isRight)
        foregroundComposite(isBottom && isLeft)
        foregroundComposite(isTop && isLeft)
        foregroundComposite(isTop && isRight)
    }
}
