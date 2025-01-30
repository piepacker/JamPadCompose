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

package gg.jam.jampadcompose.layouts.radial.secondarydials

import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.unit.Density

internal class LayoutRadialSecondaryDialScaleModifier(
    private val scale: Float,
) : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?): Any {
        val currentData =
            (parentData as? LayoutRadialSecondaryDialProperties)
                ?: LayoutRadialSecondaryDialProperties()
        return currentData.copy(scale = scale)
    }
}
