package net.ouftech.oufmime.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.ouftech.oufmime.R
import net.ouftech.oufmime.ui.theme.OufMimeTheme

@Composable
fun HeaderView(
    team1TotalScore: Int = 0,
    team1CurrentRoundScore: Int = 0,
    team2TotalScore: Int = 0,
    team2CurrentRoundScore: Int = 0,
) {
    Row(
        Modifier.background(color = colorResource(id = R.color.colorPrimary)),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ScoreBoardView(team1TotalScore, team1CurrentRoundScore)

        Spacer(modifier = Modifier.width(8.dp))

        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "App Icon"
        )

        Spacer(modifier = Modifier.width(8.dp))

        ScoreBoardView(team2TotalScore, team2CurrentRoundScore)
    }
}

@Preview(showBackground = true, widthDp = 600)
@Composable
fun HeaderPreview() {
    OufMimeTheme {
        HeaderView(10, 5, 20, 10)
    }
}

@Composable
fun ScoreBoardView(
    totalScore: Int = 0,
    currentRoundScore: Int = 0
) {
    Column(
        modifier = Modifier
            .width(120.dp)
            .border(
                width = 1.dp,
                color = White,
                shape = RoundedCornerShape(20)
            )
            .padding(8.dp)
    ) {
        ScoreLineView(scoreName = stringResource(id = R.string.score_total), score = totalScore)
        ScoreLineView(
            scoreName = stringResource(id = R.string.score_round),
            score = currentRoundScore
        )
    }
}

@Preview(showBackground = true, widthDp = 200)
@Composable
fun ScoreBoardPreview() {
    OufMimeTheme {
        ScoreBoardView(10, 5)
    }
}

@Composable
fun ScoreLineView(
    scoreName: String,
    score: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = scoreName, color = White)
        Text(text = score.toString(), color = White)
    }
}

@Preview(showBackground = true, widthDp = 200)
@Composable
fun ScoreLinePreview() {
    OufMimeTheme {
        ScoreLineView("Total", 5)
    }
}