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

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.ouftech.oufmime.R
import net.ouftech.oufmime.ui.theme.*
import net.ouftech.oufmime.ui.views.library.FullScreenColumn
import net.ouftech.oufmime.ui.views.library.FullWidthRow
import net.ouftech.oufmime.ui.views.library.SizedButton

@Composable
fun ScoreboardScreen(
    isExpandedScreen: Boolean,
    dimens: Dimens,
    hasMoreRounds: Boolean,
    teamBlueScoreboardUiModel: TeamScoreboardUiModel,
    teamOrangeScoreboardUiModel: TeamScoreboardUiModel,
    onNextClick: () -> Unit
) {
    FullScreenColumn(modifier = Modifier.background(color = White)) {
        if (isExpandedScreen) {
            FullWidthRow {
                TeamScoreboardView(team = 0, dimens = dimens, teamBlueScoreboardUiModel)
                TeamScoreboardView(team = 1, dimens = dimens, teamOrangeScoreboardUiModel)
            }
        } else {
            TeamScoreboardView(team = 0, dimens = dimens, teamBlueScoreboardUiModel)
            TeamScoreboardView(team = 1, dimens = dimens, teamOrangeScoreboardUiModel)
        }

        SizedButton(
            onClick = onNextClick,
            text = stringResource(id = if (hasMoreRounds) R.string.next_round else R.string.new_game),
            dimens = dimens
        )
    }
}

@Composable
private fun TeamScoreboardView(
    team: Int,
    dimens: Dimens,
    model: TeamScoreboardUiModel,
) {
    val shape = RoundedCornerShape(16.dp)
    Column(
        modifier = Modifier
            .width(dimens.fullScoreBoardWidth)
            .clip(shape = shape)
            .border(
                width = dimens.timerStrokeWidth,
                color = model.color,
                shape = shape
            )
            .background(shape = shape, color = White)
            .padding(dimens.paddingLarge),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(
                id = if (team == 0) R.string.team_blue else R.string.team_orange,
                team + 1
            ),
            fontSize = dimens.titleText,
            color = Accent,
            textAlign = TextAlign.Center
        )

        ScoreboardLineView(
            roundName = stringResource(id = R.string.describe),
            score = model.describeScore,
            dimens = dimens
        )
        ScoreboardLineView(
            roundName = stringResource(id = R.string.word),
            score = model.wordScore,
            dimens = dimens
        )
        ScoreboardLineView(
            roundName = stringResource(id = R.string.mime),
            score = model.mimeScore,
            dimens = dimens
        )

        Text(
            text = model.totalScore.toString(),
            fontSize = dimens.titleText,
            color = Accent
        )
    }
}

@Composable
private fun ScoreboardLineView(
    roundName: String,
    score: Int,
    dimens: Dimens
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimens.paddingMedium),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = roundName, color = Accent, fontSize = dimens.bodyText)
        Text(
            text = if (score == -1) "--" else score.toString(),
            color = Accent,
            fontSize = dimens.bodyText
        )
    }
}

@Preview(showBackground = true, name = "Scoreboard Phone", device = Devices.PIXEL_4)
@Composable
private fun ScoreboardScreenPreviewPhone() {
    OufMimeTheme {
        ScoreboardScreen(
            isExpandedScreen = false,
            dimens = MediumDimens,
            hasMoreRounds = true,
            teamBlueScoreboardUiModel = getStubTeamScoreboardUiModel(0),
            teamOrangeScoreboardUiModel = getStubTeamScoreboardUiModel(1),
            onNextClick = { }
        )
    }
}

@Preview(showBackground = true, name = "Scoreboard Tablet", device = Devices.PIXEL_C)
@Composable
private fun ScoreboardScreenPreviewTablet() {
    OufMimeTheme {
        ScoreboardScreen(
            isExpandedScreen = true,
            dimens = ExpandedDimens,
            hasMoreRounds = true,
            teamBlueScoreboardUiModel = getStubTeamScoreboardUiModel(0),
            teamOrangeScoreboardUiModel = getStubTeamScoreboardUiModel(1),
            onNextClick = { }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFF6F00)
@Composable
private fun TeamScoreboardPreview() {
    TeamScoreboardView(
        team = 0,
        dimens = MediumDimens,
        getStubTeamScoreboardUiModel(0),
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun ScoreboardLineWithScorePreview() {
    ScoreboardLineView(
        roundName = stringResource(id = R.string.describe),
        score = 9,
        dimens = MediumDimens
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun ScoreboardLineWithoutScorePreview() {
    ScoreboardLineView(
        roundName = stringResource(id = R.string.describe),
        score = -1,
        dimens = MediumDimens
    )
}

data class TeamScoreboardUiModel(
    val color: Color,
    val describeScore: Int,
    val wordScore: Int,
    val mimeScore: Int,
    val totalScore: Int,
)

private fun getStubTeamScoreboardUiModel(team: Int) = TeamScoreboardUiModel(
    color = if (team == 0) Accent else Primary,
    describeScore = team + 1,
    wordScore = (team + 1) * 2,
    mimeScore = (team + 1) * 3,
    totalScore = (team + 1) * 6
)