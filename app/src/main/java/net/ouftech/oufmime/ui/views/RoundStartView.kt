package net.ouftech.oufmime.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.ouftech.oufmime.R
import net.ouftech.oufmime.data.WordsViewModel
import net.ouftech.oufmime.ui.theme.OufMimeTheme

@Composable
fun RoundStartView(
    viewModel: WordsViewModel,
    onStartClick: () -> Unit
) {
    val roundName = when (viewModel.currentRound) {
        1 -> stringResource(id = R.string.describe)
        2 -> stringResource(id = R.string.word)
        else -> stringResource(id = R.string.mime)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.colorPrimary))
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        HeaderView(
            viewModel.team1TotalScore,
            viewModel.team1CurrentRoundScore,
            viewModel.team2TotalScore,
            viewModel.team2CurrentRoundScore
        )

        Text(
            stringResource(id = R.string.round_title, viewModel.currentRound, roundName),
            color = White,
            fontSize = 50.sp,
            textAlign = TextAlign.Center
        )

        Button(
            onClick = onStartClick,
            colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.colorAccent))
        ) {
            Text(
                stringResource(
                    id = R.string.team_x_plays,
                    viewModel.currentTeam
                ).toUpperCase(Locale.current),
                color = White,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RoundStartPreview() {
    OufMimeTheme {
        RoundStartView(WordsViewModel()) {}
    }
}