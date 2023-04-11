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

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.ouftech.oufmime.R
import net.ouftech.oufmime.data.Categories
import net.ouftech.oufmime.data.Word
import net.ouftech.oufmime.ui.theme.Accent
import net.ouftech.oufmime.ui.theme.Dimens
import net.ouftech.oufmime.ui.theme.ExpandedDimens
import net.ouftech.oufmime.ui.theme.Green
import net.ouftech.oufmime.ui.theme.MediumDimens
import net.ouftech.oufmime.ui.theme.OufMimeTheme
import net.ouftech.oufmime.ui.theme.Red
import net.ouftech.oufmime.ui.views.library.AppIcon
import net.ouftech.oufmime.ui.views.library.FullScreenColumn
import net.ouftech.oufmime.ui.views.library.FullWidthRow
import net.ouftech.oufmime.ui.views.library.ScoreBoardView
import net.ouftech.oufmime.ui.views.library.SizedButton

@Composable
fun TurnEndScreen(
    model: TurnEndUiState,
    onWordChange: (Pair<Word, Boolean>) -> Unit,
    onNextClick: () -> Unit,
) = with(model) {
    FullScreenColumn {
        val scoreBoard = getTurnEndScoreboardView(
            wordsPlayed = wordsPlayed,
            dimens = dimens,
            currentTeam = currentTeam
        )

        if (dimens.isExpandedScreen) {
            TabletTurnEndView(
                modifier = Modifier.weight(0.8f),
                scoreBoard = scoreBoard,
                wordsPlayed = wordsPlayed,
                dimens = dimens,
                onWordChange = onWordChange
            )
        } else {
            PhoneTurnEndView(
                modifier = Modifier.weight(0.8f),
                scoreBoard = scoreBoard,
                wordsPlayed = wordsPlayed,
                dimens = dimens,
                onWordChange = onWordChange
            )
        }

        Box(modifier = Modifier.weight(0.1f)) {
            SizedButton(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(id = R.string.next),
                onClick = onNextClick,
                dimens = MediumDimens
            )
        }
    }
}

@Composable
private fun PhoneTurnEndView(
    modifier: Modifier = Modifier,
    scoreBoard: @Composable () -> Unit,
    wordsPlayed: List<Pair<Word, Boolean>>,
    dimens: Dimens,
    onWordChange: (Pair<Word, Boolean>) -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        scoreBoard()
        WordsListView(
            modifier = Modifier.weight(1f),
            wordsPlayed = wordsPlayed,
            dimens = dimens,
            onWordChange = onWordChange
        )
    }
}

@Composable
private fun TabletTurnEndView(
    modifier: Modifier = Modifier,
    scoreBoard: @Composable () -> Unit,
    wordsPlayed: List<Pair<Word, Boolean>>,
    dimens: Dimens,
    onWordChange: (Pair<Word, Boolean>) -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        scoreBoard()
        WordsListView(
            modifier = Modifier
                .fillMaxHeight()
                .width(600.dp),
            wordsPlayed = wordsPlayed,
            dimens = dimens,
            onWordChange = onWordChange
        )
    }
}

@Composable
private fun getTurnEndScoreboardView(
    wordsPlayed: List<Pair<Word, Boolean>>,
    dimens: Dimens,
    currentTeam: Int,
): @Composable () -> Unit {
    val foundWordsCount = wordsPlayed.count { it.second }
    val missedWordsCount = wordsPlayed.count { !it.second }

    return {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AppIcon(dimens = dimens, currentTeam = currentTeam)

            ScoreBoardView(
                topLabel = stringResource(id = R.string.found),
                topScore = foundWordsCount,
                middleLabel = stringResource(id = R.string.missed),
                middleScore = missedWordsCount,
                dimens = dimens,
                color = Accent
            )
        }
    }
}

@Composable
private fun WordsListView(
    modifier: Modifier,
    wordsPlayed: List<Pair<Word, Boolean>>,
    dimens: Dimens,
    onWordChange: (Pair<Word, Boolean>) -> Unit
) {
    val boxShape = RoundedCornerShape(20.dp)

    LazyColumn(
        modifier = modifier
            .padding(vertical = dimens.paddingXLarge)
            .shadow(dimens.defaultShadow, boxShape)
            .clip(boxShape)
            .background(color = White, shape = boxShape)
    ) {
        items(wordsPlayed) { word ->
            WordPlayedView(word = word, dimens = dimens, onClick = onWordChange)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFF6F00, name = "Turn End - Phone", device = Devices.PIXEL_4)
@Composable
private fun TurnEndScreenPreviewPhone() {
    OufMimeTheme {
        TurnEndScreen(
            model = getStubUiModel(MediumDimens),
            onWordChange = { },
            onNextClick = { }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFF6F00, name = "Turn End - Tablet", device = Devices.PIXEL_C)
@Composable
private fun TurnEndScreenPreviewTablet() {
    OufMimeTheme {
        TurnEndScreen(
            model = getStubUiModel(ExpandedDimens),
            onWordChange = { },
            onNextClick = { }
        )
    }
}

@Composable
private fun WordPlayedView(
    word: Pair<Word, Boolean>,
    dimens: Dimens,
    onClick: (Pair<Word, Boolean>) -> Unit
) {
    FullWidthRow(
        modifier = Modifier
            .padding(horizontal = dimens.paddingMedium, vertical = dimens.paddingSmall)
            .clickable { onClick.invoke(word) },
        arrangement = Arrangement.SpaceBetween,
    ) {
        val found = word.second
        Text(text = word.first.word, color = Black, fontSize = dimens.bodyText)
        Icon(
            modifier = Modifier
                .size(24.dp)
                .background(color = if (found) Green else Red, shape = CircleShape)
                .padding(1.dp),
            imageVector = if (found) Icons.Default.Check else Icons.Default.Close,
            contentDescription = stringResource(
                id = if (found) R.string.found else R.string.missed
            ),
            tint = White
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun WordPlayedPreview() {
    OufMimeTheme {
        WordPlayedView(
            word = Pair(Word("Squid", Categories.ANIMALS, "en"), true),
            dimens = MediumDimens,
            onClick = { }
        )
    }
}

data class TurnEndUiState(
    val dimens: Dimens,
    val currentTeam: Int,
    val wordsPlayed: List<Pair<Word, Boolean>>,
)

private fun getStubUiModel(dimens: Dimens = MediumDimens) = TurnEndUiState(
    dimens = dimens,
    currentTeam = 0,
    wordsPlayed = listOf(
        Pair(Word("Squid", Categories.ANIMALS, "en"), true),
        Pair(Word("Blue Bear", Categories.ANIMALS, "en"), false)
    ),
)