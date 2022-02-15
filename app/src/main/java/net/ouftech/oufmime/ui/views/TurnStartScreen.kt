package net.ouftech.oufmime.ui.views

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import net.ouftech.oufmime.R
import net.ouftech.oufmime.data.WordsViewModel
import net.ouftech.oufmime.ui.theme.OufMimeTheme

@Composable
fun TurnStartScreen(
    viewModel: WordsViewModel,
    onStartClick: () -> Unit
) {
    val roundName = when (viewModel.currentRound) {
        1 -> stringResource(id = R.string.describe)
        2 -> stringResource(id = R.string.word)
        else -> stringResource(id = R.string.mime)
    }

    FullScreenColumn {
        HeaderView(
            viewModel.getTeam1TotalScore(),
            viewModel.getTeam1RoundScore(),
            viewModel.getTeam2TotalScore(),
            viewModel.getTeam2RoundScore()
        )

        Text(
            stringResource(id = R.string.round_title, viewModel.currentRound, roundName),
            color = White,
            fontSize = 50.sp,
            textAlign = TextAlign.Center
        )

        SizedButton(
            onClick = onStartClick,
            text = stringResource(id = R.string.team_x_plays, viewModel.currentTeam)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TurnStartPreview() {
    OufMimeTheme {
        TurnStartScreen(WordsViewModel()) {}
    }
}