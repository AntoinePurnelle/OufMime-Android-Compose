package net.ouftech.oufmime.ui.views.library

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import net.ouftech.oufmime.R
import net.ouftech.oufmime.ui.theme.Accent
import net.ouftech.oufmime.ui.theme.ButtonsTextSize
import net.ouftech.oufmime.ui.theme.ButtonsTextSize.MEDIUM
import net.ouftech.oufmime.ui.theme.Dimens
import net.ouftech.oufmime.ui.theme.Primary

@Composable
fun SizedButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    textSize: ButtonsTextSize = MEDIUM,
    dimens: Dimens
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)
    ) {
        Text(
            text.toUpperCase(Locale.current),
            color = Color.White,
            fontSize = dimens.buttonDimens[textSize]!!,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun FullScreenColumn(
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = horizontalAlignment,
        content = content
    )
}

@Composable
fun FullScreenRow(
    modifier: Modifier = Modifier,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = verticalAlignment,
        content = content
    )
}

@Composable
fun FullWidthRow(
    modifier: Modifier = Modifier,
    arrangement: Arrangement.Horizontal = Arrangement.SpaceEvenly,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = arrangement,
        verticalAlignment = Alignment.CenterVertically,
        content = content
    )
}

@Composable
fun AppIcon(dimens: Dimens, inverted: Boolean) {
    Box {
        Spacer(
            modifier = Modifier
                .size(dimens.iconMedium)
                .padding(dimens.paddingLarge)
                .background(shape = CircleShape, color = if (inverted) Accent else Primary)
        )
        Image(
            modifier = Modifier.size(dimens.iconMedium),
            painter = painterResource(
                id = if (inverted)
                    R.drawable.ic_launcher_foreground_inverted
                else
                    R.drawable.ic_launcher_foreground
            ),
            contentDescription = "App Icon"
        )
    }
}