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

import android.os.CountDownTimer
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import net.ouftech.oufmime.R
import net.ouftech.oufmime.data.Categories.ANIMALS
import net.ouftech.oufmime.data.Word
import net.ouftech.oufmime.ui.model.Team
import net.ouftech.oufmime.ui.theme.ScreenConfiguration
import net.ouftech.oufmime.ui.theme.ScreenConfiguration.Companion.COMPACT_LANDSCAPE
import net.ouftech.oufmime.ui.theme.ScreenConfiguration.Companion.COMPACT_PORTRAIT
import net.ouftech.oufmime.ui.theme.ScreenConfiguration.Companion.EXPANDED_LANDSCAPE
import net.ouftech.oufmime.ui.theme.ScreenConfiguration.Companion.EXPANDED_PORTRAIT
import net.ouftech.oufmime.ui.theme.ScreenConfiguration.Companion.MEDIUM
import net.ouftech.oufmime.ui.theme.ScreenConfiguredTheme
import net.ouftech.oufmime.ui.theme.getDimens
import net.ouftech.oufmime.ui.theme.isCompactLandscape
import net.ouftech.oufmime.ui.views.library.Backgrounded
import net.ouftech.oufmime.ui.views.library.CompactLandscapePreview
import net.ouftech.oufmime.ui.views.library.CompactPortraitPreview
import net.ouftech.oufmime.ui.views.library.ExpandedLandscapePreview
import net.ouftech.oufmime.ui.views.library.ExpandedPortraitPreview
import net.ouftech.oufmime.ui.views.library.FullScreenColumn
import net.ouftech.oufmime.ui.views.library.FullScreenRow
import net.ouftech.oufmime.ui.views.library.FullWidthRow
import net.ouftech.oufmime.ui.views.library.MediumPreview
import net.ouftech.oufmime.utils.createMediaPlayer

// region Views

@Composable
fun PlayScreen(
    uiState: PlayScreenUiState,
    onWordPlayed: (Boolean, Boolean) -> Unit,
    onFinishTurn: () -> Unit
) = Backgrounded {
    with(uiState) {
        var timerCurrentValue by remember { mutableStateOf(timerMaxValue) }

        TimerDisposableEffect(uiState, onWordPlayed, onFinishTurn) { timerCurrentValue -= 1000L }

        if (isCompactLandscape()) {
            CompactPortraitView(uiState, timerCurrentValue, onWordPlayed)
        } else {
            NormalView(uiState, timerCurrentValue, onWordPlayed)
        }
    }
}

@Composable
private fun NormalView(
    uiState: PlayScreenUiState,
    timerCurrentValue: Long,
    onWordPlayed: (Boolean, Boolean) -> Unit
) = with(uiState) {
    FullScreenColumn {
        Box(
            modifier = Modifier
                .widthIn(max = getDimens().wordsCardMaxWidthOuterBounds)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            WordBox(uiState.currentWord)

            CardDeck(
                modifier = Modifier.align(Alignment.TopStart),
                count = wordsToPlayCount,
            )

            Timer(
                modifier = Modifier.align(Alignment.TopEnd),
                value = timerCurrentValue,
                maxValue = timerMaxValue,
            )
        }

        FullWidthRow {
            ButtonPass(onWordPlayed)
            ButtonFound(onWordPlayed)
        }
    }
}

@Composable
private fun CompactPortraitView(
    uiState: PlayScreenUiState,
    timerCurrentValue: Long,
    onWordPlayed: (Boolean, Boolean) -> Unit
) = with(uiState) {
    FullScreenRow {
        ButtonPass(onWordPlayed)

        Box(
            modifier = Modifier
                .widthIn(max = getDimens().wordsCardMaxWidthOuterBounds)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            WordBox(uiState.currentWord)

            CardDeck(
                modifier = Modifier.align(Alignment.TopStart),
                count = wordsToPlayCount,
            )

            Timer(
                modifier = Modifier.align(Alignment.TopEnd),
                value = timerCurrentValue,
                maxValue = timerMaxValue,
            )
        }

        ButtonFound(onWordPlayed)
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

// endregion Views

// region Previews

@CompactPortraitPreview
@Composable
private fun CompactPortraitPreviewPlayScreen() = PlayScreenPreview(COMPACT_PORTRAIT, Team.Orange)

@CompactLandscapePreview
@Composable
private fun CompactLandscapePreviewPlayScreen() = PlayScreenPreview(COMPACT_LANDSCAPE, Team.Blue)

@MediumPreview
@Composable
private fun MediumPreviewPlayScreen() = PlayScreenPreview(MEDIUM, Team.Orange)

@ExpandedPortraitPreview
@Composable
private fun ExpandedPortraitPreviewPlayScreen() = PlayScreenPreview(EXPANDED_PORTRAIT, Team.Orange)

@ExpandedLandscapePreview
@Composable
private fun ExpandedLandscapePreviewPlayScreen() = PlayScreenPreview(EXPANDED_LANDSCAPE, Team.Blue)

@Composable
private fun PlayScreenPreview(screenConfiguration: ScreenConfiguration, team: Team) =
    ScreenConfiguredTheme(screenConfiguration, team.colors) { PlayScreen(stubPlayUiState, { _, _ -> }, {}) }

// endregion Previews

data class PlayScreenUiState(
    val wordsToPlayCount: Int,
    val timerMaxValue: Long,
    val currentWord: Word?,
    val invertColors: Boolean = false,
)

private val stubPlayUiState
    get() = PlayScreenUiState(
        wordsToPlayCount = 4,
        timerMaxValue = 40000L,
        currentWord = Word("Squid", ANIMALS, "en"),
    )