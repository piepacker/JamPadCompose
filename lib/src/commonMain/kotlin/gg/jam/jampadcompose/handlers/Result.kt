package gg.jam.jampadcompose.handlers

import gg.jam.jampadcompose.inputstate.InputState

data class Result(val inputState: InputState, val gestureStartPointer: Pointer? = null)
