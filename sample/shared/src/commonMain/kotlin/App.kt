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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
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
            HalfPad(
                modifier = Modifier.weight(1f, fill = false),
                top = {
                    ControlButton(modifier = Modifier.weight(1f), id = 0)
                    ControlButton(modifier = Modifier.weight(1f), id = 1)
                    ControlButton(modifier = Modifier.weight(1f), id = 2)
                },
                primary = {
                    ControlCross(
                        modifier = Modifier.fillMaxSize(),
                        id = 0,
                    )
                },
                secondary = {
                    ControlAnalog(
                        modifier = Modifier.fillMaxSize(),
                        id = 1,
                    )
                },
            )
            HalfPad(
                modifier = Modifier.weight(1f, fill = false),
                top = {
                    ControlButton(modifier = Modifier.weight(1f), id = 3)
                    ControlButton(modifier = Modifier.weight(1f), id = 4)
                    ControlButton(modifier = Modifier.weight(1f), id = 5)
                },
                primary = {
                    ControlFaceButtons(
                        modifier = Modifier.fillMaxSize(),
                        ids = listOf(6, 7, 8),
                    )
                },
                secondary = {
                    ControlAnalog(
                        modifier = Modifier.fillMaxSize(),
                        id = 2,
                    )
                },
            )
        }
    }
}

@Composable
private fun HalfPad(
    modifier: Modifier = Modifier,
    top: @Composable () -> Unit,
    primary: @Composable () -> Unit,
    secondary: @Composable () -> Unit,
) {
    Column(
        modifier =
            modifier
                .padding(16.dp)
                .aspectRatio(3f / 7f)
                .widthIn(0.dp, 50.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().aspectRatio(3f),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            top()
        }
        Box(modifier = Modifier.fillMaxWidth().aspectRatio(1f)) {
            primary()
        }
        Box(modifier = Modifier.fillMaxWidth().aspectRatio(1f)) {
            secondary()
        }
    }
}
