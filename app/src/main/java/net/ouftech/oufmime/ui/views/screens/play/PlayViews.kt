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

package net.ouftech.oufmime.ui.views.screens.play

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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import net.ouftech.oufmime.R
import net.ouftech.oufmime.data.Categories
import net.ouftech.oufmime.data.Word
import net.ouftech.oufmime.ui.theme.Accent
import net.ouftech.oufmime.ui.theme.AccentTransparent
import net.ouftech.oufmime.ui.theme.Dimens
import net.ouftech.oufmime.ui.theme.Green
import net.ouftech.oufmime.ui.theme.NoTeamColors
import net.ouftech.oufmime.ui.theme.Red
import net.ouftech.oufmime.ui.theme.getDimens
import net.ouftech.oufmime.ui.views.library.PreviewView
import net.ouftech.oufmime.utils.circleShadowedBackground
import net.ouftech.oufmime.utils.playSound
import net.ouftech.oufmime.utils.roundedRectShadowedBackground

private const val WIDGET_ROTATION = 20f

// region Views

@Composable
internal fun WordBox(
    currentWord: Word?,
) = Column(
    modifier = Modifier
        .padding(getDimens().playWidgetsSize / 3)
        .heightIn(min = 400.dp)
        .widthIn(max = getDimens().wordsCardMaxWidth)
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
            fontSize = getDimens().wordCardWordText,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(id = currentWord.category.resId),
            color = AccentTransparent,
            fontSize = getDimens().wordCardCategoryText,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CardDeck(
    modifier: Modifier = Modifier,
    count: Int,
    reversed: Boolean = false,
    color: Color = MaterialTheme.colorScheme.tertiary
) {
    val dimens = getDimens()

    Box(
        modifier = modifier
            .size(dimens.playWidgetsSize)
            .circleShadowedBackground(backgroundColor = color)
            .rotate(if (reversed) WIDGET_ROTATION else -WIDGET_ROTATION)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimens.paddingMedium)
        ) {
            drawCardWithShadow(0, color, reversed, dimens)
            drawCardWithShadow(1, color, reversed, dimens)
            drawCardWithShadow(2, color, reversed, dimens)
        }

        val textPaddingTop = dimens.paddingMedium + dimens.cardPadding * 5
        val textPaddingLeft = dimens.paddingMedium + if (reversed) 0.dp else dimens.cardPadding * 5

        Box(
            modifier = Modifier
                .padding(start = textPaddingLeft, top = textPaddingTop)
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

internal fun DrawScope.drawCardWithShadow(cardNb: Int, color: Color, reversed: Boolean, dimens: Dimens) {
    val shadowTopOffset = dimens.cardPadding * cardNb * 2
    val shadowLeftOffset = if (reversed) {
        dimens.cardPadding * (3 - cardNb) * 2
    } else {
        shadowTopOffset
    }

    val cardTopOffset = shadowTopOffset + dimens.cardPadding
    val cardLeftOffset = shadowLeftOffset + if (reversed) -dimens.cardPadding else dimens.cardPadding

    drawCard(
        color = color,
        offset = Offset(shadowLeftOffset.toPx(), shadowTopOffset.toPx()),
        dimens = dimens
    )
    drawCard(
        color = Color.White,
        offset = Offset(cardLeftOffset.toPx(), cardTopOffset.toPx()),
        dimens = dimens
    )
}

internal fun DrawScope.drawCard(color: Color, offset: Offset, dimens: Dimens) {
    drawRoundRect(
        color = color,
        topLeft = offset,
        size = Size(dimens.cardWidth.toPx(), dimens.cardHeight.toPx()),
        cornerRadius = CornerRadius(dimens.cardCornerRadius.toPx())
    )
}

@Composable
internal fun Timer(
    modifier: Modifier = Modifier,
    value: Long,
    maxValue: Long,
) {
    Box(
        modifier = modifier
            .size(getDimens().playWidgetsSize)
            .rotate(WIDGET_ROTATION),
        contentAlignment = Alignment.Center
    ) {
        val colorTransparent = MaterialTheme.colorScheme.onSurfaceVariant
        val color = MaterialTheme.colorScheme.surfaceVariant

        val animatedProgress = animateFloatAsState(
            targetValue = value.toFloat() / maxValue * 360,
            animationSpec = tween(durationMillis = 1000, easing = LinearEasing), label = ""
        ).value

        Canvas(
            modifier = Modifier
                .size(size = getDimens().playWidgetsSize)
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
            color = Color.White,
            fontSize = getDimens().bigTitleText
        )
    }
}

@Composable
fun ButtonFound(onWordPlayed: (Boolean, Boolean) -> Unit) = AnswerButton(
    sound = R.raw.word_wrong,
    color = Red,
    iconRes = R.drawable.ic_cross,
    contentDescription = stringResource(id = R.string.missed),
    onCLick = { onWordPlayed(false, false) }
)

@Composable
fun ButtonPass(onWordPlayed: (Boolean, Boolean) -> Unit) = AnswerButton(
    sound = R.raw.word_ok,
    color = Green,
    iconRes = R.drawable.ic_check,
    contentDescription = stringResource(id = R.string.found),
    onCLick = { onWordPlayed(true, false) }
)

@Composable
private fun AnswerButton(
    @RawRes sound: Int,
    color: Color,
    @DrawableRes iconRes: Int,
    contentDescription: String?,
    onCLick: () -> Unit
) {
    val interactionSource = MutableInteractionSource()
    val coroutineScope = rememberCoroutineScope()
    val scale = remember { Animatable(1f) }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .size(getDimens().bigIconButton)
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
                .padding(getDimens().paddingLarge)
                .fillMaxSize(),
            painter = painterResource(id = iconRes),
            contentDescription = contentDescription,
            tint = Color.White
        )
    }
}

// endregion Views

// region Previews

@Preview(widthDp = 1000, heightDp = 1500)
@Composable
private fun WordBoxPreview() = PreviewView(colors = NoTeamColors) { WordBox(Word("Batman", Categories.FICTIONAL, "en")) }

@Preview(widthDp = 450, heightDp = 450)
@Composable
private fun TimerPreview() = PreviewView { Timer(value = 32000L, maxValue = 40000L) }

@Preview(widthDp = 450, heightDp = 450)
@Composable
private fun CardDeckPreview() = PreviewView { CardDeck(count = 32) }

@Preview(widthDp = 450, heightDp = 450)
@Composable
private fun CardDeckReversedPreview() = PreviewView { CardDeck(count = 32, reversed = true) }

@Preview(widthDp = 350, heightDp = 550)
@Composable
private fun AnswerButtonFoundPreview() = PreviewView(NoTeamColors) { ButtonFound { _, _ -> } }

@Preview(widthDp = 350, heightDp = 550)
@Composable
private fun AnswerButtonPassPreview() = PreviewView(NoTeamColors) { ButtonPass { _, _ -> } }

// endregion Previews