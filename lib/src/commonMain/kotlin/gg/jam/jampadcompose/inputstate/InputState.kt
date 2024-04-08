package gg.jam.jampadcompose.inputstate

import androidx.compose.ui.geometry.Offset
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.persistentSetOf

data class InputState(
    internal val digitalKeys: PersistentSet<Int> = persistentSetOf(),
    internal val continuousDirections: PersistentMap<Int, Offset> = persistentMapOf(),
    internal val discreteDirections: PersistentMap<Int, Offset> = persistentMapOf(),
) {
    fun setDigitalKey(
        digitalId: Int,
        value: Boolean,
    ): InputState {
        return if (value) {
            copy(digitalKeys = digitalKeys.add(digitalId))
        } else {
            copy(digitalKeys = digitalKeys.remove(digitalId))
        }
    }

    fun getDigitalKey(digitalId: Int): Boolean {
        return digitalKeys.contains(digitalId)
    }

    fun setContinuousDirection(
        continuousDirectionId: Int,
        offset: Offset,
    ): InputState {
        return if (offset == Offset.Unspecified) {
            copy(continuousDirections = continuousDirections.remove(continuousDirectionId))
        } else {
            copy(continuousDirections = continuousDirections.put(continuousDirectionId, offset))
        }
    }

    fun getContinuousDirection(
        continuousDirectionId: Int,
        default: Offset = Offset.Unspecified,
    ): Offset {
        return continuousDirections.getOrElse(continuousDirectionId) { default }
    }

    fun setDiscreteDirection(
        discreteDirectionId: Int,
        offset: Offset,
    ): InputState {
        return if (offset == Offset.Unspecified) {
            copy(discreteDirections = discreteDirections.remove(discreteDirectionId))
        } else {
            copy(discreteDirections = discreteDirections.put(discreteDirectionId, offset))
        }
    }

    fun getDiscreteDirection(
        discreteDirectionId: Int,
        default: Offset = Offset.Unspecified,
    ): Offset {
        return discreteDirections.getOrElse(discreteDirectionId) { default }
    }
}
