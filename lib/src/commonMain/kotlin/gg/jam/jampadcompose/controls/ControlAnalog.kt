package gg.jam.jampadcompose.controls

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
import gg.jam.jampadcompose.handlers.AnalogPointerHandler
import gg.jam.jampadcompose.ui.DefaultButtonForeground
import gg.jam.jampadcompose.ui.DefaultControlBackground

@Composable
fun GamePadScope.ControlAnalog(
    modifier: Modifier = Modifier,
    id: Int,
    background: @Composable () -> Unit = { DefaultControlBackground() },
    foreground: @Composable (Boolean) -> Unit = {
        DefaultButtonForeground(pressed = it, scale = 1f)
    },
) {
    val position = inputState.value.getContinuousDirection(id, Offset.Zero)

    BoxWithConstraints(
        modifier =
            modifier
                .aspectRatio(1f)
                .onGloballyPositioned { registerHandler(AnalogPointerHandler(id, it.boundsInRoot())) },
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
