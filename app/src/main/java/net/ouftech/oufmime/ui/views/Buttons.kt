package net.ouftech.oufmime.ui.views

import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import net.ouftech.oufmime.R
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
        colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.colorAccent))
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