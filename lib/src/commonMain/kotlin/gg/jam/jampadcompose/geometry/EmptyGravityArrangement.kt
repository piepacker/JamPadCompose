package gg.jam.jampadcompose.geometry

object EmptyGravityArrangement : GravityArrangement() {
    override fun computeGravityPoints(): List<GravityPoint> {
        return emptyList()
    }

    override fun computeSize(): Float {
        return 0f
    }
}
