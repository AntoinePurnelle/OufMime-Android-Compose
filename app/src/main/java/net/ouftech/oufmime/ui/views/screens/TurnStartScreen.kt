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

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import net.ouftech.oufmime.R
import net.ouftech.oufmime.ui.theme.Dimens
import net.ouftech.oufmime.ui.theme.ExpandedDimens
import net.ouftech.oufmime.ui.theme.MediumDimens
import net.ouftech.oufmime.ui.theme.OufMimeTheme
import net.ouftech.oufmime.ui.views.library.FullScreenColumn
import net.ouftech.oufmime.ui.views.library.HeaderView
import net.ouftech.oufmime.ui.views.library.SizedButton

@Composable
fun TurnStartScreen(
    dimens: Dimens,
    invertColors: Boolean,
    model: TurnStartUiModel,
    onStartClick: () -> Unit,
) {
    val roundName = stringResource(
        when (model.currentRound) {
            0 -> R.string.describe
            1 -> R.string.word
            else -> R.string.mime
        }
    )

    FullScreenColumn {
        HeaderView(
            team1TotalScore = model.blueTotalScore,
            team1CurrentRoundScore = model.blueCurrentRoundScore,
            team2TotalScore = model.orangeTotalScore,
            team2CurrentRoundScore = model.orangeCurrentRoundScore,
            dimens = dimens,
            invertColors = invertColors
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(
                    id = R.string.round_title,
                    model.currentRound + 1
                ),
                color = White,
                fontSize = dimens.titleText,
                textAlign = TextAlign.Center
            )
            Text(
                text = roundName.uppercase(),
                color = White,
                fontSize = dimens.titleText,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }

        SizedButton(
            onClick = onStartClick,
            text = stringResource(
                id = R.string.team_x_plays,
                stringResource(id = model.teamNameId)
            ),
            dimens = dimens
        )
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFF6F00,
    name = "TurnStart - Phone",
    device = Devices.PIXEL_4,
    locale = "fr"
)
@Composable
private fun TurnStartPreviewPhone() {
    OufMimeTheme {
        TurnStartScreen(MediumDimens, false, stubTurnStartUiModel) {}
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFF6F00,
    name = "TurnStart - Tablet",
    device = Devices.PIXEL_C,
)
@Composable
private fun TurnStartPreviewTablet() {
    OufMimeTheme {
        TurnStartScreen(ExpandedDimens, false, stubTurnStartUiModel) {}
    }
}

data class TurnStartUiModel(
    val currentRound: Int,
    @StringRes val teamNameId: Int,
    val blueTotalScore: Int,
    val blueCurrentRoundScore: Int,
    val orangeTotalScore: Int,
    val orangeCurrentRoundScore: Int,
)

private val stubTurnStartUiModel
    get() = TurnStartUiModel(0, R.string.team_blue, 4, 2, 6, 3)