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

package gg.jam.jampadcompose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import gg.jam.jampadcompose.config.HapticFeedbackType
import gg.jam.jampadcompose.handlers.Pointer
import gg.jam.jampadcompose.haptics.InputHapticGenerator
import gg.jam.jampadcompose.haptics.rememberHapticGenerator
import gg.jam.jampadcompose.ids.ControlId
import gg.jam.jampadcompose.inputevents.InputEvent
import gg.jam.jampadcompose.inputevents.InputEventsGenerator
import gg.jam.jampadcompose.inputstate.InputState
import gg.jam.jampadcompose.utils.AsyncLaunchedEffect
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
@Composable
fun JamPad(
    modifier: Modifier = Modifier,
    onInputStateUpdated: ((InputState) -> Unit)? = null,
    onInputEvents: ((List<InputEvent>) -> Unit)? = null,
    hapticFeedbackType: HapticFeedbackType = HapticFeedbackType.PRESS,
    simulatedControlIds: State<Set<ControlId>> = mutableStateOf(emptySet()),
    simulatedState: State<InputState> = mutableStateOf(InputState()),
    content: @Composable JamPadScope.() -> Unit,
) {
    val scope = remember { JamPadScope() }
    val rootPosition = remember { mutableStateOf(Offset.Zero) }

    val hapticGenerator = rememberHapticGenerator()
    val inputHapticGenerator = remember { InputHapticGenerator(hapticGenerator, hapticFeedbackType) }
    val inputEventsGenerator = remember { InputEventsGenerator() }

    LaunchedEffect(simulatedState.value) {
        scope.inputState.value = scope.handleSimulatedInputEvents(
            simulatedControlIds.value,
            scope.inputState.value,
            simulatedState.value
        )
    }

    Box(
        modifier =
            modifier
                .fillMaxSize()
                .onGloballyPositioned { rootPosition.value = it.positionInRoot() }
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            val pointers = event.changes
                                .asSequence()
                                .filter { it.pressed }
                                .map { Pointer(it.id.value, it.position + rootPosition.value) }

                            val updatedInputState = scope.handleInputEvent(pointers)
                            scope.inputState.value = scope.handleSimulatedInputEvents(
                                simulatedControlIds.value,
                                updatedInputState,
                                simulatedState.value
                            )
                        }
                    }
                },
    ) {
        scope.content()
    }

    val inputState = scope.inputState.value

    if (onInputStateUpdated != null) {
        LaunchedEffect(inputState) {
            onInputStateUpdated.invoke(inputState)
        }
    }

    if (onInputEvents != null) {
        LaunchedEffect(inputState) {
            onInputEvents(inputEventsGenerator.onInputStateChanged(inputState))
        }
    }

    AsyncLaunchedEffect("Haptics", inputState) {
        inputHapticGenerator.onInputStateChanged(inputState)
    }
}
