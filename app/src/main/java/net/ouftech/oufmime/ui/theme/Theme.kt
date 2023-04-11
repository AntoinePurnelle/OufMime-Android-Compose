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

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color.Companion.White

private val NoTeamColorPalette = lightColors(
    primary = Orange,
    primaryVariant = OrangeVariant,
    secondary = White,
    secondaryVariant = BlueVariant,
    background = DarkerGray,
)

private val Team0ColorPalette = lightColors(
    primary = Orange,
    primaryVariant = OrangeVariant,
    secondary = Blue,
    secondaryVariant = BlueVariant,
    background = Orange,
)

private val Team1ColorPalette = lightColors(
    primary = Blue,
    primaryVariant = BlueVariant,
    secondary = Orange,
    secondaryVariant = OrangeVariant,
    background = Blue,
)

@Composable
fun OufMimeTheme(
    colorPalette: Int = 0,
    content: @Composable () -> Unit
) = MaterialTheme(
    colors = when (colorPalette) {
        0 -> Team0ColorPalette
        1 -> Team1ColorPalette
        else -> NoTeamColorPalette
    },
    typography = Typography,
    shapes = Shapes,
    content = content
)