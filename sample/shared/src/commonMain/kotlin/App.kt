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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import gg.jam.jampadcompose.JamPad
import gg.jam.jampadcompose.config.HapticFeedbackType
import gg.jam.jampadcompose.controls.ControlAnalog
import gg.jam.jampadcompose.controls.ControlButton
import gg.jam.jampadcompose.controls.ControlCross
import gg.jam.jampadcompose.controls.ControlFaceButtons
import gg.jam.jampadcompose.ids.DirectionId
import gg.jam.jampadcompose.ids.KeyId

@Composable
fun App() {
    MaterialTheme {
        SampleGamePad()
    }
}

@Composable
private fun SampleGamePad() {
    JamPad(
        hapticFeedbackType = HapticFeedbackType.PRESS,
        modifier = Modifier.fillMaxSize(),
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
        ) {
            LayoutRadial(
                modifier = Modifier.padding(8.dp).weight(1f, fill = false),
                primaryDial = {
                    ControlCross(
                        modifier = Modifier.fillMaxSize(),
                        id = DirectionId(0),
                    )
                },
                secondaryDials = {
                    ControlButton(modifier = Modifier.radialPosition(120f), id = KeyId(0))
                    ControlButton(modifier = Modifier.radialPosition(90f), id = KeyId(1))
                    ControlButton(modifier = Modifier.radialPosition(60f), id = KeyId(2))
                    ControlAnalog(
                        modifier = Modifier.radialPosition(-90f).radialScale(2f),
                        id = DirectionId(1)
                    )
                },
            )
            LayoutRadial(
                modifier = Modifier.padding(8.dp).weight(1f, fill = false),
                primaryDial = {
                    ControlFaceButtons(
                        modifier = Modifier.fillMaxSize(),
                        ids = listOf(6, 7, 8).map { KeyId(it) },
                    )
                },
                secondaryDials = {
                    ControlButton(modifier = Modifier.radialPosition(120f), id = KeyId(3))
                    ControlButton(modifier = Modifier.radialPosition(90f), id = KeyId(4))
                    ControlButton(modifier = Modifier.radialPosition(60f), id = KeyId(5))
                    ControlAnalog(
                        modifier = Modifier.radialPosition(-90f).radialScale(2f),
                        id = DirectionId(2)
                    )
                },
            )
        }
    }
}
