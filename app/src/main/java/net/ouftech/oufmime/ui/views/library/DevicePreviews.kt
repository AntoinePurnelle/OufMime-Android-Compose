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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import net.ouftech.oufmime.ui.theme.NoTeamColors
import net.ouftech.oufmime.ui.theme.ScreenConfiguration.Companion.COMPACT_PORTRAIT
import net.ouftech.oufmime.ui.theme.ScreenConfiguration.Companion.EXPANDED_LANDSCAPE
import net.ouftech.oufmime.ui.theme.ScreenConfiguration.Companion.MEDIUM
import net.ouftech.oufmime.ui.theme.ScreenConfiguredTheme
import net.ouftech.oufmime.ui.theme.TeamBlueColors
import net.ouftech.oufmime.ui.theme.TeamOrangeColors

@Preview(name = "compact portrait", device = "spec:width=411dp,height=891dp")
annotation class CompactPortraitPreview

@Preview(name = "compact landscape", device = "spec:width=891dp,height=411dp")
annotation class CompactLandscapePreview

@Preview(name = "medium", device = "spec:width=720dp,height=720dp")
annotation class MediumPreview

@Preview(name = "expanded portrait", device = "spec:width=800dp,height=1280dp,dpi=480")
annotation class ExpandedPortraitPreview

@Preview(name = "expanded portrait", device = "spec:width=1280dp,height=800dp,dpi=480")
annotation class ExpandedLandscapePreview

@Composable
fun PreviewView(
    hasNoTeamColors: Boolean = false,
    view: @Composable () -> Unit,
) = PreviewView(
    mutableMapOf(
        TeamOrangeColors to view,
        TeamBlueColors to view,
    ).apply {
        if (hasNoTeamColors) {
            put(NoTeamColors, view)
        }
    }
)


@Composable
fun PreviewView(
    views: Map<ColorScheme, (@Composable () -> Unit)>,
) = Row(
    modifier = Modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceEvenly,
) {
    views.forEach { (colors, view) -> PreviewView(colors, view) }
}

@Composable
fun PreviewView(
    colors: ColorScheme,
    view: @Composable () -> Unit
) = Column(
    modifier = Modifier.fillMaxHeight(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceEvenly,
) {
    ScreenConfiguredTheme(COMPACT_PORTRAIT, colors) { view() }
    ScreenConfiguredTheme(MEDIUM, colors) { view() }
    ScreenConfiguredTheme(EXPANDED_LANDSCAPE, colors) { view() }
}

