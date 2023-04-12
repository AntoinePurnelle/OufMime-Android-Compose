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

@file:Suppress("TooManyFunctions")

package net.ouftech.oufmime.ui.views.screens

import android.os.CountDownTimer
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import net.ouftech.oufmime.R
import net.ouftech.oufmime.data.Categories.ANIMALS
import net.ouftech.oufmime.data.Word
import net.ouftech.oufmime.ui.theme.Accent
import net.ouftech.oufmime.ui.theme.AccentTransparent
import net.ouftech.oufmime.ui.theme.Blue
import net.ouftech.oufmime.ui.theme.BlueLight
import net.ouftech.oufmime.ui.theme.Dimens
import net.ouftech.oufmime.ui.theme.ExpandedDimens
import net.ouftech.oufmime.ui.theme.Green
import net.ouftech.oufmime.ui.theme.MediumDimens
import net.ouftech.oufmime.ui.theme.Orange
import net.ouftech.oufmime.ui.theme.OrangeDark
import net.ouftech.oufmime.ui.theme.OufMimeTheme
import net.ouftech.oufmime.ui.theme.Red
import net.ouftech.oufmime.ui.views.library.FullScreenColumn
import net.ouftech.oufmime.ui.views.library.FullWidthRow
import net.ouftech.oufmime.utils.circleShadowedBackground
import net.ouftech.oufmime.utils.createMediaPlayer
import net.ouftech.oufmime.utils.playSound
import net.ouftech.oufmime.utils.roundedRectShadowedBackground

@Composable
fun PlayScreen(
    uiState: PlayScreenUiState,
    onWordPlayed: (Boolean, Boolean) -> Unit,
    onFinishTurn: () -> Unit
) = with(uiState) {
    var timerCurrentValue by remember { mutableStateOf(uiState.timerMaxValue) }

    TimerDisposableEffect(uiState, onWordPlayed, onFinishTurn) { timerCurrentValue -= 1000L }

    FullScreenColumn {
        Box(
            modifier = Modifier
                .widthIn(max = 900.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            WordBox(uiState)

            CardDeck(
                modifier = Modifier.align(Alignment.TopStart),
                count = wordsToPlayCount,
                dimens = dimens,
            )

            Timer(
                modifier = Modifier.align(Alignment.TopEnd),
                value = timerCurrentValue,
                maxValue = timerMaxValue,
                dimens = dimens,
                invertColors = invertColors
            )
        }

        ButtonsRow(dimens, onWordPlayed)
    }
}

@Composable
private fun TimerDisposableEffect(
    uiState: PlayScreenUiState,
    onWordPlayed: (Boolean, Boolean) -> Unit,
    onFinishTurn: () -> Unit,
    onTick: () -> Unit,
) {
    val context = LocalContext.current
    val lastSecondsMP by remember { mutableStateOf(context.createMediaPlayer(R.raw.timer)) }
    val timesUpMP by remember { mutableStateOf(context.createMediaPlayer(R.raw.times_up)) }
    lateinit var timer: CountDownTimer

    DisposableEffect(key1 = null) {
        timer = object : CountDownTimer(uiState.timerMaxValue, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                onTick()

                if (millisUntilFinished < 4500L) {
                    lastSecondsMP?.start()
                }
            }

            override fun onFinish() {
                timesUpMP?.start()
                onTick()
                // Add last non played word in list in case player didn't have the time to mark as found
                onWordPlayed(false, true)
                onFinishTurn()
            }
        }

        timer.start()

        onDispose {
            try {
                if (lastSecondsMP?.isPlaying == true) {
                    lastSecondsMP?.stop()
                }
            } catch (_: IllegalStateException) {
                // Do nothing
            }

            timer.cancel()
        }
    }
}

