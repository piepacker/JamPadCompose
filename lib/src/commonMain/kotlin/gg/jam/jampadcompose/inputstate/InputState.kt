package gg.jam.jampadcompose.inputstate

import androidx.compose.ui.geometry.Offset
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.persistentSetOf

data class InputState(
    internal val digitalKeys: PersistentSet<Int> = persistentSetOf(),
    internal val analogKeys: PersistentMap<Int, Offset> = persistentMapOf(),
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

    fun setAnalogKey(
        analogId: Int,
        offset: Offset,
    ): InputState {
        return copy(analogKeys = analogKeys.put(analogId, offset))
    }

    fun getAnalogKey(analogId: Int): Offset {
        return analogKeys.getOrElse(analogId) { Offset.Zero }
    }
}
