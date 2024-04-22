# JamPadCompose

JamPadCompose is a Compose Multiplatform library that allows developers to create custom virtual gamepads for games and applications.

### Controls

The following controls are currently supported:
* ControlAnalog
* ControlButton
* ControlCross
* ControlFaceButtons

### Usage

Here's a how you can use JamPadCompose to create a very simple gamepad layout.

```kotlin
@Composable
private fun MyGamePad() {
   JamPad(
      modifier = Modifier.fillMaxSize().aspectRatio(2f),
      onInputStateUpdated = { }
   ) {
      Row(modifier = Modifier.fillMaxSize()) {
         ControlCross(
            modifier = Modifier.weight(1f),
            id = 0
         )
         ControlFaceButtons(
            modifier = Modifier.weight(1f),
            ids = listOf(1, 2, 3)
         )
      }
   }
}
```
