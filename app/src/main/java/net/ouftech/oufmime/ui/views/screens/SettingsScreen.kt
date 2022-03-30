package net.ouftech.oufmime.ui.views.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Checkbox
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
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
import net.ouftech.oufmime.data.WordsViewModel
import net.ouftech.oufmime.ui.theme.*
import net.ouftech.oufmime.ui.views.library.FullScreenColumn
import net.ouftech.oufmime.ui.views.library.FullScreenRow
import net.ouftech.oufmime.ui.views.library.SizedButton
import net.ouftech.oufmime.utils.LanguageUtils

@Composable
fun SettingsScreen(
    viewModel: WordsViewModel,
    dimens: Dimens,
    isExpandedScreen: Boolean,
    onStartClick: () -> Unit,
    onLanguageClick: () -> Unit
) {
    if (isExpandedScreen) {
        FullScreenRow {
            CategoriesPickerView(
                viewModel = viewModel,
                dimens = dimens
            )

            InputFieldsView(
                viewModel = viewModel,
                dimens = dimens,
                onStartClick = onStartClick,
                onLanguageClick = onLanguageClick
            )
        }
    } else {
        FullScreenColumn(modifier = Modifier.padding(40.dp)) {
            CategoriesPickerView(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                viewModel = viewModel,
                dimens = dimens
            )

            Spacer(modifier = Modifier.height(dimens.paddingLarge))

            InputFieldsView(
                viewModel = viewModel,
                dimens = dimens,
                onStartClick = onStartClick,
                onLanguageClick = onLanguageClick
            )
        }
    }
}

@Composable
fun CategoriesPickerView(modifier: Modifier = Modifier, viewModel: WordsViewModel, dimens: Dimens) {
    val categories =
        Categories.values()
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
                    isChecked = viewModel.selectedCategories[category.name]!!,
                    dimens = dimens,
                    onClick = { viewModel.selectedCategories[category.name] = it }
                )
            }
        }
    }
}

@Composable
fun InputFieldsView(
    viewModel: WordsViewModel,
    dimens: Dimens,
    onStartClick: () -> Unit,
    onLanguageClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        var wordsCount by remember { mutableStateOf(viewModel.wordsCount.toString()) }

        OutlinedTextField(
            label = { Text(text = stringResource(id = R.string.words_count)) },
            value = wordsCount,
            onValueChange = { wordsCount = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(dimens.paddingLarge))

        var timerTotalTime by remember { mutableStateOf((viewModel.timerTotalTime / 1000).toString()) }

        OutlinedTextField(
            label = { Text(text = stringResource(id = R.string.duration)) },
            value = timerTotalTime,
            onValueChange = { timerTotalTime = it },
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
                viewModel.wordsCount = wordsCount.toInt()
                viewModel.timerTotalTime = timerTotalTime.trim().toLong() * 1000
                onStartClick()
            }
        )
    }
}

@Composable
fun LanguageButton(
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
fun CategoryCheckbox(
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
fun SettingsPreviewPhone() {
    OufMimeTheme {
        SettingsScreen(
            viewModel = WordsViewModel(),
            dimens = MediumDimens,
            isExpandedScreen = false,
            onStartClick = {},
            onLanguageClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Settings Tablet", device = Devices.PIXEL_C)
@Composable
fun SettingsPreviewTablet() {
    OufMimeTheme {
        SettingsScreen(
            viewModel = WordsViewModel(),
            dimens = ExpandedDimens,
            isExpandedScreen = true,
            onStartClick = {},
            onLanguageClick = {}
        )
    }
}
