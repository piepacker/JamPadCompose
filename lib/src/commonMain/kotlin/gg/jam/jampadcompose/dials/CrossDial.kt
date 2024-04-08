package gg.jam.jampadcompose.dials

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import gg.jam.jampadcompose.GamePadScope
import gg.jam.jampadcompose.handlers.CrossHandler
import gg.jam.jampadcompose.ui.CrossForegroundDefault
import gg.jam.jampadcompose.ui.DialBackgroundDefault

@Composable
fun GamePadScope.CrossDial(
    modifier: Modifier = Modifier,
    id: Int,
    background: @Composable () -> Unit = { DialBackgroundDefault() },
    foreground: @Composable (Offset) -> Unit = { CrossForegroundDefault(direction = it) },
) {
    Box(
        modifier =
            modifier
                .aspectRatio(1f)
                .onGloballyPositioned { registerHandler(CrossHandler(id, it.boundsInRoot())) },
    ) {
        background()
        foreground(inputState.value.getDiscreteDirection(id))
    }
}
