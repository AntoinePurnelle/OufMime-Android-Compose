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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import net.ouftech.oufmime.R
import net.ouftech.oufmime.ui.theme.BlueTeam
import net.ouftech.oufmime.ui.theme.NoTeamColors
import net.ouftech.oufmime.ui.theme.OrangeTeam
import net.ouftech.oufmime.ui.theme.TeamBlueColors
import net.ouftech.oufmime.ui.theme.TeamOrangeColors
import net.ouftech.oufmime.ui.theme.getDimens
import net.ouftech.oufmime.ui.views.library.AppIcon
import net.ouftech.oufmime.ui.views.library.PreviewView
import net.ouftech.oufmime.utils.roundedRectShadowedBackground

// region Views

@Composable
fun HeaderView(
    team1TotalScore: Int = 0,
    team1CurrentRoundScore: Int = 0,
    team2TotalScore: Int = 0,
    team2CurrentRoundScore: Int = 0,
    currentTeam: Int,
) = Row(
    modifier = Modifier
        .widthIn(max = getDimens().largeCardMaxWidth)
        .roundedRectShadowedBackground(backgroundColor = White)
        .padding(getDimens().paddingLarge),
    horizontalArrangement = Arrangement.Center,
    verticalAlignment = Alignment.CenterVertically
) {
    ScoreBoardView(
        modifier = Modifier.weight(1f),
        topLabel = stringResource(id = R.string.score_total),
        topScore = team2TotalScore,
        bottomLabel = stringResource(id = R.string.score_round),
        bottomScore = team2CurrentRoundScore,
        color = OrangeTeam
    )

    Spacer(modifier = Modifier.width(getDimens().paddingLarge))

    AppIcon(currentTeam = currentTeam)

    Spacer(modifier = Modifier.width(getDimens().paddingLarge))

    ScoreBoardView(
        modifier = Modifier.weight(1f),
        topLabel = stringResource(id = R.string.score_total),
        topScore = team1TotalScore,
        bottomLabel = stringResource(id = R.string.score_round),
        bottomScore = team1CurrentRoundScore,
        color = BlueTeam
    )
}

@Composable
fun ScoreBoardView(
    modifier: Modifier = Modifier,
    topLabel: String,
    topScore: Int = 0,
    bottomLabel: String,
    bottomScore: Int = 0,
    color: Color = MaterialTheme.colorScheme.tertiary,
) {
    Column(
        modifier = modifier
            .width(getDimens().simpleScoreBoardWidth)
            .roundedRectShadowedBackground(backgroundColor = color)
            .padding(getDimens().paddingMedium)
    ) {
        ScoreLineView(
            scoreName = topLabel,
            score = topScore,
        )
        ScoreLineView(
            scoreName = bottomLabel,
            score = bottomScore,
        )
    }
}

@Composable
fun ScoreLineView(
    scoreName: String,
    score: Int,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ScoreText(text = scoreName)
        ScoreText(text = score.toString())
    }
}

@Composable
private fun ScoreText(
    text: String
) = Text(
    text = text,
    color = White,
    style = MaterialTheme.typography.bodyMedium,
)

// endregion Views

// region Previews

@Preview(widthDp = 850, heightDp = 600)
@Composable
fun HeaderOrangePreviews() = PreviewView(TeamOrangeColors) {
    HeaderView(team1TotalScore = 10, team1CurrentRoundScore = 5, team2TotalScore = 20, team2CurrentRoundScore = 10, currentTeam = 0)
}

@Preview(widthDp = 850, heightDp = 600)
@Composable
fun HeaderBluePreviews() = PreviewView(TeamBlueColors) {
    HeaderView(team1TotalScore = 10, team1CurrentRoundScore = 5, team2TotalScore = 20, team2CurrentRoundScore = 10, currentTeam = 1)
}

@Preview(widthDp = 500, heightDp = 450)
@Composable
fun ScoreBoardPreviews() = PreviewView {
    ScoreBoardView(
        topLabel = "Total",
        topScore = 10,
        bottomLabel = "Round",
        bottomScore = 5,
    )
}

@Preview(widthDp = 200, heightDp = 150)
@Composable
fun ScoreLinePreview() = PreviewView(NoTeamColors) { ScoreLineView("Total", 5) }

// endregion Previews