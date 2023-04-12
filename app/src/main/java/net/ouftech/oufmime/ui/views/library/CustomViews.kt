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

package net.ouftech.oufmime.ui.views.library

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.ouftech.oufmime.R
import net.ouftech.oufmime.ui.theme.ButtonsTextSize
import net.ouftech.oufmime.ui.theme.ButtonsTextSize.MEDIUM
import net.ouftech.oufmime.ui.theme.Dimens
import net.ouftech.oufmime.ui.theme.MediumDimens
import net.ouftech.oufmime.ui.theme.OufMimeTheme

@Composable
fun SizedButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    textSize: ButtonsTextSize = MEDIUM,
    textColor: Color = MaterialTheme.colorScheme.onTertiary,
    backgroundColor: Color = MaterialTheme.colorScheme.tertiary,
    dimens: Dimens
) = Button(
    modifier = modifier,
    onClick = onClick,
    colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
) {
    Text(
        modifier = Modifier.padding(horizontal = dimens.paddingLarge),
        text = text.toUpperCase(Locale.current),
        color = textColor,
        fontSize = dimens.buttonDimens[textSize]!!,
        style = MaterialTheme.typography.labelLarge,
        textAlign = TextAlign.Center
    )
}

@Composable
fun FullScreenColumn(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.SpaceEvenly,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    content: @Composable ColumnScope.() -> Unit
) = Column(
    modifier = modifier
        .fillMaxSize()
        .padding(20.dp),
    verticalArrangement = verticalArrangement,
    horizontalAlignment = horizontalAlignment,
    content = content
)

@Composable
fun FullScreenRow(
    modifier: Modifier = Modifier,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    content: @Composable RowScope.() -> Unit
) = Row(
    modifier = modifier
        .fillMaxSize()
        .padding(20.dp),
    horizontalArrangement = Arrangement.SpaceEvenly,
    verticalAlignment = verticalAlignment,
    content = content
)

@Composable
fun FullWidthRow(
    modifier: Modifier = Modifier,
    arrangement: Arrangement.Horizontal = Arrangement.SpaceEvenly,
    content: @Composable RowScope.() -> Unit
) = Row(
    modifier = modifier.fillMaxWidth(),
    horizontalArrangement = arrangement,
    verticalAlignment = Alignment.CenterVertically,
    content = content
)

@Composable
fun FullScreenBox(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) = Box(
    modifier = modifier.fillMaxWidth(),
    content = content
)

@Composable
fun AppIcon(
    modifier: Modifier = Modifier,
    dimens: Dimens,
    currentTeam: Int,
) {
    Box(modifier) {
        Spacer(
            modifier = Modifier
                .size(dimens.iconMedium)
                .padding(dimens.paddingLarge)
                .background(shape = CircleShape, color = MaterialTheme.colorScheme.primary)
        )

        Icon(
            modifier = Modifier.size(dimens.iconMedium),
            painter = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = "App Icon",
        )

        Image(
            modifier = Modifier.size(dimens.iconMedium),
            painter = painterResource(
                id = when (currentTeam) {
                    1 -> R.drawable.ic_launcher_foreground_inverted
                    else -> R.drawable.ic_launcher_foreground
                },
            ),
            contentDescription = "App Icon"
        )
    }
}

@Preview
@Composable
private fun SizedButtonPreviews() {
    Column {
        OufMimeTheme(colorPalette = 0) {
            SizedButton(text = "Sized Button", onClick = {}, dimens = MediumDimens)
        }
        OufMimeTheme(colorPalette = 1) {
            SizedButton(text = "Sized Button", onClick = {}, dimens = MediumDimens)
        }
        OufMimeTheme(colorPalette = -1) {
            SizedButton(text = "Sized Button", onClick = {}, dimens = MediumDimens)
        }
    }

}

@Preview
@Composable
private fun AppIconPreviews() {
    Column {
        OufMimeTheme(colorPalette = 0) {
            AppIcon(dimens = MediumDimens, currentTeam = 0)
        }
        OufMimeTheme(colorPalette = 1) {
            AppIcon(dimens = MediumDimens, currentTeam = 1)
        }
        OufMimeTheme(colorPalette = -1) {
            AppIcon(dimens = MediumDimens, currentTeam = -1)
        }
    }
}