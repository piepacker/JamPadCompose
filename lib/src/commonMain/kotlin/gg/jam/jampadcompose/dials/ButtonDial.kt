package gg.jam.jampadcompose.dials

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import gg.jam.jampadcompose.GamePadScope
import gg.jam.jampadcompose.handlers.ButtonHandler
import gg.jam.jampadcompose.ui.ButtonForegroundDefault
import gg.jam.jampadcompose.ui.DialBackgroundDefault

@Composable
fun GamePadScope.ButtonDial(
    modifier: Modifier = Modifier,
    id: Int,
    background: @Composable () -> Unit = { DialBackgroundDefault() },
    foreground: @Composable () -> Unit = {
        ButtonForegroundDefault(
            pressed = inputState.value.getDigitalKey(id)
        )
    }
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .onGloballyPositioned {
                registerHandler(ButtonHandler(id, it.boundsInRoot()))
            }
    ) {
        background()
        foreground()
    }
}
