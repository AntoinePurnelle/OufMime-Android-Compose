package net.ouftech.oufmime.ui.views

import android.media.MediaPlayer
import android.os.CountDownTimer
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
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
import net.ouftech.oufmime.ui.theme.*

@Composable
fun PlayScreen(
    foundWordsCount: Int,
    missedWordsCount: Int,
    timerMaxValue: Long,
    currentWord: Word?,
    onWordPlayed: (Boolean) -> Unit,
    onFinishTurn: () -> Unit
) {
    var currentTimerValue by remember { mutableStateOf(timerMaxValue + 1000L) }
    lateinit var timer: CountDownTimer
    val context = LocalContext.current
    var lastSecondsMP: MediaPlayer? = null

    DisposableEffect(key1 = null) {
        timer = object : CountDownTimer(currentTimerValue, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                currentTimerValue -= 1000L

                if (currentTimerValue == 4000L) {
                    lastSecondsMP = context.playSound(R.raw.timer)
                }
            }

            override fun onFinish() {
                onFinishTurn()
            }
        }

        timer.start()

        onDispose {
            lastSecondsMP?.stop()
            timer.cancel()
        }
    }

    // TODO Sounds
    FullScreenColumn {
        FullWidthRow {
            ScoreBoardView(
                topLabel = stringResource(id = R.string.found),
                topScore = foundWordsCount,
                bottomLabel = stringResource(id = R.string.missed),
                bottomScore = missedWordsCount
            )

            Timer(currentTimerValue, timerMaxValue)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(color = White, shape = RoundedCornerShape(8.dp))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                currentWord?.let {
                    Text(
                        text = currentWord.word,
                        color = Accent,
                        fontSize = 40.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        modifier = Modifier.padding(top = 8.dp),
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
                onCLick = {
                    onWordPlayed(true)
                    context.playSound(R.raw.word_ok)
                }
            )
            AnswerButton(
                color = Red,
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(id = R.string.missed),
                onCLick = {
                    onWordPlayed(false)
                    context.playSound(R.raw.word_wrong)
                }
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
        timerMaxValue = 40000L,
        currentWord = Word("Squid", Categories.ANIMALS),
        onWordPlayed = {},
        onFinishTurn = {}
    )
}

@Composable
fun Timer(value: Long, maxValue: Long) {
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
            text = (value / 1000).toString(),
            color = Accent,
            fontSize = 60.sp
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFF6F00)
@Composable
fun TimerPreview() {
    OufMimeTheme {
        Timer(20000L, 40000L)
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