package net.ouftech.oufmime.ui.views.library

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.ouftech.oufmime.R
import net.ouftech.oufmime.ui.theme.*

@Composable
fun HeaderView(
    team1TotalScore: Int = 0,
    team1CurrentRoundScore: Int = 0,
    team2TotalScore: Int = 0,
    team2CurrentRoundScore: Int = 0,
    dimens: Dimens,
    invertColors: Boolean
) {
    Row(
        modifier = Modifier
            .widthIn(max = 600.dp)
            .background(shape = RoundedCornerShape(20), color = White)
            .padding(horizontal = dimens.paddingLarge),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ScoreBoardView(
            modifier = Modifier.weight(1f),
            topLabel = stringResource(id = R.string.score_total),
            topScore = team1TotalScore,
            middleLabel = stringResource(id = R.string.score_round),
            middleScore = team1CurrentRoundScore,
            dimens = dimens,
            color = Accent
        )

        Spacer(modifier = Modifier.width(dimens.paddingSmall))

        AppIcon(dimens = dimens, inverted = invertColors)

        Spacer(modifier = Modifier.width(dimens.paddingSmall))

        ScoreBoardView(
            modifier = Modifier.weight(1f),
            topLabel = stringResource(id = R.string.score_total),
            topScore = team2TotalScore,
            middleLabel = stringResource(id = R.string.score_round),
            middleScore = team2CurrentRoundScore,
            dimens = dimens,
            color = Primary
        )
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun HeaderPreview() {
    OufMimeTheme {
        HeaderView(10, 5, 20, 10, MediumDimens, invertColors = false)
    }
}

@Composable
fun ScoreBoardView(
    modifier: Modifier = Modifier,
    topLabel: String,
    topScore: Int = 0,
    middleLabel: String,
    middleScore: Int = 0,
    bottomLabel: String? = null,
    bottomScore: Int? = null,
    dimens: Dimens,
    color: Color
) {
    Column(
        modifier = modifier
            .width(dimens.simpleScoreBoardWidth)
            .border(
                width = dimens.borderSmall,
                color = color,
                shape = RoundedCornerShape(20)
            )
            .padding(dimens.paddingMedium)
    ) {
        ScoreLineView(
            scoreName = topLabel,
            score = topScore,
            dimens = dimens,
            color = color
        )
        ScoreLineView(
            scoreName = middleLabel,
            score = middleScore,
            dimens = dimens,
            color = color
        )
        if (bottomLabel != null && bottomScore != null) {
            ScoreLineView(
                scoreName = bottomLabel,
                score = bottomScore,
                dimens = dimens,
                color = color
            )
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun ScoreBoardPreviewPhone() {
    OufMimeTheme {
        ScoreBoardView(
            topLabel = "Total",
            topScore = 10,
            middleLabel = "Round",
            middleScore = 5,
            dimens = MediumDimens,
            color = Primary
        )
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_C)
@Composable
fun ScoreBoardPreviewTablet() {
    OufMimeTheme {
        ScoreBoardView(
            topLabel = "Total",
            topScore = 10,
            middleLabel = "Round",
            middleScore = 5,
            dimens = ExpandedDimens,
            color = Primary
        )
    }
}

@Composable
fun ScoreLineView(
    scoreName: String,
    score: Int,
    dimens: Dimens,
    color: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = scoreName, color = color, fontSize = dimens.bodyText)
        Text(text = score.toString(), color = color, fontSize = dimens.bodyText)
    }
}

@Preview(showBackground = true, widthDp = 200)
@Composable
fun ScoreLinePreview() {
    OufMimeTheme {
        ScoreLineView("Total", 5, MediumDimens, Primary)
    }
}