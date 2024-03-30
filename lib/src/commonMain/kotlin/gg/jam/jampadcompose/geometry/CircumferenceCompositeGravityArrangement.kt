package gg.jam.jampadcompose.geometry

import androidx.compose.ui.geometry.Offset
import gg.jam.jampadcompose.utils.Constants
import gg.jam.jampadcompose.utils.GeometryUtils.toRadians
import kotlinx.collections.immutable.persistentSetOf
import kotlin.math.cos
import kotlin.math.sin

class CircumferenceCompositeGravityArrangement(
    private val ids: List<Int>,
    private val rotationInDegrees: Float,
) : GravityArrangement() {
    override fun computeGravityPoints(): List<GravityPoint> {
        val baseRotation = rotationInDegrees.toRadians()

        val compositeGravityPoints =
            (ids + ids.take(1))
                .zipWithNext()
                .mapIndexed { index, (prev, next) ->
                    val radius = 0.9f
                    val angle = (baseRotation + Constants.PI2 * (index + 0.5f) / ids.size)
                    GravityPoint(
                        Offset(cos(angle), sin(angle)) * radius,
                        0.25f,
                        persistentSetOf(prev, next),
                    )
                }

        return compositeGravityPoints
    }

    override fun computeSize(): Float {
        return 0.1f
    }
}
