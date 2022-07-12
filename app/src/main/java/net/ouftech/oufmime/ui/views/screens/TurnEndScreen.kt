package net.ouftech.oufmime.ui.views.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.ouftech.oufmime.R
import net.ouftech.oufmime.data.Categories
import net.ouftech.oufmime.data.Word
import net.ouftech.oufmime.ui.theme.*
import net.ouftech.oufmime.ui.views.library.*

@Composable
fun TurnEndScreen(
    wordsPlayed: List<Pair<Word, Boolean>>,
    dimens: Dimens,
    isExpandedScreen: Boolean,
    onWordChange: (Pair<Word, Boolean>) -> Unit,
    onNextClick: () -> Unit,
    invertColors: Boolean
) {

    FullScreenColumn {

        val scoreBoard = getTurnEndScoreboardView(
            wordsPlayed = wordsPlayed,
            dimens = dimens,
            invertColors = invertColors
        )

        if (isExpandedScreen) {
            TabletTurnEndView(
                modifier = Modifier.weight(1f),
                scoreBoard = scoreBoard,
                wordsPlayed = wordsPlayed,
                dimens = dimens,
                onWordChange = onWordChange
            )
        } else {
            PhoneTurnEndView(
                modifier = Modifier.weight(1f),
                scoreBoard = scoreBoard,
                wordsPlayed = wordsPlayed,
                dimens = dimens,
                onWordChange = onWordChange
            )
        }

        SizedButton(
            text = stringResource(id = R.string.next),
            onClick = onNextClick,
            dimens = MediumDimens
        )
    }
}

@Composable
fun PhoneTurnEndView(
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
fun TabletTurnEndView(
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
fun getTurnEndScoreboardView(
    wordsPlayed: List<Pair<Word, Boolean>>,
    dimens: Dimens,
    invertColors: Boolean
): @Composable () -> Unit {
    val foundWordsCount = wordsPlayed.count { it.second }
    val missedWordsCount = wordsPlayed.count { !it.second }

    return {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AppIcon(dimens = dimens, inverted = invertColors)

            ScoreBoardView(
                topLabel = stringResource(id = R.string.found),
                topScore = foundWordsCount,
                middleLabel = stringResource(id = R.string.missed),
                middleScore = missedWordsCount,
                dimens = dimens,
                color = White
            )
        }
    }
}

@Composable
fun WordsListView(
    modifier: Modifier,
    wordsPlayed: List<Pair<Word, Boolean>>,
    dimens: Dimens,
    onWordChange: (Pair<Word, Boolean>) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .padding(vertical = dimens.paddingXLarge)
            .background(color = White, shape = RoundedCornerShape(8.dp))
    ) {
        items(wordsPlayed) { word ->
            WordPlayedView(word = word, dimens = dimens, onClick = onWordChange)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFF6F00, name = "Turn End - Phone", device = Devices.PIXEL_4)
@Composable
fun TurnEndScreenPreviewPhone() {
    OufMimeTheme {
        TurnEndScreen(
            wordsPlayed = listOf(
                Pair(Word("Squid", Categories.ANIMALS, "en"), true),
                Pair(Word("Blue Bear", Categories.ANIMALS, "en"), false)
            ),
            dimens = MediumDimens,
            isExpandedScreen = false,
            invertColors = false,
            onWordChange = { },
            onNextClick = { }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFF6F00, name = "Turn End - Tablet", device = Devices.PIXEL_C)
@Composable
fun TurnEndScreenPreviewTablet() {
    OufMimeTheme {
        TurnEndScreen(
            wordsPlayed = listOf(
                Pair(Word("Squid", Categories.ANIMALS, "en"), true),
                Pair(Word("Blue Bear", Categories.ANIMALS, "en"), false)
            ),
            dimens = ExpandedDimens,
            isExpandedScreen = true,
            invertColors = false,
            onWordChange = { },
            onNextClick = { }
        )
    }
}

@Composable
fun WordPlayedView(
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
fun WordPlayedPreview() {
    OufMimeTheme {
        WordPlayedView(
            word = Pair(Word("Squid", Categories.ANIMALS, "en"), true),
            dimens = MediumDimens,
            onClick = { }
        )
    }
}