/*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     https://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package net.ouftech.oufmime.ui.views.screens

import android.media.MediaPlayer
import android.os.CountDownTimer
import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.ouftech.oufmime.R
import net.ouftech.oufmime.data.Categories.ANIMALS
import net.ouftech.oufmime.data.Categories.CELEBRITIES
import net.ouftech.oufmime.data.Word
import net.ouftech.oufmime.ext.playSound
import net.ouftech.oufmime.ui.theme.*
import net.ouftech.oufmime.ui.views.library.FullScreenColumn
import net.ouftech.oufmime.ui.views.library.FullWidthRow
import net.ouftech.oufmime.ui.views.library.ScoreBoardView

@Composable
fun PlayScreen(
    foundWordsCount: Int,
    missedWordsCount: Int,
    wordsToPlayCount: Int,
    timerMaxValue: Long,
    currentWord: Word?,
    dimens: Dimens,
    invertColors: Boolean,
    onWordPlayed: (Boolean, Boolean) -> Unit,
    onFinishTurn: () -> Unit
) {
    // Add -1000L to forgo initial tick
    var currentTimerValue by remember { mutableStateOf(timerMaxValue + 1000L) }
    lateinit var timer: CountDownTimer
    val context = LocalContext.current
    var lastSecondsMP: MediaPlayer? = null

    DisposableEffect(key1 = null) {
        timer = object : CountDownTimer(timerMaxValue, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                Log.d("Timer", "Time: $millisUntilFinished")
                currentTimerValue -= 1000L

                if (currentTimerValue == 4000L) {
                    lastSecondsMP = context.playSound(R.raw.timer)
                }
            }

            override fun onFinish() {
                context.playSound(R.raw.times_up)
                currentTimerValue -= 1000L
                // Add last non played word in list in case player didn't have the time to mark as found
                onWordPlayed(false, true)
                onFinishTurn()
            }
        }

        timer.start()

        onDispose {
            try {
                lastSecondsMP?.stop()
            } catch (e: IllegalStateException) {
                Log.w("PlayScreen", "Error while stopping MediaPlayer: $e")
            }

            timer.cancel()
        }
    }

    FullScreenColumn {
        FullWidthRow {
            ScoreBoardView(
                topLabel = stringResource(id = R.string.found),
                topScore = foundWordsCount,
                middleLabel = stringResource(id = R.string.missed),
                middleScore = missedWordsCount,
                dimens = dimens,
                color = White,
                bottomLabel = stringResource(id = R.string.to_play),
                bottomScore = wordsToPlayCount
            )

            Timer(currentTimerValue, timerMaxValue, dimens, invertColors)
        }

        Box(
            modifier = Modifier
                .heightIn(min = 200.dp)
                .widthIn(max = 800.dp)
                .fillMaxWidth()
                .background(color = White, shape = RoundedCornerShape(8.dp))
                .padding(dimens.paddingLarge),
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                currentWord?.let {
                    Text(
                        text = currentWord.word,
                        color = Accent,
                        fontSize = dimens.titleText,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        modifier = Modifier.padding(top = 8.dp),
                        text = stringResource(id = currentWord.category.resId),
                        color = AccentTransparent,
                        fontSize = dimens.subtitleText,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        FullWidthRow {
            AnswerButton(
                color = Green,
                imageVector = Icons.Default.Check,
                contentDescription = stringResource(id = R.string.found),
                dimens,
                onCLick = {
                    onWordPlayed(true, false)
                    context.playSound(R.raw.word_ok)
                }
            )
            AnswerButton(
                color = Red,
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(id = R.string.missed),
                dimens,
                onCLick = {
                    onWordPlayed(false, false)
                    context.playSound(R.raw.word_wrong)
                }
            )
        }
    }
}

@Composable
private fun Timer(
    value: Long,
    maxValue: Long,
    dimens: Dimens,
    invertColors: Boolean
) {
    Box(
        modifier = Modifier.size(dimens.timerSize),
        contentAlignment = Alignment.Center
    ) {
        val color = if (invertColors) Primary else Accent

        val animatedProgress = animateFloatAsState(
            targetValue = value.toFloat() / maxValue,
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
        ).value

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimens.paddingXSmall)
                .border(shape = CircleShape, color = TransparentWhite, width = dimens.borderMedium),
            color = Transparent
        ) {}

        CircularProgressIndicator(
            modifier = Modifier.fillMaxSize(),
            progress = animatedProgress,
            color = color,
            strokeWidth = dimens.timerStrokeWidth
        )

        Text(
            text = (value / 1000).toString(),
            color = color,
            fontSize = dimens.bigTitleText
        )
    }
}

@Composable
private fun AnswerButton(
    color: Color,
    imageVector: ImageVector,
    contentDescription: String?,
    dimens: Dimens,
    onCLick: () -> Unit
) {
    Button(
        modifier = Modifier
            .size(dimens.iconMedium)
            .clip(CircleShape),
        colors = ButtonDefaults.buttonColors(backgroundColor = color),
        onClick = onCLick
    ) {
        Icon(
            modifier = Modifier.size(dimens.iconSmall),
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = White
        )
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFF6F00,
    name = "Play - Phone",
    device = Devices.PIXEL_4,
    locale = "fr"
)
@Composable
private fun PlayScreenPreviewPhone() {
    PlayScreen(
        foundWordsCount = 5,
        missedWordsCount = 2,
        wordsToPlayCount = 4,
        timerMaxValue = 40000L,
        currentWord = Word("Squid", CELEBRITIES, "en"),
        dimens = MediumDimens,
        invertColors = false,
        onWordPlayed = { _, _ -> },
        onFinishTurn = {}
    )
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFF6F00,
    name = "Play - Tablet",
    device = Devices.PIXEL_C
)
@Composable
private fun PlayScreenPreviewTablet() {
    PlayScreen(
        foundWordsCount = 5,
        missedWordsCount = 2,
        wordsToPlayCount = 4,
        timerMaxValue = 40000L,
        currentWord = Word("Squid", ANIMALS, "en"),
        dimens = ExpandedDimens,
        invertColors = false,
        onWordPlayed = { _, _ -> },
        onFinishTurn = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFF6F00, name = "Timer")
@Composable
private fun TimerPreview() {
    OufMimeTheme {
        Timer(20000L, 40000L, MediumDimens, false)
    }
}