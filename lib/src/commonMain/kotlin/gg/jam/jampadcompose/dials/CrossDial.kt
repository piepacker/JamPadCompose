package gg.jam.jampadcompose.dials

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import gg.jam.jampadcompose.GamePadScope
import gg.jam.jampadcompose.handlers.CrossHandler
import gg.jam.jampadcompose.layouts.CircularLayout
import gg.jam.jampadcompose.ui.ButtonForegroundDefault
import gg.jam.jampadcompose.ui.DialBackgroundDefault

@Composable
fun GamePadScope.CrossDial(
    modifier: Modifier = Modifier,
    id: Int,
    background: @Composable () -> Unit = { DialBackgroundDefault() },
    rightDial: @Composable (Boolean) -> Unit = {
        ButtonForegroundDefault(
            pressed = it,
            icon = Icons.Default.KeyboardArrowRight
        )
    },
    bottomDial: @Composable (Boolean) -> Unit = {
        ButtonForegroundDefault(
            pressed = it,
            icon = Icons.Default.KeyboardArrowDown
        )
    },
    leftDial: @Composable (Boolean) -> Unit = {
        ButtonForegroundDefault(
            pressed = it,
            icon = Icons.Default.KeyboardArrowLeft
        )
    },
    topDial: @Composable (Boolean) -> Unit = {
        ButtonForegroundDefault(
            pressed = it,
            icon = Icons.Default.KeyboardArrowUp
        )
    },
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .onGloballyPositioned { registerHandler(CrossHandler(id, it.boundsInRoot())) }
    ) {
        background()

        CircularLayout(modifier = Modifier.fillMaxSize()) {
            val position = inputState.value.getAnalogKey(id)

            rightDial(position.x > 0.5f)
            bottomDial(position.y < -0.5f)
            leftDial(position.x < -0.5f)
            topDial(position.y > 0.5f)
        }
    }
}
