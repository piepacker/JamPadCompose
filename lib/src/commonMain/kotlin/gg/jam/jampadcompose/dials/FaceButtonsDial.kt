package gg.jam.jampadcompose.dials

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import gg.jam.jampadcompose.GamePadScope
import gg.jam.jampadcompose.geometry.CircleGravityArrangement
import gg.jam.jampadcompose.geometry.CircumferenceCompositeGravityArrangement
import gg.jam.jampadcompose.geometry.CircumferenceGravityArrangement
import gg.jam.jampadcompose.geometry.EmptyGravityArrangement
import gg.jam.jampadcompose.geometry.GravityArrangement
import gg.jam.jampadcompose.handlers.GravityPointsHandler
import gg.jam.jampadcompose.layouts.GravityPointsLayout
import gg.jam.jampadcompose.ui.ButtonForegroundDefault
import gg.jam.jampadcompose.ui.CompositeForegroundDefault
import gg.jam.jampadcompose.ui.DialBackgroundDefault

@Composable
fun GamePadScope.FaceButtonsDial(
    modifier: Modifier = Modifier,
    rotationInDegrees: Float = 0f,
    ids: List<Int>,
    startFromCenter: Boolean = false, // TODO... Let's find a better name for this value here.
    includeComposite: Boolean = true,
    background: @Composable () -> Unit = { DialBackgroundDefault() },
    foreground: @Composable (Int, Boolean) -> Unit = { _, pressed ->
        ButtonForegroundDefault(pressed = pressed)
    },
    foregroundComposite: @Composable (Boolean) -> Unit = { pressed ->
        CompositeForegroundDefault(pressed = pressed)
    }
) {
    val primaryArrangement = rememberPrimaryArrangement(ids, startFromCenter, rotationInDegrees)
    val compositeArrangement =
        rememberCompositeArrangement(includeComposite, ids, rotationInDegrees)

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .onGloballyPositioned {
                registerHandler(
                    GravityPointsHandler(
                        ids.hashCode(),
                        it.boundsInRoot(),
                        primaryArrangement,
                        compositeArrangement
                    )
                )
            }
    ) {
        background()

        GravityPointsLayout(
            modifier = Modifier.fillMaxSize(),
            gravityArrangement = primaryArrangement
        ) {
            ids.forEach { id ->
                foreground(id, inputState.value.getDigitalKey(id))
            }
        }

        GravityPointsLayout(
            modifier = Modifier.fillMaxSize(),
            gravityArrangement = compositeArrangement
        ) {
            compositeArrangement.getGravityPoints().forEach { id ->
                foregroundComposite(id.keys.all { inputState.value.getDigitalKey(it) })
            }
        }
    }
}

@Composable
private fun rememberCompositeArrangement(
    includeCompositeButtons: Boolean,
    ids: List<Int>,
    rotationInDegrees: Float
): GravityArrangement {
    return remember(ids, includeCompositeButtons, rotationInDegrees) {
        if (includeCompositeButtons) {
            CircumferenceCompositeGravityArrangement(ids, rotationInDegrees)
        } else {
            EmptyGravityArrangement
        }
    }
}

@Composable
private fun rememberPrimaryArrangement(
    ids: List<Int>,
    startFromCenter: Boolean,
    rotationInDegrees: Float
): GravityArrangement {
    return remember(ids, startFromCenter, rotationInDegrees) {
        if (startFromCenter) {
            CircleGravityArrangement(ids, rotationInDegrees)
        } else {
            CircumferenceGravityArrangement(ids, rotationInDegrees)
        }
    }
}
