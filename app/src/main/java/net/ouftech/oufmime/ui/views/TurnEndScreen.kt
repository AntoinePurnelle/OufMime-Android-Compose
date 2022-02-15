package net.ouftech.oufmime.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.ouftech.oufmime.R
import net.ouftech.oufmime.data.Categories
import net.ouftech.oufmime.data.Word
import net.ouftech.oufmime.ext.playSound
import net.ouftech.oufmime.ui.theme.Green
import net.ouftech.oufmime.ui.theme.OufMimeTheme
import net.ouftech.oufmime.ui.theme.Red

@Composable
fun TurnEndScreen(
    wordsPlayed: List<Pair<Word, Boolean>>,
    onWordChange: (Pair<Word, Boolean>) -> Unit,
    onNextClick: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = null) {
        context.playSound(R.raw.times_up)
    }

    FullScreenColumn {
        val wordsFoundCount = wordsPlayed.count { it.second }

        val headerText = LocalContext.current.resources.getQuantityString(
            R.plurals.words_found_count,
            wordsFoundCount,
            wordsFoundCount
        )

        Text(
            text = headerText,
            color = White,
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )

        if (wordsFoundCount > 0) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 16.dp)
                    .background(color = White)
                    .padding(4.dp)
            ) {
                items(wordsPlayed) { word ->
                    WordPlayedView(word = word, onClick = onWordChange)
                }
            }
        }

        SizedButton(text = stringResource(id = R.string.next), onClick = onNextClick)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFF6F00)
@Composable
fun TurnEndScreenPreview() {
    OufMimeTheme {
        TurnEndScreen(
            wordsPlayed = listOf(
                Pair(Word("Squid", Categories.ANIMALS), true),
                Pair(Word("Blue Bear", Categories.ANIMALS), false)
            ),
            onWordChange = {},
            onNextClick = {})
    }
}

@Composable
fun WordPlayedView(word: Pair<Word, Boolean>, onClick: (Pair<Word, Boolean>) -> Unit) {
    FullWidthRow(
        modifier = Modifier
            .padding(4.dp)
            .clickable { onClick.invoke(word) },
        arrangement = Arrangement.SpaceBetween,
    ) {
        val found = word.second
        Text(text = word.first.word, color = Black)
        Icon(
            modifier = Modifier
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
        WordPlayedView(word = Pair(Word("Squid", Categories.ANIMALS), true), onClick = { })
    }
}