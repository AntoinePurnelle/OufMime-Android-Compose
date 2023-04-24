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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import net.ouftech.oufmime.R
import net.ouftech.oufmime.ui.theme.Accent
import net.ouftech.oufmime.ui.theme.getDimens
import net.ouftech.oufmime.ui.views.library.PreviewView
import net.ouftech.oufmime.utils.roundedRectShadowedBackground

// region Views

@Composable
fun TeamScoreboardView(
    team: Int,
    uiState: TeamScoreboardUiState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .widthIn(max = getDimens().fullScoreBoardWidth)
            .roundedRectShadowedBackground(backgroundColor = Color.White)
            .padding(getDimens().paddingLarge),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(
                id = if (team == 0) R.string.team_orange else R.string.team_blue,
                team + 1
            ),
            fontSize = getDimens().titleText,
            color = Accent,
            textAlign = TextAlign.Center
        )

        ScoreboardLineView(
            roundName = stringResource(id = R.string.describe),
            score = uiState.describeScore,
        )
        ScoreboardLineView(
            roundName = stringResource(id = R.string.word),
            score = uiState.wordScore,
        )
        ScoreboardLineView(
            roundName = stringResource(id = R.string.mime),
            score = uiState.mimeScore,
        )

        Text(
            text = uiState.totalScore.toString(),
            fontSize = getDimens().titleText,
            color = Accent
        )
    }
}

@Composable
fun ScoreboardLineView(
    roundName: String,
    score: Int,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(getDimens().paddingSmall),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = roundName,
            color = Accent,
            fontSize = getDimens().bodyLarge
        )
        Text(
            text = if (score == -1) "--" else score.toString(),
            color = Accent,
            fontSize = getDimens().bodyLarge
        )
    }
}

// endregion Views

// region Previews

@Preview(widthDp = 500, heightDp = 1200)
@Composable
private fun TeamScoreboardPreview() = PreviewView { TeamScoreboardView(team = 1, stubTeamScoreboardUiState) }

@Preview(widthDp = 500, heightDp = 160, showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun ScoreboardLineWithScorePreview() = PreviewView {
    ScoreboardLineView(
        roundName = stringResource(id = R.string.describe),
        score = 9,
    )
}

@Preview(widthDp = 500, heightDp = 160, showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
private fun ScoreboardLineWithoutScorePreview() = PreviewView {
    ScoreboardLineView(
        roundName = stringResource(id = R.string.describe),
        score = -1,
    )
}

// endregion Previews

private val stubTeamScoreboardUiState
    get() = TeamScoreboardUiState(
        describeScore = 1,
        wordScore = 2,
        mimeScore = 3,
        totalScore = 6
    )