package gg.jam.jampadcompose.arrangements

import gg.jam.jampadcompose.utils.memoize

abstract class GravityArrangement {
    fun getGravityPoints(): List<GravityPoint> {
        return ::computeGravityPoints.memoize().invoke()
    }

    fun getSize(): Float {
        return ::computeSize.memoize().invoke()
    }

    protected abstract fun computeGravityPoints(): List<GravityPoint>

    protected abstract fun computeSize(): Float
}
