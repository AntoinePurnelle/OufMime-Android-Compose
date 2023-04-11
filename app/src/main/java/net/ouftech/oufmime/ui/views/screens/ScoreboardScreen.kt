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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.ouftech.oufmime.R
import net.ouftech.oufmime.ext.circleShadowedBackground
import net.ouftech.oufmime.ext.roundedRectShadowedBackground
import net.ouftech.oufmime.ui.theme.Accent
import net.ouftech.oufmime.ui.theme.BlueTeamAccent
import net.ouftech.oufmime.ui.theme.Dimens
import net.ouftech.oufmime.ui.theme.ExpandedDimens
import net.ouftech.oufmime.ui.theme.MediumDimens
import net.ouftech.oufmime.ui.theme.OrangeTeamAccent
import net.ouftech.oufmime.ui.theme.OufMimeTheme
import net.ouftech.oufmime.ui.views.library.FullScreenBox
import net.ouftech.oufmime.ui.views.library.FullScreenColumn
import net.ouftech.oufmime.ui.views.library.FullWidthRow
import net.ouftech.oufmime.ui.views.library.SizedButton

@Composable
fun ScoreboardScreen(
    uiState: ScoreboardScreenUiState,
    onNextClick: () -> Unit
) = if (uiState.dimens.isExpandedScreen) {
    TabletScoreboardScreen(uiState, onNextClick)
} else {
    PhoneScoreboardScreen(uiState, onNextClick)
}


@Composable
private fun TabletScoreboardScreen(
    uiState: ScoreboardScreenUiState,
    onNextClick: () -> Unit
) = with(uiState) {
    FullScreenColumn {
        FullWidthRow {
            TeamScoreboardView(team = 0, dimens = dimens, teamBlueScoreboardUiState)
            Spacer(modifier = Modifier.size(0.dp))
            TeamScoreboardView(team = 1, dimens = dimens, teamOrangeScoreboardUiState)
        }

        SizedButton(
            onClick = onNextClick,
            text = stringResource(id = if (hasMoreRounds) R.string.next_round else R.string.new_game),
            textColor = Accent,
            dimens = dimens
        )
    }
}

@Composable
private fun PhoneScoreboardScreen(
    uiState: ScoreboardScreenUiState,
    onNextClick: () -> Unit
) = with(uiState) {
    FullScreenBox {
        FullScreenColumn {
            TeamScoreboardView(team = 0, dimens = dimens, teamBlueScoreboardUiState)
            Spacer(modifier = Modifier.size(0.dp))
            TeamScoreboardView(team = 1, dimens = dimens, teamOrangeScoreboardUiState)
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_chevron_right),
            contentDescription = "TODO", // TODO
            modifier = Modifier
                .padding(end = dimens.paddingXLarge)
                .size(dimens.smallIconButton)
                .circleShadowedBackground()
                .clickable(onClick = onNextClick)
                .align(Alignment.CenterEnd)
        )
    }
}

@Composable
private fun TeamScoreboardView(
    team: Int,
    dimens: Dimens,
    uiState: TeamScoreboardUiState,
) {
    Column(
        modifier = Modifier
            .width(dimens.fullScoreBoardWidth)
            .roundedRectShadowedBackground(backgroundColor = White)
            .padding(dimens.paddingLarge),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(
                id = if (team == 0) R.string.team_orange else R.string.team_blue,
                team + 1
            ),
            fontSize = dimens.titleText,
            color = Accent,
            textAlign = TextAlign.Center
        )

        ScoreboardLineView(
            roundName = stringResource(id = R.string.describe),
            score = uiState.describeScore,
            dimens = dimens
        )
        ScoreboardLineView(
            roundName = stringResource(id = R.string.word),
            score = uiState.wordScore,
            dimens = dimens
        )
        ScoreboardLineView(
            roundName = stringResource(id = R.string.mime),
            score = uiState.mimeScore,
            dimens = dimens
        )

        Text(
            text = uiState.totalScore.toString(),
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
        Text(
            text = roundName,
            color = Accent,
            fontSize = dimens.bodyText
        )
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
            uiState = getStubScoreboardScreenUiState(MediumDimens),
            onNextClick = { }
        )
    }
}

@Preview(showBackground = true, name = "Scoreboard Tablet", device = Devices.PIXEL_C)
@Composable
private fun ScoreboardScreenPreviewTablet() {
    OufMimeTheme {
        ScoreboardScreen(
            uiState = getStubScoreboardScreenUiState(ExpandedDimens),
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

data class ScoreboardScreenUiState(
    val dimens: Dimens,
    val hasMoreRounds: Boolean,
    val teamBlueScoreboardUiState: TeamScoreboardUiState,
    val teamOrangeScoreboardUiState: TeamScoreboardUiState,
)

data class TeamScoreboardUiState(
    val color: Color,
    val describeScore: Int,
    val wordScore: Int,
    val mimeScore: Int,
    val totalScore: Int,
)

private fun getStubScoreboardScreenUiState(dimens: Dimens) = ScoreboardScreenUiState(
    dimens = dimens,
    hasMoreRounds = true,
    teamBlueScoreboardUiState = getStubTeamScoreboardUiModel(0),
    teamOrangeScoreboardUiState = getStubTeamScoreboardUiModel(1),
)

private fun getStubTeamScoreboardUiModel(team: Int) = TeamScoreboardUiState(
    color = if (team == 0) BlueTeamAccent else OrangeTeamAccent,
    describeScore = team + 1,
    wordScore = (team + 1) * 2,
    mimeScore = (team + 1) * 3,
    totalScore = (team + 1) * 6
)