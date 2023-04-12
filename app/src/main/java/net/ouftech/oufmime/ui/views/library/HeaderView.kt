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

package net.ouftech.oufmime.ui.views.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
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
import net.ouftech.oufmime.ui.theme.Accent
import net.ouftech.oufmime.ui.theme.BlueTeam
import net.ouftech.oufmime.ui.theme.Dimens
import net.ouftech.oufmime.ui.theme.ExpandedDimens
import net.ouftech.oufmime.ui.theme.MediumDimens
import net.ouftech.oufmime.ui.theme.OrangeTeam
import net.ouftech.oufmime.ui.theme.OufMimeTheme
import net.ouftech.oufmime.utils.roundedRectShadowedBackground

@Composable
fun HeaderView(
    team1TotalScore: Int = 0,
    team1CurrentRoundScore: Int = 0,
    team2TotalScore: Int = 0,
    team2CurrentRoundScore: Int = 0,
    dimens: Dimens,
    currentTeam: Int,
) {
    Row(
        modifier = Modifier
            .widthIn(max = 700.dp)
            .roundedRectShadowedBackground(backgroundColor = White)
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
            color = BlueTeam
        )

        Spacer(modifier = Modifier.width(dimens.paddingSmall))

        AppIcon(dimens = dimens, currentTeam = currentTeam)

        Spacer(modifier = Modifier.width(dimens.paddingSmall))

        ScoreBoardView(
            modifier = Modifier.weight(1f),
            topLabel = stringResource(id = R.string.score_total),
            topScore = team2TotalScore,
            middleLabel = stringResource(id = R.string.score_round),
            middleScore = team2CurrentRoundScore,
            dimens = dimens,
            color = OrangeTeam
        )
    }
}

@Preview(device = Devices.PIXEL_4)
@Composable
fun HeaderPreview() {
    OufMimeTheme {
        HeaderView(10, 5, 20, 10, MediumDimens, currentTeam = 0)
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
    color: Color = Accent,
) {
    Column(
        modifier = modifier
            .width(dimens.simpleScoreBoardWidth)
            .roundedRectShadowedBackground()
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

@Preview(device = Devices.PIXEL_4)
@Composable
fun ScoreBoardPreviewPhone() {
    OufMimeTheme {
        ScoreBoardView(
            topLabel = "Total",
            topScore = 10,
            middleLabel = "Round",
            middleScore = 5,
            dimens = MediumDimens,
            color = OrangeTeam
        )
    }
}

@Preview(device = Devices.PIXEL_C)
@Composable
fun ScoreBoardPreviewTablet() {
    OufMimeTheme {
        ScoreBoardView(
            topLabel = "Total",
            topScore = 10,
            middleLabel = "Round",
            middleScore = 5,
            dimens = ExpandedDimens,
            color = OrangeTeam
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
        ScoreLineView("Total", 5, MediumDimens, OrangeTeam)
    }
}