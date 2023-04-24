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

package net.ouftech.oufmime.ui.views.screens.welcome

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import net.ouftech.oufmime.R
import net.ouftech.oufmime.ui.theme.Accent
import net.ouftech.oufmime.ui.theme.ButtonsTextSize.BIG
import net.ouftech.oufmime.ui.theme.NoTeamColors
import net.ouftech.oufmime.ui.theme.ScreenConfiguration
import net.ouftech.oufmime.ui.theme.ScreenConfiguration.Companion.COMPACT_LANDSCAPE
import net.ouftech.oufmime.ui.theme.ScreenConfiguration.Companion.COMPACT_PORTRAIT
import net.ouftech.oufmime.ui.theme.ScreenConfiguration.Companion.EXPANDED_LANDSCAPE
import net.ouftech.oufmime.ui.theme.ScreenConfiguration.Companion.EXPANDED_PORTRAIT
import net.ouftech.oufmime.ui.theme.ScreenConfiguration.Companion.MEDIUM
import net.ouftech.oufmime.ui.theme.ScreenConfiguredTheme
import net.ouftech.oufmime.ui.theme.getDimens
import net.ouftech.oufmime.ui.views.library.CompactLandscapePreview
import net.ouftech.oufmime.ui.views.library.CompactPortraitPreview
import net.ouftech.oufmime.ui.views.library.ExpandedLandscapePreview
import net.ouftech.oufmime.ui.views.library.ExpandedPortraitPreview
import net.ouftech.oufmime.ui.views.library.FullScreenColumn
import net.ouftech.oufmime.ui.views.library.MediumPreview
import net.ouftech.oufmime.ui.views.library.SizedButton
import net.ouftech.oufmime.ui.views.library.SplitBackgrounded

// region Views

@Composable
fun WelcomeScreen(
    onStartClick: () -> Unit,
    onSettingsClick: () -> Unit
) = SplitBackgrounded {
    Box(Modifier.fillMaxSize()) {
        FullScreenColumn {
            Text(
                text = stringResource(id = R.string.app_name),
                fontSize = getDimens().bigTitleText,
                color = White,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.size(0.dp))

            SizedButton(
                onClick = onStartClick,
                text = stringResource(id = R.string.start),
                textSize = BIG,
                textColor = Accent,
            )
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_settings),
            contentDescription = stringResource(id = R.string.settings_content_description),
            modifier = Modifier
                .padding(top = getDimens().paddingLarge, end = getDimens().paddingLarge)
                .size(30.dp)
                .align(Alignment.TopEnd)
                .clickable(onClick = onSettingsClick),
            tint = White
        )
    }
}

// endregion Views

// region Previews

@CompactPortraitPreview
@Composable
private fun CompactPortraitPreviewWelcomeScreen() = WelcomeScreenPreview(COMPACT_PORTRAIT)

@CompactLandscapePreview
@Composable
private fun CompactLandscapePreviewWelcomeScreen() = WelcomeScreenPreview(COMPACT_LANDSCAPE)

@MediumPreview
@Composable
private fun MediumPreviewWelcomeScreen() = WelcomeScreenPreview(MEDIUM)

@ExpandedPortraitPreview
@Composable
private fun ExpandedPortraitPreviewWelcomeScreen() = WelcomeScreenPreview(EXPANDED_PORTRAIT)

@ExpandedLandscapePreview
@Composable
private fun ExpandedLandscapePreviewWelcomeScreen() = WelcomeScreenPreview(EXPANDED_LANDSCAPE)

@Composable
private fun WelcomeScreenPreview(screenConfiguration: ScreenConfiguration) = ScreenConfiguredTheme(screenConfiguration, NoTeamColors) {
    WelcomeScreen(onStartClick = {}, onSettingsClick = {})
}

// endregion Previews