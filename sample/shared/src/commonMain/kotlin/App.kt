import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import gg.jam.jampadcompose.GamePad
import gg.jam.jampadcompose.config.HapticFeedbackType
import gg.jam.jampadcompose.dials.AnalogDial
import gg.jam.jampadcompose.dials.ButtonDial
import gg.jam.jampadcompose.dials.CrossDial
import gg.jam.jampadcompose.dials.FaceButtonsDial

@Composable
fun App() {
    MaterialTheme {
        Column {
            Spacer(modifier = Modifier.weight(0.8f))
            GamePad(
                hapticFeedbackType = HapticFeedbackType.PRESS,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .weight(1f),
            ) {
                Row(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column(
                        modifier =
                            Modifier
                                .widthIn(0.dp, 200.dp)
                                .weight(1f, fill = false),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.Start,
                    ) {
                        Row(
                            modifier =
                                Modifier
                                    .height(54.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                        ) {
                            ButtonDial(modifier = Modifier.weight(1f), id = 4)
                            ButtonDial(modifier = Modifier.weight(1f), id = 5)
                            ButtonDial(modifier = Modifier.weight(1f), id = 6)
                        }
                        CrossDial(
                            modifier = Modifier.weight(1f),
                            id = 0,
                        )
                        AnalogDial(
                            modifier = Modifier.weight(1f),
                            id = 1,
                        )
                    }

                    Column(
                        modifier =
                            Modifier
                                .widthIn(0.dp, 200.dp)
                                .weight(1f, fill = false),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.End,
                    ) {
                        Row(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .height(54.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                        ) {
                            ButtonDial(modifier = Modifier.weight(1f), id = 7)
                            ButtonDial(modifier = Modifier.weight(1f), id = 8)
                            ButtonDial(modifier = Modifier.weight(1f), id = 9)
                        }
                        FaceButtonsDial(
                            modifier = Modifier.weight(1f),
                            ids = listOf(0, 1, 2, 3),
                        )
                        AnalogDial(
                            modifier = Modifier.weight(1f),
                            id = 2,
                        )
                    }
                }
            }
        }
    }
}
