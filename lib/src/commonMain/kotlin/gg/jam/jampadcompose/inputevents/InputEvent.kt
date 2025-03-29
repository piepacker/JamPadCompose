/*
 * Copyright (c) Filippo Scognamiglio 2025.
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

package gg.jam.jampadcompose.inputevents

import androidx.compose.ui.geometry.Offset

sealed interface InputEvent {
    data class ContinuousDirection(val id: Int, val direction: Offset) : InputEvent

    data class DiscreteDirection(val id: Int, val direction: Offset) : InputEvent

    data class Button(val id: Int, val pressed: Boolean) : InputEvent
}
