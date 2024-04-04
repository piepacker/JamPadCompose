package gg.jam.jampadcompose.handlers

import androidx.compose.ui.geometry.Rect
import gg.jam.jampadcompose.geometry.GravityArrangement
import gg.jam.jampadcompose.inputstate.InputState

class GravityPointsHandler(
    override val id: Int,
    override val rect: Rect,
    primaryArrangement: GravityArrangement,
    compositeArrangement: GravityArrangement,
) : Handler {
    private val gravityPoints = primaryArrangement.getGravityPoints()
    private val compositePoints = compositeArrangement.getGravityPoints()
    private val allPoints = gravityPoints + compositePoints
    private val allKeys =
        allPoints
            .flatMap { it.keys }
            .toSet()

    override fun handle(
        pointers: List<Pointer>,
        inputState: InputState,
        currentGestureStart: Pointer?,
    ): HandleResult {
        val pressedKeys =
            pointers
                .flatMap { pointer ->
                    allPoints
                        .minBy { it.distance(pointer.position) }
                        .keys
                }
                .toSet()

        val finalState =
            allKeys.fold(inputState) { updatedState, key ->
                updatedState.setDigitalKey(key, key in pressedKeys)
            }

        return HandleResult(finalState)
    }
}
