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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import gg.jam.jampadcompose.JamPadScope
import gg.jam.jampadcompose.anchors.ButtonAnchor
import gg.jam.jampadcompose.anchors.rememberFaceButtonCompositeAnchors
import gg.jam.jampadcompose.anchors.rememberFaceButtonsAnchors
import gg.jam.jampadcompose.handlers.FaceButtonsPointerHandler
import gg.jam.jampadcompose.ids.KeyId
import gg.jam.jampadcompose.layouts.anchors.ButtonAnchorsLayout
import gg.jam.jampadcompose.ui.DefaultButtonForeground
import gg.jam.jampadcompose.ui.DefaultCompositeForeground
import gg.jam.jampadcompose.ui.DefaultControlBackground

@Composable
fun JamPadScope.ControlFaceButtons(
    modifier: Modifier = Modifier,
    rotationInDegrees: Float = 0f,
    ids: List<KeyId>,
    includeComposite: Boolean = true,
    background: @Composable () -> Unit = { DefaultControlBackground() },
    foreground: @Composable (KeyId, Boolean) -> Unit = { _, pressed ->
        DefaultButtonForeground(pressed = pressed)
    },
    foregroundComposite: @Composable (Boolean) -> Unit = { pressed ->
        DefaultCompositeForeground(pressed = pressed)
    },
) {
    val mainAnchors = rememberFaceButtonsAnchors(ids, rotationInDegrees)
    val compositeAnchors = if (includeComposite) {
        rememberFaceButtonCompositeAnchors(ids, rotationInDegrees)
    } else {
        emptyList()
    }

    ControlFaceButtons(
        modifier = modifier,
        mainButtonAnchors = mainAnchors,
        compositeButtonAnchors = compositeAnchors,
        background = background,
        foreground = foreground,
        foregroundComposite = foregroundComposite,
    )
}

@Composable
fun JamPadScope.ControlFaceButtons(
    modifier: Modifier = Modifier,
    mainButtonAnchors: List<ButtonAnchor>,
    compositeButtonAnchors: List<ButtonAnchor>,
    background: @Composable () -> Unit = { DefaultControlBackground() },
    foreground: @Composable (KeyId, Boolean) -> Unit = { _, pressed ->
        DefaultButtonForeground(pressed = pressed)
    },
    foregroundComposite: @Composable (Boolean) -> Unit = { pressed ->
        DefaultCompositeForeground(pressed = pressed)
    },
) {
    val anchors = mainButtonAnchors + compositeButtonAnchors
    val handler = remember(anchors) { FaceButtonsPointerHandler(anchors) }
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

        ButtonAnchorsLayout(
            modifier = Modifier.fillMaxSize(),
            buttonAnchors = mainButtonAnchors,
        ) {
            mainButtonAnchors
                .flatMap { it.buttons }
                .forEach {
                    val keyState = remember {
                        derivedStateOf { inputState.value.getDigitalKey(it) }
                    }
                    foreground(it, keyState.value)
                }
        }

        ButtonAnchorsLayout(
            modifier = Modifier.fillMaxSize(),
            buttonAnchors = compositeButtonAnchors,
        ) {
            compositeButtonAnchors.forEach { point ->
                val compositeState = remember {
                    derivedStateOf { point.buttons.all { inputState.value.getDigitalKey(it) } }
                }
                foregroundComposite(compositeState.value)
            }
        }
    }
}
