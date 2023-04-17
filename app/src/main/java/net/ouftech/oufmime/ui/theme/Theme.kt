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

package net.ouftech.oufmime.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White

val NoTeamColors = lightColorScheme(
    primary = Orange,
    secondary = OrangeVariant,
    tertiary = White,
    onTertiary = Blue,
    surface = Black,
    background = White
)

val TeamOrangeColors = lightColorScheme(
    primary = Orange,
    secondary = OrangeVariant,
    tertiary = Blue,
    onTertiary = White,
    surface = Black,
    background = Orange,
    surfaceVariant = Blue,
    onSurfaceVariant = BlueLight,
)

val TeamBlueColors = lightColorScheme(
    primary = Blue,
    secondary = BlueVariant,
    tertiary = Orange,
    onTertiary = White,
    surface = Black,
    background = Blue,
    surfaceVariant = Orange,
    onSurfaceVariant = OrangeDark,
)

@Composable
fun OufMimeTheme(
    colorScheme: ColorScheme = NoTeamColors,
    content: @Composable () -> Unit
) = MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    shapes = Shapes,
    content = content
)

@Composable
fun ScreenConfiguredTheme(
    screenConfiguration: ScreenConfiguration = ScreenConfiguration.MEDIUM,
    colorScheme: ColorScheme = NoTeamColors,
    content: @Composable () -> Unit
) = CompositionLocalProvider(LocalScreenConfiguration provides screenConfiguration) {
    OufMimeTheme(colorScheme, content)
}