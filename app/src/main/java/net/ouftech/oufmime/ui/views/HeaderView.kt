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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import net.ouftech.oufmime.R
import net.ouftech.oufmime.ui.theme.*

@Composable
fun HeaderView(
    team1TotalScore: Int = 0,
    team1CurrentRoundScore: Int = 0,
    team2TotalScore: Int = 0,
    team2CurrentRoundScore: Int = 0,
    dimens: Dimens
) {
    Row(
        Modifier.background(color = Primary),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ScoreBoardView(
            topLabel = stringResource(id = R.string.score_total),
            topScore = team1TotalScore,
            bottomLabel = stringResource(id = R.string.score_round),
            bottomScore = team1CurrentRoundScore,
            dimens
        )

        Spacer(modifier = Modifier.width(dimens.paddingSmall))

        Image(
            modifier = Modifier.size(dimens.iconMedium),
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "App Icon"
        )

        Spacer(modifier = Modifier.width(dimens.paddingSmall))

        ScoreBoardView(
            topLabel = stringResource(id = R.string.score_total),
            topScore = team2TotalScore,
            bottomLabel = stringResource(id = R.string.score_round),
            bottomScore = team2CurrentRoundScore,
            dimens
        )
    }
}

@Preview(showBackground = true, widthDp = 600)
@Composable
fun HeaderPreview() {
    OufMimeTheme {
        HeaderView(10, 5, 20, 10, MediumDimens)
    }
}

@Composable
fun ScoreBoardView(
    topLabel: String,
    topScore: Int = 0,
    bottomLabel: String,
    bottomScore: Int = 0,
    dimens: Dimens
) {
    Column(
        modifier = Modifier
            .width(dimens.simpleScoreBoardWidth)
            .border(
                width = dimens.borderSmall,
                color = White,
                shape = RoundedCornerShape(20)
            )
            .padding(dimens.paddingMedium)
    ) {
        ScoreLineView(
            scoreName = topLabel,
            score = topScore,
            dimens = dimens
        )
        ScoreLineView(
            scoreName = bottomLabel,
            score = bottomScore,
            dimens = dimens
        )
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun ScoreBoardPreviewPhone() {
    OufMimeTheme {
        ScoreBoardView("Total", 10, "Round", 5, MediumDimens)
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_C)
@Composable
fun ScoreBoardPreviewTablet() {
    OufMimeTheme {
        ScoreBoardView("Total", 10, "Round", 5, ExpandedDimens)
    }
}

@Composable
fun ScoreLineView(
    scoreName: String,
    score: Int,
    dimens: Dimens
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = scoreName, color = White, fontSize = dimens.bodyText)
        Text(text = score.toString(), color = White, fontSize = dimens.bodyText)
    }
}

@Preview(showBackground = true, widthDp = 200)
@Composable
fun ScoreLinePreview() {
    OufMimeTheme {
        ScoreLineView("Total", 5, MediumDimens)
    }
}