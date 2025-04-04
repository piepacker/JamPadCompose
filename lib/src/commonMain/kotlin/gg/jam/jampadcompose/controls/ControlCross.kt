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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import gg.jam.jampadcompose.JamPadScope
import gg.jam.jampadcompose.anchors.rememberCompositeAnchors
import gg.jam.jampadcompose.anchors.rememberPrimaryAnchors
import gg.jam.jampadcompose.handlers.CrossPointerHandler
import gg.jam.jampadcompose.ids.DiscreteDirectionId
import gg.jam.jampadcompose.ui.DefaultControlBackground
import gg.jam.jampadcompose.ui.DefaultCrossForeground

@Composable
fun JamPadScope.ControlCross(
    modifier: Modifier = Modifier,
    id: DiscreteDirectionId,
    allowDiagonals: Boolean = true,
    background: @Composable () -> Unit = { DefaultControlBackground() },
    foreground: @Composable (Offset) -> Unit = {
        DefaultCrossForeground(direction = it, allowDiagonals = allowDiagonals)
    },
) {
    val directions = CrossPointerHandler.Direction.entries
    val primaryAnchors = rememberPrimaryAnchors(directions, 0f)
    val compositeAnchors =
        if (allowDiagonals) {
            rememberCompositeAnchors(directions, 0f)
        } else {
            emptyList()
        }

    val positionState =
        remember {
            derivedStateOf { inputState.value.getDiscreteDirection(id) }
        }

    val handler =
        remember(id, primaryAnchors, compositeAnchors) {
            CrossPointerHandler(id, primaryAnchors + compositeAnchors)
        }

    DisposableEffect(handler) {
        registerHandler(handler)
        onDispose {
            unregisterHandler(handler)
        }
    }

    Box(
        modifier =
            modifier
                .aspectRatio(1f)
                .onGloballyPositioned { updateHandlerPosition(handler, it.boundsInRoot()) },
    ) {
        background()
        foreground(positionState.value)
    }
}
