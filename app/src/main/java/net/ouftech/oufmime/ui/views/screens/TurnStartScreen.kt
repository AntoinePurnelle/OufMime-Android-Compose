package net.ouftech.oufmime.ui.views.screens

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import net.ouftech.oufmime.R
import net.ouftech.oufmime.data.WordsViewModel
import net.ouftech.oufmime.ui.theme.Dimens
import net.ouftech.oufmime.ui.theme.ExpandedDimens
import net.ouftech.oufmime.ui.theme.MediumDimens
import net.ouftech.oufmime.ui.theme.OufMimeTheme
import net.ouftech.oufmime.ui.views.library.FullScreenColumn
import net.ouftech.oufmime.ui.views.library.HeaderView
import net.ouftech.oufmime.ui.views.library.SizedButton

@Composable
fun TurnStartScreen(
    viewModel: WordsViewModel,
    dimens: Dimens,
    invertColors: Boolean,
    onStartClick: () -> Unit
) {
    val roundName = when (viewModel.currentRound) {
        0 -> stringResource(id = R.string.describe)
        1 -> stringResource(id = R.string.word)
        else -> stringResource(id = R.string.mime)
    }

    FullScreenColumn {
        HeaderView(
                viewModel.getTeamTotalScore(0),
                viewModel.getTeamCurrentRoundScore(0),
                viewModel.getTeamTotalScore(1),
                viewModel.getTeamCurrentRoundScore(1),
                dimens,
                invertColors = invertColors
        )

        Text(
            text = stringResource(id = R.string.round_title, viewModel.currentRound + 1, roundName),
            color = White,
            fontSize = dimens.titleText,
            textAlign = TextAlign.Center
        )

        SizedButton(
            onClick = onStartClick,
            text = stringResource(
                id = R.string.team_x_plays,
                    stringResource(id = viewModel.teamNameId)
            ),
            dimens = dimens
        )
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFF6F00,
    name = "TurnStart - Phone",
    device = Devices.PIXEL_4
)
@Composable
fun TurnStartPreviewPhone() {
    OufMimeTheme {
        TurnStartScreen(WordsViewModel(), MediumDimens, false) {}
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFF6F00,
    name = "TurnStart - Tablet",
    device = Devices.PIXEL_C
)
@Composable
fun TurnStartPreviewTablet() {
    OufMimeTheme {
        TurnStartScreen(WordsViewModel(), ExpandedDimens, false) {}
    }
}