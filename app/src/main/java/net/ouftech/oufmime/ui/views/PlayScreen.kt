package net.ouftech.oufmime.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.ouftech.oufmime.R
import net.ouftech.oufmime.data.Categories
import net.ouftech.oufmime.data.Word
import net.ouftech.oufmime.ui.theme.*

@Composable
fun PlayScreen(
    foundWordsCount: Int,
    missedWordsCount: Int,
    timerValue: Int,
    timerMaxValue: Int,
    currentWord: Word?,
    onWordPlayed: (Boolean) -> Unit
) {
    // TODO Sounds
    FullScreenColumn {
        FullWidthRow {
            ScoreBoardView(
                topLabel = stringResource(id = R.string.found),
                topScore = foundWordsCount,
                bottomLabel = stringResource(id = R.string.missed),
                bottomScore = missedWordsCount
            )

            Timer(timerValue, timerMaxValue) // TODO Use timer
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = White, shape = RoundedCornerShape(8.dp))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                currentWord?.let {
                    Text(text = currentWord.word, color = Accent, fontSize = 60.sp)
                    Text(
                        text = stringResource(id = currentWord.category.resId),
                        color = AccentTransparent,
                        fontSize = 20.sp
                    )
                }

            }
        }

        FullWidthRow {
            AnswerButton(
                color = Green,
                imageVector = Icons.Default.Check,
                contentDescription = stringResource(id = R.string.found),
                onCLick = { onWordPlayed(true) }
            )
            AnswerButton(
                color = Red,
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(id = R.string.missed),
                onCLick = { onWordPlayed(false) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlayScreenPreview() {
    PlayScreen(
        foundWordsCount = 5,
        missedWordsCount = 2,
        timerValue = 15,
        timerMaxValue = 40,
        currentWord = Word("Squid", Categories.ANIMALS),
        onWordPlayed = {}
    )
}

@Composable
fun Timer(value: Int, maxValue: Int) {
    Box(
        modifier = Modifier.size(100.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(2.dp)
                .border(shape = CircleShape, color = TransparentWhite, width = 2.dp),
            color = Transparent
        ) {}

        CircularProgressIndicator(
            modifier = Modifier.fillMaxSize(),
            progress = value.toFloat() / maxValue,
            color = Accent,
            strokeWidth = 6.dp
        )

        Text(
            text = value.toString(),
            color = Accent,
            fontSize = 60.sp
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFF6F00)
@Composable
fun TimerPreview() {
    OufMimeTheme {
        Timer(20, 40)
    }
}

@Composable
fun AnswerButton(
    color: Color,
    imageVector: ImageVector,
    contentDescription: String?,
    onCLick: () -> Unit
) {
    Button(
        modifier = Modifier
            .size(100.dp)
            .clip(CircleShape),
        colors = ButtonDefaults.buttonColors(backgroundColor = color),
        onClick = onCLick
    ) {
        Icon(
            modifier = Modifier.size(80.dp),
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = White
        )
    }
}