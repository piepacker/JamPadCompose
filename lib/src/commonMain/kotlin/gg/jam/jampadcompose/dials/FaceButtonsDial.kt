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
import gg.jam.jampadcompose.config.FaceButtonsLayout
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
    sockets: Int = ids.size,
    faceButtonsLayout: FaceButtonsLayout = FaceButtonsLayout.CIRCUMFERENCE,
    includeComposite: Boolean = true,
    background: @Composable () -> Unit = { DialBackgroundDefault() },
    foreground: @Composable (Int, Boolean) -> Unit = { _, pressed ->
        ButtonForegroundDefault(pressed = pressed)
    },
    foregroundComposite: @Composable (Boolean) -> Unit = { pressed ->
        CompositeForegroundDefault(pressed = pressed)
    },
) {
    val primaryArrangement = rememberPrimaryArrangement(ids, sockets, faceButtonsLayout, rotationInDegrees)
    val compositeArrangement =
        rememberCompositeArrangement(includeComposite, ids, sockets, rotationInDegrees)

    Box(
        modifier =
            modifier
                .aspectRatio(1f)
                .onGloballyPositioned {
                    registerHandler(
                        GravityPointsHandler(
                            listOf(ids, faceButtonsLayout, rotationInDegrees, sockets).hashCode(),
                            it.boundsInRoot(),
                            primaryArrangement,
                            compositeArrangement,
                        ),
                    )
                },
    ) {
        background()

        GravityPointsLayout(
            modifier = Modifier.fillMaxSize(),
            gravityArrangement = primaryArrangement,
        ) {
            ids.forEach { id ->
                foreground(id, inputState.value.getDigitalKey(id))
            }
        }

        GravityPointsLayout(
            modifier = Modifier.fillMaxSize(),
            gravityArrangement = compositeArrangement,
        ) {
            compositeArrangement.getGravityPoints().forEach { point ->
                foregroundComposite(point.keys.all { inputState.value.getDigitalKey(it) })
            }
        }
    }
}

@Composable
private fun rememberCompositeArrangement(
    includeCompositeButtons: Boolean,
    ids: List<Int>,
    sockets: Int,
    rotationInDegrees: Float,
): GravityArrangement {
    return remember(ids, includeCompositeButtons, rotationInDegrees) {
        if (includeCompositeButtons) {
            CircumferenceCompositeGravityArrangement(ids, sockets, rotationInDegrees)
        } else {
            EmptyGravityArrangement
        }
    }
}

@Composable
private fun rememberPrimaryArrangement(
    ids: List<Int>,
    sockets: Int,
    faceButtonsLayout: FaceButtonsLayout,
    rotationInDegrees: Float,
): GravityArrangement {
    return remember(ids, faceButtonsLayout, rotationInDegrees) {
        when (faceButtonsLayout) {
            FaceButtonsLayout.CIRCUMFERENCE ->
                CircumferenceGravityArrangement(
                    ids,
                    sockets,
                    rotationInDegrees,
                )

            FaceButtonsLayout.CIRCLE -> CircleGravityArrangement(ids, sockets, rotationInDegrees)
        }
    }
}
