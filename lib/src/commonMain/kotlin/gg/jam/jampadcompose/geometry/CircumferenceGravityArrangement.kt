package gg.jam.jampadcompose.geometry

import androidx.compose.ui.geometry.Offset
import gg.jam.jampadcompose.utils.Constants
import gg.jam.jampadcompose.utils.GeometryUtils
import gg.jam.jampadcompose.utils.GeometryUtils.toRadians
import kotlinx.collections.immutable.persistentSetOf
import kotlin.math.cos
import kotlin.math.sin

class CircumferenceGravityArrangement(
    private val ids: List<Int>,
    private val sockets: Int,
    private val rotationInDegrees: Float,
) : GravityArrangement() {
    override fun computeGravityPoints(): List<GravityPoint> {
        val baseRotation = rotationInDegrees.toRadians()

        val primaryGravityPoints =
            ids.mapIndexed { index, id ->
                val angle = (baseRotation + Constants.PI2 * index / sockets)
                GravityPoint(
                    Offset(cos(angle), sin(angle)),
                    1f,
                    persistentSetOf(id),
                )
            }

        return primaryGravityPoints
    }

    override fun computeSize(): Float {
        return GeometryUtils.computeSizeOfItemsOnCircumference(sockets)
    }
}
