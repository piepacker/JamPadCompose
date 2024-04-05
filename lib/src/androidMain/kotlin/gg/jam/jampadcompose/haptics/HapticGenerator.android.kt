package gg.jam.jampadcompose.haptics

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

class AndroidHapticGenerator(applicationContext: Context) : HapticGenerator {
    private val vibrator = buildVibrator(applicationContext)
    private val strongEffect = buildStrongVibrationEffect()
    private val weakEffect = buildWeakVibrationEffect()

    override fun generate(type: HapticEffect) {
        val effect =
            when (type) {
                HapticEffect.PRESS -> strongEffect
                HapticEffect.RELEASE -> weakEffect
            }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && effect != null) {
            vibrator.vibrate(effect)
        }
    }

    private fun buildVibrator(applicationContext: Context): Vibrator {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            applicationContext.getSystemService(VibratorManager::class.java).defaultVibrator
        } else {
            applicationContext.getSystemService(Vibrator::class.java)
        }
    }

    private fun buildStrongVibrationEffect(): VibrationEffect? {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return null
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            return VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE)
        }

        return VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK)
    }

    private fun buildWeakVibrationEffect(): VibrationEffect? {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return null
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            return VibrationEffect.createOneShot(15, VibrationEffect.DEFAULT_AMPLITUDE)
        }

        return VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK)
    }
}

@Composable
actual fun rememberHapticGenerator(): HapticGenerator {
    val applicationContext = LocalContext.current.applicationContext
    return remember {
        AndroidHapticGenerator(applicationContext)
    }
}
