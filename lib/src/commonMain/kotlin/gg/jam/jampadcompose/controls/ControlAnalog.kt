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

package gg.jam.jampadcompose.controls

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import gg.jam.jampadcompose.JamPadScope
import gg.jam.jampadcompose.handlers.AnalogPointerHandler
import gg.jam.jampadcompose.ids.ContinuousDirectionId
import gg.jam.jampadcompose.ids.KeyId
import gg.jam.jampadcompose.ui.DefaultButtonForeground
import gg.jam.jampadcompose.ui.DefaultControlBackground
import gg.jam.jampadcompose.utils.ifUnspecified

@Composable
fun JamPadScope.ControlAnalog(
    modifier: Modifier = Modifier,
    id: ContinuousDirectionId,
    analogPressId: KeyId? = null,
    background: @Composable () -> Unit = { DefaultControlBackground() },
    foreground: @Composable (Boolean) -> Unit = {
        DefaultButtonForeground(pressed = it, scale = 1f)
    },
) {
    val positionState = remember {
        derivedStateOf { inputState.value.getContinuousDirection(id) }
    }

    val position = positionState.value
    val safePosition = position.ifUnspecified { Offset.Zero }

    BoxWithConstraints(
        modifier =
            modifier
                .aspectRatio(1f)
                .onGloballyPositioned {
                    registerHandler(
                        AnalogPointerHandler(id, it.boundsInRoot(), analogPressId),
                        AnalogPointerHandler.Data()
                    )
                },
        contentAlignment = Alignment.Center,
    ) {
        Box(modifier = Modifier.fillMaxSize(0.75f)) {
            background()
        }
        Box(
            modifier =
                Modifier
                    .fillMaxSize(0.50f)
                    .offset(maxWidth * safePosition.x * 0.25f, maxHeight * safePosition.y * 0.25f),
        ) {
            foreground(position != Offset.Unspecified)
        }
    }
}
