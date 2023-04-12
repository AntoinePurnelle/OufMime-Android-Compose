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
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.ouftech.oufmime.R
import net.ouftech.oufmime.ui.theme.Accent
import net.ouftech.oufmime.ui.theme.ButtonsTextSize.BIG
import net.ouftech.oufmime.ui.theme.Dimens
import net.ouftech.oufmime.ui.theme.ExpandedDimens
import net.ouftech.oufmime.ui.theme.MediumDimens
import net.ouftech.oufmime.ui.theme.OufMimeTheme
import net.ouftech.oufmime.ui.views.library.FullScreenColumn
import net.ouftech.oufmime.ui.views.library.SizedButton

@Composable
fun WelcomeScreen(
    dimens: Dimens,
    onStartClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Box(Modifier.fillMaxSize()) {
        FullScreenColumn {
            Text(
                text = stringResource(id = R.string.app_name),
                fontSize = dimens.bigTitleText,
                color = White,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.size(0.dp))

            SizedButton(
                onClick = onStartClick,
                text = stringResource(id = R.string.start),
                textSize = BIG,
                textColor = Accent,
                dimens = dimens,
            )
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_settings),
            contentDescription = "TODO", // TODO
            modifier = Modifier
                .padding(top = dimens.paddingLarge, end = dimens.paddingLarge)
                .size(30.dp)
                .align(Alignment.TopEnd)
                .clickable(onClick = onSettingsClick),
            tint = White
        )
    }
}

@Preview(showBackground = true, name = "Welcome - Phone", device = Devices.PIXEL_4)
@Composable
private fun WelcomeScreenPreviewPhone() {
    OufMimeTheme {
        WelcomeScreen(dimens = MediumDimens, onStartClick = {}, onSettingsClick = {})
    }
}

@Preview(showBackground = true, name = "Welcome - Tablet", device = Devices.PIXEL_C)
@Composable
private fun WelcomeScreenPreviewTablet() {
    OufMimeTheme {
        WelcomeScreen(dimens = ExpandedDimens, onStartClick = {}, onSettingsClick = {})
    }
}