package gg.jam.jampadcompose.geometry

import androidx.compose.ui.geometry.Offset
import kotlinx.collections.immutable.persistentSetOf

class CircleGravityArrangement(
    private val ids: List<Int>,
    rotationInDegrees: Float
) : GravityArrangement() {

    private val circumferenceGravityArrangement = CircumferenceGravityArrangement(
        ids.drop(1),
        rotationInDegrees
    )

    override fun computeGravityPoints(): List<GravityPoint> {
        val centralGravityPoint = GravityPoint(
            Offset.Zero,
            1f,
            persistentSetOf(ids.first())
        )

        val circumferenceGravityPoint = circumferenceGravityArrangement.getGravityPoints()
        return listOf(centralGravityPoint) + circumferenceGravityPoint
    }

    override fun computeSize(): Float {
        return minOf(circumferenceGravityArrangement.getSize(), 0.33f)
    }
}
