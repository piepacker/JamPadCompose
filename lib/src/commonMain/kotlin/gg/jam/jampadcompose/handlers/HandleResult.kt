package gg.jam.jampadcompose.handlers

import gg.jam.jampadcompose.inputstate.InputState

data class HandleResult(val inputState: InputState, val gestureStartPointer: Pointer? = null)
