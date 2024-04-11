package gg.jam.jampadcompose.controls

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import gg.jam.jampadcompose.GamePadScope
import gg.jam.jampadcompose.handlers.ButtonPointerHandler
import gg.jam.jampadcompose.ui.DefaultButtonForeground
import gg.jam.jampadcompose.ui.DefaultDialBackground

@Composable
fun GamePadScope.ControlButton(
    modifier: Modifier = Modifier,
    id: Int,
    background: @Composable (Boolean) -> Unit = { DefaultDialBackground() },
    foreground: @Composable (Boolean) -> Unit = { DefaultButtonForeground(pressed = it) },
) {
    Box(
        modifier =
            modifier
                .fillMaxSize()
                .onGloballyPositioned {
                    registerHandler(ButtonPointerHandler(id, it.boundsInRoot()))
                },
    ) {
        val pressed = inputState.value.getDigitalKey(id)
        background(pressed)
        foreground(pressed)
    }
}
