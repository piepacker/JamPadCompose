package gg.jam.jampadcompose

import android.app.Application
import gg.jam.jampadcompose.haptics.HapticGenerator

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        HapticGenerator.initialize(this)
    }
}
