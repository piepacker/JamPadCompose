package gg.jam.jampadcompose.haptics

import android.app.Application
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

actual object HapticGenerator {

    private lateinit var vibrator: Vibrator

    private var weakEffect: VibrationEffect? = null
    private var strongEffect: VibrationEffect? = null

    fun initialize(application: Application) {
        vibrator = buildVibrator(application)
        strongEffect = buildStrongVibrationEffect()
        weakEffect = buildWeakVibrationEffect()
    }

    actual fun generate(type: HapticEffect) {
        val effect = when (type) {
            HapticEffect.PRESS -> strongEffect
            HapticEffect.RELEASE -> weakEffect
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && effect != null) {
            vibrator.vibrate(effect)
        }
    }

    private fun buildVibrator(application: Application): Vibrator {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            application.getSystemService(VibratorManager::class.java).defaultVibrator
        } else {
            application.getSystemService(Vibrator::class.java)
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
