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
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import net.ouftech.oufmime.R
import net.ouftech.oufmime.data.Categories.ANIMALS
import net.ouftech.oufmime.data.Word
import net.ouftech.oufmime.ext.playSound
import net.ouftech.oufmime.ui.theme.*
import net.ouftech.oufmime.ui.views.library.FullScreenColumn
import net.ouftech.oufmime.ui.views.library.FullWidthRow
import net.ouftech.oufmime.ui.views.library.ScoreBoardView

@Composable
fun PlayScreen(
    uiModel: PlayScreenUiModel,
    dimens: Dimens,
    invertColors: Boolean,
    onWordPlayed: (Boolean, Boolean) -> Unit,
    onFinishTurn: () -> Unit
) {
    // Add -1000L to forgo initial tick
    var currentTimerValue by remember { mutableStateOf(uiModel.timerMaxValue + 1000L) }

    TimerDisposableEffect(uiModel.timerMaxValue, currentTimerValue, onWordPlayed, onFinishTurn) { currentTimerValue -= 1000L }

    FullScreenColumn {

        HeaderRow(currentTimerValue, uiModel, dimens, invertColors)

        WordBox(uiModel.currentWord, dimens)

        ButtonsRow(dimens, onWordPlayed)

    }
}

@Composable
private fun TimerDisposableEffect(
    timerMaxValue: Long,
    currentTimerValue: Long,
    onWordPlayed: (Boolean, Boolean) -> Unit,
    onFinishTurn: () -> Unit,
    onTick: () -> Unit,
) {
    val context = LocalContext.current
    var lastSecondsMP: MediaPlayer? = null
    lateinit var timer: CountDownTimer

    DisposableEffect(key1 = null) {
        timer = object : CountDownTimer(timerMaxValue, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                onTick()

                if (currentTimerValue == 4000L) {
                    lastSecondsMP = context.playSound(R.raw.timer)
                }
            }

            override fun onFinish() {
                context.playSound(R.raw.times_up)
                onTick()
                // Add last non played word in list in case player didn't have the time to mark as found
                onWordPlayed(false, true)
                onFinishTurn()
            }
        }

        timer.start()

        onDispose {
            try {
                lastSecondsMP?.stop()
            } catch (_: IllegalStateException) {
                // Do nothing
            }

            timer.cancel()
        }
    }
}

@Composable
private fun HeaderRow(currentTimerValue: Long, uiModel: PlayScreenUiModel, dimens: Dimens, invertColors: Boolean) = FullWidthRow {
    ScoreBoardView(
        topLabel = stringResource(id = R.string.found),
        topScore = uiModel.foundWordsCount,
        middleLabel = stringResource(id = R.string.missed),
        middleScore = uiModel.missedWordsCount,
        dimens = dimens,
        color = White,
        bottomLabel = stringResource(id = R.string.to_play),
        bottomScore = uiModel.wordsToPlayCount
    )

    Timer(
        value = currentTimerValue,
        maxValue = uiModel.timerMaxValue,
        dimens = dimens,
        invertColors = invertColors
    )
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
private fun WordBox(currentWord: Word?, dimens: Dimens) {
    val boxShape = RoundedCornerShape(8.dp)

    Box(
        modifier = Modifier
            .heightIn(min = 200.dp)
            .widthIn(max = 800.dp)
            .graphicsLayer {
                shadowElevation = 8.dp.toPx()
                shape = boxShape
                clip = true
            }
            .fillMaxWidth()
            .background(color = White, shape = boxShape)
            .animateContentSize()
            .padding(dimens.paddingLarge),
        contentAlignment = Alignment.Center,
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
}

@Composable
private fun ButtonsRow(
    dimens: Dimens,
    onWordPlayed: (Boolean, Boolean) -> Unit
) = FullWidthRow {
    val context = LocalContext.current

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

@Composable
private fun AnswerButton(
    color: Color,
    imageVector: ImageVector,
    contentDescription: String?,
    dimens: Dimens,
    onCLick: () -> Unit
) {
    val interactionSource = MutableInteractionSource()
    val coroutineScope = rememberCoroutineScope()
    val scale = remember { Animatable(1f) }

    Box(
        modifier = Modifier
            .size(dimens.iconMedium)
            .scale(scale = scale.value)
            .background(
                color = color,
                shape = RoundedCornerShape(20)
            )
            .clickable(interactionSource = interactionSource, indication = null) {
                coroutineScope.launch {
                    scale.animateTo(
                        0.9f,
                        animationSpec = tween(100),
                    )
                    scale.animateTo(
                        1f,
                        animationSpec = tween(100),
                    )
                }
                onCLick()
            },
        contentAlignment = Alignment.Center
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
        uiModel = stubPlayUiModel,
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
        uiModel = stubPlayUiModel,
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

data class PlayScreenUiModel(
    val foundWordsCount: Int,
    val missedWordsCount: Int,
    val wordsToPlayCount: Int,
    val timerMaxValue: Long,
    val currentWord: Word?,
)

private val stubPlayUiModel
    get() = PlayScreenUiModel(
        foundWordsCount = 5,
        missedWordsCount = 2,
        wordsToPlayCount = 4,
        timerMaxValue = 40000L,
        currentWord = Word("Squid", ANIMALS, "en"),
    )