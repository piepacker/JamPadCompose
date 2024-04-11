package gg.jam.jampadcompose.arrangements

import androidx.compose.ui.geometry.Offset
import kotlinx.collections.immutable.persistentSetOf

class CircleArrangement(
    private val ids: List<Int>,
    private val sockets: Int,
    rotationInDegrees: Float,
) : GravityArrangement() {
    private val circumferenceGravityArrangement =
        CircumferenceGravityArrangement(
            ids.drop(1),
            sockets - 1,
            rotationInDegrees,
        )

    override fun computeGravityPoints(): List<GravityPoint> {
        val centralGravityPoint =
            GravityPoint(
                Offset.Zero,
                1f,
                persistentSetOf(ids.first()),
            )

        val circumferenceGravityPoint = circumferenceGravityArrangement.getGravityPoints()
        return listOf(centralGravityPoint) + circumferenceGravityPoint
    }

    override fun computeSize(): Float {
        if (sockets == 1) {
            return 0.5f
        }
        return minOf(circumferenceGravityArrangement.getSize(), 0.33f)
    }
}
