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

package net.ouftech.oufmime.ui.views.screens.scoreboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import net.ouftech.oufmime.R
import net.ouftech.oufmime.ui.theme.Accent
import net.ouftech.oufmime.ui.theme.NoTeamColors
import net.ouftech.oufmime.ui.theme.ScreenConfiguration
import net.ouftech.oufmime.ui.theme.ScreenConfiguration.Companion.COMPACT_LANDSCAPE
import net.ouftech.oufmime.ui.theme.ScreenConfiguration.Companion.COMPACT_PORTRAIT
import net.ouftech.oufmime.ui.theme.ScreenConfiguration.Companion.EXPANDED_LANDSCAPE
import net.ouftech.oufmime.ui.theme.ScreenConfiguration.Companion.EXPANDED_PORTRAIT
import net.ouftech.oufmime.ui.theme.ScreenConfiguration.Companion.MEDIUM
import net.ouftech.oufmime.ui.theme.ScreenConfiguredTheme
import net.ouftech.oufmime.ui.theme.getDimens
import net.ouftech.oufmime.ui.theme.isCompactLandscape
import net.ouftech.oufmime.ui.theme.isExpandedLandscape
import net.ouftech.oufmime.ui.views.library.CompactLandscapePreview
import net.ouftech.oufmime.ui.views.library.CompactPortraitPreview
import net.ouftech.oufmime.ui.views.library.ExpandedLandscapePreview
import net.ouftech.oufmime.ui.views.library.ExpandedPortraitPreview
import net.ouftech.oufmime.ui.views.library.FullScreenBox
import net.ouftech.oufmime.ui.views.library.FullScreenColumn
import net.ouftech.oufmime.ui.views.library.FullScreenRow
import net.ouftech.oufmime.ui.views.library.FullWidthRow
import net.ouftech.oufmime.ui.views.library.MediumPreview
import net.ouftech.oufmime.ui.views.library.SizedButton
import net.ouftech.oufmime.ui.views.library.SplitBackgrounded
import net.ouftech.oufmime.utils.circleShadowedBackground

// region Views

@Composable
fun ScoreboardScreen(
    uiState: ScoreboardScreenUiState,
    onNextClick: () -> Unit
) = when {
    isCompactLandscape() -> CompactLandscapeScoreboardScreen(uiState, onNextClick)
    isExpandedLandscape() -> ExpandedLandscapeScoreboardScreen(uiState, onNextClick)
    else -> NormalScoreboardScreen(uiState, onNextClick)
}

@Composable
private fun ExpandedLandscapeScoreboardScreen(
    uiState: ScoreboardScreenUiState,
    onNextClick: () -> Unit
) = SplitBackgrounded {
    with(uiState) {
        FullScreenColumn {
            FullWidthRow {
                Spacer(modifier = Modifier.weight(1f))
                TeamScoreboardView(team = 0, uiState = teamBlueScoreboardUiState, modifier = Modifier.weight(4f))
                Spacer(modifier = Modifier.weight(1f))
                TeamScoreboardView(team = 1, uiState = teamOrangeScoreboardUiState, modifier = Modifier.weight(4f))
                Spacer(modifier = Modifier.weight(1f))

            }

            SizedButton(
                onClick = onNextClick,
                text = stringResource(id = if (hasMoreRounds) R.string.next_round else R.string.new_game),
                textColor = Accent,
            )
        }
    }
}

@Composable
private fun CompactLandscapeScoreboardScreen(
    uiState: ScoreboardScreenUiState,
    onNextClick: () -> Unit
) = SplitBackgrounded {
    with(uiState) {
        FullScreenRow {
            TeamScoreboardView(team = 0, uiState = teamBlueScoreboardUiState)
            Spacer(modifier = Modifier.size(0.dp))
            TeamScoreboardView(team = 1, uiState = teamOrangeScoreboardUiState)
            Spacer(modifier = Modifier.size(0.dp))

            NextIconButton(onNextClick = onNextClick)
        }
    }
}

@Composable
private fun NormalScoreboardScreen(
    uiState: ScoreboardScreenUiState,
    onNextClick: () -> Unit
) = SplitBackgrounded {
    with(uiState) {
        FullScreenBox {
            FullScreenColumn {
                TeamScoreboardView(team = 0, uiState = teamBlueScoreboardUiState)
                Spacer(modifier = Modifier.size(0.dp))
                TeamScoreboardView(team = 1, uiState = teamOrangeScoreboardUiState)
            }

            NextIconButton(Modifier.align(Alignment.CenterEnd), onNextClick)
        }
    }
}

@Composable
private fun NextIconButton(modifier: Modifier = Modifier, onNextClick: () -> Unit) = Icon(
    painter = painterResource(id = R.drawable.ic_chevron_right),
    contentDescription = stringResource(id = R.string.next_round_content_description),
    modifier = modifier
        .padding(end = getDimens().paddingXLarge)
        .size(getDimens().smallIconButton)
        .circleShadowedBackground()
        .clickable(onClick = onNextClick)
)

// endregion Views

// region Previews

@CompactPortraitPreview
@Composable
private fun CompactPortraitPreviewScoreboardScreen() = ScoreboardScreenPreview(COMPACT_PORTRAIT)

@CompactLandscapePreview
@Composable
private fun CompactLandscapePreviewScoreboardScreen() = ScoreboardScreenPreview(COMPACT_LANDSCAPE)

@MediumPreview
@Composable
private fun MediumPreviewScoreboardScreen() = ScoreboardScreenPreview(MEDIUM)

@ExpandedPortraitPreview
@Composable
private fun ExpandedPortraitPreviewScoreboardScreen() = ScoreboardScreenPreview(EXPANDED_PORTRAIT)

@ExpandedLandscapePreview
@Composable
private fun ExpandedLandscapePreviewScoreboardScreen() = ScoreboardScreenPreview(EXPANDED_LANDSCAPE)

@Composable
private fun ScoreboardScreenPreview(screenConfiguration: ScreenConfiguration) = ScreenConfiguredTheme(screenConfiguration, NoTeamColors) {
    ScoreboardScreen(stubUiState) {}
}

// endregion Previews

data class ScoreboardScreenUiState(
    val hasMoreRounds: Boolean,
    val teamBlueScoreboardUiState: TeamScoreboardUiState,
    val teamOrangeScoreboardUiState: TeamScoreboardUiState,
)

data class TeamScoreboardUiState(
    val describeScore: Int,
    val wordScore: Int,
    val mimeScore: Int,
    val totalScore: Int,
)

private val stubUiState
    get() = ScoreboardScreenUiState(
        hasMoreRounds = true,
        teamBlueScoreboardUiState = getStubTeamScoreboardUiState(0),
        teamOrangeScoreboardUiState = getStubTeamScoreboardUiState(1),
    )

private fun getStubTeamScoreboardUiState(team: Int) = TeamScoreboardUiState(
    describeScore = team + 1,
    wordScore = (team + 1) * 2,
    mimeScore = (team + 1) * 3,
    totalScore = (team + 1) * 6
)