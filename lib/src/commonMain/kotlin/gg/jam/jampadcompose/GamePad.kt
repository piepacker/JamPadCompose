package gg.jam.jampadcompose

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import gg.jam.jampadcompose.handlers.Pointer
import gg.jam.jampadcompose.inputstate.InputState
import gg.jam.jampadcompose.utils.relativeTo

@Composable
fun GamePad(
    modifier: Modifier = Modifier,
    onInputStateUpdated: (InputState) -> Unit = { },
    content: @Composable GamePadScope.() -> Unit
) {
    val scope = remember { GamePadScope() }
    val rootPosition = remember { mutableStateOf(Offset.Zero) }

    Row(
        modifier = modifier
            .fillMaxSize()
            .onGloballyPositioned { rootPosition.value = it.positionInRoot() }
            .pointerInput("InputEventKey") {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()

                        val pointers = event.changes
                            .asSequence()
                            .filter { it.pressed }
                            .map {
                                Pointer(it.id.value, it.position + rootPosition.value)
                            }
                            .toList()

                        val trackedPointersId = scope.gestureStart
                            .values
                            .mapNotNull { it?.pointerId }
                            .toSet()

                        val pointersInDial = pointers.groupBy { pointer ->
                            scope.handlers.values.firstOrNull {
                                it.rect.contains(pointer.position)
                            }
                        }

                        scope.inputState.value =
                            scope.handlers.values.fold(scope.inputState.value) { state, handler ->
                                val currentlyTrackedPointer =
                                    scope.gestureStart[handler.handlerId()]

                                val dialUntrackedPointers = pointersInDial
                                    .getOrElse(handler) { emptyList() }
                                    .filter { it.pointerId !in trackedPointersId }

                                val dialTrackedPointers = pointers
                                    .filter { it.pointerId == scope.gestureStart[handler.handlerId()]?.pointerId }

                                val allPointers = (dialUntrackedPointers + dialTrackedPointers)
                                    .map { pointer ->
                                        pointer.copy(
                                            position = pointer.position.relativeTo(handler.rect)
                                        )
                                    }

                                val (updatedState, updatedTracked) = handler.handle(
                                    allPointers,
                                    state,
                                    currentlyTrackedPointer
                                )

                                scope.gestureStart[handler.handlerId()] = updatedTracked
                                updatedState
                            }
                    }
                }
            }
    ) {
        scope.content()
    }

    DisposableEffect(key1 = scope.inputState.value) {
        onInputStateUpdated(scope.inputState.value)
        onDispose { }
    }
}
