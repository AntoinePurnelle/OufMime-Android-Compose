package net.ouftech.oufmime.ui.views

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import net.ouftech.oufmime.R
import net.ouftech.oufmime.ui.theme.OufMimeTheme
import net.ouftech.oufmime.ui.views.ButtonsTextSize.BIG

@Composable
fun WelcomeScreen(
    onStartClick: () -> Unit
) {
    FullScreenColumn {
        Text(
            text = stringResource(id = R.string.app_name),
            fontSize = 64.sp,
            color = White
        )

        SizedButton(
            onClick = onStartClick,
            text = stringResource(id = R.string.start),
            textSize = BIG
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    OufMimeTheme {
        WelcomeScreen {}
    }
}