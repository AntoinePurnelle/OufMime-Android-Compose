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

@file:OptIn(ExperimentalMaterial3Api::class)

package net.ouftech.oufmime.ui.views.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.ouftech.oufmime.R
import net.ouftech.oufmime.data.Categories
import net.ouftech.oufmime.ui.theme.ButtonsTextSize
import net.ouftech.oufmime.ui.theme.Dimens
import net.ouftech.oufmime.ui.theme.ExpandedDimens
import net.ouftech.oufmime.ui.theme.MediumDimens
import net.ouftech.oufmime.ui.theme.OufMimeTheme
import net.ouftech.oufmime.ui.views.library.FullScreenColumn
import net.ouftech.oufmime.ui.views.library.FullScreenRow
import net.ouftech.oufmime.ui.views.library.SizedButton
import net.ouftech.oufmime.utils.LanguageUtils

@Composable
fun SettingsScreen(
    uiState: SettingsScreenUiState,
    listener: SettingsScreenListener
) = with(uiState) {
    if (dimens.isExpandedScreen) {
        FullScreenRow {
            CategoriesPickerView(
                dimens = dimens,
                selectedCategories = selectedCategories,
                onCategoryClick = listener::onCategoryClick
            )

            InputFieldsView(
                dimens = dimens,
                wordsCount = wordsCount,
                timerTotalTime = timerTotalTime,
                onStartClick = listener::onStartClick,
                onLanguageClick = listener::onLanguageClick
            )
        }
    } else {
        FullScreenColumn(modifier = Modifier.padding(40.dp)) {
            CategoriesPickerView(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                dimens = dimens,
                selectedCategories = selectedCategories,
                onCategoryClick = listener::onCategoryClick
            )

            Spacer(modifier = Modifier.height(dimens.paddingLarge))

            InputFieldsView(
                dimens = dimens,
                wordsCount = wordsCount,
                timerTotalTime = timerTotalTime,
                onStartClick = listener::onStartClick,
                onLanguageClick = listener::onLanguageClick
            )
        }
    }
}

@Composable
private fun CategoriesPickerView(
    modifier: Modifier = Modifier,
    dimens: Dimens,
    selectedCategories: Map<String, Boolean>,
    onCategoryClick: (String, Boolean) -> Unit,
) {
    val categories = Categories.values()
        .associateBy { stringResource(it.resId) }
        .toSortedMap().values.toList()

    Column(modifier) {
        Text(
            modifier = Modifier.padding(start = 12.dp),
            text = stringResource(id = R.string.categories),
            fontSize = dimens.bodyText
        )

        LazyColumn {
            items(categories) { category ->
                CategoryCheckbox(
                    category = category,
                    isChecked = selectedCategories[category.name]!!,
                    dimens = dimens,
                    onClick = { onCategoryClick(category.name, it) }
                )
            }
        }
    }
}

@Composable
private fun InputFieldsView(
    dimens: Dimens,
    wordsCount: Int,
    timerTotalTime: Long,
    onStartClick: (Int, Long) -> Unit,
    onLanguageClick: () -> Unit,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        var count by remember { mutableStateOf(wordsCount.toString()) }

        OutlinedTextField(
            label = { Text(text = stringResource(id = R.string.words_count)) },
            value = count,
            onValueChange = { count = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(dimens.paddingLarge))

        var time by remember { mutableStateOf(timerTotalTime.toString()) }

        OutlinedTextField(
            label = { Text(text = stringResource(id = R.string.duration)) },
            value = time,
            onValueChange = { time = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            maxLines = 1
        )

        Spacer(modifier = Modifier.height(dimens.paddingLarge))

        LanguageButton(
            language = "en",
            textId = R.string.switch_language_en,
            dimens = dimens,
            onLanguageClick = onLanguageClick
        )

        LanguageButton(
            language = "fr",
            textId = R.string.switch_language_fr,
            dimens = dimens,
            onLanguageClick = onLanguageClick
        )

        Spacer(modifier = Modifier.height(dimens.paddingLarge))

        SizedButton(
            text = stringResource(id = R.string.start),
            textSize = ButtonsTextSize.BIG,
            dimens = dimens,
            onClick = {
                onStartClick(count.toInt(), time.trim().toLong())
            }
        )
    }
}

@Composable
private fun LanguageButton(
    language: String,
    @StringRes textId: Int,
    dimens: Dimens,
    onLanguageClick: () -> Unit
) {
    val context = LocalContext.current

    Text(
        modifier = Modifier.clickable(onClick = {
            LanguageUtils.setLanguage(language, context)
            onLanguageClick()
        }),
        text = stringResource(id = textId),
        fontSize = dimens.bodyText,
        color = Color.Gray
    )
}

@Composable
private fun CategoryCheckbox(
    category: Categories,
    isChecked: Boolean,
    dimens: Dimens,
    onClick: (Boolean) -> Unit
) {
    Row(
        Modifier.clickable(onClick = { onClick(!isChecked) }),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = isChecked, onCheckedChange = { onClick(it) })
        Text(
            modifier = Modifier.padding(start = dimens.paddingSmall),
            text = stringResource(id = category.resId),
            fontSize = dimens.bodyText
        )
    }
}

@Preview(showBackground = true, name = "Settings Phone", device = Devices.PIXEL_4)
@Composable
private fun SettingsPreviewPhone() {
    OufMimeTheme {
        SettingsScreen(
            uiState = getStubSettingsScreenUiState(MediumDimens),
            listener = stubSettingsScreenListener,
        )
    }
}

@Preview(showBackground = true, name = "Settings Tablet", device = Devices.PIXEL_C)
@Composable
private fun SettingsPreviewTablet() {
    OufMimeTheme {
        SettingsScreen(
            uiState = getStubSettingsScreenUiState(ExpandedDimens),
            listener = stubSettingsScreenListener,
        )
    }
}

data class SettingsScreenUiState(
    val dimens: Dimens,
    val selectedCategories: Map<String, Boolean>,
    val wordsCount: Int,
    val timerTotalTime: Long,
)

private fun getStubSettingsScreenUiState(dimens: Dimens) = SettingsScreenUiState(
    dimens = dimens,
    selectedCategories = Categories.values().map { it.name to true }.toMutableStateMap(),
    wordsCount = 40,
    timerTotalTime = 40,
)

private val stubSettingsScreenListener
    get() = object : SettingsScreenListener {
        override fun onCategoryClick(category: String, selected: Boolean) = Unit
        override fun onStartClick(wordsCount: Int, timerTotalTime: Long) = Unit
        override fun onLanguageClick() = Unit
    }

interface SettingsScreenListener {
    fun onCategoryClick(category: String, selected: Boolean)
    fun onStartClick(wordsCount: Int, timerTotalTime: Long)
    fun onLanguageClick()
}