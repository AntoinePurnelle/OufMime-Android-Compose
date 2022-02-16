package net.ouftech.oufmime.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.ouftech.oufmime.R
import net.ouftech.oufmime.data.WordsViewModel
import net.ouftech.oufmime.ui.theme.Accent
import net.ouftech.oufmime.ui.theme.Dimens
import net.ouftech.oufmime.ui.theme.ExpandedDimens
import net.ouftech.oufmime.ui.theme.MediumDimens

@Composable
fun ScoreboardScreen(
    viewModel: WordsViewModel,
    isExpandedScreen: Boolean,
    dimens: Dimens,
    onNextClick: () -> Unit
) {
    FullScreenColumn {
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
            text = stringResource(id = if (viewModel.currentRound == 2) R.string.new_game else R.string.next_round),
            dimens = dimens
        )
    }
}

@Preview(showBackground = true, name = "Scoreboard Phone", device = Devices.PIXEL_4)
@Composable
fun ScoreboardScreenPreviewPhone() {
    ScoreboardScreen(
        viewModel = WordsViewModel(),
        isExpandedScreen = false,
        dimens = MediumDimens,
        onNextClick = {})
}

@Preview(showBackground = true, name = "Scoreboard Tablet", device = Devices.PIXEL_C)
@Composable
fun ScoreboardScreenPreviewTablet() {
    ScoreboardScreen(
        viewModel = WordsViewModel(),
        isExpandedScreen = true,
        dimens = ExpandedDimens,
        onNextClick = {})
}

@Composable
fun TeamScoreboardView(
    viewModel: WordsViewModel,
    team: Int,
    dimens: Dimens
) {
    Column(
        modifier = Modifier
            .width(dimens.fullScoreBoardWidth)
            .background(shape = RoundedCornerShape(2.dp), color = White)
            .padding(dimens.paddingLarge),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.team_x, team + 1),
            fontSize = dimens.titleText,
            color = Accent
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
