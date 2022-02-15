package net.ouftech.oufmime.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.ouftech.oufmime.ui.theme.Accent
import net.ouftech.oufmime.ui.theme.Primary
import net.ouftech.oufmime.ui.views.ButtonsTextSize.MEDIUM

@Composable
fun SizedButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    textSize: ButtonsTextSize = MEDIUM
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(backgroundColor = Accent)
    ) {
        Text(
            text.toUpperCase(Locale.current),
            color = Color.White,
            fontSize = textSize.value,
            textAlign = TextAlign.Center
        )
    }
}

enum class ButtonsTextSize(val value: TextUnit) {
    SMALL(8.sp),
    MEDIUM(16.sp),
    BIG(32.sp)
}

@Composable
fun FullScreenColumn(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Primary)
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        content = content
    )
}

@Composable
fun FullWidthRow(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        content = content
    )
}