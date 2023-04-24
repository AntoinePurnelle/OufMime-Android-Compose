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

package net.ouftech.oufmime.ui.views.screens.turnstart

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import net.ouftech.oufmime.R
import net.ouftech.oufmime.ui.model.Team
import net.ouftech.oufmime.ui.theme.LocalScreenConfiguration
import net.ouftech.oufmime.ui.theme.ScreenConfiguration
import net.ouftech.oufmime.ui.theme.ScreenConfiguration.Companion.COMPACT_LANDSCAPE
import net.ouftech.oufmime.ui.theme.ScreenConfiguration.Companion.COMPACT_PORTRAIT
import net.ouftech.oufmime.ui.theme.ScreenConfiguration.Companion.EXPANDED_LANDSCAPE
import net.ouftech.oufmime.ui.theme.ScreenConfiguration.Companion.EXPANDED_PORTRAIT
import net.ouftech.oufmime.ui.theme.ScreenConfiguration.Companion.MEDIUM
import net.ouftech.oufmime.ui.theme.ScreenConfiguredTheme
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
fun TurnStartScreen(
    model: TurnStartUiState,
    onStartClick: () -> Unit,
) = Backgrounded {
    val roundName = stringResource(
        when (model.currentRound) {
            0 -> R.string.describe
            1 -> R.string.word
            else -> R.string.mime
        }
    )

    val dimens = LocalScreenConfiguration.current.dimens

    FullScreenColumn {
        HeaderView(
            team1TotalScore = model.blueTotalScore,
            team1CurrentRoundScore = model.blueCurrentRoundScore,
            team2TotalScore = model.orangeTotalScore,
            team2CurrentRoundScore = model.orangeCurrentRoundScore,
            currentTeam = model.currentTeam
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(
                    id = R.string.round_title,
                    model.currentRound + 1
                ),
                color = White,
                fontSize = dimens.titleText,
                textAlign = TextAlign.Center,
            )
            Text(
                text = roundName.uppercase(),
                color = White,
                fontSize = dimens.titleText,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }

        SizedButton(
            onClick = onStartClick,
            text = stringResource(
                id = R.string.team_x_plays,
                stringResource(id = model.teamNameId)
            ),
        )
    }
}

// endregion Views

// region Previews

@CompactPortraitPreview
@Composable
private fun CompactPortraitPreviewTurnStartScreen() = TurnStartPreview(COMPACT_PORTRAIT, Team.Orange)

@CompactLandscapePreview
@Composable
private fun CompactLandscapePreviewTurnStartScreen() = TurnStartPreview(COMPACT_LANDSCAPE, Team.Blue)

@MediumPreview
@Composable
private fun MediumPreviewTurnStartScreen() = TurnStartPreview(MEDIUM, Team.Orange)

@ExpandedPortraitPreview
@Composable
private fun ExpandedPortraitPreviewTurnStartScreen() = TurnStartPreview(EXPANDED_PORTRAIT, Team.Blue)

@ExpandedLandscapePreview
@Composable
private fun ExpandedLandscapePreviewTurnStartScreen() = TurnStartPreview(EXPANDED_LANDSCAPE, Team.Orange)

@Composable
private fun TurnStartPreview(screenConfiguration: ScreenConfiguration, team: Team) =
    ScreenConfiguredTheme(screenConfiguration, team.colors) { TurnStartScreen(getStubTurnStartUiState(team)) {} }

// endregion Previews

data class TurnStartUiState(
    val currentTeam: Int,
    val currentRound: Int,
    @StringRes val teamNameId: Int,
    val blueTotalScore: Int,
    val blueCurrentRoundScore: Int,
    val orangeTotalScore: Int,
    val orangeCurrentRoundScore: Int,
)

private fun getStubTurnStartUiState(team: Team) = TurnStartUiState(team.value, 0, R.string.team_blue, 4, 2, 6, 3)

