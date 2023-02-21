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
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.ouftech.oufmime.R
import net.ouftech.oufmime.data.WordsViewModel
import net.ouftech.oufmime.ui.theme.*
import net.ouftech.oufmime.ui.views.library.FullScreenColumn
import net.ouftech.oufmime.ui.views.library.FullWidthRow
import net.ouftech.oufmime.ui.views.library.SizedButton

@Composable
fun ScoreboardScreen(
    viewModel: WordsViewModel,
    isExpandedScreen: Boolean,
    dimens: Dimens,
    onNextClick: () -> Unit
) {
    FullScreenColumn(modifier = Modifier.background(color = White)) {
        if (isExpandedScreen) {
            FullWidthRow {
                TeamScoreboardView(viewModel = viewModel, team = 0, dimens = dimens)
                TeamScoreboardView(viewModel = viewModel, team = 1, dimens = dimens)
            }
        } else {
            TeamScoreboardView(viewModel = viewModel, team = 0, dimens = dimens)
            TeamScoreboardView(viewModel = viewModel, team = 1, dimens = dimens)
        }

        SizedButton(
            onClick = onNextClick,
            text = stringResource(id = if (viewModel.hasMoreRounds) R.string.next_round else R.string.new_game),
            dimens = dimens
        )
    }
}

@Preview(showBackground = true, name = "Scoreboard Phone", device = Devices.PIXEL_4)
@Composable
fun ScoreboardScreenPreviewPhone() {
    OufMimeTheme {
        ScoreboardScreen(
            viewModel = WordsViewModel(),
            isExpandedScreen = false,
            dimens = MediumDimens,
            onNextClick = { }
        )
    }
}

@Preview(showBackground = true, name = "Scoreboard Tablet", device = Devices.PIXEL_C)
@Composable
fun ScoreboardScreenPreviewTablet() {
    OufMimeTheme {
        ScoreboardScreen(
            viewModel = WordsViewModel(),
            isExpandedScreen = true,
            dimens = ExpandedDimens,
            onNextClick = { }
        )
    }
}

@Composable
fun TeamScoreboardView(
    viewModel: WordsViewModel,
    team: Int,
    dimens: Dimens
) {
    val shape = RoundedCornerShape(16.dp)
    Column(
        modifier = Modifier
            .width(dimens.fullScoreBoardWidth)
            .clip(shape = shape)
            .border(
                width = dimens.timerStrokeWidth,
                color = viewModel.getTeamColor(team),
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
            score = viewModel.getTeamRoundScore(team, 0),
            dimens = dimens
        )
        ScoreboardLineView(
            roundName = stringResource(id = R.string.word),
            score = viewModel.getTeamRoundScore(team, 1),
            dimens = dimens
        )
        ScoreboardLineView(
            roundName = stringResource(id = R.string.mime),
            score = viewModel.getTeamRoundScore(team, 2),
            dimens = dimens
        )

        Text(
            text = viewModel.getTeamTotalScore(team).toString(),
            fontSize = dimens.titleText,
            color = Accent
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFF6F00)
@Composable
fun TeamScoreboardPreview() {
    TeamScoreboardView(viewModel = WordsViewModel(), team = 0, dimens = MediumDimens)
}

@Composable
fun ScoreboardLineView(
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

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun ScoreboardLineWithScorePreview() {
    ScoreboardLineView(
        roundName = stringResource(id = R.string.describe),
        score = 9,
        dimens = MediumDimens
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun ScoreboardLineWithoutScorePreview() {
    ScoreboardLineView(
        roundName = stringResource(id = R.string.describe),
        score = -1,
        dimens = MediumDimens
    )
}