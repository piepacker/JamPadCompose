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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import gg.jam.jampadcompose.JamPadScope
import gg.jam.jampadcompose.handlers.CrossPointerHandler
import gg.jam.jampadcompose.ui.DefaultControlBackground
import gg.jam.jampadcompose.ui.DefaultCrossForeground

@Composable
fun JamPadScope.ControlCross(
    modifier: Modifier = Modifier,
    id: Int,
    background: @Composable () -> Unit = { DefaultControlBackground() },
    foreground: @Composable (Offset) -> Unit = { DefaultCrossForeground(direction = it) },
) {
    Box(
        modifier =
            modifier
                .aspectRatio(1f)
                .onGloballyPositioned { registerHandler(CrossPointerHandler(id, it.boundsInRoot())) },
    ) {
        background()
        foreground(inputState.value.getDiscreteDirection(id))
    }
}
