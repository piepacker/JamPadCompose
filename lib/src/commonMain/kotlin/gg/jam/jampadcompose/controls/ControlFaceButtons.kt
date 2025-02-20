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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import gg.jam.jampadcompose.JamPadScope
import gg.jam.jampadcompose.arrangements.EmptyArrangement
import gg.jam.jampadcompose.arrangements.FaceButtonsCircleArrangement
import gg.jam.jampadcompose.arrangements.FaceButtonsCircumferenceArrangement
import gg.jam.jampadcompose.arrangements.FaceButtonsCompositeArrangement
import gg.jam.jampadcompose.arrangements.GravityArrangement
import gg.jam.jampadcompose.config.FaceButtonsLayout
import gg.jam.jampadcompose.handlers.FaceButtonsPointerHandler
import gg.jam.jampadcompose.ids.KeyId
import gg.jam.jampadcompose.layouts.gravity.GravityArrangementLayout
import gg.jam.jampadcompose.ui.DefaultButtonForeground
import gg.jam.jampadcompose.ui.DefaultCompositeForeground
import gg.jam.jampadcompose.ui.DefaultControlBackground

@Composable
fun JamPadScope.ControlFaceButtons(
    modifier: Modifier = Modifier,
    rotationInDegrees: Float = 0f,
    ids: List<KeyId>,
    sockets: Int = ids.size,
    faceButtonsLayout: FaceButtonsLayout = FaceButtonsLayout.CIRCUMFERENCE,
    includeComposite: Boolean = true,
    background: @Composable () -> Unit = { DefaultControlBackground() },
    foreground: @Composable (KeyId, Boolean) -> Unit = { _, pressed ->
        DefaultButtonForeground(pressed = pressed)
    },
    foregroundComposite: @Composable (Boolean) -> Unit = { pressed ->
        DefaultCompositeForeground(pressed = pressed)
    },
) {
    val primaryArrangement = rememberPrimaryArrangement(ids, sockets, faceButtonsLayout, rotationInDegrees)
    val compositeArrangement =
        rememberCompositeArrangement(includeComposite, ids, sockets, rotationInDegrees)

    Box(
        modifier =
            modifier
                .aspectRatio(1f)
                .onGloballyPositioned {
                    registerHandler(
                        FaceButtonsPointerHandler(
                            listOf(ids, faceButtonsLayout, rotationInDegrees, sockets).hashCode(),
                            it.boundsInRoot(),
                            primaryArrangement,
                            compositeArrangement,
                        ),
                    )
                },
    ) {
        background()

        GravityArrangementLayout(
            modifier = Modifier.fillMaxSize(),
            gravityArrangement = primaryArrangement,
        ) {
            ids.forEach { id ->
                foreground(id, inputState.value.getDigitalKey(id))
            }
        }

        GravityArrangementLayout(
            modifier = Modifier.fillMaxSize(),
            gravityArrangement = compositeArrangement,
        ) {
            compositeArrangement.getGravityPoints().forEach { point ->
                val compositeState = remember {
                    derivedStateOf { point.keys.all { inputState.value.getDigitalKey(KeyId(it)) } }
                }
                foregroundComposite(compositeState.value)
            }
        }
    }
}

@Composable
private fun rememberCompositeArrangement(
    includeCompositeButtons: Boolean,
    ids: List<KeyId>,
    sockets: Int,
    rotationInDegrees: Float,
): GravityArrangement {
    return remember(ids, includeCompositeButtons, rotationInDegrees) {
        if (includeCompositeButtons) {
            FaceButtonsCompositeArrangement(ids, sockets, rotationInDegrees)
        } else {
            EmptyArrangement
        }
    }
}

@Composable
private fun rememberPrimaryArrangement(
    ids: List<KeyId>,
    sockets: Int,
    faceButtonsLayout: FaceButtonsLayout,
    rotationInDegrees: Float,
): GravityArrangement {
    return remember(ids, faceButtonsLayout, rotationInDegrees) {
        when (faceButtonsLayout) {
            FaceButtonsLayout.CIRCUMFERENCE ->
                FaceButtonsCircumferenceArrangement(
                    ids,
                    sockets,
                    rotationInDegrees,
                )

            FaceButtonsLayout.CIRCLE -> FaceButtonsCircleArrangement(ids, sockets, rotationInDegrees)
        }
    }
}
