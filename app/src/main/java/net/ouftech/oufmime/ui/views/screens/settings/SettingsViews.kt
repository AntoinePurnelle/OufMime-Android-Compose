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

package net.ouftech.oufmime.ui.views.screens.settings

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import net.ouftech.oufmime.R
import net.ouftech.oufmime.data.Categories
import net.ouftech.oufmime.ui.theme.ButtonsTextSize
import net.ouftech.oufmime.ui.theme.getDimens
import net.ouftech.oufmime.ui.views.library.SizedButton
import net.ouftech.oufmime.utils.LanguageUtils
import net.ouftech.oufmime.utils.roundedRectShadowedBackground


@Composable
fun CategoriesPickerView(
    modifier: Modifier = Modifier,
    selectedCategories: Map<String, Boolean>,
    onCategoryClick: (String, Boolean) -> Unit,
) {
    val categories = Categories.values()
        .associateBy { stringResource(it.resId) }
        .toSortedMap().values.toList()

    Column(
        modifier
            .roundedRectShadowedBackground()
            .padding(getDimens().paddingMedium)
    ) {
        Text(
            modifier = Modifier
                .padding(start = 12.dp)
                .align(Alignment.CenterHorizontally),
            text = stringResource(id = R.string.categories),
            fontSize = getDimens().bodyLarge,
        )

        LazyColumn {
            items(categories) { category ->
                CategoryCheckbox(
                    category = category,
                    isChecked = selectedCategories[category.name]!!,
                    onClick = { onCategoryClick(category.name, it) }
                )
            }
        }
    }
}

@Composable
fun InputFieldsView(
    wordsCount: Int,
    timerTotalTime: Long,
    onStartClick: (Int, Long) -> Unit,
    onLanguageClick: () -> Unit,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        var count by remember { mutableStateOf(wordsCount.toString()) }
        var time by remember { mutableStateOf(timerTotalTime.toString()) }

        Column(
            modifier = Modifier
                .roundedRectShadowedBackground()
                .padding(getDimens().paddingLarge)
        ) {

            SettingsTextField(labelId = R.string.words_count, value = count, onValueChange = { count = it })

            Spacer(modifier = Modifier.height(getDimens().paddingLarge))

            SettingsTextField(labelId = R.string.duration, value = time, onValueChange = { time = it })
        }


        Spacer(modifier = Modifier.height(getDimens().paddingLarge))
        LanguageButton(
            language = "en",
            textId = R.string.switch_language_en,
            onLanguageClick = onLanguageClick
        )

        LanguageButton(
            language = "fr",
            textId = R.string.switch_language_fr,
            onLanguageClick = onLanguageClick
        )
        Spacer(modifier = Modifier.height(getDimens().paddingLarge))

        SizedButton(
            text = stringResource(id = R.string.start),
            textSize = ButtonsTextSize.BIG,
            onClick = {
                onStartClick(count.toInt(), time.trim().toLong())
            }
        )
    }
}

@Composable
private fun SettingsTextField(
    @StringRes labelId: Int,
    value: String,
    onValueChange: (String) -> Unit,
) = OutlinedTextField(
    label = { Text(text = stringResource(id = labelId)) },
    value = value,
    onValueChange = onValueChange,
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    maxLines = 1,
    colors = TextFieldDefaults.outlinedTextFieldColors(containerColor = Color.White),
)

@Composable
fun LanguageButton(
    language: String,
    @StringRes textId: Int,
    onLanguageClick: () -> Unit
) {
    val context = LocalContext.current

    Text(
        modifier = Modifier.clickable(onClick = {
            LanguageUtils.setLanguage(language, context)
            onLanguageClick()
        }),
        text = stringResource(id = textId),
        fontSize = getDimens().bodyMedium,
        color = Color.White
    )
}

@Composable
fun CategoryCheckbox(
    category: Categories,
    isChecked: Boolean,
    onClick: (Boolean) -> Unit
) {
    Row(
        Modifier.clickable(onClick = { onClick(!isChecked) }),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = isChecked, onCheckedChange = { onClick(it) })
        Text(
            modifier = Modifier.padding(start = getDimens().paddingSmall),
            text = stringResource(id = category.resId),
            fontSize = getDimens().bodyMedium
        )
    }
}