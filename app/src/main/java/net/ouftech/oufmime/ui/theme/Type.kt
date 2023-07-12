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

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import net.ouftech.oufmime.R

val Nunito = FontFamily(
    Font(R.font.nunito_regular),
    Font(R.font.nunito_regular, weight = FontWeight.Normal),
    Font(R.font.nunito_italic, weight = FontWeight.Normal, style = FontStyle.Italic),
    Font(R.font.nunito_black, weight = FontWeight.Black),
    Font(R.font.nunito_black_italic, weight = FontWeight.Black, style = FontStyle.Italic),
    Font(R.font.nunito_bold, weight = FontWeight.Bold),
    Font(R.font.nunito_bold_italic, weight = FontWeight.Bold, style = FontStyle.Italic),
    Font(R.font.nunito_extra_bold, weight = FontWeight.ExtraBold),
    Font(R.font.nunito_extra_bold_italic, weight = FontWeight.ExtraBold, style = FontStyle.Italic),
    Font(R.font.nunito_extra_light, weight = FontWeight.ExtraLight),
    Font(R.font.nunito_extra_light_italic, weight = FontWeight.ExtraLight, style = FontStyle.Italic),
    Font(R.font.nunito_light, weight = FontWeight.Light),
    Font(R.font.nunito_light_italic, weight = FontWeight.Light, style = FontStyle.Italic),
    Font(R.font.nunito_medium, weight = FontWeight.Medium),
    Font(R.font.nunito_medium_italic, weight = FontWeight.Medium, style = FontStyle.Italic),
    Font(R.font.nunito_semi_bold, weight = FontWeight.SemiBold),
    Font(R.font.nunito_semi_bold_italic, weight = FontWeight.SemiBold, style = FontStyle.Italic),
)

fun getTypography(dimens: Dimens) = Typography(
    bodyLarge = TextStyle(
        fontFamily = Nunito,
        fontWeight = FontWeight.Medium,
        fontSize = dimens.bodyLarge
    ),
    bodyMedium = TextStyle(
        fontFamily = Nunito,
        fontWeight = FontWeight.Medium,
        fontSize = dimens.bodyMedium
    ),
    labelLarge = TextStyle(
        fontFamily = Nunito,
        fontWeight = FontWeight.ExtraBold,
        fontSize = dimens.smallButtonText
    )
)