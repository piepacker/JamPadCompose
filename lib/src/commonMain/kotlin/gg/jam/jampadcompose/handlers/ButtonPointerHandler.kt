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

package gg.jam.jampadcompose.handlers

import androidx.compose.ui.geometry.Rect
import gg.jam.jampadcompose.ids.KeyId
import gg.jam.jampadcompose.inputstate.InputState

data class ButtonPointerHandler(
    private val buttonId: KeyId,
    override val rect: Rect
) : PointerHandler {

    override val id: Int = buttonId.value

    override fun handle(
        pointers: List<Pointer>,
        inputState: InputState,
        startDragGesture: Pointer?,
        data: Any?,
    ): Result {
        return Result(inputState.setDigitalKey(KeyId(id), pointers.isNotEmpty()))
    }
}
