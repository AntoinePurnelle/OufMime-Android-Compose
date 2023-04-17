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

package net.ouftech.oufmime.ui.views.screens.turnend

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.ouftech.oufmime.R
import net.ouftech.oufmime.data.Categories
import net.ouftech.oufmime.data.Word
import net.ouftech.oufmime.ui.theme.Green
import net.ouftech.oufmime.ui.theme.NoTeamColors
import net.ouftech.oufmime.ui.theme.Red
import net.ouftech.oufmime.ui.theme.ScreenConfiguredTheme
import net.ouftech.oufmime.ui.theme.getDimens
import net.ouftech.oufmime.ui.views.library.FullWidthRow
import net.ouftech.oufmime.ui.views.library.PreviewView
import net.ouftech.oufmime.ui.views.screens.play.CardDeck
import net.ouftech.oufmime.utils.roundedRectShadowedBackground

// region Views

@Composable
fun WordsListView(
    modifier: Modifier = Modifier,
    wordsPlayed: List<WordListItem.WordItem>,
    onWordChange: (Pair<Word, Boolean>) -> Unit
) = LazyColumn(
    modifier = modifier
        .widthIn(max = 800.dp)
        .roundedRectShadowedBackground()
        .padding(horizontal = getDimens().paddingXLarge)
) {
    val items = listOf<WordListItem>(WordListItem.Header) + wordsPlayed + WordListItem.Footer
    items(items) { item ->
        when (item) {
            is WordListItem.Header -> Spacer(Modifier.height(getDimens().paddingXXLarge))
            is WordListItem.WordItem -> WordPlayedView(item = item, onClick = onWordChange)
            is WordListItem.Footer -> Spacer(Modifier.height(getDimens().paddingXLarge))
        }
    }
}

@Composable
private fun WordPlayedView(
    item: WordListItem.WordItem,
    onClick: (Pair<Word, Boolean>) -> Unit
) {
    FullWidthRow(
        modifier = Modifier
            .padding(horizontal = getDimens().paddingMedium, vertical = getDimens().paddingSmall)
            .clickable { onClick.invoke(Pair(item.word, item.wasFound)) },
        arrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = item.word.word, color = Color.Black, fontSize = getDimens().bodyLarge)
        Icon(
            modifier = Modifier
                .size(getDimens().iconSmall)
                .background(color = if (item.wasFound) Green else Red, shape = CircleShape)
                .padding(getDimens().paddingSmall),
            painter = painterResource(id = if (item.wasFound) R.drawable.ic_check else R.drawable.ic_cross),
            contentDescription = stringResource(
                id = if (item.wasFound) R.string.found else R.string.missed
            ),
            tint = Color.White
        )
    }
}

@Composable
fun DecoratedCardDeck(
    modifier: Modifier = Modifier,
    count: Int,
    isFound: Boolean,
) = Box(
    modifier = modifier
        .size(getDimens().playWidgetsSize)
) {
    CardDeck(count = count, reversed = isFound, color = if (isFound) Green else Red)
}

// endregion Views

// region Previews

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun WordsListPreview() = ScreenConfiguredTheme {
    WordsListView(wordsPlayed = stubWords) {}
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun WordPlayedPreview() = ScreenConfiguredTheme {
    WordPlayedView(
        item = WordListItem.WordItem(Word("Batman", Categories.FICTIONAL, "en"), true),
    ) { }
}

@Preview(widthDp = 450, heightDp = 450)
@Composable
private fun FoundPreview() = PreviewView(colors = NoTeamColors) { DecoratedCardDeck(count = 4, isFound = true) }

@Preview(widthDp = 450, heightDp = 450)
@Composable
private fun MissedPreview() = PreviewView(colors = NoTeamColors) { DecoratedCardDeck(count = 4, isFound = false) }

// endregion Previews

private val stubWords
    get() = listOf(
        WordListItem.WordItem(Word("Squid", Categories.ANIMALS, "en"), true),
        WordListItem.WordItem(Word("Blue Bear", Categories.ANIMALS, "en"), false),
        WordListItem.WordItem(Word("Zeus", Categories.FICTIONAL, "en"), false),
    )