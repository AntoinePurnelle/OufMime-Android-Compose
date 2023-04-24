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

package net.ouftech.oufmime.ui.views.screens.turnend

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import net.ouftech.oufmime.R
import net.ouftech.oufmime.data.Categories
import net.ouftech.oufmime.data.Word
import net.ouftech.oufmime.ui.model.Team
import net.ouftech.oufmime.ui.theme.ScreenConfiguration
import net.ouftech.oufmime.ui.theme.ScreenConfiguration.Companion.COMPACT_LANDSCAPE
import net.ouftech.oufmime.ui.theme.ScreenConfiguration.Companion.COMPACT_PORTRAIT
import net.ouftech.oufmime.ui.theme.ScreenConfiguration.Companion.EXPANDED_LANDSCAPE
import net.ouftech.oufmime.ui.theme.ScreenConfiguration.Companion.MEDIUM
import net.ouftech.oufmime.ui.theme.ScreenConfiguredTheme
import net.ouftech.oufmime.ui.theme.getDimens
import net.ouftech.oufmime.ui.views.library.Backgrounded
import net.ouftech.oufmime.ui.views.library.CompactLandscapePreview
import net.ouftech.oufmime.ui.views.library.CompactPortraitPreview
import net.ouftech.oufmime.ui.views.library.ExpandedLandscapePreview
import net.ouftech.oufmime.ui.views.library.ExpandedPortraitPreview
import net.ouftech.oufmime.ui.views.library.FullScreenColumn
import net.ouftech.oufmime.ui.views.library.MediumPreview
import net.ouftech.oufmime.ui.views.library.SizedButton

// region Views

@Composable
fun TurnEndScreen(
    uiState: TurnEndUiState,
    onWordChange: (Pair<Word, Boolean>) -> Unit,
    onNextClick: () -> Unit,
) = Backgrounded {
    with(uiState) {
        FullScreenColumn(
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Box(
                modifier = Modifier
                    .weight(0.7f)
                    .widthIn(max = getDimens().largeCardMaxWidth)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                WordsListView(
                    modifier = Modifier
                        .padding(getDimens().playWidgetsSize / 3)
                        .fillMaxSize(),
                    wordsPlayed = wordsPlayed,
                    onWordChange = onWordChange
                )

                DecoratedCardDeck(
                    modifier = Modifier.align(Alignment.TopStart),
                    count = wordsPlayed.count { !it.wasFound },
                    isFound = false
                )

                DecoratedCardDeck(
                    modifier = Modifier.align(Alignment.TopEnd),
                    count = wordsPlayed.count { it.wasFound },
                    isFound = true
                )
            }

            Box(modifier = Modifier.weight(0.1f)) {
                SizedButton(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(id = R.string.next),
                    onClick = onNextClick,
                )
            }
        }
    }
}

// endregion Views

// region Previews

@CompactPortraitPreview
@Composable
private fun CompactPortraitPreviewTurnEndScreen() = TurnEndScreenPreview(COMPACT_PORTRAIT, Team.Orange)

@CompactLandscapePreview
@Composable
private fun CompactLandscapePreviewTurnEndScreen() = TurnEndScreenPreview(COMPACT_LANDSCAPE, Team.Blue)

@MediumPreview
@Composable
private fun MediumPreviewTurnEndScreen() = TurnEndScreenPreview(MEDIUM, Team.Orange)

@ExpandedPortraitPreview
@Composable
private fun ExpandedPortraitPreviewTurnEndScreen() = TurnEndScreenPreview(ScreenConfiguration.EXPANDED_PORTRAIT, Team.Blue)

@ExpandedLandscapePreview
@Composable
private fun ExpandedLandscapePreviewTurnEndScreen() = TurnEndScreenPreview(EXPANDED_LANDSCAPE, Team.Orange)

@Composable
private fun TurnEndScreenPreview(screenConfiguration: ScreenConfiguration, team: Team) =
    ScreenConfiguredTheme(screenConfiguration, team.colors) { TurnEndScreen(uiState = stubUiState, onWordChange = { }, onNextClick = { }) }

// endregion Previews

data class TurnEndUiState(
    val currentTeam: Int,
    val wordsPlayed: List<WordListItem.WordItem>,
)

sealed interface WordListItem {
    data class WordItem(val word: Word, val wasFound: Boolean) : WordListItem
    object Header : WordListItem
    object Footer : WordListItem

}

private val stubUiState
    get() = TurnEndUiState(
        currentTeam = 0,
        wordsPlayed = listOf(
            WordListItem.WordItem(Word("Squid", Categories.ANIMALS, "en"), true),
            WordListItem.WordItem(Word("Blue Bear", Categories.ANIMALS, "en"), false),
            WordListItem.WordItem(Word("Zeus", Categories.FICTIONAL, "en"), false),
        ),
    )