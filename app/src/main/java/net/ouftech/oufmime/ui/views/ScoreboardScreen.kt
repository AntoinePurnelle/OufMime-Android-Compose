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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.ouftech.oufmime.R
import net.ouftech.oufmime.data.WordsViewModel
import net.ouftech.oufmime.ui.theme.Accent

@Composable
fun ScoreboardScreen(
    viewModel: WordsViewModel,
    onNextClick: () -> Unit
) {
    FullScreenColumn {
        TeamScoreboardView(viewModel = viewModel, team = 0)
        TeamScoreboardView(viewModel = viewModel, team = 1)

        SizedButton(
            onClick = onNextClick,
            text = stringResource(id = if (viewModel.currentRound == 2) R.string.new_game else R.string.next_round)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFF6F00)
@Composable
fun ScoreboardScreenPreview() {
    ScoreboardScreen(viewModel = WordsViewModel(), onNextClick = {})
}

@Composable
fun TeamScoreboardView(viewModel: WordsViewModel, team: Int) {
    Column(
        modifier = Modifier
            .width(200.dp)
            .background(shape = RoundedCornerShape(2.dp), color = White)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.team_x, team + 1),
            fontSize = 32.sp,
            color = Accent
        )

        ScoreboardLineView(
            roundName = stringResource(id = R.string.describe),
            score = viewModel.getTeamRoundScore(team, 0)
        )
        ScoreboardLineView(
            roundName = stringResource(id = R.string.word),
            score = viewModel.getTeamRoundScore(team, 1)
        )
        ScoreboardLineView(
            roundName = stringResource(id = R.string.mime),
            score = viewModel.getTeamRoundScore(team, 2)
        )

        Text(
            text = viewModel.getTeamTotalScore(team).toString(),
            fontSize = 32.sp,
            color = Accent
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFF6F00)
@Composable
fun TeamScoreboardPreview() {
    TeamScoreboardView(viewModel = WordsViewModel(), team = 0)
}

@Composable
fun ScoreboardLineView(
    roundName: String,
    score: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = roundName, color = Accent)
        Text(text = if (score == -1) "--" else score.toString(), color = Accent)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun ScoreboardLineWithScorePreview() {
    ScoreboardLineView(roundName = stringResource(id = R.string.describe), score = 9)
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun ScoreboardLineWithoutScorePreview() {
    ScoreboardLineView(roundName = stringResource(id = R.string.describe), score = -1)
}
