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
import gg.jam.jampadcompose.config.HapticFeedbackType
import gg.jam.jampadcompose.handlers.Pointer
import gg.jam.jampadcompose.haptics.HapticGenerator
import gg.jam.jampadcompose.haptics.InputHapticGenerator
import gg.jam.jampadcompose.inputstate.InputState
import gg.jam.jampadcompose.utils.relativeTo

@Composable
fun GamePad(
    modifier: Modifier = Modifier,
    onInputStateUpdated: (InputState) -> Unit = { },
    hapticFeedbackType: HapticFeedbackType = HapticFeedbackType.PRESS,
    content: @Composable GamePadScope.() -> Unit,
) {
    val scope = remember { GamePadScope() }
    val rootPosition = remember { mutableStateOf(Offset.Zero) }

    val inputHapticGenerator =
        remember {
            InputHapticGenerator(HapticGenerator, hapticFeedbackType)
        }

    Row(
        modifier =
            modifier
                .fillMaxSize()
                .onGloballyPositioned { rootPosition.value = it.positionInRoot() }
                .pointerInput("InputEventKey") {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            val trackedPointers = scope.getTrackedIds()

                            val handlersAssociations =
                                event.changes
                                    .asSequence()
                                    .filter { it.pressed }
                                    .map { Pointer(it.id.value, it.position + rootPosition.value) }
                                    .groupBy { pointer ->
                                        if (pointer.pointerId in trackedPointers) {
                                            scope.getHandlerTracking(pointer.pointerId)
                                        } else {
                                            scope.getHandlerAtPosition(pointer.position)
                                        }
                                    }

                            scope.inputState.value =
                                scope.getAllHandlers()
                                    .fold(scope.inputState.value) { state, handler ->
                                        val pointers =
                                            handlersAssociations.getOrElse(handler) { emptyList() }

                                        val relativePointers =
                                            pointers
                                                .map {
                                                    Pointer(
                                                        it.pointerId,
                                                        it.position.relativeTo(handler.rect),
                                                    )
                                                }

                                        val (updatedState, updatedTracked) =
                                            handler.handle(
                                                relativePointers,
                                                state,
                                                scope.getStartGestureForHandler(handler),
                                            )

                                        scope.setStartGestureForHandler(handler, updatedTracked)
                                        updatedState
                                    }
                        }
                    }
                },
    ) {
        scope.content()
    }

    DisposableEffect(key1 = scope.inputState.value) {
        onInputStateUpdated(scope.inputState.value)
        onDispose { }
    }

    DisposableEffect(key1 = scope.inputState.value) {
        inputHapticGenerator.onInputStateChanged(scope.inputState.value)
        onDispose { }
    }
}
