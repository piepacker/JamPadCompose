package gg.jam.jampadcompose.dials

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import gg.jam.jampadcompose.GamePadScope
import gg.jam.jampadcompose.handlers.AnalogHandler
import gg.jam.jampadcompose.ui.ButtonForegroundDefault
import gg.jam.jampadcompose.ui.DialBackgroundDefault

@Composable
fun GamePadScope.AnalogDial(
    modifier: Modifier = Modifier,
    id: Int,
    background: @Composable () -> Unit = { DialBackgroundDefault() },
    foreground: @Composable (Boolean) -> Unit = {
        ButtonForegroundDefault(pressed = it, scale = 1f)
    },
) {
    val position = inputState.value.getContinuousDirection(id, Offset.Zero)

    BoxWithConstraints(
        modifier =
            modifier
                .aspectRatio(1f)
                .onGloballyPositioned { registerHandler(AnalogHandler(id, it.boundsInRoot())) },
        contentAlignment = Alignment.Center,
    ) {
        Box(modifier = Modifier.fillMaxSize(0.75f)) {
            background()
        }
        Box(
            modifier =
                Modifier
                    .fillMaxSize(0.50f)
                    .offset(maxWidth * position.x * 0.25f, maxHeight * position.y * 0.25f),
        ) {
            foreground(inputState.value.getContinuousDirection(id) != Offset.Unspecified)
        }
    }
}
