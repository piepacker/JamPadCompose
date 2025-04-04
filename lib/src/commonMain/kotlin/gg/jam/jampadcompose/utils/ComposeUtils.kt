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

package gg.jam.jampadcompose.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.withContext

@OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
@Composable
fun AsyncLaunchedEffect(
    threadKey: String,
    key: Any?,
    block: suspend CoroutineScope.() -> Unit,
) {
    val executor = remember { newSingleThreadContext(threadKey) }

    LaunchedEffect(key) {
        withContext(executor) {
            block()
        }
    }

    DisposableEffect(Unit) {
        onDispose { executor.close() }
    }
}
