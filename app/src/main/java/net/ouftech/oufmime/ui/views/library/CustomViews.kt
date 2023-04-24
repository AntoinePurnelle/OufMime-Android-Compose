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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import net.ouftech.oufmime.R
import net.ouftech.oufmime.ui.theme.ButtonsTextSize
import net.ouftech.oufmime.ui.theme.NoTeamColors
import net.ouftech.oufmime.ui.theme.TeamBlueColors
import net.ouftech.oufmime.ui.theme.TeamOrangeColors
import net.ouftech.oufmime.ui.theme.getDimens

// region Views

@Composable
fun SizedButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    textSize: ButtonsTextSize = ButtonsTextSize.MEDIUM,
    textColor: Color = MaterialTheme.colorScheme.onTertiary,
    backgroundColor: Color = MaterialTheme.colorScheme.tertiary,
) = Button(
    modifier = modifier,
    onClick = onClick,
    colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
) {
    Text(
        modifier = Modifier.padding(horizontal = getDimens().paddingLarge),
        text = text.toUpperCase(Locale.current),
        color = textColor,
        fontSize = getDimens().buttonDimens[textSize]!!,
        style = MaterialTheme.typography.labelLarge,
        textAlign = TextAlign.Center
    )
}

@Composable
fun AppIcon(
    currentTeam: Int,
    modifier: Modifier = Modifier,
) = Image(
    modifier = modifier
        .size(getDimens().iconLarge)
        .background(shape = CircleShape, color = MaterialTheme.colorScheme.primary)
        .graphicsLayer(
            scaleX = 1.6f,
            scaleY = 1.6f,
        ),
    painter = painterResource(
        id = when (currentTeam) {
            1 -> R.drawable.ic_launcher_foreground_inverted
            else -> R.drawable.ic_launcher_foreground
        },
    ),
    contentDescription = "App Icon"
)

// endregion Views

// region Previews

@Preview(widthDp = 400, heightDp = 250)
@Composable
private fun SizedButtonOrangePreviews() = PreviewView(TeamOrangeColors) { SizedButton(text = "Sized Button", onClick = {}) }

@Preview(widthDp = 400, heightDp = 250)
@Composable
private fun SizedButtonBluePreviews() = PreviewView(TeamBlueColors) { SizedButton(text = "Sized Button", onClick = {}) }

@Preview(widthDp = 400, heightDp = 250)
@Composable
private fun SizedButtonNoTeamPreviews() = PreviewView(NoTeamColors) { SizedButton(text = "Sized Button", onClick = {}) }

@Preview(widthDp = 700, heightDp = 500)
@Composable
private fun AppIconPreviews() = PreviewView(
    mapOf(
        TeamOrangeColors to getAppIconNoTeamPreview(0),
        TeamBlueColors to getAppIconNoTeamPreview(1),
        NoTeamColors to getAppIconNoTeamPreview(-1),
    )
)

@Composable
private fun getAppIconNoTeamPreview(currentTeam: Int): @Composable () -> Unit = { AppIcon(currentTeam) }

// endregion Previews