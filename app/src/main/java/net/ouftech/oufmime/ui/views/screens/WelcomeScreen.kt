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

package net.ouftech.oufmime.ui.views.screens

import androidx.compose.foundation.clickable
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import net.ouftech.oufmime.R
import net.ouftech.oufmime.ui.theme.*
import net.ouftech.oufmime.ui.theme.ButtonsTextSize.BIG
import net.ouftech.oufmime.ui.views.library.FullScreenColumn
import net.ouftech.oufmime.ui.views.library.SizedButton

@Composable
fun WelcomeScreen(
    dimens: Dimens,
    onStartClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    FullScreenColumn {
        Text(
            text = stringResource(id = R.string.app_name),
            fontSize = dimens.bigTitleText,
            color = Primary,
            fontWeight = FontWeight.Bold
        )

        SizedButton(
            onClick = onStartClick,
            text = stringResource(id = R.string.start),
            textSize = BIG,
            dimens = dimens
        )

        Text(
            modifier = Modifier.clickable(onClick = onSettingsClick),
            text = stringResource(id = R.string.settings),
            fontSize = dimens.bodyText,
            color = Color.Gray
        )
    }
}

@Preview(showBackground = true, name = "Welcome - Phone", device = Devices.PIXEL_4)
@Composable
fun WelcomeScreenPreviewPhone() {
    OufMimeTheme {
        WelcomeScreen(dimens = MediumDimens, onStartClick = {}, onSettingsClick = {})
    }
}

@Preview(showBackground = true, name = "Welcome - Tablet", device = Devices.PIXEL_C)
@Composable
fun WelcomeScreenPreviewTablet() {
    OufMimeTheme {
        WelcomeScreen(dimens = ExpandedDimens, onStartClick = {}, onSettingsClick = {})
    }
}