@Composable
private fun WordBox(
    uiState: PlayScreenUiState,
) = with(uiState) {
    Column(
        modifier = Modifier
            .padding(dimens.playWidgetsSize / 3)
            .heightIn(min = 400.dp)
            .widthIn(max = 800.dp)
            .fillMaxWidth()
            .roundedRectShadowedBackground()
            .animateContentSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        currentWord?.let {
            Text(
                text = currentWord.word,
                color = Accent,
                fontSize = dimens.wordCardWordText,
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(id = currentWord.category.resId),
                color = AccentTransparent,
                fontSize = dimens.subtitleText,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun CardDeck(
    modifier: Modifier = Modifier,
    count: Int,
    dimens: Dimens
) {
    val color = MaterialTheme.colors.secondary

    Box(
        modifier = modifier
            .size(dimens.playWidgetsSize)
            .circleShadowedBackground(backgroundColor = color)
            .rotate(-dimens.widgetRotation)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimens.paddingMedium)
        ) {
            drawCardWithShadow(0, color, dimens)
            drawCardWithShadow(1, color, dimens)
            drawCardWithShadow(2, color, dimens)
        }

        val textPadding = dimens.cardPadding * 5 + dimens.paddingMedium

        Box(
            modifier = Modifier
                .padding(start = textPadding, top = textPadding)
                .size(dimens.cardWidth, dimens.cardHeight)
        ) {
            Text(
                text = count.toString(),
                modifier = Modifier.align(Alignment.Center),
                color = color,
                fontSize = dimens.cardDeckText,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
        }
    }
}

private fun DrawScope.drawCardWithShadow(cardNb: Int, color: Color, dimens: Dimens) {
    drawCard(
        color = color,
        topLeftOffset = dimens.cardPadding * cardNb * 2,
        dimens = dimens
    )
    drawCard(
        color = White,
        topLeftOffset = dimens.cardPadding * cardNb * 2 + dimens.cardPadding,
        dimens = dimens
    )
}

private fun DrawScope.drawCard(color: Color, topLeftOffset: Dp, dimens: Dimens) {
    drawRoundRect(
        color = color,
        topLeft = Offset(topLeftOffset.toPx(), topLeftOffset.toPx()),
        size = Size(dimens.cardWidth.toPx(), dimens.cardHeight.toPx()),
        cornerRadius = CornerRadius(dimens.cardCornerRadius.toPx())
    )
}

@Composable
private fun Timer(
    modifier: Modifier = Modifier,
    value: Long,
    maxValue: Long,
    dimens: Dimens,
    invertColors: Boolean
) {
    Box(
        modifier = modifier
            .size(dimens.playWidgetsSize)
            .rotate(dimens.widgetRotation),
        contentAlignment = Alignment.Center
    ) {
        val colorTransparent = if (invertColors) OrangeDark else BlueLight
        val color = if (invertColors) Orange else Blue

        val animatedProgress = animateFloatAsState(
            targetValue = value.toFloat() / maxValue * 360,
            animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
        ).value

        Canvas(
            modifier = Modifier
                .size(size = dimens.playWidgetsSize)
                .circleShadowedBackground()
        ) {
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = animatedProgress,
                useCenter = true
            )

            drawArc(
                color = colorTransparent,
                startAngle = -90f + animatedProgress,
                sweepAngle = 360 - animatedProgress,
                useCenter = true
            )
        }
        Text(
            text = (value / 1000 + 1).toString(),
            color = White,
            fontSize = dimens.bigTitleText
        )
    }
}

@Composable
private fun ButtonsRow(
    dimens: Dimens,
    onWordPlayed: (Boolean, Boolean) -> Unit
) = FullWidthRow {
    AnswerButton(
        sound = R.raw.word_wrong,
        color = Red,
        iconRes = R.drawable.ic_cross,
        contentDescription = stringResource(id = R.string.missed),
        dimens = dimens,
        onCLick = { onWordPlayed(false, false) }
    )

    AnswerButton(
        sound = R.raw.word_ok,
        color = Green,
        iconRes = R.drawable.ic_check,
        contentDescription = stringResource(id = R.string.found),
        dimens = dimens,
        onCLick = { onWordPlayed(true, false) }
    )
}

@Composable
private fun AnswerButton(
    @RawRes sound: Int,
    color: Color,
    @DrawableRes iconRes: Int,
    contentDescription: String?,
    dimens: Dimens,
    onCLick: () -> Unit
) {
    val interactionSource = MutableInteractionSource()
    val coroutineScope = rememberCoroutineScope()
    val scale = remember { Animatable(1f) }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .size(dimens.bigIconButton)
            .scale(scale = scale.value)
            .roundedRectShadowedBackground(backgroundColor = color)
            .clickable(interactionSource = interactionSource, indication = null) {
                onCLick()
                context.playSound(sound)
                coroutineScope.launch {
                    scale.animateTo(
                        targetValue = 0.9f,
                        animationSpec = tween(100),
                    )
                    scale.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(100),
                    )
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier
                .padding(dimens.paddingLarge)
                .fillMaxSize(),
            painter = painterResource(id = iconRes),
            contentDescription = contentDescription,
            tint = White
        )
    }
}

data class PlayScreenUiState(
    val dimens: Dimens,
    val wordsToPlayCount: Int,
    val timerMaxValue: Long,
    val currentWord: Word?,
    val invertColors: Boolean = false,
)

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFF6F00,
    name = "Play - Phone",
    device = Devices.PIXEL_4,
    locale = "fr"
)
@Composable
private fun PlayScreenPreviewPhone() {
    OufMimeTheme {
        PlayScreen(
            uiState = getStubPlayUiState(MediumDimens),
            onWordPlayed = { _, _ -> },
            onFinishTurn = {}
        )
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFF6F00,
    name = "Play - Tablet",
    device = Devices.PIXEL_C
)
@Composable
private fun PlayScreenPreviewTablet() {
    OufMimeTheme {
        PlayScreen(
            uiState = getStubPlayUiState(ExpandedDimens),
            onWordPlayed = { _, _ -> },
            onFinishTurn = {}
        )
    }
}

private fun getStubPlayUiState(dimens: Dimens = MediumDimens) = PlayScreenUiState(
    dimens = dimens,
    wordsToPlayCount = 4,
    timerMaxValue = 40000L,
    currentWord = Word("Squid", ANIMALS, "en"),
)

@Preview(showBackground = true, backgroundColor = 0xFFFFFF, name = "Timer")
@Composable
private fun TimerPreview() {
    OufMimeTheme {
        Timer(value = 32000L, maxValue = 40000L, dimens = MediumDimens, invertColors = false)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
private fun CardDeckPreviewMedium() {
    OufMimeTheme {
        CardDeck(count = 32, dimens = MediumDimens)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
private fun CardDeckPreviewExpanded() {
    OufMimeTheme {
        CardDeck(count = 32, dimens = ExpandedDimens)
    }
}